package es.sixey.png2svg;

import es.sixey.png2svg.color.Color;
import es.sixey.png2svg.color.Palette;
import es.sixey.png2svg.minimization.Tine;
import org.jfree.svg.*;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Drawing {

    private final SVGGraphics2D surface;
    private final double surfaceWidth;
    private final double surfaceHeight;
    private final Palette palette;
    private final Tine tine;

    private final static Color WHITE = new Color(255, 255, 255);
    private final static float SIZE_CUTOFF = 0.15f;

    public Drawing(Palette palette) {
        tine = new Tine(palette, 0.1, 100, 100);
        this.palette = palette;
        surface = new SVGGraphics2D(297, 210, SVGUnits.MM);
        this.surfaceWidth = surface.getWidth();
        this.surfaceHeight = surface.getHeight();
        System.out.println(surfaceWidth + " " + surfaceHeight);
        surface.setStroke(new BasicStroke(0.6f));
    }

    interface Waveshaper {
        double shape(double value);
    }

    public void drawGrid(Grid grid, Image image, Color color, double circleSize) {
        drawGrid(grid, image, color, circleSize, input -> {
            // if (1 == 1) return input;
            var calculated = Math.min(1.0, Math.max(0.0, Math.pow(input, 3.5)));
            return calculated;
        });
    }

    public void drawGrid(Grid grid, Image image, Color drawColor, double circleSize, Waveshaper waveshaper) {
        if (drawColor.equals(WHITE)) return;

        surface.setRenderingHint(SVGHints.KEY_BEGIN_GROUP, drawColor.toString());

        var scaleX = 297.0f/image.getWidth();
        var scaleY = 210.0f/image.getHeight();
        var halfSize = circleSize/2;

        surface.setPaint(drawColor.toJavaColor());
        var points = grid.getPoints();

        for (var point : points) {
            var imageColor = image.getColor((int) point.x(), (int) point.y());
            if (imageColor.equals(WHITE)) continue;

            var weights = tine.getWeightsForColor(imageColor);
            weights.keySet().forEach(color -> {
                if (color.equals(drawColor)) {
                    // double size = (1.0d - weights.get(color));
                    double size = weights.get(color);
                    size = waveshaper.shape(size);

                    if (size <= SIZE_CUTOFF) return;

                    var circle = new Ellipse2D.Double(
                            (point.x() - (halfSize*size))*scaleX,
                            (point.y() - (halfSize*size))*scaleY,
                            size*circleSize*scaleX,
                            size*circleSize*scaleY
                    );
                    surface.draw(circle);
                }
            });
        }
        surface.setRenderingHint(SVGHints.KEY_END_GROUP, drawColor.toString());
    }


    public String getSvg() {
        var viewbox = new ViewBox(0, 0, 297, 210);
        return surface.getSVGElement(
                null,
                true, // include dimensions,
                viewbox,
                PreserveAspectRatio.XMAX_YMAX,
                MeetOrSlice.MEET
        );
    }
}

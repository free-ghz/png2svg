package es.sixey.png2svg;

import es.sixey.png2svg.color.Color;
import es.sixey.png2svg.color.Palette;
import org.jfree.svg.*;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Drawing {

    private final SVGGraphics2D surface;
    private final double surfaceWidth;
    private final double surfaceHeight;

    private final static Color WHITE = new Color(255, 255, 255);
    private final static float SIZE_CUTOFF = 0.15f;

    public Drawing() {
        surface = new SVGGraphics2D(297, 210, SVGUnits.MM);
        this.surfaceWidth = surface.getWidth();
        this.surfaceHeight = surface.getHeight();
        System.out.println(surfaceWidth + " " + surfaceHeight);
        surface.setStroke(new BasicStroke(0.6f));
    }

    interface Waveshaper {
        float shape(float value);
    }

    public void drawGrid(Grid grid, Image image, Palette palette, Color color, double circleSize) {
        drawGrid(grid, image, palette, color, circleSize, input -> {
            var calculated = (float) Math.min(1.0, Math.max(0.0, Math.pow(input, 2)));
            return calculated;
        });
    }

    public void drawGrid(Grid grid, Image image, Palette palette, Color color, double circleSize, Waveshaper waveshaper) {
        if (color.equals(WHITE)) return;

        surface.setRenderingHint(SVGHints.KEY_BEGIN_GROUP, color.toString());

        var scaleX = 297.0f/image.getWidth();
        var scaleY = 210.0f/image.getHeight();
        var halfSize = circleSize/2;

        surface.setPaint(color.toJavaColor());
        var points = grid.getPoints();

        for (var point : points) {
            var imageColor = image.getColor((int) point.x(), (int) point.y());
            if (imageColor.equals(WHITE)) continue;

            var set = palette.firstFor(3, imageColor);
            set.forEach(colorMix -> {
                if (colorMix.color().equals(color)) {
                    float size = 1.0f - colorMix.weight();
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
        surface.setRenderingHint(SVGHints.KEY_END_GROUP, color.toString());
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

package es.sixey.png2svg;

import es.sixey.png2svg.color.Color;
import org.jfree.svg.*;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Drawing {

    private final SVGGraphics2D surface;
    private final double surfaceWidth;
    private final double surfaceHeight;

    private final static Color WHITE = new Color(255, 255, 255);
    private final static float SIZE_CUTOFF = 0.25f;

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

    public void drawGrid(Grid grid, Image image, Color color, double circleSize) {
        // drawGrid(grid, image, color, circleSize, input -> input);
        drawGrid(grid, image, color, circleSize, input -> {
            var calculated = (float) Math.min(1.0, Math.max(0.0, Math.pow(input, 3)));
            if (calculated < 0.3) return 0;
            return calculated;
        });
    }

    public void drawGrid(Grid grid, Image image, Color color, double circleSize, Waveshaper waveshaper) {
        var scaleX = 297.0f/image.getWidth();
        var scaleY = 210.0f/image.getHeight();
        var halfSize = circleSize/2;

        surface.setPaint(color.toJavaColor());
        var points = grid.getPoints();

        for (var point : points) {
            var imageColor = image.getColor((int) point.x(), (int) point.y());
            if (imageColor.equals(WHITE)) continue;
            var distance = imageColor.distanceTo(color);

            float size = 1.0f - distance;
            size = waveshaper.shape(size);
            if (size <= SIZE_CUTOFF) continue;

            var circle = new Ellipse2D.Double(
                    (point.x() - (halfSize*size))*scaleX,
                    (point.y() - (halfSize*size))*scaleY,
                    size*circleSize*scaleX,
                    size*circleSize*scaleY
            );
            surface.draw(circle);
        }
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

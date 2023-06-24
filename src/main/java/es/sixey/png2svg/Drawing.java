package es.sixey.png2svg;

import es.sixey.png2svg.color.Color;
import es.sixey.png2svg.color.Palette;
import es.sixey.png2svg.minimization.Tine;
import org.jfree.svg.*;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

public class Drawing {

    private final SVGGraphics2D surface;
    private final double surfaceWidth;
    private final double surfaceHeight;
    private final Tine tine;

    private final static Color WHITE = new Color(255, 255, 255);
    private final static float SIZE_CUTOFF = 0.15f;

    public Drawing(Palette palette) {
        tine = new Tine(palette, 0.1, 100, 100);
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

    public void drawPaths(java.util.List<Path> paths, Image image) {
        surface.setRenderingHint(SVGHints.KEY_BEGIN_GROUP, image.toString());

        var xFactor = surfaceWidth / image.getWidth();
        var yFactor = surfaceHeight / image.getHeight();

        for (var path: paths) {
            drawPath(path, 0, 0, xFactor, yFactor);
        }

        surface.setRenderingHint(SVGHints.KEY_END_GROUP, image.toString());
    }

    private void drawPath(Path path, double xOffset, double yOffset, double xFactor, double yFactor) {
        var points = path.getPoints();
        System.out.println("Drawing " + points.size() + " points.");
        if (points.isEmpty()) return;
        if (points.size() == 1) {
            System.err.println("point...");
            return;
        }

        for (var point: points) {
            var circle = new Ellipse2D.Double(
                    point.x() * xFactor,
                    point.y() * yFactor,
                    xFactor,
                    yFactor
            );
            // surface.draw(circle);
        }

        Path2D path2D = new Path2D.Double();
        path2D.moveTo (points.get(0).x() * xFactor, points.get(0).y() * yFactor);

        for (int i = 1; i < points.size(); i++) {
            var point = points.get(i);
            path2D.lineTo((point.x() * xFactor), (point.y() * yFactor));
        }
        surface.draw(path2D);
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

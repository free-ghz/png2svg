package es.sixey.png2svg;

import es.sixey.png2svg.color.Color;

import java.util.HashSet;
import java.util.Set;

public class Grid {
    private final int distance;
    private final int tilt;
    private final int width;
    private final int height;

    private Set<Point> points = new HashSet<>();

    public Grid(int distance, int tilt, int width, int height) {
        this.distance = distance;
        this.tilt = tilt;
        this.width = width;
        this.height = height;

        var normalWidth = width/distance;
        var normalHeight = height/distance;
        for (int y = -normalHeight; y < (normalHeight * 2); y++) {
            for (int x = -normalWidth; x < (normalWidth * 2); x++) {
                var realX = x * distance;
                if (realX < 0 || realX > width) continue;
                var realY = y * distance;
                if (realY < 0 || realY > height) continue;

                points.add(new Point(realX, realY));
            }
        }
    }

    public Set<Point> getPoints() {
        return points;
    }
}

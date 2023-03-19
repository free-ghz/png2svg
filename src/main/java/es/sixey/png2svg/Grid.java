package es.sixey.png2svg;

import es.sixey.png2svg.color.Color;

import java.util.HashSet;
import java.util.Set;

public class Grid {

    private final Set<Point> points = new HashSet<>();

    public Grid(int distance, int tilt, int width, int height) {
        var normalWidth = width/distance;
        var normalHeight = height/distance;
        System.out.println("querying " + (normalHeight*7*normalWidth*7) + " points lol.");
        for (int y = -normalHeight*2; y < (normalHeight * 5); y++) {
            for (int x = -normalWidth*2; x < (normalWidth * 5); x++) {
                var realXZ = (x * distance);
                var realYZ = (y * distance);
                var realX = (realXZ * Math.cos(tilt)) + (realYZ * Math.sin(tilt));
                var realY = -(realXZ * Math.sin(tilt)) + (realYZ * Math.cos(tilt));
                if (realX < 0 || realX > width) continue;
                if (realY < 0 || realY > height) continue;

                points.add(new Point(realX, realY));
            }
        }
        System.out.println("... kept " + points.size() + " points.");
    }

    public Set<Point> getPoints() {
        return points;
    }
}

package es.sixey.png2svg;

import es.sixey.png2svg.color.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LineFinder {
    public static List<Path> getLines(Image image, int attractionCutoff) {
        var black = new Color(0, 0, 0, "black");
        var imagePoints = image.getPoints(color -> color.equals(black));
        System.out.println("found how many ponts? " + imagePoints.size());

        // if within A_C of any points in a group, add to that group
        // else create new group
        var groups = new HashSet<Set<PointWithColor>>();

        imagePoints.forEach(point -> {
            Set<PointWithColor> bestGroup = null;
            double bestDistance = Double.MAX_VALUE;
            for (Set<PointWithColor> group : groups) {
                for (PointWithColor pointInGroup: group) {
                    var distance = distance(point, pointInGroup);
                    if (distance > attractionCutoff) break;
                    if (distance < bestDistance) {
                        bestDistance = distance;
                        bestGroup = group;
                    }
                }
            }
            if (bestGroup == null) {
                var newGroup = new HashSet<PointWithColor>();
                newGroup.add(point);
                groups.add(newGroup);
            } else {
                bestGroup.add(point);
            }
        });

        var paths = new ArrayList<Path>();
        var totalSize = 0;
        for(var group: groups) {
            var path = new Path();
            sort(group).forEach(point -> path.addPoint(point.x(), point.y()));
            paths.add(path);
            totalSize += path.size();
        }
        System.out.println(totalSize + " points in paths");
        return paths;
    }

    public static double distance(PointWithColor a, PointWithColor b) {
        var distanceX = Math.abs(a.x() - b.x());
        var distanceY = Math.abs(a.y() - b.y());
        return Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
    }

    private static List<PointWithColor> sort(Set<PointWithColor> input) {
        List<PointWithColor> pool = new ArrayList<>(input);
        for (var i = 0; i < input.size(); i++) {
            pool = sort(pool, i);
        }
        return pool;
    }
    private static List<PointWithColor> sort(List<PointWithColor> input, int pickIndex) {
        var pool = new ArrayList<>(input);
        var sorted = new ArrayList<PointWithColor>();

        if (pool.size() == 0) {
            System.out.println("Pool size zero?");
            return sorted;
        }

        var current = pool.get(pickIndex);
        do {
            pool.remove(current);
            sorted.add(current);
            double minDistance = Double.MAX_VALUE;
            PointWithColor minimum = null;
            for (var other : pool) {
                if (distance(current, other) < minDistance) {
                    minDistance = distance(current, other);
                    minimum = other;
                }
            }
            current = minimum;
        } while (current != null);
        return sorted;
    }
}

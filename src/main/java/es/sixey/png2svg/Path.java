package es.sixey.png2svg;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private final List<Point> points = new ArrayList<>();

    public Path addPoint(double x, double y) {
        points.add(new Point(x, y));
        return this;
    }
    public Path addPoint(Point point) {
        points.add(point);
        return this;
    }

    public List<Point> getPoints() {
        return points;
    }

    public Path size(double width, double height, double xMax, double yMax) {

        Path copy = new Path();
        for (var point : points) {
            copy.addPoint(new Point(point.x()/xMax*width, point.y()/yMax*height));
        }
        return copy;
    }

    public int size() {
        return points.size();
    }

    private double getAngle(Point a, Point b) {
        double dx = a.x() - b.x();
        double dy = a.y() - b.y();
        return Math.atan2(dy, dx);
    }

    public Path smooth() {
        var TAU = 2 * Math.PI;
        var TWOTAU = 2 * TAU;
        List<Point> p2 = new ArrayList<>(points);
        var extraTries = 3;
        var maxDiff = (Math.PI)/64;
        var newPath = new Path();
        newPath.addPoint(p2.get(0));
        for (int start = 0; start < p2.size() - 1;) {
            System.out.println("Start " + start);
            var startPoint = p2.get(start);
            var tries = 0;
            int lastGood = start + 1;
            Double sweetAngle = null;
            for (int next = lastGood; next < p2.size(); next++) {
                System.out.println("Next " + next);
                var nextPoint = p2.get(next);
                double angle = getAngle(startPoint, nextPoint);
                if (sweetAngle == null) {
                    System.out.println("Sweetangle " + angle);
                    sweetAngle = angle;
                    continue;
                }
                var diff = Math.abs(((sweetAngle + TWOTAU)%TAU) - ((angle + TWOTAU)%TAU));
                System.out.println("angle is " + angle + " diffing " + diff + " to " + sweetAngle);
                if (diff > maxDiff) {
                    if (tries == extraTries) {
                        System.out.println("too diff! max tries! adding " + lastGood + " (lg) to path and setting start");
                        start = lastGood;
                        newPath.addPoint(p2.get(lastGood));
                        break;
                    }
                    tries++;
                    System.out.println("too diff! tries++ " + tries);
                } else {
                    System.out.println("angle good. tries 0. lg " + lastGood);
                    tries = 0;
                    lastGood = next;
                }
            }
            start++;
            newPath.addPoint(p2.get(lastGood));
        }
        System.out.println("DONE!");
        return newPath;
    }
}

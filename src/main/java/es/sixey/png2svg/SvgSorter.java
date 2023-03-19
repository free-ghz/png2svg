package es.sixey.png2svg;

import java.util.ArrayList;
import java.util.List;

public class SvgSorter {

    public static String sort(String svg) {
        StringBuilder collector = new StringBuilder();
        var tags = svg.split("<");
        ArrayList<Ellipse> current = new ArrayList<>();
        int ellipses = 0;
        for (var tag : tags) {
            if (tag.startsWith("g")) {
                current = new ArrayList<>();
                collector.append("<").append(tag);
            } else if (tag.startsWith("ellipse")) {
                current.add(new Ellipse(tag));
                ellipses += 1;
            } else if (tag.startsWith("/g")) {
                var minItem = findSmallest(current);
                current.sort((a, b) -> (int) ((a.distanceTo(minItem) - b.distanceTo(minItem)) * 100));
                for (var a : current) {
                    collector.append("<").append(a.tag);
                }
                collector.append("<").append(tag);
            } else if (tag.isBlank()) {
            } else {
                collector.append("<").append(tag);
            }
        }
        System.out.println("Sorted " + ellipses + " ellipses.");
        return collector.toString();
    }

    private static Ellipse findSmallest(List<Ellipse> list) {
        var minSize = Double.MAX_VALUE;
        Ellipse minItem = null;
        for (var item : list) {
            var distance = Math.sqrt(Math.pow(item.x, 2) + Math.pow(item.y, 2));
            if (distance < minSize) {
                minSize = distance;
                minItem = item;
            }
        }
        return minItem;
    }

    private static class Ellipse {
        private final String tag;
        private double x;
        private double y;
        public Ellipse(String tag) {
            this.tag = tag;
            var stuff = tag.split(" ");
            for (var thing : stuff) {
                if (thing.startsWith("cx")) {
                    var cx = thing.substring(4, thing.length()-1);
                    x = Double.parseDouble(cx);
                }
                if (thing.startsWith("cy")) {
                    var cy = thing.substring(4, thing.length()-1);
                    y = Double.parseDouble(cy);
                }
            }
        }
        public double distanceTo(Ellipse o) {
            var distanceX = this.x - o.x;
            var distanceY = this.y - o.y;
            //return Math.sqrt(Math.pow(distanceX, 2) - Math.pow(distanceY, 2));
            return distanceX + distanceY;
        }
    }
}

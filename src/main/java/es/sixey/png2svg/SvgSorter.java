package es.sixey.png2svg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SvgSorter {

    public static void main(String[] stroke) throws IOException {
        var svg = Files.readString(Path.of("toSort.svg"));
        var outputPath = Path.of("sorted.svg");
        Files.writeString(outputPath, SvgSorter.sort(svg));
    }

    public static String sort(String svg) {
        StringBuilder collector = new StringBuilder();
        var tags = svg.split("<");
        ArrayList<Item> current = new ArrayList<>();
        int ellipses = 0;
        for (var tag : tags) {
            if (tag.startsWith("g")) {
                System.out.println("GROUP " + tag);
                current = new ArrayList<>();
                collector.append("<").append(tag);
            } else if (tag.startsWith("ellipse")) {
                current.add(new Item(tag));
                ellipses += 1;
            } else if (tag.startsWith("/g")) {
                printDistance(current);
                var sorted = sort(current);
                printDistance(sorted);
                for (var a : sorted) {
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

    private static void printDistance(List<Item> list) {
        var totalDistance = 0.0;
        for (var i = 0; i < list.size() - 1; i++) {
            totalDistance += list.get(i).distanceTo(list.get(i+1));
        }
        System.out.println("Total passive travel distance: " + totalDistance);
    }

    private static List<Item> sort(List<Item> input) {
        var pool = new ArrayList<>(input);
        var sorted = new ArrayList<Item>();

        if (pool.size() == 0) {
            System.out.println("Pool size zero?");
            return sorted;
        }

        var current = pool.get(pool.size() - 1);
        do {
            pool.remove(current);
            sorted.add(current);
            double minDistance = Double.MAX_VALUE;
            Item minimum = null;
            for (var other : pool) {
                if (current.distanceTo(other) < minDistance) {
                    minDistance = current.distanceTo(other);
                    minimum = other;
                }
            }
            current = minimum;
        } while (current != null);
        return sorted;
    }

    private static class Item {
        private final String tag;
        private double entryX;
        private double entryY;
        private double exitX;
        private double exitY;
        public Item(String tag) {
            this.tag = tag;
            tag = tag.replaceAll("\r?\n", "");
            var stuff = tag.split(" ");
            for (var thing : stuff) {
                if (thing.strip().length() == 0) continue;
                if (thing.charAt(thing.length()-1) == '"') thing = thing.substring(0, thing.length() - 2);
                if (thing.startsWith("cx")) {
                    var cx = thing.substring(4, thing.length()-1);
                    entryX = Double.parseDouble(cx);
                }
                if (thing.startsWith("cy")) {
                    var cy = thing.substring(4, thing.length()-1);
                    entryY = Double.parseDouble(cy);
                }
            }
            exitX = entryX;
            exitY = entryY;
        }

        public double distanceTo(Item o) {
            var distanceX = Math.abs(this.exitX - o.entryX);
            var distanceY = Math.abs(this.exitY - o.entryY);
            // return Math.sqrt(Math.pow(distanceX, 2) - Math.pow(distanceY, 2));
            return distanceX + distanceY;
        }
    }
}

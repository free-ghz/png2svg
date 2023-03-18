package es.sixey.png2svg.color;

import java.util.Set;

public class Palette {
    Set<Color> colors;
    public Palette(Set<Color> colors) {
        this.colors = colors;
    }

    public Color closest(Color color) {
        int minDist = Integer.MAX_VALUE;
        Color bestMatch = null;
        for (var test: colors) {
            var distance = color.distanceTo(test);
            if (distance < minDist) {
                minDist = distance;
                bestMatch = test;
            }
        }
        return bestMatch;
    }
}

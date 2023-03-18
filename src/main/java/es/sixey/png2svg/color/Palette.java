package es.sixey.png2svg.color;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Palette {
    Set<Color> colors;
    public Palette(Set<Color> colors) {
        this.colors = colors;
    }

    public Color closest(Color color) {
        float minDist = Integer.MAX_VALUE;
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

    public List<Mix> firstFor(int max, Color color) {
        var howMany = Math.min(max, colors.size());
        var a = new TreeSet<Color>((c1, c2) -> (int) ((c1.distanceTo(color) * 256) - (c2.distanceTo(color)*256)));
        a.addAll(colors);

        var mixes = new ArrayList<Mix>();
        float totalDistance = 0;
        for (int i = 0; i < howMany; i++) {
            var colorOut = a.pollFirst();
            var distanceToColor = colorOut.distanceTo(color);
            mixes.add(new Mix(colorOut, distanceToColor));
            totalDistance += distanceToColor;
        }

        var normalizedMixes = new ArrayList<Mix>();
        for (var mix : mixes) {
            normalizedMixes.add(new Mix(
                    mix.color(),
                    mix.weight()/totalDistance
            ));
        }

        return normalizedMixes;
    }

    public Set<Color> getColors() {
        return colors;
    }
}

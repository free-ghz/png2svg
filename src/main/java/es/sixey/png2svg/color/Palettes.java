package es.sixey.png2svg.color;

import java.util.Set;

public class Palettes {
    /**
     * Don't read too much into these values, they are eyeballed or eyedropped from images from a sub-par scanner.
     */
    public static class Stabilo {
        /**
         * Set of eight pens 88/8-01
         */
        public static final Palette PASTELS = new Palette(Set.of(
                new Color(224, 228, 115, "88/24"),
                new Color(223, 156, 157, "88/26"),
                new Color(217, 144, 175, "88/29"),
                new Color(186, 159, 222, "88/59"),
                new Color(163, 208, 230, "88/11"),
                new Color(87, 193, 186, "88/13"),
                new Color(164, 212, 182, "88/16"),
                new Color(140, 142, 156, "88/96")
        ));

        /**
         * Just a few ones i bought separately.
         */
        public static final Palette RANDOM = new Palette(Set.of(
                new Color(251, 147, 7, "88/54"),
                new Color(186, 159, 222, "88/59"), // i've got doubles apparently
                new Color(53, 168, 154, "88/51"),
                new Color(102, 68, 175, "88/55"),
                new Color(158, 50, 199, "88/58")
        ));
    }
}

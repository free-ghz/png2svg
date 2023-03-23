package es.sixey.png2svg;

import es.sixey.png2svg.color.Color;
import es.sixey.png2svg.color.Palette;
import es.sixey.png2svg.color.Palettes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        //var input = new File("input.png");
        var input = new File("milkytest.sniff.png");
        BufferedImage inputImage = ImageIO.read(input);

        Image image = new Image(inputImage);
        // Palette palette = Palettes.Stabilo.PASTELS.withWhite();
        Palette palette = new Palette(Set.of(
                new Color(164, 212, 182, "88/16"),
                new Color(223, 156, 157, "88/26"),
                new Color(87, 193, 186, "88/13")
        )).withWhite().withBlack();

        Drawing drawing = new Drawing(palette);
        int i = 10;
        int xoffset = 0;
        for (var color : palette.getColors()) {
            Grid grid = new Grid(60, i, image.getWidth(), image.getHeight(), xoffset);
            drawing.drawGrid(grid, image, color, 60);
            // i += 2;
            xoffset += 20;
        }

        var outputPath = Path.of("output-swarm4.svg");
        Files.writeString(outputPath, SvgSorter.sort(drawing.getSvg()));
    }
}

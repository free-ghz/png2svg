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
        var input = new File("input.png");
        BufferedImage inputImage = ImageIO.read(input);

        Image image = new Image(inputImage);
        Palette palette = Palettes.Stabilo.PASTELS.withWhite().with(Palettes.Stabilo.RANDOM);

        Drawing drawing = new Drawing();
        int i = 10;
        for (var color : palette.getColors()) {
            Grid grid = new Grid(20, i, image.getWidth(), image.getHeight());
            drawing.drawGrid(grid, image, palette, color, 20);
            i += 7;
        }

        var outputPath = Path.of("output-close-color-bl2b.svg");
        Files.writeString(outputPath, drawing.getSvg());

    }
}

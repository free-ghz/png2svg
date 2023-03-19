package es.sixey.png2svg;

import es.sixey.png2svg.color.Palette;
import es.sixey.png2svg.color.Palettes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        var input = new File("input.png");
        BufferedImage inputImage = ImageIO.read(input);

        Image image = new Image(inputImage);
        Palette palette = Palettes.Stabilo.PASTELS.withWhite();

        Drawing drawing = new Drawing();
        int i = 10;
        for (var color : palette.getColors()) {
            Grid grid = new Grid(30, i, image.getWidth(), image.getHeight());
            drawing.drawGrid(grid, image, palette, color, 60);
            i += 2;
        }

        var outputPath = Path.of("output-sch.svg");
        Files.writeString(outputPath, SvgSorter.sort(drawing.getSvg()));
    }
}

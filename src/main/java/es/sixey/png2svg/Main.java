package es.sixey.png2svg;

import es.sixey.png2svg.color.Color;
import es.sixey.png2svg.color.Palette;

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
        Palette palette = new Palette(Set.of(
                new Color(255, 255, 255),
                new Color(0 ,0 ,0),
                new Color(251, 147, 7),
                new Color(186, 110, 222),
                new Color(53, 168, 154)
        ));

        Drawing drawing = new Drawing();
        int i = 10;
        for (var color : palette.getColors()) {
            Grid grid = new Grid(20, i, image.getWidth(), image.getHeight());
            drawing.drawGrid(grid, image, palette, color, 20);
            i += 7;
        }

        var outputPath = Path.of("output-5.svg");
        Files.writeString(outputPath, drawing.getSvg());

    }
}

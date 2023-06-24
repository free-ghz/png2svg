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
        var time = System.currentTimeMillis();
        //var input = new File("input.png");
        var input = new File("jaja.png");
        BufferedImage inputImage = ImageIO.read(input);

        Image image = new Image(inputImage);
        var lines = LineFinder.getLines(image, 40);

        var palette = Palettes.Stabilo.PASTELS;
        Drawing drawing = new Drawing(palette);
        drawing.drawPaths(lines, image);

        var outputPath = Path.of("output-lines.svg");
        Files.writeString(outputPath, drawing.getSvg());
    }
}


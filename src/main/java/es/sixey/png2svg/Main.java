package es.sixey.png2svg;

import es.sixey.png2svg.color.Palettes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        final var outputName = "animal-horse-body";
        final var inputFilename = outputName + ".png";
        final var cutoffSizesToTry = List.of(4, 5, 6, 7);


        var input = new File(inputFilename);
        BufferedImage inputImage = ImageIO.read(input);
        var palette = Palettes.Stabilo.PASTELS;

        Image image = new Image(inputImage);
        for (var cutoff : cutoffSizesToTry) {
            var lines = LineFinder.getLines(image, cutoff);

            Drawing drawing = new Drawing(palette);
            drawing.drawPaths(lines, image);

            var outputFilename = "output-" + outputName + "-" + cutoff + ".svg";
            var outputPath = Path.of(outputFilename);
            Files.writeString(outputPath, SvgSorter.sort(drawing.getSvg()));
        }
    }
}


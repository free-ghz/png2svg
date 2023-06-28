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
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        var inputFilename = "0237-sleeper.png";
        var outputName = "0237-sleeper";


        var input = new File(inputFilename);
        BufferedImage inputImage = ImageIO.read(input);
        var palette = Palettes.Stabilo.PASTELS;

        Image image = new Image(inputImage);
        for (var cutoff : List.of(5, 10, 20, 40)) {
            var lines = LineFinder.getLines(image, cutoff);

            Drawing drawing = new Drawing(palette);
            drawing.drawPaths(lines, image);

            var outputFilename = "output-" + outputName + "-" + cutoff + ".svg";
            var outputPath = Path.of(outputFilename);
            Files.writeString(outputPath, SvgSorter.sort(drawing.getSvg()));
        }
    }
}


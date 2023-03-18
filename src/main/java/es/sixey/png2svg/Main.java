package es.sixey.png2svg;

import es.sixey.png2svg.color.Color;
import es.sixey.png2svg.color.Palette;

import javax.imageio.ImageIO;
import javax.swing.*;
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
        image.applyPalette(new Palette(Set.of(
                new Color(255, 0, 0),
                new Color(0, 0, 0),
                new Color(255, 255, 255),
                new Color(255, 255, 0)
        )));

        var outputImage = image.toBufferedImage();
        var output = new File("output.png");
        ImageIO.write(outputImage, "png", output);
    }
}

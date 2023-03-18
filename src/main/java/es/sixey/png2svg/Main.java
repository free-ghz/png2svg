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

        Drawing drawing = new Drawing();
        Grid grid = new Grid(20, 0, image.getWidth(), image.getHeight());
        drawing.drawGrid(grid, image, new Color(62, 53, 109), 30);

        grid = new Grid(20, 0, image.getWidth(), image.getHeight());
        drawing.drawGrid(grid, image, new Color(201, 220, 170), 30);

        grid = new Grid(20, 0, image.getWidth(), image.getHeight());
        drawing.drawGrid(grid, image, new Color(188, 157, 110), 30);

        var outputPath = Path.of("output.svg");
        Files.writeString(outputPath, drawing.getSvg());

    }
}

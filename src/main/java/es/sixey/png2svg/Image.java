package es.sixey.png2svg;

import es.sixey.png2svg.color.Color;
import es.sixey.png2svg.color.Palette;

import java.awt.image.BufferedImage;

public class Image {

    private final int width;
    private final int height;
    private final Color[][] colors;

    public Image(BufferedImage source) {
        width = source.getWidth();
        height = source.getHeight();
        colors = new Color[width][height];
        for (var y = 0; y < height; y++) {
            for (var x = 0; x < width; x++) {
                colors[x][y] = new Color(source.getRGB(x, y));
            }
        }
    }

    public void applyPalette(Palette palette) {
        for (var x = 0; x < width; x++) {
            for (var y = 0; y < height; y++) {
                colors[x][y] = palette.closest(colors[x][y]);
            }
        }
    }

    public BufferedImage toBufferedImage() {
        var image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (var y = 0; y < height; y++) {
            for (var x = 0; x < width; x++) {
                image.setRGB(x, y, colors[x][y].toInt());
            }
        }
        return image;
    }
}

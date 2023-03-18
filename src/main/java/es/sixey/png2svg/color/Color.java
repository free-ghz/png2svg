package es.sixey.png2svg.color;

public class Color {
    private final int r;
    private final int g;
    private final int b;

    public Color(int color) {
        r = (color & 0x00ff0000) >> 16;
        g = (color & 0x0000ff00) >> 8;
        b = color & 0x000000ff;
    }

    public Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int toInt() {
        return (r << 16) + (g << 8) + b;
    }

    public int distanceTo(Color other) {
        var rDist = Math.abs(r-other.r);
        var gDist = Math.abs(g-other.g);
        var bDist = Math.abs(b-other.b);
        return rDist + gDist + bDist;
    }
}

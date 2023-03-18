package es.sixey.png2svg.color;

public class Color {
    private final int r;
    private final int g;
    private final int b;
    private final float h;
    private final float s;
    private final float v;

    public Color(int color) {
        this((color & 0x00ff0000) >> 16, (color & 0x0000ff00) >> 8, color & 0x000000ff);
    }

    public Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;

        var hsbvals = java.awt.Color.RGBtoHSB(r, g, b, null);
        h = hsbvals[0];
        s = hsbvals[1];
        v = hsbvals[2];
    }

    public int toInt() {
        return (r << 16) + (g << 8) + b;
    }

    public int distanceTo(Color other) {
        var hDist = Math.abs(Math.sin(h * Math.PI * 2) - Math.sin(other.h * Math.PI * 2))/2;
        var sDist = Math.abs(s - other.s);
        var vDist = Math.abs(v - other.v);
        return (int)((hDist + sDist + vDist)*255);
    }
}

package es.sixey.png2svg.color;

public class Color {
    private final int r; // 255
    private final int g;
    private final int b;
    private final int lumen;
    private final float h; // 1.0
    private final float s;
    private final float v;

    private String name = null;

    public Color(int color) {
        this((color & 0x00ff0000) >> 16, (color & 0x0000ff00) >> 8, color & 0x000000ff);
    }

    public Color(int r, int g, int b, String name) {
        this(r, g, b);
        this.name = name;
    }

    public Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;

        lumen = (int)((0.299*(float)r) + (0.587*(float)g) + (0.114*(float)b));

        var hsbvals = java.awt.Color.RGBtoHSB(r, g, b, null);
        h = hsbvals[0];
        s = hsbvals[1];
        v = hsbvals[2];
    }

    public int toInt() {
        return (r << 16) + (g << 8) + b;
    }

    public java.awt.Color toJavaColor() {
        return new java.awt.Color(r, g, b);
    }

    public double distanceTo(Color other) {
        var hueDegrees = h * 360;
        var otherHueDegrees = other.h * 360;
        var hDist = Math.min(Math.abs(otherHueDegrees - hueDegrees), 360-Math.abs(otherHueDegrees - hueDegrees)) / 180;
        var sDist = Math.abs(s - other.s);
        var vDist = Math.abs(v - other.v);
        return Math.pow(hDist, 2) + Math.pow(sDist, 2) + Math.pow(vDist, 2) + Math.pow(((float)lumen - other.lumen)/255.0d, 2);
        // return ((float)Math.abs(lumen - other.lumen))/255.0f;
    }

    public Color add(Color other, double weight) {
        var blendR = blend(r, other.r, weight);
        var blendG = blend(g, other.g, weight);
        var blendB = blend(b, other.b, weight);
        return new Color(blendR, blendG, blendB);
    }

    private int blend(int v, int v2, double weight) {
        var counterWeight = 1 - weight;
        var fullTransformation = (v * v2) / 255d;
        return (int) ((fullTransformation * weight) + (v * counterWeight));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Color other) {
            return other.r == r && other.b == b && other.g == g;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return toInt();
    }

    @Override public String toString() {
        String base = "(" + r + ", " + g + ", " + b + ")";
        if (name != null) base = name + " " + base;
        return base;
    }
}

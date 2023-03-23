package es.sixey.png2svg.minimization;

public class Dimension {
    final double minPos;
    final double maxPos;
    final double minSpeed;
    final double maxSpeed;

    public Dimension(double minPos, double maxPos, double minSpeed, double maxSpeed) {
        this.minPos = minPos;
        this.maxPos = maxPos;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
    }
}
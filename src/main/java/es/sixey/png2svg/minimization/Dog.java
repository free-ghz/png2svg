package es.sixey.png2svg.minimization;

import java.util.List;
import java.util.Map;

public class Dog {
    private final List<Dimension> dimensions;
    private final Map<Dimension, Double> position;
    private final Map<Dimension, Double> speed;
    private final CostFunction costFunction;

    private double cost;

    public interface CostFunction {
        double cost(Map<Dimension, Double> position);
    }

    public Dog(List<Dimension> dimensions, Map<Dimension, Double> position, Map<Dimension, Double> speed, CostFunction costFunction){
        this.dimensions = dimensions;
        this.position = position;
        this.speed = speed;
        this.costFunction = costFunction;
        cost = costFunction.cost(position);
    }

    public void move() {
        dimensions.forEach(dimension -> position.put(
                dimension,
                Math.min(dimension.maxPos, Math.max(dimension.minPos, position.get(dimension) + speed.get(dimension)))
        ));
        cost = costFunction.cost(position);
    }

    public void affect(Dog other, double affinity) {
        var envy = cost - other.cost;
        if (envy <= 0) return;

        dimensions.forEach(dimension -> {
            var diff = other.position.get(dimension) - position.get(dimension);
            diff = Math.min(dimension.maxSpeed, Math.max(dimension.minSpeed, diff));
            diff *= affinity;
            speed.put(
                    dimension,
                    Math.min(dimension.maxSpeed, Math.max(dimension.minSpeed, speed.get(dimension) + diff))
            );
        });
    }

    public Map<Dimension, Double> getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return position.values().toString();
    }
}

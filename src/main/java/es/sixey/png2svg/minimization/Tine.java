package es.sixey.png2svg.minimization;

import es.sixey.png2svg.color.Color;
import es.sixey.png2svg.color.Palette;

import java.util.*;

// swarm from memory
public class Tine {

    private Palette palette;
    private double speed;
    private int tineSize;
    private int leaps;

    private Map<Color, Map<Color, Double>> memory = new HashMap<>();

    public Tine(Palette palette, double speed, int tineSize, int leaps) {
        this.palette = palette;
        this.speed = speed;
        this.tineSize = tineSize;
        this.leaps = leaps;
    }

    public Map<Color, Double> getWeightsForColor(Color target) {
        if (memory.containsKey(target)) {
            return memory.get(target);
        }

        var random = new Random();
        List<Dimension> dimensions = new ArrayList<>();
        Map<Dimension, Color> colorForDimension = new HashMap<>();
        for (Color color : palette.getColors()) {
            var dimension = new Dimension(0, 1, -speed, speed);
            dimensions.add(dimension);
            colorForDimension.put(dimension, color);
        }

        var costFunction = getCostFunction(target, colorForDimension);

        var dogs = new ArrayList<Dog>();
        for (var i = 0; i < tineSize; i++) {
            var dogPosition = new HashMap<Dimension, Double>();
            var dogSpeed = new HashMap<Dimension, Double>();
            for (var dimension : dimensions) {
                dogPosition.put(dimension, random.nextDouble(dimension.minPos, dimension.maxPos));
                dogSpeed.put(dimension, random.nextDouble(dimension.minSpeed, dimension.maxSpeed));
            }
            var dog = new Dog(dimensions, dogPosition, dogSpeed, costFunction);
            dogs.add(dog);
        }

        var affinity = 0.1;
        for (var i = 0; i < leaps; i++) {
            for (var dog1 : dogs) {
                for (var dog2 : dogs) {
                    if (dog1 == dog2) continue;
                    dog1.affect(dog2, affinity);
                }
            }
            for (var dog : dogs) {
                dog.move();
            }
        }

        var result = getWeights(dogs.get(0).getPosition(), colorForDimension);
        memory.put(target, result);
        return result;
    }

    private Map<Color, Double> getWeights(Map<Dimension, Double> weights, Map<Dimension, Color> colorForDimension) {
        double max = weights.values().stream().mapToDouble(a -> a).sum();
        Map<Color, Double> colorMap = new HashMap<>();
        weights.keySet().forEach(key -> colorMap.put(colorForDimension.get(key), weights.get(key)));
        return colorMap;
    }

    private Dog.CostFunction getCostFunction(Color target, Map<Dimension, Color> colorForDimension) {
        return (position) -> {
            var colorRepresentation = sum(position, colorForDimension);
            return colorRepresentation.distanceTo(target);
        };
    }

    private Color sum(Map<Dimension, Double> weights, Map<Dimension, Color> colorForDimension) {
        Color color = new Color(255, 255, 255);
        for (var dimension : weights.keySet()) {
            color = color.add(colorForDimension.get(dimension), weights.get(dimension));
        }
        return color;
    }

    private Color sum2(Map<Dimension, Double> weights, Map<Dimension, Color> colorForDimension) {
        Color color = new Color(255, 255, 255);
        for (var dimension : weights.keySet()) {
            color = color.add(colorForDimension.get(dimension), weights.get(dimension));
        }
        return color;
    }
}

package es.sixey.png2svg.minimization;

import es.sixey.png2svg.color.Color;
import es.sixey.png2svg.color.Palette;

import java.util.*;

// swarm from memory
public class Tine {

    public Map<Color, Double> getWeightsForColor(Color target, Palette palette, double speed, int tineSize, int leaps) {
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

        return getWeights(dogs.get(0).getPosition(), colorForDimension);
    }

    private Map<Color, Double> getWeights(Map<Dimension, Double> weights, Map<Dimension, Color> colorForDimension) {
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
        Color color = null;
        for (var dimension : weights.keySet()) {
            if (color == null) {
                color = colorForDimension.get(dimension);
            } else {
                color = color.add(colorForDimension.get(dimension));
            }
        }
        return color;
    }
}

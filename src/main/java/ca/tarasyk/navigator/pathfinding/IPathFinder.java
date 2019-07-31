package ca.tarasyk.navigator.pathfinding;

import ca.tarasyk.navigator.pathfinding.path.Path;
import ca.tarasyk.navigator.pathfinding.path.node.Node;

import java.util.Optional;
import java.util.function.BiFunction;

public interface IPathFinder<T extends Node<T, Double>> {
    Optional<Path<T>> search(T src, T dest, BiFunction<T, T, Double> heuristic);
    void resetPath();
}

package ca.tarasyk.navigator.pathfinding.path;

public interface Pathable<T> {
    /**
     * @return A path of type T
     */
    Path<T> pathFrom();
}
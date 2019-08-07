package ca.tarasyk.navigator.pathfinding.path;

public interface IPathable<T> {
    /**
     * @return A path of type T
     */
    Path<T> pathFrom();
}

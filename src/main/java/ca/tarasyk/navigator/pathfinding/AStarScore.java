package ca.tarasyk.navigator.pathfinding;

public class AStarScore<T> implements IScore<T> {

    private T fScore;
    private T gScore;

    AStarScore setGScore(T gScore) {
        this.gScore = gScore;
        return this;
    }

    AStarScore setFScore(T gScore) {
        this.fScore = fScore;
        return this;
    }

    public T getFScore() {
        return this.fScore;
    }

    public T getGScore() {
        return this.gScore;
    }
}

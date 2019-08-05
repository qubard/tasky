package ca.tarasyk.navigator.pathfinding;

public class AStarScore implements IScore<AStarScore> {

    private Double fScore;
    private Double gScore;

    AStarScore setGScore(Double gScore) {
        this.gScore = gScore;
        return this;
    }

    AStarScore setFScore(Double fScore) {
        this.fScore = fScore;
        return this;
    }

    public Double getFScore() {
        return this.fScore;
    }

    public Double getGScore() {
        return this.gScore;
    }

    @Override
    public AStarScore defaultValue() {
        return new AStarScore().setGScore(Double.MAX_VALUE).setFScore(Double.MAX_VALUE);
    }
}

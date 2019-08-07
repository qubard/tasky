package ca.tarasyk.navigator.pathfinding.path.node;

import ca.tarasyk.navigator.pathfinding.algorithm.score.AStarScore;

import java.util.Comparator;

public class PathNodeCompare implements Comparator<PathNode> {
    @Override
    public int compare(PathNode n1, PathNode n2) {
        AStarScore score = n1.getScore().get();
        AStarScore score2 = n2.getScore().get();
        return score.getFScore().compareTo(score2.getFScore());
    }
}

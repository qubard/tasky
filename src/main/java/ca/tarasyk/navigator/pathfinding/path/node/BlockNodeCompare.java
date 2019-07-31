package ca.tarasyk.navigator.pathfinding.path.node;

import ca.tarasyk.navigator.pathfinding.AStarScore;

import java.util.Comparator;

public class BlockNodeCompare implements Comparator<BlockNode> {
    @Override
    public int compare(BlockNode n1, BlockNode n2) {
        AStarScore score = n1.getScore().orElse(AStarScore.DEFAULT);
        AStarScore score2 = n2.getScore().orElse(AStarScore.DEFAULT);
        return score.getFScore().compareTo(score2.getFScore());
    }
}

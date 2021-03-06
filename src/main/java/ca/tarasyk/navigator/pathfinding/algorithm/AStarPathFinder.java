package ca.tarasyk.navigator.pathfinding.algorithm;

import ca.tarasyk.navigator.NavigatorMod;
import ca.tarasyk.navigator.NavigatorProvider;
import ca.tarasyk.navigator.pathfinding.algorithm.score.AStarScore;
import ca.tarasyk.navigator.pathfinding.path.BlockPosPath;
import ca.tarasyk.navigator.pathfinding.goal.Goal;
import ca.tarasyk.navigator.pathfinding.movement.Move;
import ca.tarasyk.navigator.pathfinding.node.PathNode;
import ca.tarasyk.navigator.pathfinding.node.PathNodeCompare;

import java.util.*;

public class AStarPathFinder implements PathFinder {

    private Long timeoutMs;
    private boolean hasFailed;

    public AStarPathFinder(Long timeoutMs) {
        this.timeoutMs = timeoutMs;
    }

    public boolean hasFailed() {
        return this.hasFailed;
    }

    private void setFailed(boolean failed) {
        this.hasFailed = failed;
    }

    public Optional<BlockPosPath> search(PathNode src, Goal goal) {
        Long start = System.currentTimeMillis();
        Queue<PathNode> openSet = new PriorityQueue<>(new PathNodeCompare());
        Set<PathNode> closedSet =  new HashSet<>();

        PathNode curr = src;
        src.setScore(new AStarScore().setGScore(0.0).setFScore(goal.heuristic(src.getPos())));
        openSet.add(src);

        setFailed(false);

        while (!openSet.isEmpty()) {
            curr = openSet.remove();

            // Did we meet the criteria for the goal?
            if (goal.metGoal(curr.getPos())) {
                PathNode dest = new PathNode(goal.getPos());
                dest.setParent(curr);
                NavigatorMod.printDebugMessage(closedSet.size() + " movements considered.");
                return Optional.of(new BlockPosPath(dest.pathFrom()));
            }

            // Return the best path so far if we timeout
            if (System.currentTimeMillis() - start > timeoutMs) {
                NavigatorMod.printDebugMessage(closedSet.size() + " movements considered, timeout");
                setFailed(true);
                return Optional.of(new BlockPosPath(curr.pathFrom()));
            }

            openSet.remove(curr);
            closedSet.add(curr);

            for (Move move : Move.moves){
                PathNode neighbor = move.apply(curr);

                if (closedSet.contains(neighbor)) {
                    continue;
                }

                Optional<Double> cost = Move.calculateCost(NavigatorProvider.getWorld(), curr, neighbor);

                if (cost.isPresent()) {
                    double neighborGScore = neighbor.getScore().get().getGScore();
                    double newGScore = curr.getScore().get().getGScore() + cost.get(); // curr always defined

                    if (newGScore < neighborGScore) {
                        neighbor.setParent(curr);
                        neighbor.setScore(new AStarScore().setGScore(newGScore).setFScore(newGScore + goal.heuristic(neighbor.getPos())));
                        openSet.add(neighbor);
                    } else if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        setFailed(true);
        NavigatorMod.printDebugMessage(closedSet.size() + " movements considered, but no path existed" + ", length:" + new BlockPosPath(curr.pathFrom()).getNodes().size());
        return Optional.of(new BlockPosPath(curr.pathFrom()));
    }
}

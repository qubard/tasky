package ca.tarasyk.navigator.pathfinding.algorithm;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.pathfinding.algorithm.score.AStarScore;
import ca.tarasyk.navigator.pathfinding.path.goals.Goal;
import ca.tarasyk.navigator.pathfinding.path.movement.Move;
import ca.tarasyk.navigator.pathfinding.path.node.PathNode;
import ca.tarasyk.navigator.pathfinding.path.node.PathNodeCompare;
import ca.tarasyk.navigator.pathfinding.path.Path;
import net.minecraft.client.Minecraft;

import java.util.*;

public class AStarPathFinder extends PathFinder {

    public AStarPathFinder(Goal goal) {
        super(goal);
    }

    @Override
    public Optional<Path<BetterBlockPos>> search(PathNode src) {
        Queue<PathNode> openSet = new PriorityQueue<>(new PathNodeCompare());
        Set<PathNode> closedSet = new HashSet<>();

        src.setScore(new AStarScore().setGScore(0.0).setFScore(goal.heuristic(src)));
        openSet.add(src);

        while (!openSet.isEmpty()) {
            PathNode curr = openSet.remove();

            // Did we meet the criteria for the goal?
            if (goal.metGoal(curr)) {
                System.out.println("Found goal");
                PathNode dest = new PathNode(goal.getPos());
                dest.setParent(curr);
                return Optional.of(curr.pathFrom());
            }

            openSet.remove(curr);
            closedSet.add(curr);

            for (Move move : Move.moves){
                PathNode neighbor = move.apply(curr);

                if (closedSet.contains(neighbor)) {
                    continue;
                }

                Optional<Double> weight = Move.calculateWeight(Minecraft.getMinecraft().world, curr, neighbor);

                if (weight.isPresent()) {
                    double neighborGScore = neighbor.getScore().get().getGScore();
                    double newGScore = curr.getScore().get().getGScore() + weight.get(); // curr always defined

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }

                    if (newGScore < neighborGScore) {
                        neighbor.setParent(curr);
                        neighbor.setScore(new AStarScore().setGScore(newGScore).setFScore(newGScore + goal.heuristic(neighbor)));
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return Optional.ofNullable(null);
    }
}

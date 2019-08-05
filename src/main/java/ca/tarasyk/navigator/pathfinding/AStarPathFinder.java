package ca.tarasyk.navigator.pathfinding;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.pathfinding.path.movement.Move;
import ca.tarasyk.navigator.pathfinding.path.node.PathNode;
import ca.tarasyk.navigator.pathfinding.path.node.PathNodeCompare;
import ca.tarasyk.navigator.pathfinding.path.Path;
import net.minecraft.client.multiplayer.WorldClient;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class AStarPathFinder implements IPathFinder<PathNode, BetterBlockPos> {

    private List<PathNode> currPath;

    public AStarPathFinder() {
        this.currPath = new ArrayList<>();
    }
    public void resetPath() {
        this.currPath.clear();
    }

    @Override
    public Optional<Path<BetterBlockPos>> search(PathNode src, PathNode dest, BiFunction<PathNode, PathNode, Double> heuristic) {
        Queue<PathNode> openSet = new PriorityQueue<>(new PathNodeCompare());
        Set<PathNode> closedSet = new HashSet<>();

        src.setScore(new AStarScore().setGScore(0.0).setFScore(heuristic.apply(src, dest)));
        openSet.add(src);

        while (!openSet.isEmpty()) {
            PathNode curr = openSet.remove(); // blocknode with lowest fscore

            // Return the path
            if (curr.equals(dest)) {
                System.out.println("Found solution");
                return Optional.of(curr.pathFrom());
            }

            openSet.remove(curr);
            closedSet.add(curr);

            List<PathNode> neighbors = Move.neighborsOf(curr);

            for (PathNode neighbor: neighbors){
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                double neighborGScore = neighbor.getScore().orElse(new AStarScore().defaultValue()).getGScore();
                double newGScore = curr.getScore().get().getGScore() + 0; // curr always defined

                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                }

                if (newGScore < neighborGScore) {
                    neighbor.setParent(curr);
                    neighbor.setScore(new AStarScore().setGScore(newGScore).setFScore(newGScore + heuristic.apply(neighbor, dest)));
                    openSet.add(neighbor);
                }
            }
        }

        return Optional.ofNullable(null);
    }
}

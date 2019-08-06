package ca.tarasyk.navigator.pathfinding;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.pathfinding.path.movement.Move;
import ca.tarasyk.navigator.pathfinding.path.node.PathNode;
import ca.tarasyk.navigator.pathfinding.path.node.PathNodeCompare;
import ca.tarasyk.navigator.pathfinding.path.Path;
import net.minecraft.client.Minecraft;

import java.util.*;
import java.util.function.BiFunction;

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
                dest.setParent(curr);
                return Optional.of(dest.pathFrom());
            }

            System.out.println("searching" + openSet.size());

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
                        neighbor.setScore(new AStarScore().setGScore(newGScore).setFScore(newGScore + heuristic.apply(neighbor, dest)));
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return Optional.ofNullable(null);
    }
}

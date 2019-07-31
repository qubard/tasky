package ca.tarasyk.navigator.pathfinding;

import ca.tarasyk.navigator.pathfinding.path.node.BlockNode;
import ca.tarasyk.navigator.pathfinding.path.node.BlockNodeCompare;
import ca.tarasyk.navigator.pathfinding.path.Path;
import net.minecraft.client.multiplayer.WorldClient;

import java.util.*;
import java.util.function.BiFunction;

public class AStarPathFinder implements IPathFinder<BlockNode> {

    private Optional<WorldClient> ctx;
    private List<BlockNode> currPath;

    public AStarPathFinder() {
        this.currPath = new ArrayList<>();
    }

    public void setWorldContext(WorldClient world) {
        this.ctx = Optional.of(world);
    }

    public void resetPath() {
        this.currPath.clear();
    }

    @Override
    public Optional<Path<BlockNode>> search(BlockNode src, BlockNode dest, BiFunction<BlockNode, BlockNode, Double> heuristic) {
        Queue<BlockNode> openSet = new PriorityQueue<>(new BlockNodeCompare());
        Set<BlockNode> closedSet = new HashSet<>();

        src.setScore(new AStarScore().setGScore(0).setFScore(heuristic.apply(src, dest)));
        openSet.add(src);

        while (!openSet.isEmpty()) {
            BlockNode curr = openSet.remove();

            // Return the path
            if (curr.equals(dest)) {
                return Optional.of(dest.pathFrom());
            }

            openSet.remove(curr);
            closedSet.add(curr);

            // TODO: implement this
            List<BlockNode> neighbors = new ArrayList<>();

            for (BlockNode neighbor: neighbors){
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                double neighborGScore = neighbor.getScore().orElse(AStarScore.DEFAULT).getGScore();
                double newGScore = curr.getScore().get().getGScore() + 0; // curr always defined

                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                } else if (newGScore < neighborGScore) {
                    neighbor.setParent(curr);
                    neighbor.setScore(new AStarScore().setGScore(newGScore).setFScore(newGScore + heuristic.apply(neighbor, dest)));
                    openSet.add(neighbor);
                }
            }
        }

        return null;
    }
}

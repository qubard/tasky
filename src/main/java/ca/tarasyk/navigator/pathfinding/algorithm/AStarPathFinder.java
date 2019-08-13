package ca.tarasyk.navigator.pathfinding.algorithm;

import ca.tarasyk.navigator.NavigatorProvider;
import ca.tarasyk.navigator.pathfinding.algorithm.score.AStarScore;
import ca.tarasyk.navigator.pathfinding.path.BlockPosPath;
import ca.tarasyk.navigator.pathfinding.goals.Goal;
import ca.tarasyk.navigator.pathfinding.movement.Move;
import ca.tarasyk.navigator.pathfinding.node.PathNode;
import ca.tarasyk.navigator.pathfinding.node.PathNodeCompare;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

import java.util.*;

public class AStarPathFinder extends PathFinder {

    private Long timeout;

    public AStarPathFinder(Goal goal, Long timeout) {
        super(goal);
        this.timeout = timeout;
    }

    public Optional<BlockPosPath> search(PathNode src) {
        Queue<PathNode> openSet = new PriorityQueue<>(new PathNodeCompare());
        Set<PathNode> closedSet = new HashSet<>();

        Long start = System.currentTimeMillis();

        src.setScore(new AStarScore().setGScore(0.0).setFScore(goal.heuristic(src.getPos())));
        openSet.add(src);

        PathNode curr = src;
        setFailed(false);

        while (!openSet.isEmpty()) {
            curr = openSet.remove();

            // Did we meet the criteria for the goal?
            if (goal.metGoal(curr.getPos())) {
                PathNode dest = new PathNode(goal.getPos());
                dest.setParent(curr);
                Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(closedSet.size() + " movements considered."));
                return Optional.of(new BlockPosPath(curr.pathFrom()));
            }

            // Return the best path so far if we timeout
            if (System.currentTimeMillis() - start > timeout) {
                Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(closedSet.size() + " movements considered, timeout"));
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

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }

                    if (newGScore < neighborGScore) {
                        neighbor.setParent(curr);
                        neighbor.setScore(new AStarScore().setGScore(newGScore).setFScore(newGScore + goal.heuristic(neighbor.getPos())));
                        openSet.add(neighbor);
                    }
                }
            }
        }

        setFailed(true);
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(closedSet.size() + " movements considered, but no path existed"));
        return Optional.of(new BlockPosPath(curr.pathFrom()));
    }
}

package ca.tarasyk.navigator.api.lua.func.action;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.NavigatorMod;
import ca.tarasyk.navigator.NavigatorProvider;
import ca.tarasyk.navigator.api.lua.LuaConstants;
import ca.tarasyk.navigator.api.lua.LuaExecutor;
import ca.tarasyk.navigator.pathfinding.algorithm.AStarPathFinder;
import ca.tarasyk.navigator.pathfinding.movement.PathRunner;
import ca.tarasyk.navigator.pathfinding.goal.Goal;
import ca.tarasyk.navigator.pathfinding.goal.GoalXZ;
import ca.tarasyk.navigator.pathfinding.node.PathNode;
import ca.tarasyk.navigator.pathfinding.path.BlockPosPath;
import ca.tarasyk.navigator.pathfinding.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MoveTo extends ThreeArgFunction {

    // The maximum number of times we try to find an alternate route
    private final int maxFailAttempts;

    public MoveTo(int maxFailAttempts) {
        this.maxFailAttempts = maxFailAttempts;
    }
    // rx, rz need to be object parameters for moveTo, same with the 5s

    /**
     * @param arg1 The x-coordinate
     * @param arg2 The y-coordinate
     * @param arg3 The z-coordinate
     * @return If we were able to pathfind to <x, y, z>
     */
    @Override
    public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
        int x = (int) Math.floor(arg1.checkdouble());
        int y = (int) Math.floor(arg2.checkdouble());
        int z = (int) Math.floor(arg3.checkdouble());
        EntityPlayer player = NavigatorProvider.getMinecraft().player;
        BetterBlockPos playerPos = PlayerUtil.inWaterReplacement(new BetterBlockPos(player.getPosition()));
        // Ensure playerPos is not in water
        Goal goal = new GoalXZ(x, z);
        boolean isLoaded = NavigatorProvider.getWorld().getChunkFromChunkCoords(x >> 4, z >> 4).isLoaded();
        NavigatorMod.printDebugMessage("Chunk loaded:" + isLoaded);

        if (isLoaded) {
            AStarPathFinder pathFinder = new AStarPathFinder((long) 3000);
            Future<Optional<BlockPosPath>> future = LuaExecutor.get().submit(() -> pathFinder.search(new PathNode(playerPos), goal));
            try {
                Optional<BlockPosPath> potentialPath = future.get();
                if (!pathFinder.hasFailed()) {
                    NavigatorMod.path = potentialPath.get();
                    PathRunner pathRunner = new PathRunner(potentialPath.get(), goal, 300);
                    // TODO: Use a dedicated pathRunner executor for this
                    Future pathRanFuture = LuaExecutor.get().submit(pathRunner);
                    pathRanFuture.get(); // Wait for pathRunner to finish executing
                    NavigatorMod.path = null;
                    // If the pathFinder failed it did not reach the last node
                    return LuaValue.valueOf(!pathFinder.hasFailed());
                }
            } catch (InterruptedException | ExecutionException e) {
                return LuaConstants.FALSE;
            }
        } else {
            // Traverse direction and splice
            int possibleX = (int) Math.floor(player.posX);
            int possibleZ = (int) Math.floor(player.posZ);

            int dx = x - possibleX;
            int dz = z - possibleZ;

            double angle = Math.atan2(dz, dx);

            // Find the limit to where the next chunk is unloaded
            // This is 5 chunks because we want to traverse in roughly d = 90 blocks
            // otherwise the algo is too slow
            possibleX += Math.floor(Math.cos(angle) * (double) (5 * 16));
            possibleZ += Math.floor(Math.sin(angle) * (double) (5 * 16));

            // 5 * 16 is the distance we want to go in that direction
            // so if we have the angle we do sin(angle) * dist for z
            // and cos(angle) * dist for x

            // Traverse to this point..
            try {
                Optional<BlockPosPath> potentialPath;
                Future<Optional<BlockPosPath>> future;
                Random random = new Random();
                int rx = 0;
                int rz = 0;
                int ox = 0;
                int oz = 0;
                int nAttempts = 0;
                do {
                    int tx = possibleX + ox + rx;
                    int tz = possibleZ + oz + rz;
                    NavigatorMod.printDebugMessage("Trying " + tx + "," + tz);
                    AStarPathFinder finalPathFinder = new AStarPathFinder((long) 5000);
                    future = LuaExecutor.get().submit(() -> finalPathFinder.search(new PathNode(playerPos), new GoalXZ(tx, tz)));
                    potentialPath = future.get();
                    if (!finalPathFinder.hasFailed()) {
                        break;
                    }
                    rx = random.nextInt(16) - 8;
                    rz = random.nextInt(16) - 8;
                    // Generate random offsets still in the direction we're going (basically moves up/down the line)
                    // This method of timeout reconciliation works, but we can also just pop off
                    // the closest we got and "go there"
                    // rx and rz are shifts which lets us go into a diff direction
                    int rMag = random.nextInt(64) - 24;
                    angle = Math.random() * Math.PI;
                    ox = (int) (rMag * Math.cos(angle));
                    oz = (int) (rMag * Math.sin(angle));
                    nAttempts++;
                } while (nAttempts < maxFailAttempts);

                if (nAttempts >= maxFailAttempts) {
                    return LuaConstants.FALSE;
                }

                NavigatorMod.path = potentialPath.get();
                PathRunner pathRunner = new PathRunner(potentialPath.get(), goal, 300);
                Future f = LuaExecutor.get().submit(pathRunner);
                f.get();
                // If the pathFinder failed it did not reach the last node
                // Recursively try to find the point again from the new position
                // We need a way of checking pathRunner failing
                return CoerceJavaToLua.coerce(call(arg1, arg2, arg3));
            } catch (InterruptedException | ExecutionException e) {
                return LuaConstants.FALSE;
            }
        }

        return LuaConstants.FALSE;
    }
}
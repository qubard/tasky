package ca.tarasyk.navigator.api.lua.func;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.NavigatorMod;
import ca.tarasyk.navigator.NavigatorProvider;
import ca.tarasyk.navigator.pathfinding.algorithm.AStarPathFinder;
import ca.tarasyk.navigator.pathfinding.algorithm.PathRunner;
import ca.tarasyk.navigator.pathfinding.goals.Goal;
import ca.tarasyk.navigator.pathfinding.goals.GoalXYZ;
import ca.tarasyk.navigator.pathfinding.goals.GoalXZ;
import ca.tarasyk.navigator.pathfinding.movement.Move;
import ca.tarasyk.navigator.pathfinding.node.PathNode;
import ca.tarasyk.navigator.pathfinding.path.BlockPosPath;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MoveTo extends ThreeArgFunction {
    @Override
    public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
        int x = arg1.checkint();
        int y = arg2.checkint();
        int z = arg3.checkint();
        EntityPlayer player = Minecraft.getMinecraft().player;
        BetterBlockPos playerPos = new BetterBlockPos(player.getPosition());
        Goal goal = new GoalXZ(x, z);
        boolean isLoaded = NavigatorProvider.getWorld().getChunkFromChunkCoords(x >> 4, z >> 4).isLoaded();
        NavigatorMod.printDebugMessage("Chunk loaded:" + isLoaded);

        if (isLoaded) {
            AStarPathFinder pathFinder = new AStarPathFinder(goal, (long) 3000);
            Future<Optional<BlockPosPath>> future = NavigatorMod.executorService.submit(() -> pathFinder.search(new PathNode(playerPos)));
            try {
                Optional<BlockPosPath> potentialPath = future.get();
                if (!pathFinder.hasFailed()) {
                    NavigatorMod.path = potentialPath.get();
                    PathRunner pathRunner = new PathRunner(potentialPath.get(), goal);
                    Future f = NavigatorMod.executorService.submit(pathRunner);
                    f.get();
                    // If the pathFinder failed it did not reach the last node
                    return CoerceJavaToLua.coerce(!pathFinder.hasFailed());
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            // Traverse direction and splice
            int chunkX = (int) player.posX >> 4;
            int chunkZ = (int) player.posZ >> 4;

            // This is technically slightly off since player is not in block coordinates, but whatever
            int dirX = Integer.compare(x - (int) player.posX, 0);
            int dirZ = Integer.compare(z - (int) player.posZ, 0);

            // Find the limit to where the next chunk is unloaded
            // This is 5 chunks because we want to traverse in roughly d = 90 blocks
            // otherwise the algo is too slow
            chunkX += dirX * 5;
            chunkZ += dirZ * 5;

            // Traverse to this point..
            try {
                System.out.println(NavigatorProvider.getWorld().getChunkFromChunkCoords(chunkX, chunkZ).isLoaded() + "");
                AStarPathFinder pathFinder;
                Optional<BlockPosPath> potentialPath;
                Future<Optional<BlockPosPath>> future;
                Random random = new Random();
                int rx = random.nextInt(48);
                int rz = random.nextInt(48);
                int nAttempts = 0;
                do {
                    int tx = chunkX * 16 + 8 - dirX * rx;
                    int tz = chunkZ * 16 + 8 - dirZ * rz;
                    goal = new GoalXZ(tx, tz);
                    pathFinder = new AStarPathFinder(goal, (long) 2000);
                    AStarPathFinder finalPathFinder = pathFinder;
                    future = NavigatorMod.executorService.submit(() -> finalPathFinder.search(new PathNode(playerPos)));
                    potentialPath = future.get();
                    rx = random.nextInt(48);
                    rz = random.nextInt(48);
                    nAttempts++;
                } while (pathFinder.hasFailed() && nAttempts < 30);

                if (nAttempts >= 30) {
                    return CoerceJavaToLua.coerce(false);
                }

                NavigatorMod.path = potentialPath.get();
                PathRunner pathRunner = new PathRunner(potentialPath.get(), goal);
                Future f = NavigatorMod.executorService.submit(pathRunner);
                f.get();
                // If the pathFinder failed it did not reach the last node
                // Recursion :)
                return CoerceJavaToLua.coerce(call(arg1, arg2, arg3));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return CoerceJavaToLua.coerce(false);
    }
}
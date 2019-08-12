package ca.tarasyk.navigator.api.lua;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.NavigatorMod;
import ca.tarasyk.navigator.NavigatorProvider;
import ca.tarasyk.navigator.pathfinding.algorithm.AStarPathFinder;
import ca.tarasyk.navigator.pathfinding.algorithm.PathRunner;
import ca.tarasyk.navigator.pathfinding.path.BlockPosPath;
import ca.tarasyk.navigator.pathfinding.path.goals.Goal;
import ca.tarasyk.navigator.pathfinding.path.goals.GoalXZ;
import ca.tarasyk.navigator.pathfinding.path.node.PathNode;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class HookLib extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue root = tableOf();
        LuaValue table = tableOf();
        table.set("add", new HookAdd());
        env.set("hook", table);
        env.set("printChat", new PrintChat());
        env.set("moveTo", new MoveTo());
        return root;
    }

    static class PrintChat extends OneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            if (NavigatorProvider.getPlayer() != null) {
                NavigatorMod.printDebugMessage(arg.checkstring().tojstring());
            }
            return null;
        }
    }

    static class MoveTo extends ThreeArgFunction {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
            int x = arg1.checkint();
            int y = arg2.checkint();
            int z = arg3.checkint();
            EntityPlayer player = Minecraft.getMinecraft().player;
            BetterBlockPos playerPos = new BetterBlockPos(player.getPosition());
            Goal goal = new GoalXZ(new BetterBlockPos(x, y, z));
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
                } else {
                    NavigatorMod.printDebugMessage("pathfinder failed hasFailed()");
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            return CoerceJavaToLua.coerce(false);
        }
    }

    /**
     * Add the hooked LuaFunction to the hook table
     */
    static class HookAdd extends TwoArgFunction {
        @Override
        public LuaValue call(LuaValue hook, LuaValue func) {
            HookProvider.getProvider().addHook(Hook.hookForName(hook.checkjstring()).get(), func.checkfunction());
            return null;
        }
    }
}

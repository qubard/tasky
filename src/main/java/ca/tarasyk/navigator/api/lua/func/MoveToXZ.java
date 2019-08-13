package ca.tarasyk.navigator.api.lua.func;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.NavigatorMod;
import ca.tarasyk.navigator.NavigatorProvider;
import ca.tarasyk.navigator.pathfinding.algorithm.AStarPathFinder;
import ca.tarasyk.navigator.pathfinding.algorithm.PathRunner;
import ca.tarasyk.navigator.pathfinding.goals.Goal;
import ca.tarasyk.navigator.pathfinding.goals.GoalXYZ;
import ca.tarasyk.navigator.pathfinding.node.PathNode;
import ca.tarasyk.navigator.pathfinding.path.BlockPosPath;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MoveToXZ extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue arg1, LuaValue arg2) {
        return null;
    }
}

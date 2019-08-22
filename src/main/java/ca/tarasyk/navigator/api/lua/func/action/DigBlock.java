package ca.tarasyk.navigator.api.lua.func.action;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.NavigatorMod;
import ca.tarasyk.navigator.api.lua.LuaConstants;
import ca.tarasyk.navigator.pathfinding.movement.Move;
import ca.tarasyk.navigator.pathfinding.util.PlayerUtil;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;

public class DigBlock extends ThreeArgFunction {
    private final long timeoutMs;

    public DigBlock(long timeoutMs) {
        this.timeoutMs = timeoutMs;
    }

    @Override
    public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
        int x = (int) Math.floor(arg1.checkdouble());
        int y = (int) Math.floor(arg2.checkdouble());
        int z = (int) Math.floor(arg3.checkdouble());

        BetterBlockPos pos = new BetterBlockPos(x, y, z);

        if (Move.isAir(pos)) {
            return LuaConstants.TRUE;
        }

        if (!PlayerUtil.canReach(x, y, z)) {
            return LuaConstants.FALSE;
        }

        // Block completely occluded
        if (Move.isOccluded(pos)) {
            return LuaConstants.FALSE;
        }

        long start = System.currentTimeMillis();
        PlayerUtil.lookAtXYZ(pos);
        PlayerUtil.attack(true);

        // Destroy the block
        while (!Thread.currentThread().isInterrupted() && Move.isStrictlySolid(pos)) {
            if (!PlayerUtil.canReach(x, y, z) || Move.isAir(pos) || (System.currentTimeMillis() - start >= timeoutMs)) {
                PlayerUtil.attack(false);
                return LuaConstants.FALSE;
            }
        }

        PlayerUtil.attack(false);
        return LuaConstants.TRUE;
    }
}

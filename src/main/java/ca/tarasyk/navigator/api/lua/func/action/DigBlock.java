package ca.tarasyk.navigator.api.lua.func.action;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.api.lua.LuaConstants;
import ca.tarasyk.navigator.pathfinding.movement.Move;
import ca.tarasyk.navigator.pathfinding.util.PlayerUtil;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;

public class DigBlock extends ThreeArgFunction {
    @Override
    public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
        int x = (int) Math.floor(arg1.checkdouble());
        int y = (int) Math.floor(arg2.checkdouble());
        int z = (int) Math.floor(arg3.checkdouble());

        if (!PlayerUtil.canReach(x, y, z)) {
            return LuaConstants.FALSE;
        }

        BetterBlockPos pos = new BetterBlockPos(x, y, z);

        // Block completely occluded
        if (Move.isStrictlySolid(pos.east()) && Move.isStrictlySolid(pos.west()) && Move.isStrictlySolid(pos.up()) && Move.isStrictlySolid(pos.down())) {
            return LuaConstants.FALSE;
        }

        PlayerUtil.lookAtXYZ(pos);
        PlayerUtil.attack(true);

        while (Move.isStrictlySolid(pos)) {
            if (!PlayerUtil.canReach(x, y, z)) {
                PlayerUtil.attack(false);
                return LuaConstants.FALSE;
            }
        } // Need a Future wait until thing..

        PlayerUtil.attack(false);
        return LuaConstants.TRUE;
    }
}

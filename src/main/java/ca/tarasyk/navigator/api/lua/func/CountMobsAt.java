package ca.tarasyk.navigator.api.lua.func;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.NavigatorProvider;
import ca.tarasyk.navigator.pathfinding.util.PlayerUtil;
import net.minecraft.entity.Entity;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;

import java.util.List;

public class CountMobsAt extends ThreeArgFunction {
    @Override
    public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
        int x = (int) Math.floor(arg1.checkdouble());
        int y = (int) Math.floor(arg2.checkdouble());
        int z = (int) Math.floor(arg3.checkdouble());

        BetterBlockPos pos = new BetterBlockPos(x, y, z);
        List<Entity> loadedEnts = NavigatorProvider.getWorld().getLoadedEntityList();
        int count = loadedEnts.stream().mapToInt(ent -> PlayerUtil.entityAtXYZ(ent, pos) ? 1 : 0).sum();
        return LuaValue.valueOf(count);
    }
}

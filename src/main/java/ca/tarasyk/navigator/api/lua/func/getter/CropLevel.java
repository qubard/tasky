package ca.tarasyk.navigator.api.lua.func.getter;

import ca.tarasyk.navigator.NavigatorProvider;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;

public class CropLevel extends ThreeArgFunction {
    @Override
    public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
        int x = (int) Math.floor(arg1.checkdouble());
        int y = (int) Math.floor(arg2.checkdouble());
        int z = (int) Math.floor(arg3.checkdouble());
        IBlockState state = NavigatorProvider.getWorld().getBlockState(new BlockPos(x, y, z));

        if (!(state.getBlock() instanceof BlockCrops)) {
            return LuaValue.valueOf(-1);
        }

        return LuaValue.valueOf((state.getValue(BlockCrops.AGE)).intValue());
    }
}

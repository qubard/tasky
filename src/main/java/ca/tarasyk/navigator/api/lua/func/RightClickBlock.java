package ca.tarasyk.navigator.api.lua.func;

import ca.tarasyk.navigator.NavigatorProvider;
import ca.tarasyk.navigator.api.lua.LuaConstants;
import ca.tarasyk.navigator.pathfinding.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;

public class RightClickBlock extends ThreeArgFunction {
    /**
     * @param arg1 The x-coordinate
     * @param arg2 The y-coordinate
     * @param arg3 The z-coordinate
     * @return If we were able to right click the block
     */
    @Override
    public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
        int x = (int) Math.floor(arg1.checkdouble());
        int y = (int) Math.floor(arg2.checkdouble());
        int z = (int) Math.floor(arg3.checkdouble());
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        WorldClient world = NavigatorProvider.getWorld();
        // Too far, can't reach
        float reach = NavigatorProvider.getMinecraft().playerController.getBlockReachDistance();
        if (Math.sqrt(player.getDistanceSqToCenter(new BlockPos(x, y, z))) > reach) {
            return LuaConstants.FALSE;
        }
        PlayerUtil.lookAtXYZ(x, y, z);
        EnumActionResult result = NavigatorProvider.getMinecraft().playerController.processRightClickBlock(player, world,
                new BlockPos(x, y, z), EnumFacing.DOWN, player.getLookVec(), EnumHand.MAIN_HAND);
        return LuaValue.valueOf(result == EnumActionResult.SUCCESS);
    }
}

package ca.tarasyk.navigator.api.lua.func.action;

import ca.tarasyk.navigator.NavigatorMod;
import ca.tarasyk.navigator.NavigatorProvider;
import ca.tarasyk.navigator.api.lua.LuaConstants;
import ca.tarasyk.navigator.pathfinding.util.PlayerUtil;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import java.util.Optional;

public class MoveStack extends TwoArgFunction {

    private int waitDelayMs;

    public MoveStack(int waitDelayMs) {
        this.waitDelayMs = waitDelayMs;
    }

    @Override
    public LuaValue call(LuaValue arg1, LuaValue arg2) {
        int slotSrc = arg1.checkint();
        int slotDest = arg2.checkint();

        if (slotSrc < 0 || slotDest < 0) {
            return LuaConstants.FALSE;
        }

        Optional<Container> container = Optional.ofNullable(NavigatorProvider.getPlayer().openContainer);

        if (!container.isPresent()) {
            return LuaConstants.FALSE;
        }

        NonNullList<ItemStack> inventory = container.get().getInventory();
        ItemStack srcStk = inventory.get(slotSrc);
        ItemStack dstStk = inventory.get(slotDest);

        // Can't move to a slot already taken, or src already empty
        if (srcStk.getItem().equals(Items.AIR) || !dstStk.getItem().equals(Items.AIR) ) {
            return LuaConstants.FALSE;
        }

        PlayerUtil.moveStack(container.get(), slotSrc, slotDest, waitDelayMs);

        return LuaConstants.TRUE;
    }
}

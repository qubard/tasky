package ca.tarasyk.navigator.api.lua.func.action;

import ca.tarasyk.navigator.NavigatorProvider;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class PutInContainer extends OneArgFunction {
    /**
     * @param arg1 The integer slot id
     * @return Whether or not the stack was placed into the chest
     */
    @Override
    public LuaValue call(LuaValue arg1) {
        int slot = Math.max(0, arg1.checkint());
        // Shift click into the container
        NavigatorProvider.getMinecraft().playerController.windowClick(
                0,
                slot,
                0,
                ClickType.QUICK_MOVE,
                NavigatorProvider.getPlayer());

        // If it was put, then the slot is now empty
        return LuaValue.valueOf(NavigatorProvider.getPlayer().inventory.getStackInSlot(slot) == ItemStack.EMPTY);
    }
}

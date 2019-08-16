package ca.tarasyk.navigator.api.lua.func.action;

import ca.tarasyk.navigator.NavigatorProvider;
import ca.tarasyk.navigator.api.lua.LuaConstants;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

public class MoveStack extends TwoArgFunction {

    private int waitDelayMs;

    public MoveStack(int waitDelayMs) {
        this.waitDelayMs = waitDelayMs;
    }

    @Override
    public LuaValue call(LuaValue arg1, LuaValue arg2) {
        int slotSrc = Math.max(0, arg1.checkint());
        int slotDest = Math.max(0, arg2.checkint());

        InventoryPlayer inventory = NavigatorProvider.getPlayer().inventory;
        ItemStack srcStk = inventory.getStackInSlot(slotSrc);

        NavigatorProvider.getMinecraft().playerController.windowClick(
                0,
                slotSrc,
                0,
                ClickType.PICKUP,
                NavigatorProvider.getPlayer());

        // We need to wait before dropping, cause otherwise it happened too fast
        try {
            Thread.sleep(waitDelayMs);
        } catch (InterruptedException e) {
            return LuaConstants.FALSE;
        }

        NavigatorProvider.getMinecraft().playerController.windowClick(
                0,
                slotDest,
                0,
                ClickType.PICKUP,
                NavigatorProvider.getPlayer());

        return LuaValue.valueOf(inventory.getStackInSlot(slotSrc) == ItemStack.EMPTY && inventory.getStackInSlot(slotDest) == srcStk);
    }
}

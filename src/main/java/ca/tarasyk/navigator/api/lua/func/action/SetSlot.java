package ca.tarasyk.navigator.api.lua.func.action;

import ca.tarasyk.navigator.NavigatorProvider;
import ca.tarasyk.navigator.pathfinding.util.PlayerUtil;
import net.minecraft.inventory.ClickType;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class SetSlot extends OneArgFunction {
    @Override
    public LuaValue call(LuaValue arg) {
        int slot = arg.checkint();
        // See https://wiki.vg/Inventory
        if (slot >= 36 && slot < 44) {
            NavigatorProvider.getPlayer().inventory.currentItem = slot - 36;
        } else {
            // Move the item into the current slot :)
            NavigatorProvider.getMinecraft().playerController.windowClick(
                    NavigatorProvider.getPlayer().openContainer.windowId,
                    slot,
                    PlayerUtil.getCurrentSlot(),
                    ClickType.SWAP,
                    NavigatorProvider.getPlayer());
        }
        return null;
    }
}

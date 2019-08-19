package ca.tarasyk.navigator.api.lua.func.getter;

import ca.tarasyk.navigator.NavigatorProvider;
import ca.tarasyk.navigator.api.lua.LuaConstants;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class InventoryHas extends OneArgFunction {
    @Override
    public LuaValue call(LuaValue arg) {
        String searchName = arg.checkjstring();
        NonNullList<net.minecraft.item.ItemStack> stacks = NavigatorProvider.getPlayer().openContainer.getInventory();
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack stk = stacks.get(i);
            if (stk.getDisplayName().equals(searchName)) {
                return LuaConstants.TRUE;
            }
        }
        return LuaConstants.FALSE;
    }
}

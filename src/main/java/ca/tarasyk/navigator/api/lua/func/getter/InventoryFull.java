package ca.tarasyk.navigator.api.lua.func.getter;

import ca.tarasyk.navigator.NavigatorProvider;
import ca.tarasyk.navigator.api.lua.LuaConstants;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

public class InventoryFull extends ZeroArgFunction {
    @Override
    public LuaValue call() {
        NonNullList<ItemStack> inventory = NavigatorProvider.getPlayer().inventory.mainInventory;
        for (ItemStack stk : inventory) {
            if (stk.getItem().equals(Items.AIR)) {
                return LuaConstants.FALSE;
            }
        }
        return LuaConstants.TRUE;
    }
}

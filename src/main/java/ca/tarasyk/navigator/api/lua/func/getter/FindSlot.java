package ca.tarasyk.navigator.api.lua.func.getter;

import ca.tarasyk.navigator.NavigatorProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

/**
 * Find the slot associated with the search name
 */
public class FindSlot extends OneArgFunction {
    @Override
    public LuaValue call(LuaValue arg) {
        String searchName = arg.checkjstring();
        NonNullList<ItemStack> stacks = NavigatorProvider.getPlayer().openContainer.getInventory();
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack stk = stacks.get(i);
            if (stk.getDisplayName().equals(searchName)) {
                return LuaValue.valueOf(i);
            }
        }
        return LuaValue.valueOf(-1);
    }
}

package ca.tarasyk.navigator.api.lua.func.getter;

import ca.tarasyk.navigator.NavigatorProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class CountStacks extends OneArgFunction {
    @Override
    public LuaValue call(LuaValue arg) {
        String searchName = arg.checkjstring();
        NonNullList<net.minecraft.item.ItemStack> stacks = NavigatorProvider.getPlayer().openContainer.getInventory();
        int totalSize = 0;
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack stk = stacks.get(i);
            if (stk.getDisplayName().equals(searchName)) {
                totalSize += stk.getCount();
            }
        }
        return LuaValue.valueOf(totalSize)
    }
}

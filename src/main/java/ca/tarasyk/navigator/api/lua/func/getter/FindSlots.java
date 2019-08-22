package ca.tarasyk.navigator.api.lua.func.getter;

import ca.tarasyk.navigator.NavigatorProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.ArrayList;

public class FindSlots extends OneArgFunction {
    /**
     * @param arg The searched for item stack's name
     * @return A list of integers containing each matched slot
     */
    @Override
    public LuaValue call(LuaValue arg) {
        String searchTerm = arg.checkjstring();
        NonNullList<ItemStack> stacks = NavigatorProvider.getPlayer().openContainer.getInventory();
        ArrayList<Integer> slots = new ArrayList<>();
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack stk = stacks.get(i);
            if (stk.getDisplayName().matches(searchTerm)) {
                slots.add(i);
            }
        }
        return CoerceJavaToLua.coerce(slots);
    }
}

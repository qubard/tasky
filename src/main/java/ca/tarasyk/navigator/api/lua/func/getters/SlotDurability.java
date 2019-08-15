package ca.tarasyk.navigator.api.lua.func.getters;

import ca.tarasyk.navigator.NavigatorProvider;
import net.minecraft.item.ItemStack;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class SlotDurability extends OneArgFunction {
    @Override
    public LuaValue call(LuaValue arg) {
        int slot = arg.checkint();
        ItemStack stk = NavigatorProvider.getPlayer().inventory.getStackInSlot(slot);
        return LuaValue.valueOf(stk.getMaxDamage() - stk.getItemDamage());
    }
}

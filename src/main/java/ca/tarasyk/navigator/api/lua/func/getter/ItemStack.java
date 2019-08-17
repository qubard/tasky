package ca.tarasyk.navigator.api.lua.func.getter;

import ca.tarasyk.navigator.NavigatorProvider;
import net.minecraft.inventory.Container;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.Optional;

public class ItemStack extends OneArgFunction {
    @Override
    public LuaValue call(LuaValue arg) {
        int slot = arg.checkint();
        Optional<Container> container = Optional.ofNullable(NavigatorProvider.getPlayer().openContainer);

        if (!container.isPresent()) {
            return null;
        }

        return CoerceJavaToLua.coerce(container.get().getInventory().get(slot));
    }
}

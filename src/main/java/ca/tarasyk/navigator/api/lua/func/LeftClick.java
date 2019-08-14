package ca.tarasyk.navigator.api.lua.func;

import ca.tarasyk.navigator.NavigatorProvider;
import net.minecraft.client.settings.KeyBinding;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

public class LeftClick extends ZeroArgFunction {
    @Override
    public LuaValue call() {
        KeyBinding.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindAttack.getKeyCode(), true);
        return null;
    }
}

package ca.tarasyk.navigator.api.lua.func.action;

import ca.tarasyk.navigator.NavigatorProvider;
import net.minecraft.client.settings.KeyBinding;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class LeftClick extends OneArgFunction {
    @Override
    public LuaValue call(LuaValue arg) {
        long holdTimeMs = Math.max(0, arg.checklong());
        try {
            Thread.sleep(holdTimeMs);
        } catch (InterruptedException e) {}
        KeyBinding.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindAttack.getKeyCode(), false);
        return null;
    }
}

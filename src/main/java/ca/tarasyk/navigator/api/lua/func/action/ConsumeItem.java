package ca.tarasyk.navigator.api.lua.func.action;

import ca.tarasyk.navigator.NavigatorProvider;
import net.minecraft.client.settings.KeyBinding;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class ConsumeItem extends OneArgFunction {
    @Override
    public LuaValue call(LuaValue arg1) {
        long holdTimeMs = Math.max(0, arg1.checklong());
        KeyBinding.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindUseItem.getKeyCode(), true);
        try {
            Thread.sleep(holdTimeMs);
        } catch (InterruptedException e) {}
        KeyBinding.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindUseItem.getKeyCode(), false);
        return null;
    }
}

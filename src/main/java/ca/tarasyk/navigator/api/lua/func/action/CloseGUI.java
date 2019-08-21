package ca.tarasyk.navigator.api.lua.func.action;

import ca.tarasyk.navigator.NavigatorProvider;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class CloseGUI extends OneArgFunction {
    /**
     * @param arg The time in milliseconds to wait before closing
     * @return Nothing, but close any open GUI
     */
    @Override
    public LuaValue call(LuaValue arg) {
        long waitTimeMs = Math.max(0, arg.checklong());
        long start = System.currentTimeMillis();
        while (!Thread.currentThread().isInterrupted() &&
                NavigatorProvider.getMinecraft().currentScreen == null && (System.currentTimeMillis() - start < waitTimeMs)) { }
        NavigatorProvider.getPlayer().closeScreen();
        NavigatorProvider.getMinecraft().displayGuiScreen(null);
        return null;
    }
}

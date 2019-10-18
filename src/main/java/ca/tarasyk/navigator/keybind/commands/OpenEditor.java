package ca.tarasyk.navigator.keybind.commands;

import ca.tarasyk.navigator.NavigatorProvider;
import ca.tarasyk.navigator.gui.editor.GuiEditor;
import ca.tarasyk.navigator.keybind.KeyCommand;

public class OpenEditor extends KeyCommand {
    public OpenEditor(String description, int keyCode, String category) {
        super(description, keyCode, category);
    }

    public void onPressed() {
        NavigatorProvider.getMinecraft().displayGuiScreen(new GuiEditor("test.lua", 16, NavigatorProvider.getMinecraft().gameSettings, NavigatorProvider.getMinecraft().getTextureManager()));
    }
}

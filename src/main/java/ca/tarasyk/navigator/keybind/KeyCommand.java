package ca.tarasyk.navigator.keybind;

import net.minecraft.client.settings.KeyBinding;

public abstract class KeyCommand extends KeyBinding {
    public KeyCommand(String description, int keyCode, String category) {
        super(description, keyCode, category);
    }

    public abstract void onPressed();
}

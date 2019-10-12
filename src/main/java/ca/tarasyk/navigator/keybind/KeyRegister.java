package ca.tarasyk.navigator.keybind;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.util.ArrayList;
import java.util.List;

public class KeyRegister {

    private List<KeyCommand> commands;

    public KeyRegister() {
        commands = new ArrayList<>();
    }

    public boolean addCommand(KeyCommand command) {
        if (!commands.contains(command)) {
            ClientRegistry.registerKeyBinding(command);
            commands.add(command);
            return true;
        }
        return false;
    }

    public void onKeyEvent(InputEvent.KeyInputEvent e) {
        for (KeyCommand command : commands) {
            if (command.isPressed()) {
                command.onPressed();
            }
        }
    }

}

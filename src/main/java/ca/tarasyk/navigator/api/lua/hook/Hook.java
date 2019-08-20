package ca.tarasyk.navigator.api.lua.hook;

import java.util.Arrays;
import java.util.Optional;

public enum Hook {
    CHAT("onChat"),
    LIVING_HURT("onLivingHurt"),
    PLAY_SOUND_AT_ENTITY("onPlaySoundAtEntity");

    private final String hookName;

    Hook(String hookName) {
        this.hookName = hookName;
    }

    public String getName() {
        return this.hookName;
    }

    public static Optional<Hook> hookForName(String hookName) {
        return Arrays.stream(Hook.values()).filter(hook -> hook.getName().equals(hookName)).findFirst();
    }
}

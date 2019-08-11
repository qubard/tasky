package ca.tarasyk.navigator.api.lua;

import java.util.Arrays;
import java.util.Optional;

public enum Hook {
    ON_CHAT("onChat"),
    ON_LIVING_HURT("onLivingHurt");

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

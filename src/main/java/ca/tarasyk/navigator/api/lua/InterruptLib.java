package ca.tarasyk.navigator.api.lua;

import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.DebugLib;

/**
 * InterruptLib is a simple way to inject interrupts
 * in the Lua VM on each instruction
 */
public class InterruptLib extends DebugLib {
    private boolean interrupted = false;

    @Override
    public void onInstruction(int pc, Varargs v, int top) {
        if (interrupted) {
            throw new ScriptInterruptException();
        }
        super.onInstruction(pc, v, top);
    }

    public void setInterrupted(boolean interrupted) {
        this.interrupted = interrupted;
    }

    public static class ScriptInterruptException extends RuntimeException {}
}

package ca.tarasyk.navigator.gui.editor.scroll;

public interface MouseInteract {
    boolean isMousedOver(int mouseX, int mouseY);
    boolean onMouseDrag(int mouseX, int mouseY);
    boolean onMouseReleasE(int mouseX, int mouseY);
}

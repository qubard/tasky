package ca.tarasyk.navigator.gui.editor.scroll;

public interface MouseInteract {
    boolean isMousedOver(int mouseX, int mouseY);
    void onMouseDrag(int mouseX, int mouseY);
    void onMouseRelease(int mouseX, int mouseY);
    void onMousePress(int mouseX, int mouseY);
    boolean isDragged();
}

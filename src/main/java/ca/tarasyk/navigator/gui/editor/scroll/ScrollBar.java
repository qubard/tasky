package ca.tarasyk.navigator.gui.editor.scroll;

import net.minecraft.client.gui.GuiScreen;

public class ScrollBar extends GuiScreen implements MouseInteract {

    private int bgColor, sizePx;
    private boolean vertical;

    private int currIndex, windowSize, maxIndex, maxSizePx;

    public ScrollBar(int bgColor, int sizePx, boolean vertical) {
        this.bgColor = bgColor;
        this.sizePx = sizePx;
        this.vertical = vertical;
    }

    public int getSize() {
        return this.sizePx;
    }

    public void update(int currIndex, int windowSize, int maxIndex, int maxSizePx) {
        this.currIndex = currIndex;
        this.windowSize = windowSize;
        this.maxIndex = maxIndex;
        this.maxSizePx = maxSizePx;
    }

    public void draw(int x, int y) {
        if (vertical) {
            int yOffset = maxSizePx * currIndex / maxIndex;
            int height = maxSizePx * (currIndex + windowSize) / maxIndex - yOffset;
            y += yOffset;
            drawRect(x, y, x + sizePx, y + height, bgColor);
        }
    }

    @Override
    public boolean isMousedOver(int mouseX, int mouseY) {
        // Re-calculate position and check if moused over
        // Might have to keep track of position and just update it
        // For fast lookup
        return false;
    }

    @Override
    public boolean onMouseDrag(int mouseX, int mouseY) {
        return false;
    }

    @Override
    public boolean onMouseRelease(int mouseX, int mouseY) {
        return false;
    }
}

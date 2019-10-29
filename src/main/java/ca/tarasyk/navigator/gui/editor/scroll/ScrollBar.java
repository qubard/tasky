package ca.tarasyk.navigator.gui.editor.scroll;

import net.minecraft.client.gui.GuiScreen;

public class ScrollBar extends GuiScreen implements MouseInteract {

    private int bgColor, sizePx;
    private boolean vertical;

    private int anchorX, anchorY, windowSize, maxIndex, maxSizePx;

    private int startDragX, startDragY, tempShift, shift;
    private boolean isDragged;

    public ScrollBar(int bgColor, int sizePx, boolean vertical) {
        this.bgColor = bgColor;
        this.sizePx = sizePx;
        this.vertical = vertical;
    }

    public void update(int anchorX, int anchorY, int currIndex, int windowSize, int maxIndex, int maxSizePx) {
        this.anchorX = anchorX;
        this.anchorY = anchorY;
        this.windowSize = windowSize;
        this.maxIndex = maxIndex;
        this.maxSizePx = maxSizePx;
    }

    public int getSize() {
        return this.sizePx;
    }

    public int getMaxSize() {
        return this.maxSizePx;
    }

    public int calcDirSize() {
        return maxSizePx * windowSize / maxIndex;
    }

    public void draw() {
        if (vertical) {
            drawRect(anchorX, anchorY + calcTotalShift(), anchorX + sizePx, anchorY + calcDirSize() + calcTotalShift(), bgColor);
        }
    }

    public int calcTotalShift() {
        return tempShift + shift;
    }

    @Override
    public boolean isMousedOver(int mouseX, int mouseY) {
        if (vertical) {
            return mouseX >= anchorX && mouseX <= anchorX + sizePx && mouseY >= anchorY + calcTotalShift() && mouseY <= anchorY + calcDirSize() + calcTotalShift();
        }

        return mouseX >= anchorX + calcTotalShift() && mouseX <= anchorX + calcDirSize() + calcTotalShift() && mouseY >= anchorY && mouseY <= anchorY + sizePx;
    }

    @Override
    public void onMouseDrag(int mouseX, int mouseY) {
        int possibleShift = mouseY - (vertical ? startDragY : startDragX);
        if (shift + possibleShift < 0) {
            tempShift = -shift;
        } else if (shift + possibleShift + calcDirSize() > maxSizePx) {
            tempShift = maxSizePx - shift - calcDirSize();
        } else {
            tempShift = possibleShift;
        }
    }

    @Override
    public void onMousePress(int mouseX, int mouseY) {
        if (!isDragged) {
            startDragX = mouseX;
            startDragY = mouseY;
            isDragged = true;
        }
    }

    @Override
    public void onMouseRelease(int mouseX, int mouseY) {
        if (isDragged) {
            isDragged = false;

            // When updating shift it may go out of bounds, so clamp it
            if (shift + tempShift < 0) {
                shift = 0;
            } else if (shift + tempShift + calcDirSize() > maxSizePx) {
                shift = maxSizePx;
            } else {
                shift += tempShift;
            }
            tempShift = 0;
        }
    }

    @Override
    public boolean isDragged() {
        return isDragged;
    }
}

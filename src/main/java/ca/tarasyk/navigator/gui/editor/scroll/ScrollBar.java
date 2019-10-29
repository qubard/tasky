package ca.tarasyk.navigator.gui.editor.scroll;

import net.minecraft.client.gui.GuiScreen;

public class ScrollBar extends GuiScreen implements MouseInteract {

    private int bgColor, sizePx;
    private boolean vertical;

    private int anchorX, anchorY, windowSize, maxIndex, maxSizePx;

    private int startDragX, startDragY;

    private float tempShift, shift;
    private boolean isDragged;

    public ScrollBar(int bgColor, int sizePx, boolean vertical) {
        this.bgColor = bgColor;
        this.sizePx = sizePx;
        this.vertical = vertical;
    }

    public void update(int anchorX, int anchorY, int windowSize, int maxIndex, int maxSizePx) {
        this.anchorX = anchorX;
        this.anchorY = anchorY;
        this.windowSize = windowSize;
        this.maxIndex = maxIndex;
        this.maxSizePx = maxSizePx;
    }

    public void shift(boolean pos) {
        updateShift((pos ? 1.0f : -1.0f ) * 1.0f / maxIndex);
        applyTempShift();
    }

    public int getSize() {
        return this.sizePx;
    }

    public int getMaxSize() {
        return this.maxSizePx;
    }

    public int calcDirSize() {
        return (int) (maxSizePx * calcDirSizeRatio());
    }

    public float calcDirSizeRatio() {
        return Math.min((float) windowSize / (float) maxIndex, 1.0f);
    }

    public void draw() {
        if (vertical) {
            drawRect(anchorX, anchorY + calcTotalShift(), anchorX + sizePx, anchorY + calcTotalDirSize(), bgColor);
        }
    }

    // There will be minor errors from not adding the floats first before converting to pixels space
    // To rectify this add dirSizeRatio then the shifts
    public int calcTotalDirSize() {
        return (int) (maxSizePx * (calcDirSizeRatio() + tempShift + shift));
    }

    public int calcTotalShift() {
        return (int) (maxSizePx * (tempShift + shift));
    }

    public void updateShift(float deltaShift) {
        if (shift + deltaShift < 0) {
            tempShift = -shift;
        } else if (shift + deltaShift + calcDirSizeRatio() > 1.0f) {
            tempShift = 1.0f - shift - calcDirSizeRatio();
            // shift + x + calcDirSizeRatio() = 1
            // 1 - shift - calc
        } else {
            tempShift = deltaShift;
        }
    }

    public void applyTempShift() {
        if (shift + tempShift < 0) {
            shift = 0.0f;
        } else if (shift + tempShift + calcDirSizeRatio() > 1.0f) {
            shift = 1.0f;
        } else {
            shift += tempShift;
        }
        tempShift = 0.0f;
    }

    public void setShiftFromIndex(int index) {
        shift = (float) index / (float) maxIndex;
    }

    @Override
    public boolean isMousedOver(int mouseX, int mouseY) {
        if (vertical) {
            return mouseX >= anchorX && mouseX <= anchorX + sizePx && mouseY >= anchorY + calcTotalShift() && mouseY <= anchorY + calcTotalDirSize();
        }
        return mouseX >= anchorX + calcTotalShift() && mouseX <= anchorX + calcTotalDirSize() && mouseY >= anchorY && mouseY <= anchorY + sizePx;
    }

    @Override
    public void onMouseDrag(int mouseX, int mouseY) {
        float shiftRatio = (float)(mouseY - (vertical ? startDragY : startDragX)) / (float)maxSizePx;
        updateShift(shiftRatio);
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
            applyTempShift();
        }
    }

    @Override
    public boolean isDragged() {
        return isDragged;
    }
}

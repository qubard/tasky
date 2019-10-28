package ca.tarasyk.navigator.gui.editor;

import ca.tarasyk.navigator.ScriptHelper;
import ca.tarasyk.navigator.gui.editor.font.MonoFontRenderer;
import ca.tarasyk.navigator.gui.editor.scroll.ScrollBar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class GuiEditor extends GuiScreen {

    private static final ResourceLocation EDITOR = new ResourceLocation("navigator:editor.png");
    private static final ResourceLocation EDITOR_FONT = new ResourceLocation("navigator:editor_font.png");

    private String[] fileLines;

    /**
     * Keep track of the cursor position (row, col) wise
     */
    private int cursorRow = 0;
    private int cursorColumn = 0;

    private int cameraRow = 0;
    private int cameraColumn = 0;

    /**
     * The height (in pixels) of a line
     */
    private int lineHeight;

    private final int EDITOR_FONT_COLOR = 0xFFF8F8F2;
    private final int FOREGROUND_COLOR = 0xFF7C7F6C;
    private final int HIGHLIGHT_COLOR = 0xFF48493E;
    private final int EDITOR_COLOR = 0xFF272822;
    private final int HIGHLIGHTED_LINE_NUMBER_COLOR = 0xFFC2C6AB;
    private final int SCROLL_BAR_COLOR = 0x7DFFFFFF;

    private double scaleX = 1;
    private double scaleY = 1;

    private int topLeftX = 0;
    private int topLeftY = 0;

    private final int BORDER_SIZE = 8;

    private int maxLenLocal;

    /**
     * The number of visible characters per line
     */
    private int LINE_CHAR_COUNT = 18;

    /**
     * The maximum number of rendered lines
     */
    private int LINE_COUNT = 10;

    private FontRenderer monoRenderer;
    private TextureManager textureManager;

    private Cursor originalCursor;

    private ScrollBar verticalScrollBar;

    public GuiEditor(String fileName, int lineHeight, GameSettings gameSettings, TextureManager textureManager, int scrollBarSize) {
        try {
            String fileBytes = ScriptHelper.loadScript("tasky", fileName);
            fileLines = fileBytes.split("\\r?\\n"); // Split based on newline tokens
            this.lineHeight = lineHeight;
            this.textureManager = textureManager;
            monoRenderer = new MonoFontRenderer(gameSettings, EDITOR_FONT, textureManager, true);
            originalCursor = Mouse.getNativeCursor();
            calcEditorPos();
            maxLenLocal = maxLengthLineLocally();
            verticalScrollBar = new ScrollBar(SCROLL_BAR_COLOR, scrollBarSize, true);
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    public void updateCache() {
        calcEditorPos();
        maxLenLocal = maxLengthLineLocally();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        handleEditorClick(mouseX, mouseY, mouseButton);
    }

    private void handleEditorClick(int mouseX, int mouseY, int mouseButton) {
        int editorPointX = mouseX - (topLeftX + maxLineNumberWidth() + BORDER_SIZE + 10);
        int editorPointY = mouseY - (topLeftY + BORDER_SIZE);

        int nextRow = editorPointY / lineHeight + cameraRow;
        if (nextRow >= 0 && nextRow < fileLines.length) {
            cursorRow = nextRow;
        }

        if (editorPointX >= 0) {
            cursorColumn = (int) Math.round((editorPointX) / 8.0) + cameraColumn;
        }
    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        handleScrollInput();
    }

    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        System.out.println(mouseX + "," + mouseY);
    }

    private int maxLengthLineLocally() {
        int len = Integer.MIN_VALUE;
        for (int line = 0; line < fileLines.length; line++) {
            String textStr = getLine(line);
            len = Math.max(textStr.length(), len);
        }
        return len;
    }

    private void handleScrollInput() {
        int scrollDir = -1 * Integer.signum(Mouse.getEventDWheel());

        if (scrollDir != 0) {
            if (cameraRow + scrollDir >= 0 && cameraRow + scrollDir + LINE_COUNT <= fileLines.length) {
                cameraRow += scrollDir;
            }
        }
        updateCache();
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (keyCode == 205) {
            cursorColumn++;

            // If the line ends here, go to the next one
            if (cursorRow != fileLines.length - 1 && cursorColumn > fileLines[cursorRow].length()) {
                cursorRow++;
                cursorColumn = 0;
            }
        } else if (keyCode == 203 && !(cursorRow == 0 && cursorColumn == 0)) {
            // Bind the cursor column before iterating
            cursorColumn = boundCursorColumn();
            cursorColumn--;

            if (cursorColumn < 0) {
                cursorRow--;
                cursorColumn = getCurrentLine().length();
            }
        } else if (keyCode == 200) {
            cursorRow--;
        } else if (keyCode == 208) {
            cursorRow++;
        }

        cursorColumn = Math.max(0, cursorColumn);
        cursorRow = Math.min(Math.max(cursorRow, 0), fileLines.length - 1);

        if (cursorRow >= cameraRow + LINE_COUNT) {
            cameraRow++;
        } else if (cursorRow < cameraRow) {
            cameraRow--;
        }

        if (cursorColumn > getCurrentLine().length()) {
            cursorColumn = boundCursorColumn();
        }

        updateCameraColumn();
        updateCache();
    }

    private String getCurrentLine() {
        return getLine(-cameraRow + cursorRow);
    }

    private String getLine(int offset) {
        int index = cameraRow + offset;
        return fileLines[Math.min(Math.max(0, index), fileLines.length - 1)];
    }

    private void updateCameraColumn() {
        if (cursorColumn <= cameraColumn || cursorColumn >= cameraColumn + LINE_CHAR_COUNT) {
            // If the cursor is not visible, update cameraColumn
            cameraColumn = Math.max(0, cursorColumn - (LINE_CHAR_COUNT - 3));
        }
    }

    private void drawHorizontalScrollBar() {
        int xOffset = getEditorWidth() * cameraColumn / maxLenLocal;
        int width = getEditorWidth() * (cameraColumn + cursorColumn) / maxLenLocal - xOffset;
        int x = topLeftX + xOffset;
        drawRect(x + BORDER_SIZE + 1, topLeftY + getEditorHeight() + 3, x + width + BORDER_SIZE - 1, topLeftY + getEditorHeight() + 7, SCROLL_BAR_COLOR);
    }

    private boolean mousedOverVerticalScrollBar() {
        return false;
    }

    /**
     * Bind cursorColumn to not exceed the length of the current line
     *
     * @return cursorColumn at most the length of the current line
     */
    private int boundCursorColumn() {
        return Math.min(getCurrentLine().length(), cursorColumn);
    }

    private void drawTextLine(int lineNumber, String str, int x, int y, int lineNumberWidth, int maxCharLineNumber, boolean highlight) {
        // Draw line number
        int len = String.valueOf(lineNumber).length();
        monoRenderer.drawString(String.valueOf(lineNumber), x + 5 + (maxCharLineNumber - len) * 8, y, highlight ? HIGHLIGHTED_LINE_NUMBER_COLOR : FOREGROUND_COLOR);
        monoRenderer.drawString(str, x + lineNumberWidth + 10, y, EDITOR_FONT_COLOR);
    }

    /**
     * @return The maximum width of visible line numbers
     */
    private int maxLineNumberWidth() {
        return monoRenderer.getStringWidth(String.valueOf(cameraRow + LINE_COUNT));
    }

    public void onResize(Minecraft mcIn, int width, int height) {
        // TODO: Adjust width of editor container based on width = (lineNumberWidth + 14 + BORDER LENGTH (16)) + line width for text
        // TODO: Support resizing container with click + drag (bottom right, 3 dots resize icon) (it would be cool to have a blue opacity highlight effect when this happens)
        // TODO: Basic add/edit line functionality
        // TODO: Save file support
        // TODO: Integrate with some other gui to edit actual lua files
        // TODO: Syntax highlighting
        // TODO: Comment color highlighting
        // TODO: Rename from navigator to Tasky
        // TODO: Release
        // TODO: Resizing container cursor bug?
        // TODO: Click vertical scrollbar/horizontal to drag and scroll
        // TODO: Text selection
        // TODO: Cursor changing bug
        super.onResize(mcIn, width, height);
        width *= 2;
        height *= 2;
        scaleX = width / 427.0;
        scaleY = height / 400.0;
        resizeEditor(width, height);
        calcEditorPos();
    }

    private void calcEditorPos() {
        topLeftX = (int) ((width - 192 * scaleX) / 2);
        topLeftY = (int) ((height - 166 * scaleY) / 2);
    }

    private void resizeEditor(int screenWidth, int screenHeight) {
        LINE_COUNT = (int) (screenHeight * 10.0 / 400.0);
        LINE_CHAR_COUNT = (int) (screenWidth * 18.0 / 427.0);
    }

    private void drawHighlight(int x, int y, int color, int width) {
        drawRect(x, y - lineHeight / 2, x + width, y + lineHeight / 2 - 1, color);
    }

    private void drawContainer(int x, int y) {
        textureManager.bindTexture(EDITOR);
        int editorWidth = getEditorWidth();
        int editorHeight = getEditorHeight();
        drawScaledCustomSizeModalRect(x, y, 0, 0, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, 256, 256); // top left corner
        drawScaledCustomSizeModalRect(x + BORDER_SIZE + editorWidth, y, 184, 0, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, 256, 256); // top right corner
        drawScaledCustomSizeModalRect(x, y + BORDER_SIZE + editorHeight, 0, 168, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, 256, 256); // bottom left corner
        drawScaledCustomSizeModalRect(x + BORDER_SIZE + editorWidth, y + BORDER_SIZE + editorHeight, 184, 168, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, 256, 256); // bottom right corner
        drawScaledCustomSizeModalRect(x + BORDER_SIZE, y, BORDER_SIZE, 0, 1, BORDER_SIZE, editorWidth, BORDER_SIZE, 256, 256); // top
        drawScaledCustomSizeModalRect(x, y + BORDER_SIZE, 0, BORDER_SIZE, 8, 1, BORDER_SIZE, editorHeight, 256, 256); // left
        drawScaledCustomSizeModalRect(x + BORDER_SIZE + editorWidth, y + BORDER_SIZE, 184, BORDER_SIZE, BORDER_SIZE, 1, BORDER_SIZE, editorHeight, 256, 256); // right
        drawScaledCustomSizeModalRect(x + BORDER_SIZE, y + BORDER_SIZE + editorHeight, BORDER_SIZE, 168, 1, BORDER_SIZE, editorWidth, BORDER_SIZE, 256, 256); // bottom

        drawRect(x + BORDER_SIZE, y + BORDER_SIZE, x + BORDER_SIZE + editorWidth, y + BORDER_SIZE + editorHeight, EDITOR_COLOR);
    }

    private int getEditorHeight() {
        return lineHeight * LINE_COUNT;
    }

    private int getEditorWidth() {
        return maxLineNumberWidth() + 14 + LINE_CHAR_COUNT * 8;
    }

    private void drawEditor() {
        String currLine = getCurrentLine();

        for (int line = 0; line < LINE_COUNT && line + cameraRow < fileLines.length; line++) {
            // Highlight the current row, line + currLine is the line in global space, line is non-normalized
            boolean highlight = line + cameraRow == cursorRow;

            if (highlight) {
                drawHighlight(topLeftX + BORDER_SIZE, topLeftY + BORDER_SIZE + line * lineHeight + 8, HIGHLIGHT_COLOR, getEditorWidth());
            }

            // Draw a text line
            int lineNumberWidth = maxLineNumberWidth();
            String textStr = getLine(line);

            textStr = cameraColumn < textStr.length() ? textStr.substring(cameraColumn, Math.min(textStr.length(), cameraColumn + LINE_CHAR_COUNT)) : "";
            drawTextLine(
                    line + 1 + cameraRow,
                    textStr,
                    topLeftX + BORDER_SIZE,
                    topLeftY + line * lineHeight + BORDER_SIZE + 4,
                    lineNumberWidth,
                    String.valueOf(cameraRow + LINE_COUNT).length(),
                    highlight);

            if (line + cameraRow == cursorRow) {
                // Draw the cursor
                // offset from cursorColumn has to be calculated based on line width up to cursor column
                int lineOffset = (Math.min(currLine.length(), cursorColumn) - cameraColumn) * 8;
                drawRect(topLeftX + lineOffset + lineNumberWidth + BORDER_SIZE + 10, topLeftY + line * lineHeight + BORDER_SIZE + 1, topLeftX + lineOffset + 19 + lineNumberWidth, topLeftY + (line + 1) * lineHeight + BORDER_SIZE - 2, 0xAFFFFFFF);
            }
        }
    }

    private void handleMouseOverText(int mouseX, int mouseY) throws LWJGLException {
        if (mouseX >= topLeftX + BORDER_SIZE && mouseX <= topLeftX + BORDER_SIZE + getEditorWidth()
                && mouseY >= topLeftY + BORDER_SIZE && mouseY <= topLeftY + BORDER_SIZE + getEditorHeight()) {
            try {
                final Cursor emptyCursor = new Cursor(1, 1, 0, 0, 1, BufferUtils.createIntBuffer(1), null);
                if (Mouse.getNativeCursor() != emptyCursor)
                Mouse.setNativeCursor(emptyCursor);
                textureManager.bindTexture(EDITOR);
                drawScaledCustomSizeModalRect(mouseX, mouseY, 195, 1, 5, 11, 5, 11,256, 256);
            } catch (LWJGLException e) {
                //TODO: Consider better exception handling
                e.printStackTrace();
            }
        } else {
            if (Mouse.getNativeCursor() != originalCursor) Mouse.setNativeCursor(originalCursor);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        calcEditorPos();
        drawContainer(topLeftX, topLeftY);
        drawEditor();

        drawHorizontalScrollBar();

        try {
            handleMouseOverText(mouseX, mouseY);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        verticalScrollBar.draw(topLeftX + getEditorWidth() + BORDER_SIZE - verticalScrollBar.getSize() - 1,
                topLeftY + BORDER_SIZE + 1,
                cameraRow,
                LINE_COUNT,
                fileLines.length,
                getEditorHeight() - 2);
    }
}

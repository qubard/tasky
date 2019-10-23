package ca.tarasyk.navigator.gui.editor;

import ca.tarasyk.navigator.ScriptHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiEditor extends GuiScreen {

    private static final ResourceLocation EDITOR = new ResourceLocation("navigator:editor.png");
    private static final ResourceLocation EDITOR_FONT = new ResourceLocation("navigator:editor_font.png");

    private String[] fileLines;

    /**
     * The line number of the first visible row
     */
    private int currRow = 0;

    /**
     * Keep track of the cursor position (row, col) wise
     */
    private int cursorRow = 0;
    private int cursorColumn = 0;

    private int cameraColumn = 0;

    /**
     * The height (in pixels) of a line
     */
    private int lineHeight;

    private final int BACKGROUND_COLOR = 0xFFF8F8F2;
    private final int FOREGROUND_COLOR = 0xFF7C7F6C;
    private final int HIGHLIGHT_COLOR = 0xFF48493E;
    private final int EDITOR_COLOR = 0xFF272822;

    /**
     * The number of visible characters per line
     */
    private int LINE_WIDTH = 18;
    /**
     * The maximum number of rendered lines
     */
    private int LINE_COUNT = 10;

    private FontRenderer monoRenderer;
    private TextureManager textureManager;

    public GuiEditor(String scriptName, int lineHeight, GameSettings gameSettings, TextureManager textureManager) {
        try {
            String fileBytes = ScriptHelper.loadScript("tasky", scriptName);
            fileLines = fileBytes.split("\\r?\\n");
            this.lineHeight = lineHeight;
            this.textureManager = textureManager;
            monoRenderer = new MonoFontRenderer(gameSettings, EDITOR_FONT, textureManager, true);
        } catch (IOException err) {
            err.printStackTrace();
        }
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

        if (cursorRow >= currRow + LINE_COUNT) {
            currRow++;
        } else if (cursorRow < currRow) {
            currRow--;
        }

        if (cursorColumn > getCurrentLine().length()) {
            cursorColumn = boundCursorColumn();
        }

        updateCameraColumn();
    }

    private String getCurrentLine() {
        return getLine(-currRow + cursorRow);
    }

    private String getLine(int offset) {
        int index = currRow + offset;
        return fileLines[Math.min(Math.max(0, index), fileLines.length - 1)];
    }

    private void updateCameraColumn() {
        if (cursorColumn <= cameraColumn || cursorColumn >= cameraColumn + LINE_WIDTH) {
            // If the cursor is not visible, update cameraColumn
            cameraColumn = Math.max(0, cursorColumn - (LINE_WIDTH - 3));
        }
    }

    /**
     * Bind cursorColumn to not exceed the length of the current line
     * @return cursorColumn at most the length of the current line
     */
    private int boundCursorColumn() {
        return Math.min(getCurrentLine().length(), cursorColumn);
    }

    private void drawTextLine(int lineNumber, String str, int x, int y, int lineNumberWidth, int maxCharLineNumber) {
        // Draw line number
        int len = String.valueOf(lineNumber).length();
        monoRenderer.drawString(String.valueOf(lineNumber), x + 9 + (maxCharLineNumber - len) * 8, y, FOREGROUND_COLOR);
        monoRenderer.drawString(str, x + lineNumberWidth + 14, y, BACKGROUND_COLOR);
    }

    /**
     * @return The maximum width of visible line numbers
     */
    private int maxLineNumberWidth() {
        return monoRenderer.getStringWidth(String.valueOf(currRow + LINE_COUNT));
    }

    private void resizeEditor(int screenWidth, int screenHeight) {
        LINE_COUNT = (int) (screenHeight * 10.0 / 480.0);
        LINE_WIDTH = (int) (screenWidth * 18.0 / 854.0);
    }

    private void drawHighlight(int x, int y, int color, int width) {
        drawRect(x, y, x + width, y + lineHeight, color);
    }

    private void drawBackground(int x, int y, int scaleX, int scaleY) {
        textureManager.bindTexture(EDITOR);
        drawScaledCustomSizeModalRect(x, y, 0, 0, 8, 8, 8, 8,256, 256); // top left corner
        drawScaledCustomSizeModalRect(x + 8 + 176 * scaleX, y, 184, 0, 8, 8, 8, 8, 256, 256); // top right corner
        drawScaledCustomSizeModalRect(x, y + 8 + 160 * scaleY, 0, 168, 8, 8, 8, 8, 256, 256); // bottom left corner
        drawScaledCustomSizeModalRect(x + 8 + 176 * scaleX, y + 8 + 160 * scaleY, 184, 168, 8, 8, 8, 8, 256, 256); // bottom right corner
        drawScaledCustomSizeModalRect(x + 8, y, 8, 0, 1, 8, 176 * scaleX, 8, 256, 256); // top
        drawScaledCustomSizeModalRect(x, y + 8, 0, 8, 8, 1, 8, 160 * scaleY, 256, 256); // left
        drawScaledCustomSizeModalRect(x + 176 * scaleX + 8, y + 8, 184, 8, 8, 1, 8, 160 * scaleY, 256, 256); // right
        drawScaledCustomSizeModalRect(x + 8, y + 8 + 160 * scaleY, 8, 168, 1, 8, 176 * scaleX, 8, 256, 256); // bottom

        drawRect(x + 8, y + 8, x + 8 + 176 * scaleX, y + 8 + 160 * scaleY, EDITOR_COLOR);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        int scaleX = super.mc.displayWidth / 854;
        int scaleY = super.mc.displayHeight / 480;
        int topLeftX = (width - 192 * scaleX) / 2;
        int topLeftY = (height - 166 * scaleY) / 2;

        drawBackground(topLeftX, topLeftY, scaleX, scaleY);

        String currLine = getCurrentLine();

        resizeEditor(super.mc.displayWidth, super.mc.displayHeight);

        for (int line = 0; line < LINE_COUNT && line < fileLines.length; line++) {
            // Highlight the current row, line + currLine is the line in global space, line is non-normalized
            if (line + currRow == cursorRow) {
                drawHighlight(topLeftX + 8, topLeftY + 8 + line * lineHeight, HIGHLIGHT_COLOR, 176 * scaleX);
            }

            // Draw a text line
            int lineNumberWidth = maxLineNumberWidth();
            String codeLine = getLine(line);

            // Draw lines w.r.t camera and cursor position (we determine cameraColumn from cursorColumn)
            // currColumn can be found out from cursorColumn, since currColumn is what we use
            // currColumn = last index of line if cursorColumn exceeds line length
            // keep 3 chars on the right

            codeLine = cameraColumn < codeLine.length() ? codeLine.substring(cameraColumn, Math.min(codeLine.length(), cameraColumn + LINE_WIDTH)) : "";
            drawTextLine(
                    line + 1 + currRow,
                    codeLine,
                    topLeftX + 4,
                    topLeftY + line * lineHeight + 12,
                    lineNumberWidth,
                    String.valueOf(currRow + LINE_COUNT).length());

            if (line + currRow == cursorRow) {
                // Draw the cursor
                // offset from cursorColumn has to be calculated based on line width up to cursor column
                int lineOffset = (Math.min(currLine.length(), cursorColumn) - cameraColumn) * 8;
                drawRect(topLeftX + lineOffset + lineNumberWidth + 18, topLeftY + line * lineHeight + 8 + 2, topLeftX + lineOffset + 19 + lineNumberWidth, topLeftY + (line + 1) * lineHeight + 8 - 2, 0xAFFFFFFF);
            }
        }
    }
}

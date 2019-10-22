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
     * The maximum number of rendered lines
     */
    private final int MAX_VISIBLE_LINES = 10;

    /**
     * Keep track of the cursor position (row, col) wise
     */
    private int cursorRow = 0;
    private int cursorColumn = 0;

    /**
     * The height (in pixels) of a line
     */
    private int lineHeight;

    private final int BACKGROUND = 0xFFF8F8F2;
    private final int FOREGROUND = 0xFF7C7F6C;
    private final int HIGHLIGHT = 0xFF48493E;

    private final int VISIBLE_LINE_CHAR_WIDTH = 18;

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
        } else if (keyCode == 203) {
            // Bind the cursor column before iterating
            cursorColumn = boundCursorColumn();
            cursorColumn--;

            if (cursorColumn < 0) {
                cursorRow--;
                //cursorColumn = end of previous line (updates window too)
            }
        } else if (keyCode == 200) {
            cursorRow--;
        } else if (keyCode == 208) {
            cursorRow++;
        }

        cursorColumn = Math.max(0, cursorColumn);
        cursorRow = Math.min(Math.max(cursorRow, 0), fileLines.length - 1);

        if (cursorRow >= currRow + MAX_VISIBLE_LINES) {
            currRow++;
        } else if (cursorRow < currRow) {
            currRow--;
        }
    }

    /**
     * Bind cursorColumn to not exceed the length of the current line
     * @return cursorColumn at most the length of the current line
     */
    private int boundCursorColumn() {
        return Math.min(fileLines[cursorRow].length(), cursorColumn);
    }

    private void drawTextLine(int lineNumber, String str, int x, int y, int lineNumberWidth, int maxCharLineNumber) {
        // Draw line number
        int l = String.valueOf(lineNumber).length();
        monoRenderer.drawString(String.valueOf(lineNumber), x + 9 + (maxCharLineNumber - l) * 8, y, FOREGROUND);
        monoRenderer.drawString(str, x + lineNumberWidth + 14, y, BACKGROUND);
    }

    /**
     * @return The maximum width of visible line numbers
     */
    private int maxLineNumberWidth() {
        return monoRenderer.getStringWidth(String.valueOf(currRow + MAX_VISIBLE_LINES));
    }

    private void drawHighlight(int x, int y, int color) {
        drawRect(x, y, x + 176, y + lineHeight, color);
    }

    private void drawBackground(int x, int y) {
        textureManager.bindTexture(EDITOR);
        drawTexturedModalRect(x, y, 0, 0, 192, 176);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        int topLeftX = (width - 192) / 2;
        int topLeftY = (height - 166) / 2;

        drawBackground(topLeftX, topLeftY);

        // Calculate the camera column position
        String currLine = fileLines[cursorRow];
        int cameraColumn = Math.max(0, cursorColumn - (VISIBLE_LINE_CHAR_WIDTH - 3));

        if (cursorColumn + 3 >= currLine.length()) {
            cameraColumn = Math.max(0, currLine.length() - VISIBLE_LINE_CHAR_WIDTH);
        }

        for (int line = 0; line < MAX_VISIBLE_LINES && line < fileLines.length; line++) {
            // Highlight the current row, line + currLine is the line in global space, line is non-normalized
            if (line + currRow == cursorRow) {
                drawHighlight(topLeftX + 8, topLeftY + 8 + line * lineHeight, HIGHLIGHT);
            }

            // Draw a text line
            int lineNumberWidth = maxLineNumberWidth();
            String codeLine = fileLines[currRow + line];

            // Draw lines w.r.t camera and cursor position (we determine cameraColumn from cursorColumn)
            // currColumn can be found out from cursorColumn, since currColumn is what we use
            // currColumn = last index of line if cursorColumn exceeds line length
            // keep 3 chars on the right

            codeLine = cameraColumn < codeLine.length() ? codeLine.substring(cameraColumn, Math.min(codeLine.length(), cameraColumn + VISIBLE_LINE_CHAR_WIDTH)) : "";
            drawTextLine(
                    line + 1 + currRow,
                    codeLine,
                    topLeftX + 4,
                    topLeftY + line * lineHeight + 12,
                    lineNumberWidth,
                    String.valueOf(currRow + MAX_VISIBLE_LINES).length());

            if (line + currRow == cursorRow) {
                // Draw the cursor
                // offset from cursorColumn has to be calculated based on line width up to cursor column
                int lineOffset = (Math.min(currLine.length(), cursorColumn) - cameraColumn) * 8;
                drawRect(topLeftX + lineOffset + lineNumberWidth + 18, topLeftY + line * lineHeight + 8 + 2, topLeftX + lineOffset + 19 + lineNumberWidth, topLeftY + (line + 1) * lineHeight + 8 - 2, 0xAFFFFFFF);
            }
        }
    }
}

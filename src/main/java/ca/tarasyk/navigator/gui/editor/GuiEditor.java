package ca.tarasyk.navigator.gui.editor;

import ca.tarasyk.navigator.NavigatorProvider;
import ca.tarasyk.navigator.ScriptHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiEditor extends GuiScreen {

    private static final ResourceLocation EDITOR = new ResourceLocation("navigator:editor.png");
    private static final ResourceLocation EDITOR_FONT = new ResourceLocation("navigator:editor_font.png");

    private String[] fileLines;

    /**
     * The line number of the first visible row
     */
    private int currLine = 0;

    /**
     * The left-most current visible column
     */
    private int currColumn = 0;

    /**
     * The maximum number of rendered lines
     */
    private final int MAX_LINES = 10;

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

    private final int VISIBLE_LINE_CHAR_WIDTH = 29;

    private FontRenderer monoRenderer = new FontRenderer();

    public GuiEditor(String scriptName, int lineHeight) {
        try {
            String fileBytes = ScriptHelper.loadScript("tasky", scriptName);
            fileLines = fileBytes.split("\\r?\\n");
            this.lineHeight = lineHeight;
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
                currColumn = 0;
            } else if (currColumn < fileLines[currLine].length() && cursorColumn > currColumn + VISIBLE_LINE_CHAR_WIDTH) {
                currColumn++;
            }
        } else if (keyCode == 203) {
            // Bind the cursor column before iterating
            cursorColumn = boundCursorColumn();
            cursorColumn--;

            if (cursorColumn < 0) {
                cursorRow--;
                //cursorColumn = end of previous line (updates window too)
            } else if (cursorColumn < currColumn) {
                currColumn--;
            }
        } else if (keyCode == 200) {
            cursorRow--;
        } else if (keyCode == 208) {
            cursorRow++;
        }

        cursorColumn = Math.max(0, cursorColumn);
        cursorRow = Math.min(Math.max(cursorRow, 0), fileLines.length - 1);

        if (cursorRow >= currLine + MAX_LINES) {
            currLine++;
        } else if (cursorRow < currLine) {
            currLine--;
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
        // Build the line numbers
        StringBuilder builder = new StringBuilder();
        builder.append(lineNumber);
        String lineNumberStr = String.valueOf(lineNumber);
        for (int i = 0; i < maxCharLineNumber - lineNumberStr.length(); i++) {
            builder.insert(0, ' ');
        }

        // Draw line number
        drawString(builder.toString(), x + 9, y, FOREGROUND);
        drawString(str, x + lineNumberWidth + 14, y, BACKGROUND);
    }

    /**
     * @return The maximum width of visible line numbers
     */
    private int maxLineNumberWidth() {
        return String.valueOf(currLine + MAX_LINES).length() * 8;
    }

    private void drawHighlight(int x, int y, int color) {
        drawRect(x, y, x + 176, y + lineHeight, color);
    }

    private void drawString(String str, int x, int y, int color) {
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(EDITOR_FONT);
        GlStateManager.scale(2.0f, 0.5f, 1.0f);

        float red = color & 255;
        float green = (color >> 8) & 255;
        float blue = (color >> 16) & 255;
        float alpha = (color >> 24) & 255;
        GlStateManager.color(red, green, blue, alpha);

        for (int i = 0; i < str.length(); i++) {
            drawGlyph(str.charAt(i), x + i * 8, y);
        }
        GlStateManager.popMatrix();
    }

    private void drawGlyph(char ch, int x, int y) {
        int textureX = 8 * (ch % 32);
        int textureY = 32 * (ch / 32);
        drawTexturedModalRect(x, y, textureX, textureY, 8, 32);
    }

    private void drawBackground(int x, int y) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(EDITOR);
        drawTexturedModalRect(x, y, 1, 1, 192, 174);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        int topLeftX = (width - 192) / 2;
        int topLeftY = (height - 166) / 2;

        drawBackground(topLeftX, topLeftY);

        for (int line = 0; line < MAX_LINES && line < fileLines.length; line++) {
            // Highlight the current row, line + currLine is the line in global space, line is non-normalized
            if (line + currLine == cursorRow) {
                drawHighlight(topLeftX + 8, topLeftY + 8 + line * lineHeight, HIGHLIGHT);
            }

            // Draw a text line
            int lineNumberWidth = maxLineNumberWidth();
            String codeLine = fileLines[currLine + line];
            drawTextLine(
                    line + 1 + currLine,
                    currColumn < codeLine.length() ? codeLine.substring(currColumn, Math.min(codeLine.length(), currColumn + VISIBLE_LINE_CHAR_WIDTH)) : "",
                    topLeftX + 4,
                    topLeftY + line * lineHeight + 12,
                    lineNumberWidth,
                    String.valueOf(currLine + MAX_LINES).length());

            if (line + currLine == cursorRow) {
                // Draw the cursor
                // offset from cursorcolumn has to be calculated based on line width up to cursor column
                int lineOffset = NavigatorProvider.getMinecraft().fontRenderer.getStringWidth(fileLines[cursorRow].substring(currColumn, boundCursorColumn()));
                drawRect(topLeftX + lineOffset + lineNumberWidth + 17, topLeftY + line * lineHeight + 8 + 2, topLeftX + lineOffset + 18 + lineNumberWidth, topLeftY + (line + 1) * lineHeight + 8 - 2, 0xAFFFFFFF);
            }
        }
    }
}

package ca.tarasyk.navigator.gui.editor;

import ca.tarasyk.navigator.NavigatorProvider;
import ca.tarasyk.navigator.ScriptHelper;
import net.java.games.input.Keyboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.security.Key;

public class GuiEditor extends GuiScreen {

    private static final ResourceLocation EDITOR = new ResourceLocation("navigator:editor.png");

    private String[] fileLines;

    /**
     * The line number of the top (first) row
     */
    private int currLineTop = 0;

    /**
     * The maximum number of rendered lines
     */
    private final int MAX_LINES = 10;

    /**
     * Keep track of the cursor position (row, col) wise
     */
    private int cursorRow = 0;
    private int cursorColumn = 0;
    private int lineHeight;

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
        } else if (keyCode == 203) {
            cursorColumn--;
        } else if (keyCode == 200) {
            cursorRow--;
        } else if (keyCode == 208) {
            cursorRow++;
        }

        cursorRow = Math.min(Math.max(cursorRow, 0), fileLines.length - 1);
        cursorColumn = Math.max(0, Math.min(fileLines[cursorRow].length(), cursorColumn));

        if (cursorRow >= currLineTop + MAX_LINES) {
            currLineTop++;
        } else if (cursorRow < currLineTop) {
            currLineTop--;
        }
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
        fontRenderer.drawString(builder.toString(), x + 9, y,0x7C7F6C,false);

        FontRenderer fontRenderer = NavigatorProvider.getMinecraft().fontRenderer;
        fontRenderer.drawString(fontRenderer.trimStringToWidth(str,160 - lineNumberWidth), x + lineNumberWidth + 14, y, 0xF8F8F2,false);
    }

    private int maxLineNumberWidth() {
        return Minecraft.getMinecraft().fontRenderer.getStringWidth(String.valueOf(currLineTop + MAX_LINES));
    }

    private void drawHighlight(int x, int y, int color) {
        drawRect(x, y, x + 176, y + lineHeight, color);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        Minecraft.getMinecraft().getTextureManager().bindTexture(EDITOR);
        int topLeftX = (width - 192) / 2;
        int topLeftY = (height - 166) / 2;

        super.drawTexturedModalRect(topLeftX, topLeftY, 1, 1, 192, 174);

        for (int line = 0; line < MAX_LINES && line < fileLines.length; line++) {
            // Highlight the current row, line + currLineTop is the line in global space, line is non-normalized
            if (line + currLineTop == cursorRow) {
                drawHighlight(topLeftX + 8, topLeftY + 8 + line * lineHeight, 0xFF48493E);
            }
            // Draw a text line
            int lineNumberWidth = maxLineNumberWidth();
            drawTextLine(line + 1 + currLineTop, fileLines[currLineTop + line], topLeftX + 4, topLeftY + line * lineHeight + 12, lineNumberWidth, String.valueOf(currLineTop + MAX_LINES).length());

            if (line + currLineTop == cursorRow) {
                // Draw the cursor
                // offset from cursorcolumn has to be calculated based on line width up to cursor column
                int lineOffset = NavigatorProvider.getMinecraft().fontRenderer.getStringWidth(fileLines[cursorRow].substring(0, cursorColumn));
                drawRect(topLeftX + lineOffset + lineNumberWidth + 17, topLeftY + line * lineHeight + 8 + 2, topLeftX + lineOffset + 18 + lineNumberWidth, topLeftY + (line + 1) * lineHeight + 8 - 2, 0xAFFFFFFF);
            }
        }
    }
}

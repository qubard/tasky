package ca.tarasyk.navigator.gui.editor;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

public class MonoFontRenderer extends FontRenderer {

    public MonoFontRenderer(GameSettings gameSettingsIn, ResourceLocation location, TextureManager textureManagerIn, boolean unicode) {
        super(gameSettingsIn, location, textureManagerIn, unicode);
    }

    @Override
    public int getStringWidth(String str) {
        return str.length() * 8;
    }

    @Override
    public int getCharWidth(char character)
    {
        return 8;
    }

    /**
     * Draws the specified string.
     */
    public int drawString(String text, int x, int y, int color)
    {
        return this.drawString(text, (float)x, (float)y, color, false);
    }

    /**
     * Draws the specified string.
     */
    public int drawString(String text, float x, float y, int color, boolean dropShadow)
    {
        enableAlpha();
        int i;

        if (dropShadow)
        {
            i = this.renderString(text, x + 1.0F, y + 1.0F, color, true);
            i = Math.max(i, this.renderString(text, x, y, color, false));
        }
        else
        {
            i = this.renderString(text, x, y, color, false);
        }

        return i;
    }

    /**
     * Apply Unicode Bidirectional Algorithm to string and return a new possibly reordered string for visual rendering.
     */
    private String bidiReorder(String text)
    {
        try
        {
            Bidi bidi = new Bidi((new ArabicShaping(8)).shape(text), 127);
            bidi.setReorderingMode(0);
            return bidi.writeReordered(2);
        }
        catch (ArabicShapingException var3)
        {
            return text;
        }
    }

    /**
     * Render single line string by setting GL color, current (posX,posY), and calling renderStringAtPos()
     */
    private int renderString(String text, float x, float y, int color, boolean dropShadow)
    {
        if (text == null)
        {
            return 0;
        }
        else
        {
            if (getBidiFlag())
            {
                text = this.bidiReorder(text);
            }

            if ((color & -67108864) == 0)
            {
                color |= -16777216;
            }

            if (dropShadow)
            {
                color = (color & 16579836) >> 2 | color & -16777216;
            }

            float red = (float)(color >> 16 & 255) / 255.0F;
            float blue = (float)(color >> 8 & 255) / 255.0F;
            float green = (float)(color & 255) / 255.0F;
            float alpha = (float)(color >> 24 & 255) / 255.0F;
            setColor(red, blue, green, alpha);
            this.posX = x;
            this.posY = y;
            this.renderStringAtPos(text, dropShadow);
            return (int)this.posX;
        }
    }

    /**
     * Render a single line string at the current (posX,posY) and update posX
     */
    private void renderStringAtPos(String text, boolean shadow)
    {
        for (int i = 0; i < text.length(); ++i)
        {
            char c0 = text.charAt(i);

            float f1 = 0.5f;

            if (shadow)
            {
                this.posX -= f1;
                this.posY -= f1;
            }

            float f = this.renderChar(c0, false);

            if (shadow)
            {
                this.posX += f1;
                this.posY += f1;
            }
            doDraw(f);
        }
    }

    /**
     * Render the given char
     */
    private float renderChar(char ch, boolean italic)
    {
        if (ch == ' ')
        {
            return 8.0F;
        }
        else
        {
            return this.renderUnicodeChar(ch, italic);
        }
    }

    @Override
    protected float renderUnicodeChar(char ch, boolean italic) {
        int i = getCharWidth(ch);
        bindTexture(locationFontTexture);
        int k = i >>> 4;
        int l = i & 15;
        float f = (float)k;
        float f1 = (float)(l + 1);
        float f2 = (float)(ch % 32 * 8) + f;
        float f3 = (float)((ch & 255) / 32 * 8);
        float f4 = f1 - f;
        GlStateManager.glBegin(5);
        GlStateManager.glTexCoord2f(f2 / 256.0F, f3 / 64.0F);
        GlStateManager.glVertex3f(this.posX, this.posY, 0.0F);
        GlStateManager.glTexCoord2f(f2 / 256.0F, (f3 + 7.98F) / 64.0F);
        GlStateManager.glVertex3f(this.posX, this.posY + 7.99F, 0.0F);
        GlStateManager.glTexCoord2f((f2 + f4) / 256.0F, f3 / 64.0F);
        GlStateManager.glVertex3f(this.posX + f4, this.posY, 0.0F);
        GlStateManager.glTexCoord2f((f2 + f4) / 256.0F, (f3 + 7.98F) / 64.0F);
        GlStateManager.glVertex3f(this.posX + f4, this.posY + 7.99F, 0.0F);
        GlStateManager.glEnd();
        return 8.0f;
    }
}

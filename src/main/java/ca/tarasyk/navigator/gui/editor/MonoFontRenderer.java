package ca.tarasyk.navigator.gui.editor;

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
    public int getCharWidth(char character) {
        return 8;
    }

    @Override
    public int getStringWidth(String str) {
        return str.length() * 8;
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
        float f4 = f1 - f - 0.04F;
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

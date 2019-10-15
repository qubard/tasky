package ca.tarasyk.navigator.gui.editor;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

public class MonoFont extends FontRenderer {

    public MonoFont(GameSettings gameSettingsIn, ResourceLocation location, TextureManager textureManagerIn, boolean unicode) {
        super(gameSettingsIn, location, textureManagerIn, unicode);
    }

    @Override
    protected float renderUnicodeChar(char ch, boolean italic) {

    }
}

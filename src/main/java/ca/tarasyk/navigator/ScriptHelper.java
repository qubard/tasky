package ca.tarasyk.navigator;

import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ScriptHelper {
    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static String loadScript(String root, String name) throws IOException {
        String path = new StringBuilder().append(Minecraft.getMinecraft().mcDataDir.getPath()).append("/").append(root).append("/scripts/").append(name).toString();
        return readFile(path, StandardCharsets.UTF_8);
    }
}

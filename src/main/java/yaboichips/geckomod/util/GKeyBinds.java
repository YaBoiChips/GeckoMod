package yaboichips.geckomod.util;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public class GKeyBinds {
    public static KeyBinding GECKO_FLY_FORWARD = new KeyBinding("Gecko Forward", GLFW.GLFW_KEY_W, "Gecko Mod");

    public static void register() {
        ClientRegistry.registerKeyBinding(GECKO_FLY_FORWARD);
    }
}
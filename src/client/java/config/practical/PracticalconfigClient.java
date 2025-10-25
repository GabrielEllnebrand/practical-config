package config.practical;

import config.practical.category.ConfigCategory;
import config.practical.hud.HUDRender;
import config.practical.widgets.ConfigBool;
import config.practical.widgets.enums.ConfigOptions;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class PracticalconfigClient implements ClientModInitializer {

    private static KeyBinding openConfig;

    @Override
    public void onInitializeClient() {
        HudLayerRegistrationCallback.EVENT.register(HUDRender::render);
        openConfig = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "opens Config",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "a"));
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            if (openConfig.wasPressed()) {
                openScreen(client);
            }
        });
    }

    public void openScreen(MinecraftClient client) {

        ConfigurableScreen screen = new ConfigurableScreen(Text.literal("title").withColor(0xff22ff), null);
        ConfigCategory category = new ConfigCategory("Category");
        category.add(new ConfigBool(Text.literal("Test"), ()-> true, bool -> {}));
        category.add(new ConfigBool(Text.literal("Test"), ()-> true, bool -> {}));

        Integer[] nums = {1, 2, 3, 4};

        category.add(new ConfigOptions<Integer>(Text.literal("Hello"), nums));

        screen.addCategory(category);
        client.setScreen(screen);
    }
}
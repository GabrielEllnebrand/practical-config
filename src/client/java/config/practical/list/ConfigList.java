package config.practical.list;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ElementListWidget;

public class ConfigList extends ElementListWidget<ConfigEntry> {
    public ConfigList(MinecraftClient minecraftClient, int width, int height, int y, int itemHeight) {
        super(minecraftClient, width, height, y, itemHeight);
    }

    public int addEntry(ConfigEntry entry) {
        return super.addEntry(entry);
    }
}

package config.practical.category;

import config.practical.list.ConfigEntry;
import net.minecraft.client.gui.widget.ClickableWidget;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ConfigCategory {
    public final String name;
    public final ArrayList<ConfigEntry> entries;

    public ConfigCategory(String name) {
        this.name = name;
        entries = new ArrayList<>();
    }

    public void addWidget(ClickableWidget widget) {
        if (widget == null) return;

        ConfigEntry entry = new ConfigEntry(widget);
        entries.add(entry);
    }

    public void forEachEntry(Consumer<ConfigEntry> consumer) {
        for (ConfigEntry entry : entries) {
            consumer.accept(entry);
        }
    }
}

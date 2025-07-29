package config.practical.category;

import net.minecraft.client.gui.widget.ClickableWidget;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ConfigCategory {
    public final String name;
    public final ArrayList<ClickableWidget> widgets;

    public ConfigCategory(String name) {
        this.name = name;
        widgets = new ArrayList<>();
    }

    @SuppressWarnings("unused")
    public void add(ClickableWidget widget) {
        if (widget == null) return;
        widgets.add(widget);
    }

    public void forEachWidget(Consumer<ClickableWidget> consumer) {
        for (ClickableWidget widget : widgets) {
            consumer.accept(widget);
        }
    }
}

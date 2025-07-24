package config.practical.list;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;

import java.util.List;

public class ConfigEntry extends ElementListWidget.Entry<ConfigEntry> {

    private final ClickableWidget widget;

    public ConfigEntry (ClickableWidget widget) {
        this.widget = widget;
    }

    public ClickableWidget getWidget() {
        return widget;
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return List.of(widget);
    }

    @Override
    public List<? extends Element> children() {
        return List.of(widget);
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickProgress) {
        widget.setPosition((entryWidth - widget.getWidth()) / 2  + x, y);
        widget.render(context, mouseX, mouseY, tickProgress);
    }
}

package config.practical.widgets.abstracts;

import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class ConfigParent extends ClickableWidget implements ParentElement {

    private final ArrayList<ClickableWidget> widgets = new ArrayList<>();

    public ConfigParent(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public boolean isDragging() {
        return false;
    }

    @Override
    public void setDragging(boolean dragging) {

    }

    @Override
    public @Nullable Element getFocused() {
        return null;
    }

    @Override
    public List<? extends Element> children() {
        return widgets;
    }

    @Override
    public void setFocused(@Nullable Element focused) {

    }

    public abstract void update(int width, int height);

    public void forEachInParent(Consumer<ClickableWidget> consumer) {
        for (ClickableWidget widget: widgets) consumer.accept(widget);
    }
}

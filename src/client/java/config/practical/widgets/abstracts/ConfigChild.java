package config.practical.widgets.abstracts;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

public abstract class ConfigChild extends ClickableWidget {

    private final int normalWidth;

    public ConfigChild(int width, int height) {
        super(0, 0, 0, height, Text.empty());
        this.normalWidth = width;
    }

    public void update(int x, int y) {
        if (showWidget()) {
            setWidth(normalWidth);
        } else {
            setWidth(0);
        }

        updatePosition(x, y);
    }

    public int getNormalWidth() {return normalWidth;}

    protected abstract boolean showWidget();

    protected abstract void updatePosition(int x, int y);
}

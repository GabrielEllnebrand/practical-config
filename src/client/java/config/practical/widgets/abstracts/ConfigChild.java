package config.practical.widgets.abstracts;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

public abstract class ConfigChild extends ClickableWidget {

    private final int normalWidth;
    private final ConfigParent parent;

    public ConfigChild(ConfigParent parent, int width, int height) {
        super(0, 0, 0, height, Text.empty());
        this.normalWidth = width;
        this.parent = parent;
    }

    public void update(int x, int y) {
        if (showWidget()) {
            setWidth(normalWidth);
        } else {
            setWidth(0);
        }

        updatePosition(x, y);
    }

    @Override
    public boolean isSelected() {
        return super.isSelected();
    }

    public int getNormalWidth() {return normalWidth;}

    protected abstract boolean showWidget();

    protected abstract void updatePosition(int x, int y);
}

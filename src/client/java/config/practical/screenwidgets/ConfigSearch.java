package config.practical.screenwidgets;

import config.practical.ConfigurableScreen;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class ConfigSearch extends TextFieldWidget {

    public static final int WIDTH = 150;
    public static final int HEIGHT = 30;

    public ConfigSearch(TextRenderer textRenderer,int x, int y, ConfigurableScreen screen) {
        super(textRenderer, x, y, WIDTH, HEIGHT, Text.empty());
        this.setChangedListener(screen::updateScroll);
    }


}

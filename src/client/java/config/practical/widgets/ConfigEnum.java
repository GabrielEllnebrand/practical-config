package config.practical.widgets;

import config.practical.widgets.abstracts.ConfigParent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

public class ConfigEnum extends ConfigParent {

    public static final int WIDTH = 200;
    public static final int HEIGHT = 20;

    public class EnumList extends ClickableWidget {

        public EnumList(int x, int y, int width, int height, Text message) {
            super(x, y, width, height, message);
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {

        }

        @Override
        protected void appendClickableNarrations(NarrationMessageBuilder builder) {

        }
    }

    public ConfigEnum(Text message) {
        super(0,0,100, 100, message);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        int x = getX();
        int y = getY();
        context.fill(x, y, x + 100, y + 100, 0xff006666);
    }

    @Override
    public void update(int width, int height) {

    }
}

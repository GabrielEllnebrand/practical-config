package config.practical.widgets.enums;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

public class OptionsList<T> extends ClickableWidget {

    private static final int WIDTH = 50;
    private static final int ELEMENT_HEIGHT = 30;
    private final T[] options;
    private final ConfigOptions parent;

    public OptionsList(Text message, ConfigOptions parent, T[] options) {
        super(0, 0,WIDTH, options.length * ELEMENT_HEIGHT, message);
        this.parent = parent;
        this.options = options;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();

        context.fill(x, y, x + width, y + height, 0xff666666);
    }

    public void update() {
        if (parent.displayList()) {
            this.width = WIDTH;
        } else {
            this.width = 0;
        }

        this.setX(parent.getX() + 100);
        this.setY(parent.getY() + 20);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }
}

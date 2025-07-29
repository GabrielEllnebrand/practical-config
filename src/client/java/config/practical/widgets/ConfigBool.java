package config.practical.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ConfigBool extends ClickableWidget {


    public static final int WIDTH = 200;
    public static final int HEIGHT = 20;

    public static final int BUTTON_WIDTH = 32;
    public static final int BUTTON_HEIGHT = 14;

    public static final int BUTTON_SLIDER_SIZE = BUTTON_HEIGHT;
    public static final int BUTTON_MARGIN = 3;

    public static final int BACKGROUND_COLOR = 0xff666666;
    public static final int BLACK_COLOR = 0xff000000;
    public static final int WHITE_COLOR = 0xffffffff;

    public static final int DISABLED_COLOR = 0xffff0000;
    public static final int ENABLED_COLOR = 0xff00ff00;

    public static final int SLIDER_COLOR = 0xff444444;

    private final Consumer<Boolean> consumer;
    private final Supplier<Boolean> supplier;

    public ConfigBool(Text message, Supplier<Boolean> supplier, Consumer<Boolean> consumer) {
        super(0, 0, WIDTH, HEIGHT, message);
        this.supplier = supplier;
        this.consumer = consumer;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        int x = getX();
        int y = getY();
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;

        Text message = getMessage();

        context.fill(x, y, x + WIDTH, y + HEIGHT, BACKGROUND_COLOR);
        context.drawBorder(x, y, WIDTH, HEIGHT, this.isFocused() ? WHITE_COLOR : BLACK_COLOR);

        context.drawText(textRenderer, message, x + 6, y + 6, WHITE_COLOR, true);

        context.fill(x + WIDTH - BUTTON_WIDTH - BUTTON_MARGIN, y + BUTTON_MARGIN, x + WIDTH - BUTTON_MARGIN, y + HEIGHT - BUTTON_MARGIN, SLIDER_COLOR);
        context.drawBorder(x + WIDTH - BUTTON_WIDTH - BUTTON_MARGIN, y + BUTTON_MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT, BLACK_COLOR);

        int isTrueValue = supplier.get() ? BUTTON_WIDTH - BUTTON_SLIDER_SIZE : 0;
        int color = supplier.get() ? ENABLED_COLOR : DISABLED_COLOR;
        context.fill(x + WIDTH - BUTTON_WIDTH - BUTTON_MARGIN + isTrueValue, y + BUTTON_MARGIN, x + WIDTH - BUTTON_WIDTH - BUTTON_MARGIN + isTrueValue + BUTTON_SLIDER_SIZE, y + HEIGHT - BUTTON_MARGIN, color);
        context.drawBorder(x + WIDTH - BUTTON_WIDTH - BUTTON_MARGIN + isTrueValue, y + BUTTON_MARGIN, BUTTON_SLIDER_SIZE, BUTTON_SLIDER_SIZE, BLACK_COLOR);

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        consumer.accept(!supplier.get());
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }
}

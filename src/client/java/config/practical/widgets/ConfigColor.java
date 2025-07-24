package config.practical.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConfigColor extends ClickableWidget {

    public static final int WIDTH = 150;
    public static final int HEIGHT = 30;

    public static final int COLOR_HEIGHT = 20;

    public static final int TEXT_COLOR = 0xffffffff;

    private static final Set<Character> ALLOWED_CHARS = Set.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f');

    private final Consumer<Integer> consumer;
    private final Supplier<Integer> supplier;
    private String text;

    public ConfigColor(Text message, Supplier<Integer> supplier, Consumer<Integer> consumer) {
        super(0, 0, WIDTH, HEIGHT, message);
        this.text = String.format("%06X", supplier.get());
        this.supplier = supplier;
        this.consumer = consumer;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_BACKSPACE && !text.isEmpty()) {
            text = text.substring(0, text.length() - 1);
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (ALLOWED_CHARS.contains(chr) && text.length() < 8) {
            text += chr;
        }

        if (text.length() == 8) {
            try {
                int color = (int) Long.parseLong(text, 16);
                consumer.accept(color);
            } catch (NumberFormatException ignored) {
            }
        }

        return super.charTyped(chr, modifiers);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {

        int x = getX();
        int y = getY();
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;

        Text message = getMessage();


        context.drawText(textRenderer, message, x + (WIDTH - textRenderer.getWidth(message)) / 2, y, TEXT_COLOR, true);

        context.fill(x, y + HEIGHT - COLOR_HEIGHT, x + WIDTH, y + HEIGHT, supplier.get());

        if (this.isFocused()) {
            context.drawBorder(x, y + HEIGHT - COLOR_HEIGHT, WIDTH, COLOR_HEIGHT, 0xffffffff);
        } else {
            context.drawBorder(x, y + HEIGHT - COLOR_HEIGHT, WIDTH, COLOR_HEIGHT, 0xff000000);
        }


        context.drawText(textRenderer, "0x" + text + (canAddMoreChars() ? "_" : ""), x + 1, y + height - textRenderer.fontHeight - 1, TEXT_COLOR, true);
    }

    private boolean canAddMoreChars() {
        return this.isFocused() && text.length() < 8;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

}

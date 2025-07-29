package config.practical;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ContainerWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ConfigSection extends ContainerWidget {

    private static final int PADDING = 3;

    private static final int ITEM_MARGIN = 2;
    private static final int TEXT_HEIGHT = 16;
    private static final int DEFAULT_WIDTH = 150;

    private static  final int BACKGROUND_COLOR = 0xaa222222;
    private static  final  int TEXT_COLOR = 0xffffffff;

    private final ArrayList<ClickableWidget> children;

    public ConfigSection(Text text) {
        super(0, 0, DEFAULT_WIDTH, TEXT_HEIGHT + PADDING, text);
        children = new ArrayList<>();
    }

    @Override
    public List<? extends Element> children() {
        return children;
    }

    @SuppressWarnings("unused")
    public void add(ClickableWidget widget) {
        if (widget == null) return;
        children.add(widget);
        height += widget.getHeight() + ITEM_MARGIN;
        width = Math.max(width, widget.getWidth() + PADDING*2);
    }

    @Override
    protected int getContentsHeightWithPadding() {
        return height;
    }

    @Override
    protected double getDeltaYPerScroll() {
        return 0;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        int x = getX();
        int y = getY();

        context.enableScissor(x, y, x + width, y + height);
        context.fill(x, y, x + width, y + height, BACKGROUND_COLOR);

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        Text text = getMessage();

        context.drawText(MinecraftClient.getInstance().textRenderer, text, (width - textRenderer.getWidth(text)) / 2 + x, y + PADDING, TEXT_COLOR, true);


        for (ClickableWidget widget : children) {
            widget.render(context, mouseX, mouseY, deltaTicks);
        }
        context.disableScissor();

    }

    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);
        update();
    }

    public void update() {
        int x = getX();
        int y = getY();

        int currY = y + TEXT_HEIGHT;
        int halfWidth = width / 2;

        for(ClickableWidget widget: children) {
            width = Math.max(width, widget.getWidth() + PADDING*2);
        }

        for (ClickableWidget widget : children) {
            widget.setPosition(x + halfWidth - widget.getWidth() / 2, currY);
            currY += widget.getHeight() + ITEM_MARGIN;
        }
    }

    public boolean searchTermExists(String searchTermLowered) {
        for (ClickableWidget widget : children) {
            String message = widget.getMessage().getString().toLowerCase();
            if (message.contains(searchTermLowered)) {
                return true;
            }
        }
        return false;
    }


    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }
}

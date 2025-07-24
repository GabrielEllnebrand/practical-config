package config.practical;


import config.practical.category.ConfigCategory;
import config.practical.category.ConfigCategoryList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class ConfigurableScreen extends Screen {

    private static final int WIDGET_X_OFFSET = 4;

    private static final int LIST_Y_OFFSET = 50;

    private static final int CATEGORY_Y_OFFSET = 10;

    private static final int TITLE_COLOR = 0xffffffff;
    private static final float TITLE_SCALAR = 1.5f;
    private static final int TITLE_Y_OFFSET = 20;

    private final ConfigSearch search;
    private final ConfigCategoryList categories;
    private final ConfigScroll scroll;

    private final Screen parent;

    public ConfigurableScreen(Text title, Screen parent) {
        super(title);

        MinecraftClient client = MinecraftClient.getInstance();
        Window window = client.getWindow();

        scroll = new ConfigScroll(0, LIST_Y_OFFSET, window.getScaledWidth(), window.getScaledHeight() - LIST_Y_OFFSET, 200);
        search = new ConfigSearch(client.textRenderer, WIDGET_X_OFFSET, (LIST_Y_OFFSET - ConfigSearch.HEIGHT) / 2, this);
        categories = new ConfigCategoryList(client.textRenderer, selected -> updateScroll(""), WIDGET_X_OFFSET, LIST_Y_OFFSET + CATEGORY_Y_OFFSET);

        this.parent = parent;
    }

    public ConfigurableScreen(Text title) {
        this(title, null);
    }

    @Override
    protected void init() {

        addDrawableChild(search);
        addDrawableChild(categories);
        addDrawableChild(scroll);
        updateScroll("");
    }

    public void addCategory(ConfigCategory category) {
        categories.addCategory(category);
    }

    public void updateScroll(String searchTerm) {
        scroll.children().clear();

        if (searchTerm.isEmpty()) {
            categories.forEachInSelected(scroll::add);
        } else {
            String searchTermLowered = searchTerm.toLowerCase();

            categories.forEachWidget(widget -> {
                String message = widget.getMessage().getString().toLowerCase();
                if (message.contains(searchTermLowered)) {
                    scroll.add(widget);
                } else if (widget instanceof ConfigSection section) {
                    if (section.searchTermExists(searchTermLowered)) scroll.add(widget);
                }
            });
        }

        scroll.setScrollY(0);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);

        float centerX = (MinecraftClient.getInstance().getWindow().getScaledWidth() - (this.textRenderer.getWidth(this.title) * TITLE_SCALAR)) / 2;
        MatrixStack stack = context.getMatrices();
        stack.push();
        stack.translate(centerX, TITLE_Y_OFFSET, 0);
        stack.scale(TITLE_SCALAR, TITLE_SCALAR, 1);
        context.drawText(this.textRenderer, this.title, 0, 0, TITLE_COLOR, true);
        stack.pop();
    }

    @Override
    public void close() {
        assert this.client != null;
        this.client.setScreen(this.parent);
    }
}

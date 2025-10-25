package config.practical.category;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class ConfigCategoryList extends ClickableWidget {

    private static final int WIDTH = 120;
    private static final int CATEGORY_HEIGHT = 26;

    private static final int PADDING_X = 4;

    private static final int BACKGROUND_COLOR = 0xff222222;
    private static final int TEXT_COLOR = 0xffffffff;

    private static final int SELECTED_COLOR = 0xffffffff;
    private static final int UNSELECTED_COLOR = 0xff000000;

    private final ArrayList<ConfigCategory> categories;
    private final TextRenderer renderer;
    private final CategoryChangeListener listener;

    private ConfigCategory selected;

    public ConfigCategoryList(TextRenderer renderer, CategoryChangeListener listener, int x, int y) {
        super(x, y, WIDTH, CATEGORY_HEIGHT, Text.literal(""));
        this.categories = new ArrayList<>();
        this.renderer = renderer;
        this.listener = listener;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {

        int x = getX();
        int y = getY();

        context.enableScissor(x, y, x + width, y + height);
        context.fill(x, y, x + width, y + height, BACKGROUND_COLOR);

        int textPadding = (CATEGORY_HEIGHT - renderer.fontHeight) / 2;

        for (int i = 0; i < categories.size(); i++) {

            ConfigCategory category = categories.get(i);
            context.drawBorder(x, y + i * CATEGORY_HEIGHT, width, CATEGORY_HEIGHT, (category == selected ? SELECTED_COLOR : UNSELECTED_COLOR));

            String name = category.name;

            context.drawText(renderer, name, x + PADDING_X, y + i * CATEGORY_HEIGHT + textPadding, TEXT_COLOR, true);
        }

        context.disableScissor();
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        int index = (int) ((mouseY - getY()) / CATEGORY_HEIGHT);

        if (index < categories.size() && index > -1) {
            selected = categories.get(index);
            if (listener != null) listener.update(selected);
        }
    }

    public void addCategory(ConfigCategory category) {
        if (category == null) return;
        if (selected == null) selected = category;
        categories.add(category);

        height = CATEGORY_HEIGHT * categories.size();
    }

    public ArrayList<ClickableWidget> searchWidgets(String term) {
        if (term.isEmpty()) {
            return selected.widgets;
        }

        ArrayList<ClickableWidget> temp = new ArrayList<>();

        for (ConfigCategory category: categories) {
            temp.addAll(category.searchWidgets(term));
        }

        return temp;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

    public interface CategoryChangeListener {
        void update(ConfigCategory selected);
    }
}

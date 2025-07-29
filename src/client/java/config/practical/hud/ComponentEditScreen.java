package config.practical.hud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class ComponentEditScreen extends Screen {

    private static final Text TITLE = Text.literal("");
    private static final Text INFO_SCALE_TEXT = Text.literal("Use the mouse wheel to increase or decrease the scale");
    private static final Text INFO_RESET_TEXT = Text.literal("Press r to reset the selected component");

    private static final int RESET_WIDTH = 100;
    private static final int WIDGET_HEIGHT = 20;
    private static final int WIDGET_MARGIN = 4;

    private static final float SCALE_FACTOR = 0.02f;

    private final ArrayList<HUDComponent> components;
    private final Screen parent;

    private HUDComponent selected;
    private boolean isDragging = false;

    public ComponentEditScreen(Screen parent) {
        super(TITLE);
        this.parent = parent;
        this.components = HUDRender.getComponents();
    }

    @Override
    protected void init() {

        assert this.client != null;
        Window window = this.client.getWindow();
        int windowWidth = window.getScaledWidth();
        int windowHeight = window.getScaledHeight();

        ButtonWidget reset = ButtonWidget.builder(Text.literal("Reset All"),
                        (button -> components.forEach(HUDComponent::reset))
                ).position((windowWidth - RESET_WIDTH) / 2, (windowHeight - WIDGET_HEIGHT) / 2)
                .size(RESET_WIDTH, WIDGET_HEIGHT)
                .build();

        assert this.client != null;
        TextWidget infoScale = new TextWidget(INFO_SCALE_TEXT, this.client.textRenderer);
        infoScale.setPosition((windowWidth - infoScale.getWidth()) / 2, (windowHeight - WIDGET_HEIGHT) / 2 + WIDGET_HEIGHT + WIDGET_MARGIN);
        TextWidget infoReset = new TextWidget(INFO_RESET_TEXT, this.client.textRenderer);
        infoReset.setPosition((windowWidth - infoReset.getWidth()) / 2, (windowHeight - WIDGET_HEIGHT) / 2 + 2 * (WIDGET_HEIGHT + WIDGET_MARGIN));

        addDrawableChild(reset);
        addDrawableChild(infoScale);
        addDrawableChild(infoReset);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (selected != null) {
            float newScale = selected.getScale() + (float) Math.signum(verticalAmount) * SCALE_FACTOR;
            selected.setScale(newScale);
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (int) mouseX;
        int y = (int) mouseY;

        selected = null;
        for (HUDComponent component : components) {

            if (component.inBounds(x, y)) {
                selected = component;
                isDragging = true;
                break;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isDragging && selected != null) {
            selected.move((int) deltaX, (int) deltaY);
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        isDragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_R && selected != null) {
            selected.reset();
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        if (selected != null) {
            selected.renderHighlight(context);
        }

        for (HUDComponent component : components) {
            component.renderIgnoreConditions(context);
        }
    }

    @Override
    public void close() {
        assert this.client != null;
        this.client.setScreen(parent);
    }
}

package config.practical.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

public class HUDComponent {

    private static final float MIN_SCALE = 0.2f;
    private static final float MAX_SCALE = 5f;

    private static final int HIGHLIGHT_COLOR = 0x99ffffff;
    private static final int HIGHLIGHT_MARGIN = 2;

    private static final MinecraftClient client = MinecraftClient.getInstance();

    private transient final double defaultX, defaultY;
    private transient final float defaultScale;

    private transient int width, height;

    private double x, y;
    private float scale;
    private String info;
    private transient final ConditionSupplier conditionSupplier;
    private transient final RenderSupplier renderSupplier;

    /**
     * x, y goes from 0 to 1 and get scaled
     * up using the Window with getScaledWidth
     * and get getScaledHeight
     *
     * @param x                 0 to 1
     * @param y                 0 to 1
     * @param width             int
     * @param height            int
     * @param scale             a scale that's between MIN_SCALE and MAX_SCALE
     * @param info              text info that will display when its selected
     * @param conditionSupplier the condition to render
     * @param renderSupplier    the function that is used to render it
     */
    public HUDComponent(double x, double y, int width, int height, float scale, String info, @NotNull ConditionSupplier conditionSupplier, @NotNull RenderSupplier renderSupplier) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.scale = MathHelper.clamp(scale, MIN_SCALE, MAX_SCALE);
        this.info = info;

        defaultX = x;
        defaultY = y;
        defaultScale = MathHelper.clamp(scale, MIN_SCALE, MAX_SCALE);

        this.conditionSupplier = conditionSupplier;
        this.renderSupplier = renderSupplier;

        HUDRender.addComponent(this);
    }

    public HUDComponent(double x, double y, int width, int height, float scale, @NotNull ConditionSupplier conditionSupplier, @NotNull RenderSupplier renderSupplier) {
        this(x, y, width, height, scale, "", conditionSupplier, renderSupplier);
    }

    public void reset() {
        x = defaultX;
        y = defaultY;
        scale = defaultScale;
    }

    public void move(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    public void setScale(float scale) {
        this.scale = MathHelper.clamp(scale, MIN_SCALE, MAX_SCALE);
    }

    public void copyAttributes(HUDComponent component) {
        this.x = component.x;
        this.y = component.y;
        this.scale = component.scale;
    }

    public float getScale() {
        return scale;
    }

    public int getScaledX() {
        int screenWidth = client.getWindow().getScaledWidth();
        return (int) (x * screenWidth / scale);
    }

    public int getScaledY() {
        int screenHeight = client.getWindow().getScaledHeight();
        return (int) (y * screenHeight / scale);
    }

    @SuppressWarnings("unused")
    public int getWidth() {
        return width;
    }

    @SuppressWarnings("unused")
    public int getHeight() {
        return height;
    }

    @SuppressWarnings("unused")
    public void setWidth(int width) {
        this.width = width;
    }

    @SuppressWarnings("unused")
    public void setHeight(int height) {
        this.height = height;
    }

    @SuppressWarnings("unused")
    public void setDimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public boolean inBounds(int mouseX, int mouseY) {
        double screenX = x * client.getWindow().getScaledWidth();
        double screenY = y * client.getWindow().getScaledHeight();

        return screenX <= mouseX && mouseX <= screenX + (width * scale)
                && screenY <= mouseY && mouseY <= screenY + (height * scale);
    }

    public void render(DrawContext context) {
        if (!conditionSupplier.shouldRender()) return;

        MatrixStack stack = context.getMatrices();
        stack.push();
        stack.scale(scale, scale, 1);
        renderSupplier.render(this, context);
        stack.pop();
    }

    public void renderIgnoreConditions(DrawContext context) {
        MatrixStack stack = context.getMatrices();
        stack.push();
        stack.scale(scale, scale, 1);
        renderSupplier.render(this, context);
        stack.pop();
    }

    public void renderHighlight(DrawContext context) {
        MatrixStack stack = context.getMatrices();
        stack.push();
        stack.scale(scale, scale, 1);
        int x = getScaledX();
        int y = getScaledY();
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        context.fill(x - HIGHLIGHT_MARGIN, y - HIGHLIGHT_MARGIN, x + width + HIGHLIGHT_MARGIN, y + height + HIGHLIGHT_MARGIN, HIGHLIGHT_COLOR);
        context.drawText(textRenderer, info, x + (width - textRenderer.getWidth(info)) / 2, y - textRenderer.fontHeight - 2, 0xffffffff, true);
        stack.pop();
    }

    public interface ConditionSupplier {
        /**
         * Will be called when the function render is called
         * Make use of it so the component only renders
         * when you want it to render
         *
         * @return true if it should render, else false
         */
        boolean shouldRender();
    }

    public interface RenderSupplier {
        /**
         * Will be called in function render
         * if ConditionSupplier returns true
         * NOTE: component is the component itself
         * and scaledX and scaledY should be used
         * for the x and y position
         *
         * @param component the component itself
         * @param context   DrawContext
         */
        void render(HUDComponent component, DrawContext context);
    }
}

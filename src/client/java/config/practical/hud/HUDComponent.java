package config.practical.hud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

public class HUDComponent {

    private static final float MIN_SCALE = 0.2f;
    private static final float MAX_SCALE = 5f;

    private static final int HIGHLIGHT_COLOR = 0x99ffffff;
    private static final int HIGHLIGHT_MARGIN = 2;

    private transient final int defaultX, defaultY;
    private transient final float defaultScale;

    public transient final int width, height;

    private double x, y;
    private float scale;
    private transient final ConditionSupplier conditionSupplier;
    private transient final RenderSupplier renderSupplier;

    public HUDComponent(int x, int y, int width, int height, float scale, @NotNull ConditionSupplier conditionSupplier, @NotNull RenderSupplier renderSupplier) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.scale = scale;

        defaultX = x;
        defaultY = y;
        defaultScale = scale;

        this.conditionSupplier = conditionSupplier;
        this.renderSupplier = renderSupplier;

        HUDRender.addComponent(this);
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
        return (int) (  x / scale);
    }

    public int getScaledY() {
        return (int) (y / scale);
    }

    public boolean inBounds(int mouseX, int mouseY) {
        return x <= mouseX && mouseX <= x + (width * scale) && y <= mouseY && mouseY <= y + (height * scale);
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
        context.fill(x - HIGHLIGHT_MARGIN, y - HIGHLIGHT_MARGIN, x + width + HIGHLIGHT_MARGIN, y + height + HIGHLIGHT_MARGIN, HIGHLIGHT_COLOR);
        stack.pop();
    }

    public interface ConditionSupplier {
        /**
         * Will be called when the function render is called
         * Make use of it so the component only renders
         * when you want it to render
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
         * @param component the component itself
         * @param context DrawContext
         */
        void render(HUDComponent component, DrawContext context);
    }
}

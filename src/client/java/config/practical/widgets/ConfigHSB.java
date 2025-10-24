package config.practical.widgets;

import config.practical.PracticalconfigClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.Color;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConfigHSB extends ClickableWidget {

    private static final Identifier HUE_SPRITE = Identifier.of(PracticalconfigClient.NAMESPACE, "hue-sprite");
    private static final Identifier ALPHA_SPRITE = Identifier.of(PracticalconfigClient.NAMESPACE, "alpha-sprite");

    public static final int SPRITE_WIDTH = 64;
    public static final int SB_MASK_HEIGHT = 32;
    public static final int HUE_HEIGHT = 8;
    public static final int ALPHA_HEIGHT = 8;
    public static final int PADDING = 4;
    public static final int WIDTH = 200;
    public static final int HEIGHT = HUE_HEIGHT + SB_MASK_HEIGHT + PADDING * 4;

    public static final int BACKGROUND_COLOR = 0xff666666;
    public static final int BLACK_COLOR = 0xff000000;
    public static final int WHITE_COLOR = 0xffffffff;

    private float hue;
    private int colorAlpha;
    private final Identifier sbMask;

    private boolean isSelectingHue = false;

    private final Supplier<Integer> supplier;
    private final Consumer<Integer> consumer;

    public ConfigHSB(Text message, Supplier<Integer> supplier, Consumer<Integer> consumer, String identifier) {
        super(0, 0, WIDTH, HEIGHT, message);
        this.supplier = supplier;
        this.consumer = consumer;

        //get current hue value
        float[] hsb = new float[3];
        int color = supplier.get();

        colorAlpha = (color >> 24 & 0xff);
        int r = (color >> 16 & 0xff);
        int g = (color >> 8 & 0xff);
        int b = (color & 0xff);

        Color.RGBtoHSB(r, g, b, hsb);
        hue = hsb[0];

        sbMask = Identifier.of(PracticalconfigClient.NAMESPACE, identifier);
        makeSBMask();
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {

        int x = getX();
        int y = getY();

        context.fill(x, y, x + WIDTH, y + HEIGHT, BACKGROUND_COLOR);
        context.drawBorder(x, y, WIDTH, HEIGHT, this.isFocused() ? WHITE_COLOR : BLACK_COLOR);

        if (sbMask == null) return;
        /*context.drawText(MinecraftClient.getInstance().textRenderer, getMessage(), x, y, 0xffffffff,true);
        context.drawTexture(RenderLayer::getGuiTextured, sbMask, x + PADDING, y + PADDING, 0, 0, SPRITE_WIDTH, SB_MASK_HEIGHT, SPRITE_WIDTH, SB_MASK_HEIGHT, 0xffffffff);
        context.drawGuiTexture(RenderLayer::getGuiTextured, HUE_SPRITE, x + PADDING, y + SB_MASK_HEIGHT + PADDING * 2, SPRITE_WIDTH, HUE_HEIGHT, 0xffffffff);
        context.fill(x + PADDING * 2 + SPRITE_WIDTH, y + PADDING, x + PADDING * 2 + SPRITE_WIDTH + 24, y + PADDING + 24, supplier.get());
        context.drawGuiTexture(RenderLayer::getGuiTextured, ALPHA_SPRITE, x + PADDING, y + SB_MASK_HEIGHT + PADDING * 3 + HUE_HEIGHT, SPRITE_WIDTH, ALPHA_HEIGHT, 0xffffffff);
        */
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (inHueBounds(mouseX, mouseY)) {
            hue = getHue(mouseX);
            makeSBMask();
            isSelectingHue = true;
        }

        if (inSBMaskBounds(mouseX, mouseY)) {
            updateColor(mouseX, mouseY);
        }
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        if (isSelectingHue) {
            hue = getHue(mouseX);
            makeSBMask();
        }
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        isSelectingHue = false;
    }

    private boolean inSBMaskBounds(double mouseX, double mouseY) {
        int x = getX();
        int y = getY();

        int sbMaskCornerX = x + PADDING;
        int sbMaskCornerY = y + PADDING;

        return sbMaskCornerX <= mouseX && mouseX <= sbMaskCornerX + SPRITE_WIDTH && sbMaskCornerY <= mouseY && mouseY <= sbMaskCornerY + SB_MASK_HEIGHT;
    }

    private void updateColor(double mouseX, double mouseY) {
        float saturation = (float) ((mouseX - getX() - PADDING) / (float) SPRITE_WIDTH);
        float brightness = (float) (1 - ((mouseY - getY() - PADDING) / (float) SB_MASK_HEIGHT));

        int color = Color.HSBtoRGB(hue, saturation, brightness) | (colorAlpha << 24);
        consumer.accept(color);
    }

    private boolean inHueBounds(double mouseX, double mouseY) {
        int x = getX();
        int y = getY();

        int hueCornerX = x + PADDING;
        int hueCornerY = y + SB_MASK_HEIGHT + PADDING * 2;

        return hueCornerX <= mouseX && mouseX <= hueCornerX + SPRITE_WIDTH && hueCornerY <= mouseY && mouseY <= hueCornerY + HUE_HEIGHT;
    }

    private float getHue(double mouseX) {
        return (float) (Math.clamp((mouseX - getX() - PADDING) / SPRITE_WIDTH, 0, 1));
    }

    private void makeSBMask() {
        try (NativeImage image = new NativeImage(SPRITE_WIDTH, SB_MASK_HEIGHT, true)) {

            float saturationFraction = (float) (1.0 / (SPRITE_WIDTH - 1));
            float brightnessFraction = (float) (1.0 / (SB_MASK_HEIGHT - 1));

            for (int i = 0; i < SPRITE_WIDTH; i++) {
                for (int j = 0; j < SB_MASK_HEIGHT; j++) {

                    float saturation = Math.min(i * saturationFraction, 1);
                    float brightness = Math.min((SB_MASK_HEIGHT - 1 - j) * brightnessFraction, 1);

                    int argb = Color.HSBtoRGB(hue, saturation, brightness) | (0xff << 24);

                    //so for some reason the default format is supposed to be argb
                    //however it's all coded to use abgr for some reason so have to
                    //use this function instead...
                    image.setColorArgb(i, j, argb);
                }
            }

            TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
            NativeImageBackedTexture texture = new NativeImageBackedTexture(() -> "TODO: change this", image);

            texture.upload();
            textureManager.destroyTexture(sbMask);
            textureManager.registerTexture(sbMask, texture);
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }
}

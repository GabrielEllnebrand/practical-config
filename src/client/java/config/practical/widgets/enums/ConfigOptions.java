package config.practical.widgets.enums;

import config.practical.widgets.abstracts.ConfigParent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class ConfigOptions <T> extends ConfigParent {

    public static final int WIDTH = 200;
    public static final int HEIGHT = 20;

    private final OptionsList list;
    private boolean displayList = false;

    public ConfigOptions(Text message, T[] options) {
        super(0, 0, WIDTH, HEIGHT, message);
        list = new OptionsList(message, this, options);
        addChild(list);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();
        context.fill(x, y, x + width, y + height, 0xff006666);

        if (displayList) {
            list.renderWidget(context, mouseX, mouseY, deltaTicks);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        displayList = !displayList;
        System.out.println("toggled? " + displayList);
        list.update();
    }

    public boolean displayList() {
        return displayList;
    }

    @Override
    public void update() {
        list.update();
    }
}

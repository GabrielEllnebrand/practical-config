package config.practical.category;

import config.practical.ConfigSection;
import config.practical.widgets.abstracts.ConfigParent;
import net.minecraft.client.gui.widget.ClickableWidget;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ConfigCategory {
    public final String name;
    public final ArrayList<ClickableWidget> widgets;

    public ConfigCategory(String name) {
        this.name = name;
        widgets = new ArrayList<>();
    }

    @SuppressWarnings("unused")
    public void add(ClickableWidget widget) {
        if (widget == null) return;
        widgets.add(widget);
    }

    public ArrayList<ClickableWidget> searchWidgets(String term) {

        ArrayList<ClickableWidget> temp = new ArrayList<>();

        for (ClickableWidget widget: widgets) {
            String message = widget.getMessage().getString().toLowerCase();
            if (message.contains(term)) {

                if (widget instanceof ConfigSection section) {
                    temp.addAll(section.getAllWidgets());
                }

                if (widget instanceof ConfigParent parent) {
                    temp.addAll(parent.getAllWidgets());
                }

                temp.add(widget);
            }

        }

        return temp;
    }
}

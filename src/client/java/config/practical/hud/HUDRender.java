package config.practical.hud;

import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.fabricmc.fabric.api.client.rendering.v1.LayeredDrawerWrapper;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class HUDRender {

    private static final String IDENTIFIER = "render_hud_components";

    private static final ArrayList<HUDComponent> components = new ArrayList<>();

    public static void render(LayeredDrawerWrapper layeredDrawer) {
        layeredDrawer.attachLayerAfter(IdentifiedLayer.MISC_OVERLAYS, Identifier.of(IDENTIFIER),
                (context, tickCounter) -> {
                    for (HUDComponent component : components) {
                        component.render(context);
                    }
                });
    }

    public static void addComponent(HUDComponent component) {
        if (component == null) return;
        components.add(component);
    }

    public static ArrayList<HUDComponent> getComponents() {
        return components;
    }
}

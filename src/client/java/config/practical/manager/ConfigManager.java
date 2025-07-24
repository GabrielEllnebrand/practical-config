package config.practical.manager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ConfigManager {

    private final String filePath;
    private final List<Class<?>> classes;


    public ConfigManager(String filePath, List<Class<?>> classes) {
        this.filePath = filePath;
        this.classes = classes;
    }

    public ConfigManager(String filePath, Class<?> clazz) {
      this(filePath, List.of(clazz));
    }

    public void save() {

        JsonObject obj = new JsonObject();
        Gson gson = new Gson();

        for (Class<?> clazz : classes) {
            for (Field field : clazz.getDeclaredFields()) {
                if (!field.isAnnotationPresent(ConfigValue.class)) continue;

                field.setAccessible(true);
                try {
                    String name = field.getName();
                    Object value = field.get(null);
                    obj.add(name, gson.toJsonTree(value));
                } catch (IllegalAccessException ignored) {

                }

            }
        }

        JsonElement tree = gson.toJsonTree(obj);
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(tree.toString());
        } catch (IOException ignored) {

        }
    }

    public void load() {
        String jsonContent;
        try {
            jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException ignored) {
            return;
        }
        Gson gson = new Gson();
        JsonObject obj = gson.fromJson(jsonContent, JsonObject.class);
        for (Class<?> clazz : classes) {
            for (Field field : clazz.getDeclaredFields()) {
                if (!field.isAnnotationPresent(ConfigValue.class)) continue;

                String name = field.getName();
                if (obj.has(name)) {
                    try {
                        JsonElement jsonVal = obj.get(name);
                        Object val = gson.fromJson(jsonVal, field.getType());
                        field.set(null, val);

                    } catch (IllegalAccessException ignored) {

                    }
                }
            }
        }
    }


}

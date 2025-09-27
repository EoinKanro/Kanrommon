package io.github.eoinkanro.mc.kanrommon.conf;

import static io.github.eoinkanro.mc.kanrommon.conf.Constants.LOG;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.eoinkanro.mc.kanrommon.conf.impl.ConfigBooleanDefinition;
import io.github.eoinkanro.mc.kanrommon.conf.impl.ConfigDoubleDefinition;
import io.github.eoinkanro.mc.kanrommon.conf.impl.ConfigIntDefinition;
import io.github.eoinkanro.mc.kanrommon.conf.impl.ConfigListDefinition;
import io.github.eoinkanro.mc.kanrommon.conf.impl.ConfigStringDefinition;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Controls config file of mod
 */
public class ConfigManager {

  private final Gson gson = new Gson();

  private final Path configFilePath;
  private JsonObject config;

  public ConfigManager(Path configFolderPath, String configName) {
    this.configFilePath = configFolderPath.resolve(configName + ".json");
    reload();
  }

  /**
   * Read value from config json to definition
   */
  public void read(ConfigBooleanDefinition definition) {
    read(definition, () -> Boolean.parseBoolean(config.get(definition.getParameterName()).toString()));
  }

  /**
   * Read value from config json to definition
   */
  public void read(ConfigStringDefinition definition) {
    read(definition, () -> config.get(definition.getParameterName()).toString());
  }

  /**
   * Read value from config json to definition
   */
  public void read(ConfigListDefinition definition) {
    read(definition, () -> {
      JsonArray array = (JsonArray) config.get(definition.getParameterName());

      List<String> result = new ArrayList<>();
      for (JsonElement element : array) {
        result.add(element.getAsString());
      }

      return result;
    });
  }

  /**
   * Read value from config json to definition
   */
  public void read(ConfigIntDefinition definition) {
    readNumber(definition, () -> Integer.parseInt(config.get(definition.getParameterName()).toString()));
  }

  /**
   * Read value from config json to definition
   */
  public void read(ConfigDoubleDefinition definition) {
    readNumber(definition, () -> Double.parseDouble(config.get(definition.getParameterName()).toString()));
  }

  private <T extends Number & Comparable<T>> void readNumber(ConfigNumberDefinition<T> definition, Supplier<T> supplier) {
    read(definition, supplier);
    correctNumber(definition);
  }

  private <T> void read(ConfigDefinition<T> definition, Supplier<T> supplier) {
    T value = null;
    if (config.has(definition.getParameterName())) {
      try {
        value = supplier.get();
      } catch (Exception e) {
        LOG.warn("Error reading config definition: {}. Using default...", definition.getParameterName());
      }
    }

    if (value == null) {
      value = definition.getDefaultValue();
    }

    definition.setCurrentValue(value);
  }

  /**
   * Save value from definition to config json. See {@link #save()}
   */
  public void write(ConfigDefinition<?> definition) {
    config.add(definition.getParameterName(), gson.toJsonTree(definition.getCurrentValue()));
  }

  /**
   * Save value from definition to config json. See {@link #save()}
   */
  public void writeNumber(ConfigNumberDefinition<?> definition) {
    correctNumber(definition);
    write(definition);
  }

  private <T extends Number & Comparable<T>> void correctNumber(ConfigNumberDefinition<T> definition) {
    if (definition.getCurrentValue().compareTo(definition.getMaxValue()) > 0) {
      definition.setCurrentValue(definition.getMaxValue());
    } else if (definition.getCurrentValue().compareTo(definition.getMinValue()) < 0) {
      definition.setCurrentValue(definition.getMinValue());
    }
  }

  /**
   * Save config json to file
   */
  public void save() {
    try {
      String jsonString = gson.toJson(config);
      Files.writeString(configFilePath, jsonString, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    } catch (Exception e) {
      LOG.error("Can't save config file: {}", configFilePath.toAbsolutePath());
    }
  }

  /**
   * Reload config json from file
   */
  public void reload() {
    if (!Files.exists(configFilePath)) {
      config = new JsonObject();
      return;
    }

    try(Stream<String> lines = Files.lines(configFilePath)) {
      String jsonString = lines.collect(Collectors.joining());
      config = gson.fromJson(jsonString, JsonObject.class);
    } catch (Exception e) {
      LOG.warn("Error reading config file: {}. Creating backup and new config...", configFilePath.toAbsolutePath());
      config = new JsonObject();
      createBackup(configFilePath);
    }
  }

  private void createBackup(Path configFilePath) {
    try {
      Files.move(configFilePath, configFilePath.resolve("_bkp"), REPLACE_EXISTING);
    } catch (Exception e) {
      LOG.warn("Error creating backup file for: {}", configFilePath.toAbsolutePath());
    }
  }

}

package io.github.eoinkanro.mc.kanrommon.utils;

import static io.github.eoinkanro.mc.kanrommon.conf.Constants.LOG;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ResourceUtils {

  public static final String NAMESPACE_SEPARATOR = ":";

  /**
   * @return namespace:name
   */
  public static String getNamespaceAndName(TagKey<Block> block) {
    return block.location().toString();
  }

  /**
   * @return namespace:name
   */
  public static String getNamespaceAndName(Item item) {
    return BuiltInRegistries.ITEM.getKey(item).toString();
  }

  /**
   * Parse namespace:name to ResourceLocation and log wrong locations
   */
  public static List<ResourceLocation> parseLocations(List<String> strings) {
    if (strings == null || strings.isEmpty()) {
      return Collections.emptyList();
    }

    List<ResourceLocation> result = new ArrayList<>();
    for (String string : strings) {
      if (!string.contains(NAMESPACE_SEPARATOR)) {
        LOG.warn("Wrong namespace: {}, skipping", string);
        continue;
      }
      result.add(ResourceLocation.tryParse(string));
    }
    return result;
  }

  /**
   * Check existence of locations in registry.
   * Log if they are not registered
   */
  public static void logWrongLocations(DefaultedRegistry<?> registry, Iterable<ResourceLocation> locations) {
    for (ResourceLocation element : locations) {
      if (!registry.containsKey(element)) {
        LOG.warn("Unknown element: {}", element);
      }
    }
  }

  /**
   * Parse namespace:name to block tags
   */
  public static Set<TagKey<Block>> stringsToTags(List<String> strings) {
    return parseLocations(strings)
        .stream()
        .map(it -> TagKey.create(Registries.BLOCK, it))
        .collect(Collectors.toSet());
  }

  /**
   * Parse namespace:name to ResourceLocation
   */
  public static Set<ResourceLocation> stringsToLocations(List<String> strings) {
    return new HashSet<>(parseLocations(strings));
  }

  /**
   * Parse namespace:name to Item
   */
  public static List<Item> stringsToItems(List<String> strings) {
    return parseLocations(strings)
        .stream()
        .map(BuiltInRegistries.ITEM::get)
        .collect(Collectors.toSet())
        .stream()
        .toList();
  }

}

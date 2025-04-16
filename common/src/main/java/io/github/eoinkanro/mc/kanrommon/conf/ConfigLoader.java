package io.github.eoinkanro.mc.kanrommon.conf;

public interface ConfigLoader {

  /**
   * Load from config file to memory
   */
  void load();

  /**
   * Save new values to config file
   */
  void save();

}

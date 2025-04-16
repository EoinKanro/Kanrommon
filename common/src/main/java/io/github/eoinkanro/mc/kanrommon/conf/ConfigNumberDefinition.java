package io.github.eoinkanro.mc.kanrommon.conf;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ConfigNumberDefinition<T> extends ConfigDefinition<T> {

  @NonNull
  private T minValue;
  @NonNull
  private T maxValue;

}

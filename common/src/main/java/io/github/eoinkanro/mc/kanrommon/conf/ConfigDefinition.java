package io.github.eoinkanro.mc.kanrommon.conf;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class ConfigDefinition<T> {

  @NonNull
  private String name;
  @NonNull
  private String description;
  @NonNull
  private String parameterName;
  @NonNull
  private T defaultValue;
  @Setter
  private T currentValue;

  public T getCurrentValue() {
    return currentValue != null ? currentValue : defaultValue;
  }

}

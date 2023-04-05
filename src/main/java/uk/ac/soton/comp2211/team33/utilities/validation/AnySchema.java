package uk.ac.soton.comp2211.team33.utilities.validation;

import java.util.ArrayList;
import java.util.stream.Collectors;

public abstract class AnySchema<T, S extends AnySchema> {
  private String type;

  // ==== Flags ====

  private String label = "Value";

  private boolean required = false;

  private T defaultValue;

  private boolean strict = false;

  private final ArrayList<T> valids = new ArrayList<>();

  // ==== Rules ====

  private final ArrayList<Rule> rules = new ArrayList<>();

  public AnySchema(String type) {
    this.type = type;
  }

  public S required() {
    if (defaultValue != null) {
      throw new IllegalStateException("Cannot call required() if a default value has been set");
    }

    required = true;
    return (S) this;
  }

  public S strict() {
    strict = true;
    return (S) this;
  }

  public S label(String label) {
    this.label = label;
    return (S) this;
  }

  public S defaultValue(T defaultValue) {
    if (required) {
      throw new IllegalStateException("Cannot have a default value if required() is called");
    }

    this.defaultValue = defaultValue;
    return (S) this;
  }

  public S valid(T... values) {
    for (T value: values) {
      valids.add(value);
    }

    return (S) this;
  }

  public boolean isStrict() {
    return strict;
  }

  protected void addRule(Rule<T> rule) {
    rules.add(rule);
  }

  protected String getLabel() {
    return label;
  }

  protected abstract T validateType(Object value) throws ValidationException;

  public T validate(Object value) throws ValidationException {
    if (value == null || (value instanceof String && value.equals(""))) {
      if (required) {
        throw new ValidationException(getLabel() + " is required");
      }

      return defaultValue;
    }

    for (T valid: valids) {
      if (valid.equals(value)) {
        return valid;
      }

      throw new ValidationException(getLabel() + " must be " +
              valids.stream().map(Object::toString).collect(Collectors.joining(", ")));
    }

    T castValue = validateType(value);

    for (Rule<T> rule: rules) {
      castValue = rule.validate(castValue);
    }

    return castValue;
  }
}

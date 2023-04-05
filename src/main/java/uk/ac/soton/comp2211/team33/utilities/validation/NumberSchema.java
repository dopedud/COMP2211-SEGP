package uk.ac.soton.comp2211.team33.utilities.validation;

public abstract class NumberSchema<T extends Number & Comparable<T>, S extends NumberSchema> extends AnySchema<T, S> {
  private T upperB;

  private T lowerB;

  public NumberSchema() {
    super("number");
  }

  public S max(T upperB) {
    if (lowerB != null && lowerB.compareTo(upperB) > 0) {
      throw new IllegalStateException("Cannot set an upper-bound of " + upperB + " when the lower-bound is " + lowerB);
    }

    this.upperB = upperB;
    return (S) this;
  }

  public S min(T lowerB) {
    if (upperB != null && upperB.compareTo(lowerB) < 0) {
      throw new IllegalStateException("Cannot set a lower-bound of " + lowerB + " when the upper-bound is " + upperB);
    }

    this.lowerB = lowerB;
    return (S) this;
  }

  protected abstract T parse(String value) throws NumberFormatException;

  protected abstract T cast(Number value);

  @Override
  protected T validateType(Object value) throws ValidationException {
    T castValue;

    if (!(value instanceof Number)) {
      if (isStrict() || !(value instanceof String)) {
        throw new ValidationException(getLabel() + " must be a number");
      }

      try {
        castValue = parse((String) value);
      } catch (NumberFormatException ex) {
        throw new ValidationException(getLabel() + " must be a number");
      }
    }
    else {
      castValue = cast((Number) value);
    }

    if (lowerB != null && lowerB.compareTo(castValue) > 0) {
      throw new ValidationException(getLabel() + " must be at least " + lowerB);
    }

    if (upperB != null && upperB.compareTo(castValue) < 0) {
      throw new ValidationException(getLabel() + " must be at most " + upperB);
    }

    return castValue;
  }
}

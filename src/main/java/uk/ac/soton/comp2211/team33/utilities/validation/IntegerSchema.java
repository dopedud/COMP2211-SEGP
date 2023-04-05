package uk.ac.soton.comp2211.team33.utilities.validation;

public class IntegerSchema extends NumberSchema<Integer, IntegerSchema> {
  @Override
  protected Integer parse(String value) throws NumberFormatException {
    return Integer.parseInt(value);
  }

  @Override
  protected Integer cast(Number value) {
    return value.intValue();
  }
}

package uk.ac.soton.comp2211.team33.utilities.validation;

public class DoubleSchema extends NumberSchema<Double, DoubleSchema> {
  @Override
  protected Double parse(String value) throws NumberFormatException {
    return Double.parseDouble(value);
  }

  @Override
  protected Double cast(Number value) {
    return value.doubleValue();
  }
}

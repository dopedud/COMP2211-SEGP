package uk.ac.soton.comp2211.team33.utilities.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringSchema extends AnySchema<String, StringSchema> {
  private Pattern pattern;

  public StringSchema() {
    super("string");
  }

  public StringSchema regex(String regex) {
    pattern = Pattern.compile(regex);
    return this;
  }

  @Override
  protected String validateType(Object value) throws ValidationException {
    String castValue;

    if (!(value instanceof String)) {
      if (isStrict()) {
        throw new ValidationException(getLabel() + " must be a string");
      }

      castValue = value.toString();
    }
    else {
      castValue = (String) value;
    }

    Matcher matcher = pattern.matcher(castValue);

    if (!matcher.find()) {
      throw new ValidationException(getLabel() + " is in invalid format");
    }

    return castValue;
  }
}
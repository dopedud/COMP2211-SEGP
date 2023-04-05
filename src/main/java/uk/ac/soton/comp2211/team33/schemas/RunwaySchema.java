package uk.ac.soton.comp2211.team33.schemas;

import uk.ac.soton.comp2211.team33.utilities.validation.DoubleSchema;
import uk.ac.soton.comp2211.team33.utilities.validation.StringSchema;

public record RunwaySchema(StringSchema designator, DoubleSchema tora, DoubleSchema toda,
                           DoubleSchema asda, DoubleSchema lda, DoubleSchema resa, DoubleSchema threshold) {}

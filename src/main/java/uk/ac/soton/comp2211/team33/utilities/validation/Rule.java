package uk.ac.soton.comp2211.team33.utilities.validation;

interface Rule<T> {
  T validate(T value) throws ValidationException;
}

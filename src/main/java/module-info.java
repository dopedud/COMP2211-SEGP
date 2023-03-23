module uk.ac.soton.comp2211.team33 {
  requires org.apache.logging.log4j;
  requires org.apache.commons.collections4;
  requires javafx.controls;
  requires javafx.fxml;

  exports uk.ac.soton.comp2211.team33;
  exports uk.ac.soton.comp2211.team33.components;
  exports uk.ac.soton.comp2211.team33.models;
  exports uk.ac.soton.comp2211.team33.controllers;
  exports uk.ac.soton.comp2211.team33.utilities;
  exports uk.ac.soton.comp2211.team33.exceptions;

  opens uk.ac.soton.comp2211.team33.components to javafx.fxml;
  opens uk.ac.soton.comp2211.team33.controllers to javafx.fxml;
}
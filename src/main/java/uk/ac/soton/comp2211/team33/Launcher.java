package uk.ac.soton.comp2211.team33;

/**
 * The Launcher class that allows the application to be built into a shaded jar file which then loads JavaFX. This
 * class is used when running as a shaded .jar file.
 *
 * @author Abeed (mabs1u21@soton.ac.uk)
 */
public class Launcher {

  /**
   * Launch the JavaFX Application, passing through the command-line arguments.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    App.main(args);
  }
}

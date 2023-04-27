package uk.ac.soton.comp2211.team33.utilities;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import uk.ac.soton.comp2211.team33.models.Aircraft;
import uk.ac.soton.comp2211.team33.models.Airport;
import uk.ac.soton.comp2211.team33.models.Obstacle;
import uk.ac.soton.comp2211.team33.models.Runway;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class XMLHelpers {

  private static final Logger logger = LogManager.getLogger(XMLHelpers.class);


  /**
   * Private constructor to avoid instancing.
   */
  private XMLHelpers() {}

  public static Airport importAirport(String path) {

    logger.info("Importing airport from " + path);

    Airport newState = null;

    try {


      Document document = new SAXReader().read(path);

      var airportElement = document.getRootElement();

      newState = new Airport(airportElement.attributeValue("city"), airportElement.attributeValue("name"));
      newState.setObstaclesLoaded(Boolean.parseBoolean(airportElement.attributeValue("obstaclesLoaded")));

      var runwaysElement = airportElement.elements().get(0);
      var aircraftsElement = airportElement.elements().get(1);
      var obstaclesElement = airportElement.elements().get(2);

      for (Element runwayElement : runwaysElement.elements()) {
        String designator = runwayElement.attributeValue("designator");
        double tora = Double.parseDouble(runwayElement.elements().get(0).getText());
        double toda = Double.parseDouble(runwayElement.elements().get(1).getText());
        double asda = Double.parseDouble(runwayElement.elements().get(2).getText());
        double lda = Double.parseDouble(runwayElement.elements().get(3).getText());
        double resa = Double.parseDouble(runwayElement.elements().get(4).getText());
        double threshold = Double.parseDouble(runwayElement.elements().get(5).getText());

        newState.addRunway(designator, tora, toda, asda, lda, resa, threshold);
      }

      for (Element aircraftElement : aircraftsElement.elements()) {
        String id = aircraftElement.attributeValue("id");
        double blastProtection = Double.parseDouble(aircraftElement.elements().get(0).getText());

        newState.addAircraft(id, blastProtection);
      }

      for (Element obstacleElement : obstaclesElement.elements()) {
        String name = obstacleElement.attributeValue("name");
        double height = Double.parseDouble(obstacleElement.elements().get(0).getText());
        double length = Double.parseDouble(obstacleElement.elements().get(1).getText());
        double centerline = Double.parseDouble(obstacleElement.elements().get(2).getText());

        newState.addObstacle(name, height, length, centerline);
      }

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return newState;

  }

  public static void exportAirport(Airport airport, String savePath) {

    logger.info("Exporting airport to " + savePath);

    Document document = DocumentHelper.createDocument();

    File file = new File(savePath);

    try {
      Element airportElement = document.addElement("airport").
          addAttribute("city", airport.getCity()).
          addAttribute("name", airport.getName()).
          addAttribute("obstaclesLoaded", String.valueOf(airport.getObstaclesLoaded()));

      Element runwaysElement = airportElement.addElement("runways");
      Element aircraftsElement = airportElement.addElement("aircrafts");
      Element obstaclesElement = airportElement.addElement("obstacles");

      for (Runway runway : airport.runwayListProperty()) {
        Element runwayElement = runwaysElement.addElement("runway").addAttribute("designator", runway.getDesignator());

        runwayElement.addElement("tora").addText(String.valueOf(runway.getTora()));
        runwayElement.addElement("toda").addText(String.valueOf(runway.getToda()));
        runwayElement.addElement("asda").addText(String.valueOf(runway.getAsda()));
        runwayElement.addElement("lda").addText(String.valueOf(runway.getLda()));
        runwayElement.addElement("resa").addText(String.valueOf(runway.getResa()));
        runwayElement.addElement("threshold").addText(String.valueOf(runway.getThreshold()));
      }

      for (Aircraft aircraft : airport.aircraftListProperty()) {
        Element aircraftElement = aircraftsElement.addElement("aircraft").addAttribute("id", aircraft.getId());

        aircraftElement.addElement("blastProtection").addText(String.valueOf(aircraft.getBlastProtection()));
      }

      for (Obstacle obstacle : airport.obstacleListProperty()) {
        Element obstacleElement = obstaclesElement.addElement("obstacle").addAttribute("name", obstacle.getName());

        obstacleElement.addElement("height").addText(String.valueOf(obstacle.getHeight()));
        obstacleElement.addElement("length").addText(String.valueOf(obstacle.getLength()));
        obstacleElement.addElement("centerline").addText(String.valueOf(obstacle.getCenterline()));
      }

      var xmlWriter = new XMLWriter(new FileWriter(file), OutputFormat.createPrettyPrint());
      xmlWriter.write(document);
      xmlWriter.close();

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}

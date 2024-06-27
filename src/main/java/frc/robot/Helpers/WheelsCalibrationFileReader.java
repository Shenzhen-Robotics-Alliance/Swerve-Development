package frc.robot.Helpers;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import edu.wpi.first.wpilibj.Filesystem;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class WheelsCalibrationFileReader {
    private final Map<String, Map<String, Double>> wheelsDataDouble;
    private final Map<String, Map<String, Integer>> wheelsDataInt;

    public WheelsCalibrationFileReader(String calibrationFileName) {
        wheelsDataDouble = new HashMap<>();
        wheelsDataInt = new HashMap<>();
        readXMLFile(Filesystem.getDeployDirectory().getPath() + "/WheelsCalibrationFiles/" + calibrationFileName + ".xml");
    }

    private void readXMLFile(String filePath) {
        try {
            // Load and parse the XML file
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            // Normalize the XML structure
            doc.getDocumentElement().normalize();

            // Get the root element
            Element rootElement = doc.getDocumentElement();

            // Read each wheel calibration
            readWheelConfig(rootElement, "frontLeft");
            readWheelConfig(rootElement, "frontRight");
            readWheelConfig(rootElement, "backLeft");
            readWheelConfig(rootElement, "backRight");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readWheelConfig(Element rootElement, String wheelName) {
        NodeList nodeList = rootElement.getElementsByTagName(wheelName);
        if (nodeList.getLength() == 0) return;

        Node wheelNode = nodeList.item(0);
        if (wheelNode.getNodeType() != Node.ELEMENT_NODE) return;

        Element wheelElement = (Element) wheelNode;
        Map<String, Double> configMapDouble = new HashMap<>();
        Map<String, Integer> configMapInt = new HashMap<>();
        NodeList configNodes = wheelElement.getChildNodes();
        for (int i = 0; i < configNodes.getLength(); i++) {
            Node configNode = configNodes.item(i);
            if (configNode.getNodeType() != Node.ELEMENT_NODE) continue;

            Element configElement = (Element) configNode;
            String configName = configElement.getTagName();
            String type = configElement.getAttribute("type");

            if (type.equals("double")) {
                configMapDouble.put(configName, Double.parseDouble(configElement.getTextContent()));
            } else if (type.equals("int")) {
                configMapInt.put(configName, Integer.parseInt(configElement.getTextContent()));
            }
        }
        if (!configMapDouble.isEmpty()) {
            wheelsDataDouble.put(wheelName, configMapDouble);
        }
        if (!configMapInt.isEmpty()) {
            wheelsDataInt.put(wheelName, configMapInt);
        }
    }

    public double getDoubleConfig(String wheelName, String configName) throws NullPointerException {
        if (!wheelsDataDouble.containsKey(wheelName) || !wheelsDataDouble.get(wheelName).containsKey(configName)) {
            throw new NullPointerException("Configuration not found for wheel: " + wheelName + ", config: " + configName + ", type: double");
        }
        return wheelsDataDouble.get(wheelName).get(configName);
    }

    public int getIntConfig(String wheelName, String configName) throws NullPointerException {
        if (!wheelsDataInt.containsKey(wheelName) || !wheelsDataInt.get(wheelName).containsKey(configName)) {
            throw new NullPointerException("Configuration not found for wheel: " + wheelName + ", config: " + configName + ", type: double");
        }
        return wheelsDataInt.get(wheelName).get(configName);
    }
}

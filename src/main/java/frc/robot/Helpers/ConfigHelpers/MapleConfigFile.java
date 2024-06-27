package frc.robot.Helpers.ConfigHelpers;

import edu.wpi.first.wpilibj.Filesystem;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapleConfigFile { // TODO organize the code here and test
    private final String configType;
    private final String configName;
    private final Map<String, Map<String, Double>> doubleConfigMap = new HashMap<>();
    private final Map<String, Map<String, Integer>> intConfigMap = new HashMap<>();

    public MapleConfigFile(String configType, String configName) {
        this.configType = configType;
        this.configName = configName;
    }

    public double getDoubleConfig(String blockName, String configName) throws NullPointerException {
        if (!doubleConfigMap.containsKey(blockName) || !doubleConfigMap.get(blockName).containsKey(configName)) {
            throw new NullPointerException("Configuration not found for block: " + blockName + ", config: " + configName + ", type: double");
        }
        return doubleConfigMap.get(blockName).get(configName);
    }

    public int getIntConfig(String blockName, String configName) throws NullPointerException {
        if (!intConfigMap.containsKey(blockName) || !intConfigMap.get(blockName).containsKey(configName)) {
            throw new NullPointerException("Configuration not found for block: " + blockName + ", config: " + configName + ", type: int");
        }
        return intConfigMap.get(blockName).get(configName);
    }

    public static MapleConfigFile fromDeployedConfig(String configType, String configName) throws IllegalArgumentException, IOException {
        MapleConfigFile configFile = new MapleConfigFile(configType, configName);
        String filePath = Filesystem.getDeployDirectory().getPath() + "/configs/" + configType + "/" + configName + ".xml";
        File xmlFile = new File(filePath);
        if (!xmlFile.exists()) {
            throw new IOException("Config file does not exist: " + filePath);
        }

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            if (!doc.getDocumentElement().getNodeName().equals(configType)) {
                throw new IllegalArgumentException("Root element is not " + configType);
            }

            NodeList blocks = doc.getDocumentElement().getChildNodes();
            for (int i = 0; i < blocks.getLength(); i++) {
                Node blockNode = blocks.item(i);
                if (blockNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element blockElement = (Element) blockNode;
                    String blockName = blockElement.getTagName();
                    readBlockConfig(blockElement, blockName, configFile);
                }
            }
        } catch (Exception e) {
            throw new IOException("Error reading config file", e);
        }

        return configFile;
    }

    private static void readBlockConfig(Element blockElement, String blockName, MapleConfigFile configFile) {
        NodeList configNodes = blockElement.getChildNodes();
        for (int j = 0; j < configNodes.getLength(); j++) {
            Node configNode = configNodes.item(j);
            if (configNode.getNodeType() == Node.ELEMENT_NODE) {
                addConfigToMap((Element) configNode, blockName, configFile);
            }
        }
    }

    private static void addConfigToMap(Element configElement, String blockName, MapleConfigFile configFile) {
        String configTag = configElement.getTagName();
        String type = configElement.getAttribute("type");
        String value = configElement.getTextContent();

        if (type.equals("double")) {
            configFile.doubleConfigMap
                    .computeIfAbsent(blockName, k -> new HashMap<>())
                    .put(configTag, Double.parseDouble(value));
        } else if (type.equals("int")) {
            configFile.intConfigMap
                    .computeIfAbsent(blockName, k -> new HashMap<>())
                    .put(configTag, Integer.parseInt(value));
        }
    }

    public static void saveConfigToUSBSafe(MapleConfigFile config) {
        try {
            saveConfigToUSB(config);
        } catch (IOException ignored) {
        }
    }

    public static void saveConfigToUSB(MapleConfigFile config) throws IOException {
        File usbDir = new File("/media/sda1");
        if (!usbDir.exists()) {
            throw new IOException("No USB connected");
        }
        File configDir = new File(usbDir, "savedConfigs/" + config.configType);
        if (!configDir.exists() && !configDir.mkdirs()) {
            throw new IOException("Failed to create config directory on USB");
        }
        File configFile = new File(configDir, config.configName + ".xml");
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("<" + config.configType + ">\n");
            for (String blockName : config.doubleConfigMap.keySet()) {
                writer.write("    <" + blockName + ">\n");
                for (Map.Entry<String, Double> entry : config.doubleConfigMap.get(blockName).entrySet()) {
                    writer.write("        <" + entry.getKey() + " type=\"double\">" + entry.getValue() + "</" + entry.getKey() + ">\n");
                }
                writer.write("    </" + blockName + ">\n");
            }
            for (String blockName : config.intConfigMap.keySet()) {
                writer.write("    <" + blockName + ">\n");
                for (Map.Entry<String, Integer> entry : config.intConfigMap.get(blockName).entrySet()) {
                    writer.write("        <" + entry.getKey() + " type=\"int\">" + entry.getValue() + "</" + entry.getKey() + ">\n");
                }
                writer.write("    </" + blockName + ">\n");
            }
            writer.write("</" + config.configType + ">\n");
        }
    }
}

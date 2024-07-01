package frc.robot.Helpers.ConfigHelpers;

import edu.wpi.first.wpilibj.Filesystem;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapleConfigFile {
    private final String configType;
    private final String configName;

    public static final class ConfigBlock {
        private final String blockName;
        private final Map<String, Double> doubleConfigs = new HashMap<>();
        private final Map<String, Integer> intConfigs = new HashMap<>();

        private ConfigBlock(String blockName) {
            this.blockName = blockName;
        }

        public double getDoubleConfig(String name) throws NullPointerException {
            if (!doubleConfigs.containsKey(name))
                throw new NullPointerException("Configuration not found for block: " + name + ", config: " + name + ", type: double");
            return doubleConfigs.get(name);
        }

        public int getIntConfig(String name) throws NullPointerException {
            if (!intConfigs.containsKey(name))
                throw new NullPointerException("Configuration not found for block: " + name + ", config: " + name + ", type: int");
            return intConfigs.get(name);
        }

        public void putDoubleConfig(String configName, double value) throws IllegalArgumentException {
            if (intConfigs.containsKey(configName))
                throw new IllegalArgumentException(
                        "Cannot put double config '" + configName +  "' to block '" + blockName + "' since there is already an int config with the same name"
                );
            doubleConfigs.put(configName, value);
        }

        public void putIntConfig(String configName, int value) throws IllegalArgumentException {
            if (doubleConfigs.containsKey(configName))
                throw new IllegalArgumentException(
                        "Cannot put int config '" + configName +  "' to block '" + blockName + "' since there is already a double config with the same name"
                );
            intConfigs.put(configName, value);
        }
    }

    private final Map<String, ConfigBlock> configBlocks = new HashMap<>();
    private final List<String> configBlocksOrder = new ArrayList<>();

    public MapleConfigFile(String configType, String configName) {
        this.configType = configType;
        this.configName = configName;
    }

    public ConfigBlock getBlock(String blockName) {
        if (!configBlocksOrder.contains(blockName)) {
            configBlocks.put(blockName, new ConfigBlock(blockName));
            configBlocksOrder.add(blockName);
        }
        return configBlocks.get(blockName);
    }

    public static MapleConfigFile fromDeployedConfig(String configType, String configName) throws IllegalArgumentException, IOException {
        MapleConfigFile configFile = new MapleConfigFile(configType, configName);

        final Path xmlFilePath = Paths.get(Filesystem.getDeployDirectory().getPath(), "configs", configType, configName + ".xml");
        File xmlFile = xmlFilePath.toFile();
        if (!xmlFile.exists()) {
            throw new IOException("Config file does not exist: " + xmlFilePath);
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
            processBlocks(configFile, blocks);
        } catch (Exception e) {
            throw new IOException("Error reading config file", e);
        }

        return configFile;
    }

    private static void processBlocks(MapleConfigFile configFile, NodeList blocks) {
        for (int i = 0; i < blocks.getLength(); i++) {
            Node blockNode = blocks.item(i);
            if (blockNode.getNodeType() == Node.ELEMENT_NODE) {
                Element blockElement = (Element) blockNode;
                String blockName = blockElement.getTagName();
                ConfigBlock block = new ConfigBlock(blockName);
                readBlockConfig(blockElement, block);
                configFile.configBlocks.put(blockName, block);
            }
        }
    }

    private static void readBlockConfig(Element blockElement, ConfigBlock block) {
        NodeList configNodes = blockElement.getChildNodes();
        for (int j = 0; j < configNodes.getLength(); j++) {
            Node configNode = configNodes.item(j);
            if (configNode.getNodeType() == Node.ELEMENT_NODE) {
                addConfigToBlock((Element) configNode, block);
            }
        }
    }

    private static void addConfigToBlock(Element configElement, ConfigBlock block) {
        String configTag = configElement.getTagName();
        String type = configElement.getAttribute("type");
        String value = configElement.getTextContent();

        if (type.equals("double")) {
            block.doubleConfigs.put(configTag, Double.parseDouble(value));
        } else if (type.equals("int")) {
            block.intConfigs.put(configTag, Integer.parseInt(value));
        }
    }

    public void saveConfigToUSBSafe() {
        try {
            saveConfigToUSB();
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
    }

    public void saveConfigToUSB() throws IOException {
        File usbDir = new File("/media/sda1");
        if (!usbDir.exists()) {
            throw new IOException("No USB connected");
        }
        File configDir = new File(usbDir, "savedConfigs/" + this.configType);
        if (!configDir.exists() && !configDir.mkdirs()) {
            throw new IOException("Failed to create config directory on USB");
        }
        File configFile = new File(configDir, this.configName + ".xml");
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("<" + this.configType + ">\n");
            writeConfigBlocks(this, writer);
            writer.write("</" + this.configType + ">\n");
        }
    }

    private static void writeConfigBlocks(MapleConfigFile config, FileWriter writer) throws IOException {
        for (String blockName : config.configBlocksOrder) {
            System.out.println("block name: " + blockName);
            final ConfigBlock block = config.configBlocks.get(blockName);
            writer.write("    <" + block.blockName + ">\n");
            writeDoubleConfigs(block, writer);
            writeIntConfigs(block, writer);
            writer.write("    </" + block.blockName + ">\n");
        }
    }

    private static void writeDoubleConfigs(ConfigBlock block, FileWriter writer) throws IOException {
        for (Map.Entry<String, Double> entry : block.doubleConfigs.entrySet()) {
            writer.write("        <" + entry.getKey() + " type=\"double\">" + entry.getValue() + "</" + entry.getKey() + ">\n");
        }
    }

    private static void writeIntConfigs(ConfigBlock block, FileWriter writer) throws IOException {
        for (Map.Entry<String, Integer> entry : block.intConfigs.entrySet()) {
            writer.write("        <" + entry.getKey() + " type=\"int\">" + entry.getValue() + "</" + entry.getKey() + ">\n");
        }
    }
}

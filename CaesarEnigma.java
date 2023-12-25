import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class CaesarEnigma implements EncryptionAlgorithm {
    private int encryptionKey;
    private int incrementFactor;
    private Map<Character, Character> plugboardMappings;
    private String ALPHABET = "";

    /**
     * Constructor for Caesar Enigma
     */
    public CaesarEnigma() {
        plugboardMappings = new HashMap<>();
    }

    @Override
    public String encrypt(String cleartext) {
        StringBuilder encrypted = new StringBuilder();
        int alphabetSize = ALPHABET.length();

        for (int i = 0; i < cleartext.length(); i++) {
            char c = cleartext.charAt(i);
            int originalIndex = ALPHABET.indexOf(Character.toUpperCase(c));

            if (originalIndex != -1) {
                int newIndex = (originalIndex + encryptionKey + i * incrementFactor) % alphabetSize;

                char newChar = ALPHABET.charAt(newIndex);
                c = plugboardMappings.getOrDefault(newChar, newChar);
            }

            encrypted.append(c);
        }

        return encrypted.toString();
    }

    @Override
    public String decrypt(String ciphertext) {
        StringBuilder decrypted = new StringBuilder();
        int alphabetSize = ALPHABET.length();

        for (int i = 0; i < ciphertext.length(); i++) {
            char c = ciphertext.charAt(i);

            c = reversePlugboardMapping(c);

            int index = ALPHABET.indexOf(c);

            if (index != -1) {
                int newIndex = (index - encryptionKey - i * incrementFactor) % alphabetSize;
                if (newIndex < 0) {
                    newIndex += alphabetSize;
                }
                decrypted.append(ALPHABET.charAt(newIndex));
            } else {
                decrypted.append(c);
            }
        }

        return decrypted.toString();
    }

    /**
     * Returns the original character for a given resulting character in the
     * plugboard mapping.
     *
     * @param c The character for which to find the reverse plugboard mapping.
     * @return The original character before plugboard mapping or the character
     *         itself if no mapping exists.
     */
    private char reversePlugboardMapping(char c) {
        for (Map.Entry<Character, Character> entry : plugboardMappings.entrySet()) {
            if (entry.getValue() == c) {
                return entry.getKey();
            }
        }
        return c;
    }

    @Override
    public void configure(String configurationFilePath) throws Exception {
        File xmlFile = new File(configurationFilePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);

        doc.getDocumentElement().normalize();

        NodeList alphabetList = doc.getElementsByTagName("alphabet");
        if (alphabetList.getLength() != 1)
            throw new IllegalArgumentException("Invalid alphabet key configuration.");
        String alphabetConfig = alphabetList.item(0).getTextContent().trim();
        constructAlphabet(alphabetConfig);

        // Validate and parse encryption key
        NodeList nList = doc.getElementsByTagName("encryption-key");
        if (nList.getLength() != 1)
            throw new IllegalArgumentException("Invalid encryption key configuration.");
        Element keyElement = (Element) nList.item(0);
        int encryptionKey = Integer.parseInt(keyElement.getTextContent().trim());
        int minKey = Integer.parseInt(keyElement.getAttribute("min-value").trim());
        int maxKey = Integer.parseInt(keyElement.getAttribute("max-value").trim());
        if (encryptionKey < minKey || encryptionKey > maxKey) {
            throw new IllegalArgumentException("Encryption key must be between " + minKey + " and " + maxKey);
        }
        this.encryptionKey = encryptionKey;

        // Validate and parse increment factor
        nList = doc.getElementsByTagName("increment-factor");
        if (nList.getLength() != 1)
            throw new IllegalArgumentException("Invalid increment factor configuration.");
        Element incElement = (Element) nList.item(0);
        int incrementFactor = Integer.parseInt(incElement.getTextContent().trim());
        int minInc = Integer.parseInt(incElement.getAttribute("min-value").trim());
        int maxInc = Integer.parseInt(incElement.getAttribute("max-value").trim());
        if (incrementFactor < minInc || incrementFactor > maxInc) {
            throw new IllegalArgumentException("Increment factor must be between " + minInc + " and " + maxInc);
        }
        this.incrementFactor = incrementFactor;

        // Validate and parse plugboard configuration
        nList = doc.getElementsByTagName("plugboard");
        if (nList.getLength() != 1)
            throw new IllegalArgumentException("Invalid plugboard configuration.");
        Element plugElement = (Element) nList.item(0);
        String plugboardText = plugElement.getTextContent().trim();
        parseAndValidatePlugboard(plugboardText);
    }

    /**
     * Parses and validates the plugboard configuration from a given string.
     *
     * @param plugboardText The string representing the plugboard configuration
     * @throws IllegalArgumentException If there is any format error or collision in
     *                                  the plugboard mappings.
     * @throws Exception                For other exceptions that might occur during
     *                                  the parsing process.
     */
    private void parseAndValidatePlugboard(String plugboardText) throws Exception {
        Map<Character, Character> tempMapping = new HashMap<>();
        Map<Character, Character> reverseMapping = new HashMap<>();

        String[] entries = plugboardText.substring(1, plugboardText.length() - 1).trim().split(",\\s*");

        for (String entry : entries) {
            String[] keyValue = entry.split(":\\s*");
            if (keyValue.length != 2 || keyValue[0].length() != 3 || keyValue[1].length() != 3) {
                throw new IllegalArgumentException("Invalid plugboard entry: " + entry);
            }

            char key = keyValue[0].charAt(1);
            char value = keyValue[1].charAt(1);

            if (reverseMapping.containsKey(value) && reverseMapping.get(value) != key) {
                throw new IllegalArgumentException("Collision detected in plugboard mapping for character: " + value);
            }

            if (tempMapping.containsKey(key)) {
                throw new IllegalArgumentException("Multiple mappings for original character: " + key);
            }

            tempMapping.put(key, value);
            reverseMapping.put(value, key);
        }

        this.plugboardMappings.clear();
        this.plugboardMappings.putAll(tempMapping);
    }

    /**
     * Constructs the alphabet used for encryption and decryption based on the
     * specified configuration.
     * 
     * @param config A string representation of the desired character sets included
     *               in the alphabet.
     */
    private void constructAlphabet(String config) {
        ALPHABET = "";

        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digits = "0123456789";
        String punctuation = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

        if (config.contains("UPPER")) {
            ALPHABET += upperCase;
        }
        if (config.contains("DIGITS")) {
            ALPHABET += digits;
        }
        if (config.contains("PUNCTUATION")) {
            ALPHABET += punctuation;
        }
    }

}

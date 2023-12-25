import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * The interface that EnhancedCaesar should implement to be able to interact with the SocketServer & SocketClient
 */
interface EncryptionAlgorithm {

    String encrypt(String cleartext);

    String decrypt(String ciphertext);

    /**
     * Configure (using the input file) and returns a new instance of the encryption algorithm
     *
     * @param configurationFilePath - the absolute path where the configuration file is stored
     */
    void configure(String configurationFilePath) throws Exception;
}
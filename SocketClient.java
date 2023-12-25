import javax.print.DocFlavor;
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class SocketClient {
    private Socket socket;
    private Scanner inputScanner;

    private PrintWriter output;
    private BufferedReader input;

    private EncryptionAlgorithm encryptionAlgorithm;

    // server IP address
    final String SERVER_IP = "127.0.0.1";
    final String CLOSE_COMMAND = "BYE";
    
    // location in the command line arguments' array where the path is provided
    static int COMMAND_LINE_ARGUMENT_FILE_PATH = 0;

    /**
     * connect the client to the server at `SERVER_IP` on the given port
     * @param port - port to connect the client to
     */
    public void startConnection(int port) {
        // TODO
    }

    /**
     * Handle the client's communications
     */
    public void handleClientCommunications(String clientInput) {
        // TODO
    }
    
    /**
     * notify the server to close the session
     */
    private void sendCloseCommand() {
        // TODO
    }

    /**
     * Close ALL client I/O
     */
    public void stop() throws IOException {
        // TODO
    }

    /**
     * read the file on the given `filePath`, create, configure and return a new instance of the EncryptionAlgorithm
     * @param filePath - path where the configuration file is located
     */
    private void configureEncryptionAlgorithm(String filePath) {
        // TODO
    }

    /**
     * Decrypts the given message using the instance's encryption algorithm
     * @param message - message to be decrypted
     * @return the decrypted (original) message
     */
    private String decrypt(String message) { return this.encryptionAlgorithm.decrypt(message); }

    /**
     * Encrypts the given message using the instance's encryption algorithm
     * @param message - Message to be encrypted
     * @return the encrypted message
     */
    private String encrypt(String message) { return this.encryptionAlgorithm.encrypt(message); }

    /**
    * Mostly useful for debug purposes
    */
    public String toString() {
        return "Client Configuration{" +
                "socket Address=" + socket.getInetAddress() +
                ", clientSocket=" + socket.getPort() +
                '}' +
                "EncryptionAlgorithm: " + encryptionAlgorithm;
    }

    /**
    * __Reminders:__
    * - validate inputs
    * - connect to the server & configure the encryption algorithm
    * - handle errors
    *
    * @param args - command line arguments. args[0] SHOULD contain the absolute path for the configuration file
    */
    public static void main(String[] args) {

        // configure encryption algorithm

        // start the client
        
        // keep processing user input

        // close the connection when user uses the CLOSE command or an error occurs
    }
}

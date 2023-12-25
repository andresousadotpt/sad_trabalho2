import java.net.*;
import java.io.*;

public class SocketServer {

    private ServerSocket serverSocket;
    private Socket clientSocket;

    private PrintWriter output;
    private BufferedReader input;

    private EncryptionAlgorithm encryptionAlgorithm;

    /**
     * start the server and wait for connections on the given port

     * @param port - port to connect the client to
     */
    public void start(int port) {
        // TODO
    }

    /**
     * Handle messages received on the server and log them with the respective timestamp
     */
    public void processServerCommunications() {
     // TODO   
    }

    /**
     * Terminate the service - close ALL I/O
     */
    public void stop() {
    // TODO
    }

    /**
     * Read the file at `filePath`, xreate, configure and return a new instance of the EncryptionAlgorithm

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
    @Override
    public String toString() {
        return "Server Configuration{" +
                "server Address=" + serverSocket.getInetAddress() +
                ", clientSocket=" + serverSocket.getLocalPort() +
                '}' +
                "EncryptionAlgorithm: " + encryptionAlgorithm;
    }

    // location in the command line arguments' array where the path is provided
    static int COMMAND_LINE_ARGUMENT_FILE_PATH = 0;

    /**
    * __Reminders:__
    * - validate inputs
    * - Start the server service & configure the encryption algorithm
    * - handle errors
    * - terminate the serviceon demand
    *
    * @param args - command line arguments. args[0] SHOULD contain the absolute path for the configuration file
    */
    public static void main(String[] args) {
        // configure the algorithm

        // start the server

        // keep processing messages
      
        // stop the server
        
    }
}
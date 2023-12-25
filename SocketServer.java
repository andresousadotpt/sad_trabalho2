import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;

public class SocketServer {

    private ServerSocket serverSocket;
    private Socket clientSocket;

    private PrintWriter output;
    private BufferedReader input;

    private EncryptionAlgorithm encryptionAlgorithm;

    private static final Integer PORT = 6666;

    private static final int COMMAND_LINE_ARGUMENT_FILE_PATH = 0;

    /**
     * start the server and wait for connections on the given port
     * 
     * @param port - port to connect the client to
     */
    public void start(int port) throws IOException {
        System.out.println("Opening port " + port);
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        output = new PrintWriter(clientSocket.getOutputStream(), true);
        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        processServerCommunications();
    }

    /**
     * Handle messages received on the server and log them with the respective
     * timestamp
     */
    public void processServerCommunications() throws IOException {
        String encryptedMessage;
        while ((encryptedMessage = input.readLine()) != null) {
            String decryptedMessage = decrypt(encryptedMessage);

            SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
            String timestamp = sdf.format(new Date());
            System.out.println(timestamp + " message received: (" + encryptedMessage + ") " + decryptedMessage);

            if (decryptedMessage.equalsIgnoreCase("BYE")) {
                System.out.println("Closing Socket...");
                stop();
                break;
            }
        }
    }

    /**
     * Terminate the service - close ALL I/O
     */
    public void stop() {
        // Close input BufferedReader
        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
                System.err.println("Error closing input: " + e.getMessage());
            }
        }

        // Close output PrintWriter
        if (output != null) {
            output.close(); // PrintWriter does not throw IOException
        }

        // Close client Socket
        if (clientSocket != null) {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }

        // Close server ServerSocket
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing server socket: " + e.getMessage());
            }
        }
    }

    /**
     * Read the file at `filePath`, xreate, configure and return a new instance of
     * the EncryptionAlgorithm
     * 
     * @param filePath - path where the configuration file is located
     */
    private void configureEncryptionAlgorithm(String filePath) throws Exception {
        // Simulated configuration of the encryption algorithm
        this.encryptionAlgorithm = new CaesarEnigma();
        this.encryptionAlgorithm.configure(filePath);
    }

    /**
     * Decrypts the given message using the instance's encryption algorithm
     * 
     * @param message - message to be decrypted
     * @return the decrypted (original) message
     */
    private String decrypt(String message) {
        return this.encryptionAlgorithm.decrypt(message);
    }

    /**
     * Encrypts the given message using the instance's encryption algorithm
     * 
     * @param message - Message to be encrypted
     * @return the encrypted message
     */
    private String encrypt(String message) {
        return this.encryptionAlgorithm.encrypt(message);
    }

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

    /**
     * __Reminders:__
     * - validate inputs
     * - Start the server service & configure the encryption algorithm
     * - handle errors
     * - terminate the serviceon demand
     *
     * @param args - command line arguments. args[0] SHOULD contain the absolute
     *             path for the configuration file
     */
    public static void main(String[] args) {
        // Validate arguments (like a specific port number or config file)
        if (args.length < 1) {
            System.out.println("Error: Configuration file path not provided.");
            return;
        }

        SocketServer server = new SocketServer();
        try {
            server.configureEncryptionAlgorithm(args[COMMAND_LINE_ARGUMENT_FILE_PATH]);
            server.start(PORT); // Start server on the given port

            server.processServerCommunications();
        } catch (FileNotFoundException e) {
            System.err.println("Error: Configuration file not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            server.stop();
        }
    }

}
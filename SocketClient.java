import java.net.*;
import java.io.*;
import java.util.Scanner;

public class SocketClient {
    private Socket socket;

    private PrintWriter output;
    private BufferedReader input;

    private EncryptionAlgorithm encryptionAlgorithm;

    private final String SERVER_IP = "127.0.0.1";
    private final String CLOSE_COMMAND = "BYE";
    private static final Integer PORT = 6666;

    private static int COMMAND_LINE_ARGUMENT_FILE_PATH = 0;

    /**
     * connect the client to the server at `SERVER_IP` on the given port
     * 
     * @param port - port to connect the client to
     */

    public void startConnection(int port) throws IOException {
        socket = new Socket(SERVER_IP, port);
        output = new PrintWriter(socket.getOutputStream(), true);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("SocketClient started. Start typing your messages");
    }

    /**
     * Handle the client's communications
     */
    public void handleClientCommunications(String clientInput) throws IOException {
        String encryptedMessage = encrypt(clientInput);
        output.println(encryptedMessage);
    }

    /**
     * notify the server to close the session
     */
    private void sendCloseCommand() {
        output.println(encrypt(CLOSE_COMMAND));
        output.flush();
    }

    /**
     * Close ALL client I/O
     */
    public void stop() throws IOException {
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
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }

    /**
     * read the file on the given `filePath`, create, configure and return a new
     * instance of the EncryptionAlgorithm
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
     * @param args - command line arguments. args[0] SHOULD contain the absolute
     *             path for the configuration file
     */

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Error: Configuration file path not provided.");
            return;
        }

        SocketClient client = new SocketClient();
        try {
            client.configureEncryptionAlgorithm(args[COMMAND_LINE_ARGUMENT_FILE_PATH]);
            client.startConnection(PORT);

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String userInput = scanner.nextLine();
                if (userInput.equalsIgnoreCase("BYE")) {
                    client.sendCloseCommand();
                    break;
                } else {
                    client.handleClientCommunications(userInput);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error: Configuration file not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            try {
                client.stop();
            } catch (IOException e) {
                System.err.println("Error closing client: " + e.getMessage());
            }
        }
    }

}

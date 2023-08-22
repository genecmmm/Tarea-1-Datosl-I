import java.io.*;
import java.net.*;
import java.util.*;


/** The Server class represents a simple chat server that manages to display incoming client connections,
 manages the transmission of messages to all connected clients and handles individual client communication.
 */
public class Servidor {
    private ServerSocket serverSocket; // Server socket to achieve link with the client.
    private List<PrintWriter> clientWriters = new ArrayList<>(); // List to store customer messages

    /** Constructor of the Server class. Initializes the server on the specified port, it can be a random one as long as both ports are the same. port parameter: The port number to listen for incoming connections.
     throws IOException if an error occurs during socket creation or client handling.
     */
    public Servidor(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Servidor iniciado en " + port);

        // Continuously accept and handle client connections
        while (true) {
            new ClientHandler(serverSocket.accept()).start();
        }
    }

    /** Send a message to all connected clients, excluding itself.
     message parameter: The message to be transmitted.
     parameter sender: The PrintWriter of the sender to exclude from the transmission.
     */
    private void broadcastMessage(String message, PrintWriter sender) {
        for (PrintWriter writer : clientWriters) {
            if (writer != sender) {
                writer.println(message);
                writer.flush();
            }
        }
    }

    /** The ClientHandler inner class handles communication with an individual client.
     Each client connection is made in a separate thread.
     */
    private class ClientHandler extends Thread {
        private Socket clientSocket; // client socket for communication
        private PrintWriter writer; // PrintWriter to send messages to the client


        /** Constructor for the ClientHandler class.
         parameter socket The client socket associated with this controller.
         */
        public ClientHandler(Socket socket) {
            clientSocket = socket;
        }

        /** Send a message to the connected client.
         message parameter: The message to send.
         */
        public void sendMessage(String message) {
            writer.println(message);
            writer.flush();
        }

        /** The execution method of the thread that communicates with the client.
         */
        public void run() {
            try { // Initialize a PrintWriter to send messages to the client
                writer = new PrintWriter(clientSocket.getOutputStream(), true);
                clientWriters.add(writer);

                // Create a BufferedReader to read messages from the client
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // Continuously read messages from the customer
                String message;
                while ((message = reader.readLine()) != null) {
                    // Broadcast the received message to all clients, excluding the sender
                    broadcastMessage(message, writer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Remove the writer from the client's writer list
                if (writer != null) {
                    clientWriters.remove(writer);
                }
                try {
                    // Close the client socket
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /** The main method to start the chat server on a specific port.
     */
    public static void main(String[] args) {
        int port = 12345;
        try {
            new Servidor(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

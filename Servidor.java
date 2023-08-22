import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {
    private ServerSocket serverSocket;
    private List<PrintWriter> clientWriters = new ArrayList<>();

    public Servidor(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Servidor iniciado en " + port);

        while (true) {
            new ClientHandler(serverSocket.accept()).start();
        }
    }

    private void broadcastMessage(String message, PrintWriter sender) {
        for (PrintWriter writer : clientWriters) {
            if (writer != sender) {
                writer.println(message);
                writer.flush();
            }
        }
    }

    private class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter writer;

        public ClientHandler(Socket socket) {
            clientSocket = socket;
        }

        public void sendMessage(String message) {
            writer.println(message);
            writer.flush();
        }

        public void run() {
            try {
                writer = new PrintWriter(clientSocket.getOutputStream(), true);
                clientWriters.add(writer);

                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String message;
                while ((message = reader.readLine()) != null) {
                    broadcastMessage(message, writer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    clientWriters.remove(writer);
                }
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        int port = 12345;
        try {
            new Servidor(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

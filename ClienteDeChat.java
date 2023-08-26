import java.io.*;
import java.net.*;

/** The ChatClient class represents a client-side component responsible for
 handle communication with a chat server using sockets. Manage shipping and
 Receive messages between the client and the server.
 */
class ClienteDeChat {
    private Socket socket; // Socket for communication
    private PrintWriter out; // output stream to send messages
    private BufferedReader in; // input stream to receive messages
    private InterfazDeChat interfaz; // Reference to the chat interface

    /** Constructor of the ChatClient class.
     * @param; The chat interface associated with this client.
     */
    public ClienteDeChat(InterfazDeChat interfaz) {
        this.interfaz = interfaz;
    }

    /** Establishes a connection to the chat server on the specified host and port.
     *@param: The host address of the chat server.
     *@param: port The port number of the chat server.
     throws an IOException error during socket creation or stream initialization.
     */
    public void conectar(String host, int puerto) throws IOException {
        socket = new Socket(host, puerto);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /** Send a message to the chat server.
     * @param: The message to send.
     */
    public void enviarMensaje(String mensaje) {
        out.println(mensaje);
    }

    /** Listen for incoming messages from the server in a separate thread.
     Updates the associated chat interface when a new message is received.
     */
    public void recibirMensajes() {
        new Thread(() -> {
            try {
                String mensaje;
                while ((mensaje = in.readLine()) != null) {
                    interfaz.actualizarInterfazConMensaje(mensaje);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }
}

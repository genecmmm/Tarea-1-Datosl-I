import java.io.*;
import java.net.*;

class ClienteDeChat {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private InterfazDeChat interfaz;

    public ClienteDeChat(InterfazDeChat interfaz) {
        this.interfaz = interfaz;
    }

    public void conectar(String host, int puerto) throws IOException {
        socket = new Socket(host, puerto);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void enviarMensaje(String mensaje) {
        out.println(mensaje);
    }

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

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class InterfazDeChat extends JFrame {
    private JTextArea chatTextArea;
    private JTextField inputField;
    private JButton sendButton;
    private ClienteDeChat clienteDeChat;

    public InterfazDeChat() {
        setTitle("Interfaz de chat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLayout(new BorderLayout());

        chatTextArea = new JTextArea();
        chatTextArea.setEditable(false);
        chatTextArea.setFont(new Font("Arial",Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(chatTextArea);
        add(scrollPane, BorderLayout.CENTER);

        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN,14));
        sendButton = new JButton("Enviar");

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        clienteDeChat = new ClienteDeChat(this); // Inicializa el ClienteDeChat
        try {
            clienteDeChat.conectar("localhost", 12345); // Cambiar dirección y puerto según sea necesario
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        clienteDeChat.recibirMensajes();

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            chatTextArea.append("Yo: " + message + "\n");
            inputField.setText("");
            clienteDeChat.enviarMensaje(message); // Envía el mensaje al servidor
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfazDeChat chatInterface1 = new InterfazDeChat();
            chatInterface1.setVisible(true);

            InterfazDeChat chatInterface2 = new InterfazDeChat();
            chatInterface2.setVisible(true);
        });
    }

    public void actualizarInterfazConMensaje(String mensaje) {
        chatTextArea.append("Otro: " + mensaje + "\n");
    }
}

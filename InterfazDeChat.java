import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**This class makes a simple chat interface that uses Swing components.
 Users can exchange messages through the chat client connected to a server using sockets
 */
public class InterfazDeChat extends JFrame {
    private JTextArea chatTextArea; // display chat messages
    private JTextField inputField;  // Text field for user input
    private JButton sendButton; // Button to send messages
    private ClienteDeChat clienteDeChat; // Chat client instance

    /**Build ChatInterface class.
     Configures the UI components and initializes the chat client.
     */
    public InterfazDeChat() {
        setTitle("Interfaz de chat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLayout(new BorderLayout());

        // Create and configure the chat text area
        chatTextArea = new JTextArea();
        chatTextArea.setEditable(false);
        chatTextArea.setFont(new Font("Arial",Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(chatTextArea);
        add(scrollPane, BorderLayout.CENTER);

        // Create the input field and the submit button
        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN,14));
        sendButton = new JButton("Enviar");

        // Create a panel to contain the input field and the submit button
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        // start customer chat
        clienteDeChat = new ClienteDeChat(this);
        try {
            clienteDeChat.conectar("localhost", 12345); // Change the address and port as needed
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        clienteDeChat.recibirMensajes();

        // Set up action listeners for the submit button and input field
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

    /** Send the message written in the input field to the chat client and update the interface with the server
     */
    private void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            chatTextArea.append("Yo: " + message + "\n");
            inputField.setText("");
            clienteDeChat.enviarMensaje(message); // Send the message to the server
        }
    }

    /** The main method that creates and displays two instances of the chat interface.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfazDeChat chatInterface1 = new InterfazDeChat();
            chatInterface1.setVisible(true);

            InterfazDeChat chatInterface2 = new InterfazDeChat();
            chatInterface2.setVisible(true);
        });
    }

    /** Update the interface with a message received from another user.
    */
    public void actualizarInterfazConMensaje(String mensaje) {
        chatTextArea.append("Otro: " + mensaje + "\n");
    }
}

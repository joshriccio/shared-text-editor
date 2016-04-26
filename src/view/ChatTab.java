package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JPanel;
import javax.swing.JTextPane;

import network.Request;
import network.RequestCode;
import network.Response;

public class ChatTab extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private ChatMessages messages;
    private ChatTextArea chatArea;
    private JTextPane chatpane;
    private String conversation;
    private String name;
    private Socket socket;

    public ChatTab(String username, Socket socket) {
        this.socket = socket;
        this.messages = new ChatMessages();
        this.name = username;
        this.conversation = new String();
        this.chatpane = new JTextPane();
        this.chatArea = new ChatTextArea(chatpane);
        this.chatArea.setPreferredSize(new Dimension(1000, 350));
        this.add(messages, BorderLayout.CENTER);
        this.add(chatArea, BorderLayout.SOUTH);
        setListeners();

    }

    private void setListeners() {
        this.chatpane.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent event) {
            }

            @Override
            public void keyReleased(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_ENTER) {
                    String message = "";
                    if (chatArea.getMessage().length() > 1) {
                        message = chatArea.getMessage().substring(0, chatArea.getMessage().length() - 2);
                    }
                    try {
                        Request request = new Request(RequestCode.SEND_MESSAGE, name, message);
                        ois = new ObjectInputStream(socket.getInputStream());
                        oos = new ObjectOutputStream(socket.getOutputStream());
                        chatArea.clearText();
                        oos.writeObject(request);
                        Response response = ((Response) ois.readObject());
                        updateConversation(response.getUser().getUsername(), response.getMessage());
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void keyTyped(KeyEvent event) {
            }
        });
    }

    public void updateConversation(String name, String message) {
        conversation = conversation + name + message + "\n";
        messages.setText(conversation);
    }
}
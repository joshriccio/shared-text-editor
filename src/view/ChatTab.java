package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import network.Request;
import network.RequestCode;

public class ChatTab extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ObjectOutputStream oos;
    private ChatMessages messages;
    private ChatTextArea chatArea;
    private JTextPane chatpane;
    private String conversation;
    private String name;

    public ChatTab(String username, ObjectOutputStream oos) {
        this.oos = oos;
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
                    Request request = new Request(RequestCode.SEND_MESSAGE, name, message);
                    chatArea.clearText();
                    try {
                        oos.writeObject(request);
                    } catch (IOException e) {
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
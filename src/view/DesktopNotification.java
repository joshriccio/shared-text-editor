package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * This class is designed to show notifications from ChatTab on the users desktop.
 * @author Cody Deeran
 * {@link} https://harryjoy.me/2011/07/01/create-new-message-notification-pop-up-in-java/
 *
 */
public class DesktopNotification extends JFrame{
    /**
     *  Java wanted me to add this
     */
    private static final long serialVersionUID = 1L;
    private String message;
    private JButton closeButton;
    private JLabel notificationTitle, messageNotification;
    
    /**
     * Constructor for the DesktopNotification
     * @param message
     *             The message passed in from the ChatTab
     */
    public DesktopNotification(String message){
        this.message = message;
        this.getContentPane().setBackground(Color.ORANGE);
        this.setUndecorated(true);
        this.setSize(300,125);
        this.setAlwaysOnTop(true);
        this.setLayout(new GridBagLayout());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();// size of the screen
        Insets toolHeight = Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration());// height of the task bar
        this.setLocation(screenSize.width - this.getWidth(), screenSize.height - toolHeight.bottom - this.getHeight());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0f;
        c.weighty = 1.0f;
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.BOTH;
        notificationTitle = new JLabel("New Global Chat Message");
        this.add(notificationTitle, c);
        c.gridx++;
        c.weightx = 0f;
        c.weighty = 0f;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTH;
        closeButton = new JButton(new AbstractAction("X"){
            /**
             * Java wanted me to add this to remove warning
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent event) {
                dispose();
            }
        });
        closeButton.setMargin(new Insets(1, 4, 1, 4));
        closeButton.setFocusable(false);
        this.add(closeButton, c);
        c.gridx = 0;
        c.gridy++;
        c.weightx = 1.0f;
        c.weighty = 1.0f;
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.BOTH;
        messageNotification = new JLabel(this.message);
        this.add(messageNotification, c);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        new Thread(){
            @Override
            public void run() {
                 try {
                        Thread.sleep(5000); // time after which pop up will be disappeared.
                        dispose();
                 } catch (InterruptedException e) {
                        e.printStackTrace();
                 }
            };
      }.start();
    }
}

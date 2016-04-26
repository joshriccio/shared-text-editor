package view;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import model.User;
import network.Request;
import network.RequestCode;
import network.Response;
import network.ResponseCode;
import network.Server;

/**
 * The initial login screen
 * @author Cody, Josh
 */
public class LoginScreen extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField loginTextField = new JTextField(15);
    private JPasswordField passwordField = new JPasswordField(15);
    public static boolean loginButtonState = false; // Means button has not been pressed
    public static boolean createAccountButtonState = false; // Means button has not been pressed
    private Socket socket = null;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;
    private User user;
    private String clientUsername;
    private String clientPassword;

    /**
     * Constructor
     */
    public LoginScreen() {
        setupWindow();
    }

    private void setupWindow() {
        JPanel loginPanel;
        JButton loginButton = new JButton("Login");
        JButton createAccountButton = new JButton("Create Account");
        JButton forgottenAccountButton = new JButton("Forgot Login");
        JLabel username;
        JLabel password;

        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 150);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        username = new JLabel("Username");
        password = new JLabel("Password");
        loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        loginPanel.add(username, c);
        c.gridy = 1;
        loginPanel.add(password, c);
        c.gridx = 1;
        c.gridy = 0;
        loginPanel.add(loginTextField, c);
        c.gridx = 1;
        c.gridy = 1;
        loginPanel.add(passwordField, c);
        c.gridx = 0;
        c.gridy = 2;
        loginPanel.add(loginButton, c);
        c.gridx = 1;
        c.gridy = 2;
        loginPanel.add(createAccountButton, c);
        c.gridx = 2;
        c.gridy = 2;
        loginPanel.add(forgottenAccountButton, c);
        add(loginPanel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                loginButtonState = true;
                clientUsername = loginTextField.getText();
                clientPassword = String.valueOf(passwordField.getPassword());
                openConnection(RequestCode.LOGIN);
            }
        });
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JLabel createUsername = new JLabel("Username:");
                JTextField createUsernameField = new JTextField();
                JLabel createPassword = new JLabel("Password:");
                JPasswordField createPasswordField = new JPasswordField();
                Object[] createAccountFields = { createUsername, createUsernameField, createPassword,
                                createPasswordField };
                int response = JOptionPane.showConfirmDialog(null, createAccountFields, "Create Account",
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    createAccountButtonState = true;
                    clientUsername = createUsernameField.getText();
                    clientPassword = String.valueOf(createPasswordField.getPassword());
                    openConnection(RequestCode.CREATE_ACCOUNT);
                }
                createAccountButtonState = false;
            }
        });
        forgottenAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JLabel username = new JLabel("Username:");
                JTextField usernameField = new JTextField();
                JLabel newPassword = new JLabel("New Password:");
                JPasswordField newPasswordField = new JPasswordField();
                Object[] forgotPasswordFields = { username, usernameField, newPassword, newPasswordField };
                int response = JOptionPane.showConfirmDialog(null, forgotPasswordFields, "Forgot Password",
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    clientUsername = usernameField.getText();
                    clientPassword = String.valueOf(newPasswordField.getPassword());
                    openConnection(RequestCode.RESET_PASSWORD);
                }
            }
        });
    }

    /**
     * Opens a new connection between server and client
     * 
     * @author cdeeran11 (cdeeran11@email.arizona.edu)
     */
    private void openConnection(RequestCode requestCode) {
        try {
            socket = new Socket(Server.ADDRESS, Server.PORT_NUMBER);
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
            Request clientRequest = new Request(requestCode);
            clientRequest.setUsername(clientUsername);
            clientRequest.setPassword(clientPassword);
            oos.writeObject(clientRequest);
            Response serverResponse = (Response) ois.readObject();
            if (serverResponse.getResponseID() == ResponseCode.LOGIN_SUCCESSFUL) {
                this.user = serverResponse.getUser();
                SubGUI greetingGUI = new SubGUI(this.oos, this.ois, this.user);
                greetingGUI.setVisible(true);
                dispose();
            } else if (serverResponse.getResponseID() == ResponseCode.LOGIN_FAILED) {
                JOptionPane.showConfirmDialog(null, "Username or Password is incorrect! Please try again.",
                                "Login Failed", JOptionPane.OK_OPTION);
            } else if (serverResponse.getResponseID() == ResponseCode.ACCOUNT_CREATED_SUCCESSFULLY) {
                JOptionPane.showConfirmDialog(null, "Account created successfully! Please login to continue",
                                "Account Created Successfully", JOptionPane.OK_OPTION);
            } else if (serverResponse.getResponseID() == ResponseCode.ACCOUNT_CREATION_FAILED) {
                JOptionPane.showConfirmDialog(null, "The username you have selected already exists. Please try again.",
                                "Failed to Create Account", JOptionPane.OK_OPTION);
            } else if (serverResponse.getResponseID() == ResponseCode.ACCOUNT_RESET_PASSWORD_SUCCESSFUL) {
                JOptionPane.showConfirmDialog(null,
                                "Your password has been successfully reset. Please login to continue.",
                                "Password Successfully Reset", JOptionPane.OK_OPTION);
            } else if (serverResponse.getResponseID() == ResponseCode.ACCOUNT_RESET_PASSWORD_FAILED) {
                JOptionPane.showConfirmDialog(null, "Sorry your username you entered is incorrect!",
                                "Password Failed to Reset", JOptionPane.OK_OPTION);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
package laundry.ui;

import javax.swing.*;
import java.awt.*;
import laundry.Main;

public class LoginFrame extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private final Main app;

    public LoginFrame(Main app) {
        this.app = app;
        setTitle("Login Laundry App");
        setSize(420, 260);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(245, 247, 250));

        JLabel title = new JLabel("Masuk Aplikasi Laundry", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(16, 0, 8, 0));
        root.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        form.setBackground(root.getBackground());

        userField = new JTextField();
        passField = new JPasswordField();

        form.add(new JLabel("Username:")); form.add(userField);
        form.add(new JLabel("Password:")); form.add(passField);

        JButton loginBtn = new JButton("Login");
        JButton exitBtn = new JButton("Keluar");
        form.add(exitBtn); form.add(loginBtn);

        loginBtn.setBackground(new Color(33, 150, 243)); loginBtn.setForeground(Color.WHITE);
        exitBtn.setBackground(new Color(244, 67, 54)); exitBtn.setForeground(Color.WHITE);

        loginBtn.addActionListener(e -> doLogin());
        exitBtn.addActionListener(e -> System.exit(0));

        root.add(form, BorderLayout.CENTER);
        setContentPane(root);
    }

    private void doLogin() {
        String user = userField.getText().trim();
        String pass = new String(passField.getPassword());

        if (user.equals("kirana") && pass.equals("tanya")) {
            dispose();
            new Menu(app).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Login gagal!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

package laundry.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;
import laundry.Main;
import laundry.repo.UserCsv;
import laundry.model.User;
import laundry.exception.Data;

public class LoginFrame extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private final Main app;
    private Image backgroundImage;
    private final UserCsv userRepo = new UserCsv("src/main/resources/data/users.csv");

    public LoginFrame(Main app) {
        this.app = app;
        setTitle("Login Laundry App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        loadBackgroundImage();

        JPanel root = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
                    g2d.dispose();
                }
            }
        };
        root.setOpaque(false);

        JPanel overlay = new JPanel(new GridBagLayout());
        overlay.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);

        JLabel title = new JLabel("Aplikasi Laundry", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                String text = getText();
                int x = (getWidth() - fm.stringWidth(text)) / 2 + 1;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2 + 1;
                g2d.drawString(text, x, y);
                g2d.setColor(getForeground());
                g2d.drawString(text, x - 1, y - 1);
                g2d.dispose();
            }
        };
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(400, 40));
        overlay.add(title, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);

        JPanel form = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(255, 255, 255, 220));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        form.setOpaque(false);
        form.setPreferredSize(new Dimension(400, 250));
        form.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.anchor = GridBagConstraints.WEST;
        formGbc.insets = new Insets(8, 8, 8, 8);

        formGbc.gridx = 0;
        formGbc.gridy = 0;
        form.add(new JLabel("Username:"), formGbc);

        formGbc.gridx = 1;
        formGbc.fill = GridBagConstraints.HORIZONTAL;
        formGbc.weightx = 1.0;
        userField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2d.setColor(new Color(200, 200, 200));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2d.dispose();
            }
        };
        userField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        userField.setOpaque(false);
        userField.setBackground(Color.WHITE);
        userField.setPreferredSize(new Dimension(200, 35));
        userField.addActionListener(e -> passField.requestFocus());
        form.add(userField, formGbc);

        formGbc.gridx = 0;
        formGbc.gridy = 1;
        formGbc.fill = GridBagConstraints.NONE;
        formGbc.weightx = 0;
        form.add(new JLabel("Password:"), formGbc);

        formGbc.gridx = 1;
        formGbc.fill = GridBagConstraints.HORIZONTAL;
        formGbc.weightx = 1.0;
        passField = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2d.setColor(new Color(200, 200, 200));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2d.dispose();
            }
        };
        passField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        passField.setOpaque(false);
        passField.setBackground(Color.WHITE);
        passField.setPreferredSize(new Dimension(200, 35));
        passField.addActionListener(e -> doLogin());
        form.add(passField, formGbc);

        formGbc.gridx = 0;
        formGbc.gridy = 2;
        formGbc.gridwidth = 2;
        formGbc.fill = GridBagConstraints.NONE;
        formGbc.weightx = 0;
        formGbc.anchor = GridBagConstraints.EAST;
        formGbc.insets = new Insets(8, 8, 8, 8);
        
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        registerPanel.setOpaque(false);
        JLabel textLabel = new JLabel("Belum punya akun? ");
        textLabel.setForeground(Color.BLACK);
        JLabel registerLabel = new JLabel("<html><u>Daftar</u></html>");
        registerLabel.setForeground(new Color(33, 150, 243));
        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showRegisterDialog();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                registerLabel.setForeground(new Color(25, 118, 210));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                registerLabel.setForeground(new Color(33, 150, 243));
            }
        });
        registerPanel.add(textLabel);
        registerPanel.add(registerLabel);
        form.add(registerPanel, formGbc);

        formGbc.gridx = 0;
        formGbc.gridy = 3;
        formGbc.gridwidth = 2;
        formGbc.fill = GridBagConstraints.HORIZONTAL;
        formGbc.weightx = 1.0;
        formGbc.anchor = GridBagConstraints.CENTER;
        formGbc.insets = new Insets(20, 8, 0, 8);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton exitBtn = new JButton("Keluar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2d.setColor(getBackground().darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(getBackground().brighter());
                } else {
                    g2d.setColor(getBackground());
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                super.paintComponent(g);
            }
        };
        exitBtn.setBackground(new Color(244, 67, 54));
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setBorderPainted(false);
        exitBtn.setFocusPainted(false);
        exitBtn.setContentAreaFilled(false);
        exitBtn.setPreferredSize(new Dimension(0, 45));
        exitBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        JButton loginBtn = new JButton("Login") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2d.setColor(getBackground().darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(getBackground().brighter());
                } else {
                    g2d.setColor(getBackground());
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                super.paintComponent(g);
            }
        };
        loginBtn.setBackground(new Color(33, 150, 243));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setBorderPainted(false);
        loginBtn.setFocusPainted(false);
        loginBtn.setContentAreaFilled(false);
        loginBtn.setPreferredSize(new Dimension(0, 45));
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        loginBtn.addActionListener(e -> doLogin());
        exitBtn.addActionListener(e -> System.exit(0));
        buttonPanel.add(exitBtn);
        buttonPanel.add(loginBtn);
        form.add(buttonPanel, formGbc);

        overlay.add(form, gbc);
        root.add(overlay, BorderLayout.CENTER);
        setContentPane(root);
    }

    private void loadBackgroundImage() {
        try {
            InputStream imgStream = getClass().getClassLoader()
                .getResourceAsStream("images/login-background.jpg");
            
            if (imgStream == null) {
                imgStream = getClass().getClassLoader()
                    .getResourceAsStream("images/login-background.png");
            }
            
            if (imgStream != null) {
                backgroundImage = ImageIO.read(imgStream);
                imgStream.close();
            } else {
                backgroundImage = createPlaceholderImage();
            }
        } catch (Exception e) {
            backgroundImage = createPlaceholderImage();
        }
    }

    private Image createPlaceholderImage() {
        BufferedImage img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(33, 150, 243),
            800, 600, new Color(156, 39, 176)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, 800, 600);
        
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.setFont(new Font("SansSerif", Font.BOLD, 24));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "Placeholder Background - Ganti dengan gambar Anda";
        int x = (800 - fm.stringWidth(text)) / 2;
        int y = 300;
        g2d.drawString(text, x, y);
        
        g2d.dispose();
        return img;
    }

    private void doLogin() {
        String user = userField.getText().trim();
        String pass = new String(passField.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan password harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            User foundUser = userRepo.findByUsername(user);
            if (foundUser != null && foundUser.getPassword().equals(pass)) {
                dispose();
                new Menu(app).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Username atau password salah!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Data ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showRegisterDialog() {
        JDialog registerDialog = new JDialog(this, "Daftar Akun Baru", true);
        registerDialog.setSize(450, 400);
        registerDialog.setLocationRelativeTo(this);

        JPanel rootPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                GradientPaint gradient = new GradientPaint(
                    0, 0, Color.WHITE,
                    0, getHeight(), new Color(227, 242, 253)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        rootPanel.setOpaque(true);

        JPanel dialogPanel = new JPanel(new GridBagLayout());
        dialogPanel.setOpaque(false);
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel titleLabel = new JLabel("Daftar Akun Baru");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(33, 150, 243));
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        dialogPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        dialogPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JTextField regUserField = new JTextField(15) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2d.setColor(new Color(200, 200, 200));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2d.dispose();
            }
        };
        regUserField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        regUserField.setOpaque(false);
        regUserField.setBackground(Color.WHITE);
        regUserField.setPreferredSize(new Dimension(200, 35));
        dialogPanel.add(regUserField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        dialogPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JPasswordField regPassField = new JPasswordField(15) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2d.setColor(new Color(200, 200, 200));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2d.dispose();
            }
        };
        regPassField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        regPassField.setOpaque(false);
        regPassField.setBackground(Color.WHITE);
        regPassField.setPreferredSize(new Dimension(200, 35));
        dialogPanel.add(regPassField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        dialogPanel.add(new JLabel("Konfirmasi Password:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JPasswordField regConfirmField = new JPasswordField(15) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2d.setColor(new Color(200, 200, 200));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2d.dispose();
            }
        };
        regConfirmField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        regConfirmField.setOpaque(false);
        regConfirmField.setBackground(Color.WHITE);
        regConfirmField.setPreferredSize(new Dimension(200, 35));
        dialogPanel.add(regConfirmField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 8, 8, 8);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton cancelBtn = new JButton("Batal") {
            @Override
            public void paint(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                Color bgColor;
                if (getModel().isPressed()) {
                    bgColor = new Color(158, 158, 158).darker();
                } else if (getModel().isRollover()) {
                    bgColor = new Color(158, 158, 158).brighter();
                } else {
                    bgColor = new Color(158, 158, 158);
                }
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                FontMetrics fm = g2d.getFontMetrics(getFont());
                String text = getText();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                g2d.drawString(text, x, y);
                
                g2d.dispose();
            }
        };
        cancelBtn.setBorderPainted(false);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setContentAreaFilled(false);
        cancelBtn.setOpaque(false);
        cancelBtn.setPreferredSize(new Dimension(100, 40));
        cancelBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        JButton registerBtn = new JButton("Daftar") {
            @Override
            public void paint(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                Color bgColor;
                if (getModel().isPressed()) {
                    bgColor = new Color(33, 150, 243).darker();
                } else if (getModel().isRollover()) {
                    bgColor = new Color(33, 150, 243).brighter();
                } else {
                    bgColor = new Color(33, 150, 243);
                }
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                FontMetrics fm = g2d.getFontMetrics(getFont());
                String text = getText();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                g2d.drawString(text, x, y);
                
                g2d.dispose();
            }
        };
        registerBtn.setBorderPainted(false);
        registerBtn.setFocusPainted(false);
        registerBtn.setContentAreaFilled(false);
        registerBtn.setOpaque(false);
        registerBtn.setPreferredSize(new Dimension(100, 40));
        registerBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        cancelBtn.addActionListener(e -> registerDialog.dispose());
        registerBtn.addActionListener(e -> {
            String username = regUserField.getText().trim();
            String password = new String(regPassField.getPassword());
            String confirm = new String(regConfirmField.getPassword());

            if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                JOptionPane.showMessageDialog(registerDialog, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(registerDialog, "Password dan konfirmasi password tidak sama!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                User existingUser = userRepo.findByUsername(username);
                if (existingUser != null) {
                    JOptionPane.showMessageDialog(registerDialog, "Username sudah digunakan!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                User newUser = new User(username, password);
                userRepo.addUser(newUser);
                JOptionPane.showMessageDialog(registerDialog, "Akun berhasil didaftarkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                registerDialog.dispose();
            } catch (Data ex) {
                JOptionPane.showMessageDialog(registerDialog, "Gagal menyimpan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(cancelBtn);
        buttonPanel.add(registerBtn);
        dialogPanel.add(buttonPanel, gbc);

        rootPanel.add(dialogPanel, BorderLayout.CENTER);
        registerDialog.add(rootPanel);
        registerDialog.setVisible(true);
    }
}

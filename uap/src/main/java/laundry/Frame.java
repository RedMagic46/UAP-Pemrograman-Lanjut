package laundry;

import javax.swing.SwingUtilities;
import laundry.ui.LoginFrame;

public class Frame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main appContext = new Main();
            new LoginFrame(appContext).setVisible(true);
        });
    }
}

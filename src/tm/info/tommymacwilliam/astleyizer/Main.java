package tm.info.tommymacwilliam.astleyizer;

import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
        }
    }

}

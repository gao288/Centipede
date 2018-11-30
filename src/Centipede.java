import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class Centipede extends JFrame implements Commons {

    public Centipede() {

        initUI();
    }

    private void initUI() {

        add(new Board());
        setTitle("Centipede");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            Centipede ex = new Centipede();
            BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0,0), "blank cursor");
            ex.getContentPane().setCursor(blankCursor);
            ex.setVisible(true);
        });
    }
}
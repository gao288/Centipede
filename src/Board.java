import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

public class Board extends JPanel implements Commons{

    private final int ICRAFT_X = 40;
    private final int ICRAFT_Y = 60;
    private final int DELAY =   10;
    private Timer timer;
    private SpaceShip spaceShip;
    private int mx;
    private int my;
    private static boolean ingame = true;
    private boolean mousePressed = false;
    private List<Alien> centipede;
    public Board() {

        initBoard();

        Thread spriteThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(ingame) {
                    System.out.println(mousePressed);
                    boolean check = mousePressed;
                    if (check == true) {
                        spaceShip.fire();
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        Thread gameThread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (ingame){
                    try {
                        Thread.sleep(16);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    updateSpaceShip();
                    updateMissiles();
                    repaint();
                    //System.out.println(mousePressed);



                }

            }
        });

        spriteThread.start();
        gameThread.start();

    }
    private void initCentipede(){
            centipede = new ArrayList<>();
            for(int i = 0; i< CENTIPEDE_SIZE; i++){
                if(i == 0){
                    centipede.add(new Alien(BOARD_WIDTH-Alien.getWidth(),0));
                }
            }

    }

    private void initBoard() {
        TAdapter mouseadapt = new TAdapter();
        addMouseListener(mouseadapt);
        addMouseMotionListener(mouseadapt);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        spaceShip = new SpaceShip(ICRAFT_X, ICRAFT_Y);
        initCentipede();
//        timer = new Timer(DELAY, this);
//        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);

        Toolkit.getDefaultToolkit().sync();
    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(spaceShip.getImage(), spaceShip.getX(),
                spaceShip.getY(), this);

        List<Missile> missiles = spaceShip.getMissiles();

        for (Missile missile : missiles) {

            g2d.drawImage(missile.getImage(), missile.getX(),
                    missile.getY(), this);
        }
    }

//    @Override
//    public void actionPerformed(ActionEvent e){
//        updateMissiles();
//        updateSpaceShip();
//        repaint();
//    }
    private void updateMissiles() {

        List<Missile> missiles = spaceShip.getMissiles();

        for (int i = 0; i < missiles.size(); i++) {

            Missile missile = missiles.get(i);

            if (missile.isVisible()) {
                missile.move();
            } else {
                missiles.remove(i);
            }
        }
    }

    private void updateSpaceShip() {
        spaceShip.mousemove(mx,my);
    }



    private class TAdapter extends MouseInputAdapter {

        @Override
        public void mousePressed(MouseEvent e){
            mousePressed = true;
        }
        @Override
        public void mouseClicked(MouseEvent e){
            spaceShip.fire();
            e.consume();
        }
        @Override
        public void mouseReleased(MouseEvent e){
            mousePressed = false;
        }
        @Override
        public void mouseEntered(MouseEvent e){

        }
        @Override
        public void mouseExited(MouseEvent e){

        }
        @Override
        public void mouseDragged(MouseEvent e){
            mousePressed = true;
            mx = e.getX();
            my = e.getY();
            e.consume();



        }
        @Override
        public void mouseMoved(MouseEvent e){
            mx = e.getX();
            my = e.getY();
            e.consume();

        }






    }
}
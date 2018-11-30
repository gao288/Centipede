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
                    updateCentipede();
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
                centipede.add(new Alien(BOARD_WIDTH,0));
            }
            Alien alien = centipede.get(0);
            alien.setXY(BOARD_WIDTH-50, 0);

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
        for (Alien alien : centipede) {
            g2d.drawImage(alien.getImage(),alien.getX(),alien.getY(),this);
            //g2d.drawImage(alien.getImage(),250,250,this);
        }
    }


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
    private void updateCentipede(){
        for(int i = 1; i< centipede.size();i++){
            Alien alien_after = centipede.get(i-1);
            Alien alien_forward = centipede.get(i);
            alien_after.setXY(alien_forward.getX(), alien_forward.getY());
        }
        Alien head = centipede.get(0);
        int[] next_pos = next_pos(head);
        head.setXY(next_pos[0],next_pos[1]);

    }

    private int[] next_pos(Alien alien){
        int[] ret= {alien.getX(),alien.getY()};
        if(alien.getY() >= PLAYERMAT - alien.getHeight()/2){
            //Reaching the player mat bottom line
            if(alien.getX()<=(0+alien.getWidth()/2)){
                alien.direction = 1;
                ret[0] += alien.speed;
            }else if(alien.getX()>=(BOARD_WIDTH - alien.getWidth()/2)){
                alien.direction = 0;
                ret[0] -= alien.speed;
            }

        }else if(alien.getX()<=(0+alien.getWidth()/2)){
            alien.direction = 1;
            ret[1] += alien.getHeight();
        }else if(alien.getX()>=(BOARD_WIDTH - alien.getWidth()/2)){
            alien.direction = 0;
            ret[1] += alien.getHeight();
        }else{
            if(alien.direction == 1){
                ret[0] += alien.speed;
            }else{
                ret[0] -= alien.speed;
            }
        }

        return ret;
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
import com.sun.xml.internal.ws.assembler.jaxws.MustUnderstandTubeFactory;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import java.util.concurrent.ThreadLocalRandom;
public class Board extends JPanel implements Commons{

    private final int ICRAFT_X = 250;
    private final int ICRAFT_Y = 700;
    private final int DELAY =   10;
    private SpaceShip spaceShip;
    private int mx;
    private int my;
    private static boolean ingame = true;
    private List<Alien> centipede;
    private List<Mushroom> all_Mushroom;
    private Spider spider;
    private int duration;
    private int countdown;
    private Mousepressed mouse;
    private int Score = 0;


    public class Mousepressed{
        public boolean mousePressed;
        public Mousepressed(){
        }
    }


    public Board() {

        initBoard();
        SpaceShip.health = 3;
        mouse = new Mousepressed();
        mouse.mousePressed = false;

        SimpleSoundPlayer sound = new SimpleSoundPlayer("src/Sounds/fire2.wav");


        Thread spriteThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(ingame) {
                        if (mouse.mousePressed) {
                            spaceShip.fire();
                            InputStream stream = new ByteArrayInputStream(sound.getSamples());
                            sound.play(stream);
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                }
            }
        });
        Thread gameThread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (ingame){
                    try {
                        Thread.sleep(20);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    synchronized (spaceShip){
                        updateSpaceShip();
                    }
                    updateMissiles();
                    updateCentipede();
                    updateMushroom();
                    updateSpider();
                    checkCollisions();

                    GameCondition();
                    repaint();



                }

            }
        });

        spriteThread.start();
        gameThread.start();

    }
    private void GameCondition(){
        boolean alldead = true;
        for(Alien alien : centipede){
            if(alien.visible){
                alldead = false;
            }
        }
        if(alldead){
            Score += 600;
            initCentipede();
        }
        if(!spaceShip.visible){

            for(Mushroom mush : all_Mushroom){
                if (mush.health > 0){
                    mush.health = 3;
                    Score += 10;
                }
            }
            if(SpaceShip.health >= 1) {

                SpaceShip.health -= 1;
                spaceShip = new SpaceShip(ICRAFT_X, ICRAFT_Y);
            }else{
                ingame = false;
            }
        }
    }

    private void gameOver(Graphics g){
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 50);
        FontMetrics fm = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (BOARD_WIDTH - fm.stringWidth(msg)) / 2,
                BOARD_HEIGHT / 2);
    }
    private void PrintScore_Lives(Graphics g){
        String msg = Integer.toString(Score);
        Font small = new Font("Helvetica", Font.BOLD, 14);
        //FontMetrics fm = getFontMetrics(small);
        msg = "Score: "+msg;
        g.setColor(Color.pink);
        g.setFont(small);
        g.drawString(msg, 10, BOARD_HEIGHT-50);

        String lives = "Remaining Health: "+Integer.toString(SpaceShip.health);
        g.drawString(lives, 10, BOARD_HEIGHT-35);
    }



    private void checkCollisions(){
        Rectangle r3 = spaceShip.getBounds();
        for (Alien alien : centipede){
            Rectangle r2 = alien.getBounds();
            if(r3.intersects(r2) && alien.visible){
                spaceShip.setVisible(false);
                alien.setVisible(false);
            }
        }
        Rectangle r2 = spider.getBounds();
        if(r3.intersects(r2)){
            spaceShip.setVisible(false);
            initSpider();
        }

        List<Missile> ms = spaceShip.getMissiles();
        synchronized (ms) {
            for (Missile m : ms) {
                Rectangle r1 = m.getBounds();
                for (Alien alien : centipede) {
                    r2 = alien.getBounds();

                    if (r1.intersects(r2) && alien.visible) {
                        m.setVisible(false);
                        alien.health -= 1;
                        Score += 2;
                        if (alien.health == 0) {
                            Score += 3;
                            alien.setVisible(false);
                        }
                    }
                }

                for (Mushroom mushroom : all_Mushroom) {
                    r2 = mushroom.getBounds();
                    if (r1.intersects(r2) && mushroom.visible) {
                        m.setVisible(false);
                        mushroom.health -= 1;
                        Score += 1;
                        if (mushroom.health == 0) {
                            mushroom.setVisible(false);
                            Score += 4;
                        }
                    }

                }

                r2  = spider.getBounds();
                if(r1.intersects(r2)){
                    m.setVisible(false);
                    spider.health -= 1;
                    Score += 100;
                    if(spider.health == 0) {
                        initSpider();
                        Score += 500;
                    }
                }
            }
        }


    }
    private void initCentipede(){
            centipede = new ArrayList<>();
            for(int i = 0; i< CENTIPEDE_SIZE; i++){
                Alien alien = new Alien(BOARD_WIDTH,10);
                for (int j = 0 ; j < alien.Buffer.length;++j){
                    alien.Buffer[j][0] = BOARD_WIDTH;
                    alien.Buffer[j][1] = 0;
                }
                centipede.add(alien);

            }
            Alien alien = centipede.get(0);
            alien.setXY(BOARD_WIDTH-50, 0);

    }

    private void initSpider(){
        spider = new Spider(150,300);
        duration = 0;
        countdown = 0;
    }
    private void initBoard() {
        TAdapter mouseadapt = new TAdapter();
        addMouseListener(mouseadapt);
        addMouseMotionListener(mouseadapt);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        spaceShip = new SpaceShip(ICRAFT_X, ICRAFT_Y);
        initCentipede();
        initMushroom();
        initSpider();
//        timer = new Timer(DELAY, this);
//        timer.start();
    }

    private void initMushroom(){
        int temp;
        all_Mushroom = new ArrayList<>();
        boolean found;
        int [] Mushroom_pos_in_each_level = new int[6];
        for(int row = 1; row < 25 ; ++row) {  //26 levels
            int [] array_copy = Mushroom_pos_in_each_level.clone();
            for (int i = 0; i < 6; i++) { //try to place 6 mushrooms per level
                temp = ThreadLocalRandom.current().nextInt(2, 23);
                found = false;
                for (int j = 0; j < 6; j++) {
                    if (array_copy[j] == temp + 1 || array_copy[j] == temp - 1) { //check mushroom pos
                        found = true;
                        break;
                    }
                }

                if (found == false) {
                    Mushroom_pos_in_each_level[i] = temp;
                    all_Mushroom.add(new Mushroom(temp*20, row*20));
                }else{
                    Mushroom_pos_in_each_level[i] = 0;
                }
            }
        }

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
            if(alien.visible) {
                g2d.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            };
            //g2d.drawImage(alien.getImage(),250,250,this);
        }
        for (Mushroom mushroom : all_Mushroom){
            g2d.drawImage(mushroom.getImage(), mushroom.getX(), mushroom.getY(),this);
        }
        g2d.drawImage(spider.getImage(),spider.getX(),spider.getY(),this);

        PrintScore_Lives(g);

        if(!ingame){
           gameOver(g);
        }

    }


    private void updateMissiles() {

        List<Missile> missiles = spaceShip.getMissiles();

        for (int i = 0; i < missiles.size(); i++) {

            Missile missile = missiles.get(i);

            if (missile.isVisible()) {
                missile.move();
            } else {
                synchronized (missiles){
                    missiles.remove(i);
                }

            }
        }
    }

    private void updateMushroom(){
        for(int i = 0; i < all_Mushroom.size(); i++){
            Mushroom mus = all_Mushroom.get(i);
            if(!mus.isVisible()){
                all_Mushroom.remove(i);
            }

        }
    }

    private void updateSpider(){
        if (countdown == duration) {
            duration = ThreadLocalRandom.current().nextInt(10, 20);
            spider.dx = ThreadLocalRandom.current().nextInt(0, 15) - 7;
            spider.dy = ThreadLocalRandom.current().nextInt(0,15)-7;
            countdown = 0;
        }
        spider.x += spider.dx;
        spider.y += spider.dy;
        if(spider.x < 10 || spider.x > BOARD_WIDTH - 20){
            spider.dx = -spider.dx;
        }
        if(spider.y < 10 || spider.y > BOARD_HEIGHT -30){
            spider.dy = -spider.dy;
        }
        ++countdown;

    }

    private void updateCentipede(){
        for(int i = centipede.size()-1; i > 0;--i){
            Alien alien_after = centipede.get(i);
            Alien alien_before = centipede.get(i-1);

            for(int j = alien_after.Buffer.length-1; j>0;--j){
                alien_after.Buffer[j][0] = alien_after.Buffer[j-1][0];
                alien_after.Buffer[j][1] = alien_after.Buffer[j-1][1];
            }
            alien_after.Buffer[0][0] = alien_before.Buffer[alien_after.Buffer.length-1][0];
            alien_after.Buffer[0][1] = alien_before.Buffer[alien_after.Buffer.length-1][1];

            alien_after.setXY(alien_after.Buffer[1][0], alien_after.Buffer[1][1]);
        }
        Alien head = centipede.get(0);
        int[] next_pos = next_pos(head);
        head.Buffer[0][0] = next_pos[0];
        head.Buffer[0][1] =next_pos[1];
        head.setXY(next_pos[0],next_pos[1]);
        for(int j = head.Buffer.length-1; j>0;--j){
            head.Buffer[j][0] = head.Buffer[j-1][0];
            head.Buffer[j][1] = head.Buffer[j-1][1];
        }


    }

    private int[] next_pos(Alien alien){
        int[] ret= {alien.getX(),alien.getY()};
        boolean intersect = false;
        if(alien.getY() >= PLAYERMAT - alien.getHeight()/2){
            //Reaching the player mat bottom line
            if(alien.getX()<=5){
                alien.direction = 1;
                ret[0] += alien.speed;
            }else if(alien.getX()>=(BOARD_WIDTH - alien.getWidth())){
                alien.direction = 0;
                ret[0] -= alien.speed;
            }else{
                if(alien.direction == 1){
                    ret[0] += alien.speed;
                }else{
                    ret[0] -= alien.speed;
                }
            }

        }else if(alien.getX()<=5){
            alien.direction = 1;
            ret[1] += alien.height;
            ret[0] += 5;
        }else if(alien.getX()>=(BOARD_WIDTH - alien.getWidth())){
            alien.direction = 0;
            ret[1] += alien.height;
            ret[0] -= 5;
        }else{
            intersect = false;
            for (Mushroom mushroom : all_Mushroom){
                Rectangle r3 = mushroom.getBounds();
                Rectangle r2 = alien.getBounds();
                if(r3.intersects(r2)){
                    intersect = true;
                }
            }
            if(!intersect) {
                if (alien.direction == 1) {
                    ret[0] += alien.speed;
                } else {
                    ret[0] -= alien.speed;
                }
            }else{
                ret[1] += alien.height;
                alien.direction = alien.direction == 1? 0 : 1;
            }
        }

        return ret;
    }

    private void updateSpaceShip() {
        spaceShip.mousemove(mx,my);
    }
//    private void drawGameOver(Graphics g) {
//
//        String msg = "Game Over";
//        Font small = new Font("Helvetica", Font.BOLD, 14);
//        FontMetrics fm = getFontMetrics(small);
//
//        g.setColor(Color.white);
//        g.setFont(small);
//        g.drawString(msg, (B_WIDTH - fm.stringWidth(msg)) / 2,
//                B_HEIGHT / 2);
//    }
    private class TAdapter extends MouseInputAdapter {

        @Override
        public void mousePressed(MouseEvent e){
            //setMousePressed(1);
            //synchronized (mouse){
                mouse.mousePressed = true;
            //}
        }
        @Override
        public void mouseClicked(MouseEvent e){
            spaceShip.fire();
            e.consume();
        }
        @Override
        public void mouseReleased(MouseEvent e){
            //setMousePressed(0);
            //synchronized (mouse){
                mouse.mousePressed = false;
            //}
        }
        @Override
        public void mouseEntered(MouseEvent e){

        }
        @Override
        public void mouseExited(MouseEvent e){

        }
        @Override
        public void mouseDragged(MouseEvent e){
            mx = e.getX();
            my = e.getY();
            e.consume();
            mouse.mousePressed = true;



        }
        @Override
        public void mouseMoved(MouseEvent e){
            mx = e.getX();
            my = e.getY();
            e.consume();

        }






    }
}
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class SpaceShip extends Sprite {

    private List<Missile> missiles;

    public SpaceShip(int x, int y) {
        super(x, y);
        initSpaceShip();
    }

    private void initSpaceShip() {

        missiles = new ArrayList<>();

        loadImage("src/images/spaceship.png");
        getImageDimensions();
    }

//    public void move() {
//        x += dx;
//        y += dy;
//    }

    public void mousemove(int mx, int my){
        x = mx;
        y = my;
    }

    public List<Missile> getMissiles() {
        return missiles;
    }


//    public void keyPressed(KeyEvent e) {
//
//        int key = e.getKeyCode();
//
//        if (key == KeyEvent.VK_SPACE) {
//            fire();
//        }
//
//        if (key == KeyEvent.VK_LEFT) {
//            dx = -1;
//        }
//
//        if (key == KeyEvent.VK_RIGHT) {
//            dx = 1;
//        }
//
//        if (key == KeyEvent.VK_UP) {
//            dy = -1;
//        }
//
//        if (key == KeyEvent.VK_DOWN) {
//            dy = 1;
//        }
//    }

    public void fire() {
        missiles.add(new Missile(x +5, y - height/2));
        //System.out.println("Fire!");
    }

//    public void keyReleased(KeyEvent e) {
//
//        int key = e.getKeyCode();
//
//        if (key == KeyEvent.VK_LEFT) {
//            dx = 0;
//        }
//
//        if (key == KeyEvent.VK_RIGHT) {
//            dx = 0;
//        }
//
//        if (key == KeyEvent.VK_UP) {
//            dy = 0;
//        }
//
//        if (key == KeyEvent.VK_DOWN) {
//            dy = 0;
//        }
//    }
}
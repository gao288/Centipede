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


    public void mousemove(int mx, int my){
        x = mx;
        y = my;
    }

    public List<Missile> getMissiles() {
        return missiles;
    }


    public void fire() {
        missiles.add(new Missile(x +5, y - height/2));
        //System.out.println("Fire!");
    }


}
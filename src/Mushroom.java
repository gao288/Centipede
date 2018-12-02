import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Mushroom extends Sprite implements Commons{
    public int x;
    public int y;
    public int health = 2;
    public Mushroom(int x, int y){
        super(x,y);
        initMushroom();
    }
    private void initMushroom() {
        loadImage("src/images/mushroom.png");
        getImageDimensions();
    }


}

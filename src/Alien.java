import java.util.ArrayList;

public class Alien extends Sprite implements Commons{
    private int health = 2;
    public int speed = 5;
    public int direction = 0; // 0 means going left
    private boolean head = false;

    Alien(int x, int y){
        super(x,y);
        initAlien();
    }

    private void initAlien(){
        loadImage("src/images/alien.png");
        getImageDimensions();
    }

    private void move(){

    }


}

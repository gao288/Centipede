public class Missile extends Sprite implements Commons {

    private final int MISSILE_SPEED = 10;

    public Missile(int x, int y) {
        super(x, y);

        initMissile();
    }

    private void initMissile() {

        loadImage("src/images/bullet.png");
        getImageDimensions();
    }

    public void move() {

        y -= MISSILE_SPEED;

        if (y > BOARD_HEIGHT) {
            visible = false;
        }
    }
}
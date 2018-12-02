public class Spider extends Sprite {
    public int health = 2;
    public int dx = 0;
    public int dy = 0;
    public Spider(int x,int y){
        super(x,y);
        initSpider();
    }
    private void initSpider(){
        loadImage("src/images/spider.png");
        getImageDimensions();
    }
}

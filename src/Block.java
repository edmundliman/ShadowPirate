import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Block {
    // intialising static variables that is common among all instances of block
    private static final int MAX_DAMAGE_POINTS = 10;
    private final Image BLOCK_IMAGE = new Image("res/block.png");
    
    // initialising instance variables for each instance of block
    private Point blockCoordinate;
    private Image blockImage = BLOCK_IMAGE;
    private Rectangle hitBox;
    
    // constructor for block
    public Block(Point point){
        this.blockCoordinate = point;
        this.hitBox = new Rectangle(blockImage.getBoundingBoxAt(blockCoordinate));
    }

    // important getters and setters for the program
    public Rectangle getHitBox(){
        return this.hitBox;
    }

    // method returning how much damage points is dealt when a block attacks
    public static int blockAttack(){
        return MAX_DAMAGE_POINTS;
    }
    
    // update method for the duration of the game
    public void update(){
        // block image rendering
        blockImage.draw(blockCoordinate.x, blockCoordinate.y);
    }
}

import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Sailor {
    // intialising static variables that is common among all instances of sailor
    private static final int MAX_DAMAGE_POINTS = 25;
    private static final int MAX_HEALTH_POINTS = 100;
    private final Image SAILOR_RIGHT = new Image("res/sailorRight.png");
    private final Image SAILOR_LEFT = new Image("res/sailorLeft.png");
    private static final int STEP_SIZE = 20;
    private static final int TO_PERCENTAGE = 100;
    private static final int MEDIUM_HEALTH_PERCENTAGE = 65;
    private static final int LOW_HEALTH_PERCENTAGE = 35;

    // font and message specifications for sailor health
    private static final int FONT_SIZE = 30;
    private final Font font = new Font("res/wheaton.otf", FONT_SIZE);
    private static final int healthPercentageX = 10;
    private static final int healthPercentageY = 25;
    private static final DrawOptions colourHighHealth = new DrawOptions().setBlendColour(0, 0.8, 0.2);
    private static final DrawOptions colourMediumHealth = new DrawOptions().setBlendColour(0.9, 0.6, 0);
    private static final DrawOptions colourLowHealth = new DrawOptions().setBlendColour(1, 0, 0);

    // initialising instance variables for each instance of sailor
    private int healthPoints = MAX_HEALTH_POINTS;
    private int sailorX, sailorY;
    private Image sailorImage = SAILOR_RIGHT;
    private Rectangle hitBox;
    private Point sailorLastPosition;

    // constructor for sailor
    public Sailor(Point position){
        this.sailorX = (int)position.x;
        this.sailorY = (int)position.y;
        this.hitBox = new Rectangle(sailorImage.getBoundingBoxAt(position));
    }

    // important getters and setters for the program
    public Point getSailorPosition(){
        return new Point(sailorX, sailorY);
    }

    public void setSailorPosition(Point position){
        this.sailorX = (int)position.x;
        this.sailorY = (int)position.y;
    }

    public int getHealthPoints(){
        return this.healthPoints;
    }

    public void setHealthPoints(int health){
        this.healthPoints = health;
    }

    public static int getMaxHealthPoints(){
        return MAX_HEALTH_POINTS;
    }

    public Rectangle getHitBox(){
        return this.hitBox;
    }

    public Point getSailorLastPosition(){
        return this.sailorLastPosition;
    }

    public void setSailorLastPosition(int sailorX, int sailorY){
        sailorLastPosition = new Point(sailorX, sailorY);
    }

    // method to determine player's action when attacked by a block
    public void sailorGetHit(){
        setSailorPosition(this.sailorLastPosition);
        this.healthPoints = this.healthPoints - Block.blockAttack();
    }

    // update method for the duration of the game
    public void update(Input input){
        // sailor movement dynamics
        if (input.wasPressed(Keys.LEFT)) {
            this.setSailorLastPosition(sailorX, sailorY);
            sailorX -= STEP_SIZE;
            sailorImage = SAILOR_LEFT;
        }
        if (input.wasPressed(Keys.RIGHT)) {
            this.setSailorLastPosition(sailorX, sailorY);
            sailorX += STEP_SIZE;
            sailorImage = SAILOR_RIGHT;
        }
        if (input.wasPressed(Keys.UP)) {
            this.setSailorLastPosition(sailorX, sailorY);
            sailorY -= STEP_SIZE;
        }
        if (input.wasPressed(Keys.DOWN)) {
            this.setSailorLastPosition(sailorX, sailorY);
            sailorY += STEP_SIZE;
        }

        // sailor image rendering
        sailorImage.draw(sailorX, sailorY);
        hitBox = new Rectangle(sailorImage.getBoundingBoxAt(getSailorPosition()));

        // health percentage message and colour dynamics
        int healthPercentage = (int)Math.round(((double)healthPoints/ (double)MAX_HEALTH_POINTS) * TO_PERCENTAGE);

        if (healthPercentage >= MEDIUM_HEALTH_PERCENTAGE){
            font.drawString(healthPercentage + "%", healthPercentageX, healthPercentageY, colourHighHealth);
        } else if (healthPercentage < LOW_HEALTH_PERCENTAGE){
            font.drawString(healthPercentage + "%", healthPercentageX, healthPercentageY, colourLowHealth);
        } else{
            font.drawString(healthPercentage + "%", healthPercentageX, healthPercentageY, colourMediumHealth);
        }
    }
}

import bagel.*;
import bagel.util.Point;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Skeleton Code for SWEN20003 Project 1, Semester 1, 2022
 *
 * Please filling your name below
 * @Edmund Liman
 */
public class ShadowPirate extends AbstractGame {
    // game specifications
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;
    private static final String GAME_TITLE = "ShadowPirate";
    private final Image BACKGROUND_IMAGE = new Image("res/background0.png");
    // since our .csv file has a maximum of 50 entries and the first entry will always be a sailor
    private static final int MAX_SAILORS = 1;
    private static final int MAX_BLOCKS = 49;
    private static final int GAME_WIN_X = 990;
    private static final int GAME_WIN_Y = 630;
    private static final int GAME_OUTOFBOUND_LEFT = 0;
    private static final int GAME_OUTOFBOUND_RIGHT = WINDOW_WIDTH;
    private static final int GAME_OUTOFBOUND_UP = 60;
    private static final int GAME_OUTOFBOUND_DOWN = 670;
    private static final int MINIMUM_HEALTH_POINTS = 0;

    // csv index explanations
    private static final int DATA_NAME_INDEX = 0;
    private static final int DATA_X_INDEX = 1;
    private static final int DATA_Y_INDEX = 2;

    // font specifications
    private static final int FONT_SIZE = 55;
    private final Font font = new Font("res/wheaton.otf", FONT_SIZE);

    // all messages and message related specifications required
    private static final int INSTRUCTION_MESSAGE_HEIGHT = 402;
    private static final String INITIAL_MESSAGE = "PRESS SPACE TO START";
    private static final int INSTRUCTION_SUBMESSAGE_HEIGHT_BELOW_MESSAGE = 70;
    private static final String INITIAL_SUBMESSAGE = "USE ARROW KEYS TO FIND LADDER";
    private static final String WIN_MESSAGE = "CONGRATULATIONS!";
    private static final String LOSE_MESSAGE = "GAME OVER";

    // initialise sailor then continued on readCSV method for further initialising
    private Sailor sailor;

    // intialise block array then continued on readCSV method for further initialising
    private Block[] blocks = new Block[MAX_BLOCKS];
    private int blockCount = 0;

    // filename 
    private String worldFile= "res/level0.csv";

    // intialise flag variables
    private boolean spacePressed = false;
    private boolean gameStart = false;
    private boolean gameEnd = false;
    private boolean gameWin = false;

    public ShadowPirate() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowPirate game = new ShadowPirate();
        game.run();
    }

    /**
     * Method used to read file and create objects
     */
    private void readCSV(String fileName){
        try (BufferedReader br =
            new BufferedReader(new FileReader(fileName))){
                String text;

                while ((text = br.readLine()) != null){
                    String contents[] = text.split(",");
                    
                    if (contents[DATA_NAME_INDEX].equals("Sailor")){
                        sailor = new Sailor(new Point(Integer.parseInt(contents[DATA_X_INDEX]), Integer.parseInt(contents[DATA_Y_INDEX])));
                    } else{
                        blocks[blockCount] = new Block(new Point(Integer.parseInt(contents[DATA_X_INDEX]), Integer.parseInt(contents[DATA_Y_INDEX])));
                        blockCount++;
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        if ((input.wasPressed(Keys.SPACE)) && (!spacePressed)){
            spacePressed = true;
            // read world
            this.readCSV(worldFile);
            gameStart = true;
        }

        if (gameStart){
            // game duration update methods for sailor and block
            sailor.update(input);
            
            for (int i = 0; i < blockCount; i++){
                blocks[i].update();

                // collision detection and attack
                if (sailor.getHitBox().intersects(blocks[i].getHitBox())){
                    sailor.sailorGetHit();
                    System.out.println("Block inflicts 10 damage points on Sailor. Sailor's current health: " + sailor.getHealthPoints() + "/" + Sailor.getMaxHealthPoints());
                }
            }

            // win detection
            if ((sailor.getSailorPosition().x >= GAME_WIN_X) && (sailor.getSailorPosition().y >= GAME_WIN_Y)){
                gameStart = false;
                gameEnd = true;
                gameWin = true;
            }

            // lose (out of bound) detection
            if ((sailor.getSailorPosition().x < GAME_OUTOFBOUND_LEFT) || (sailor.getSailorPosition().x > GAME_OUTOFBOUND_RIGHT)){
                gameStart = false;
                gameEnd = true;
            }

            if ((sailor.getSailorPosition().y > GAME_OUTOFBOUND_DOWN) || (sailor.getSailorPosition().y < GAME_OUTOFBOUND_UP)){
                gameStart = false;
                gameEnd = true;
            }

            // lose (no health) detection
            if ((sailor.getHealthPoints() <= MINIMUM_HEALTH_POINTS)){
                gameStart = false;
                gameEnd = true;
            }

        } else if (gameEnd){
            // classify if game is won or lost and show their respective messages
            if (gameWin){
                // game won messages
                double winMessageX = Window.getWidth()/ 2.0 - font.getWidth(WIN_MESSAGE)/ 2.0;
                font.drawString(WIN_MESSAGE, winMessageX, INSTRUCTION_MESSAGE_HEIGHT);
            } else{
                // game lost messages
                double loseMessageX = Window.getWidth()/ 2.0 - font.getWidth(LOSE_MESSAGE)/ 2.0;
                font.drawString(LOSE_MESSAGE, loseMessageX, INSTRUCTION_MESSAGE_HEIGHT);
            }
        } else{
            // start of game messages
            double initialMessageX = Window.getWidth()/ 2.0 - font.getWidth(INITIAL_MESSAGE)/ 2.0;
            font.drawString(INITIAL_MESSAGE, initialMessageX, INSTRUCTION_MESSAGE_HEIGHT);

            double initialSubmessageX = Window.getWidth()/ 2.0 - font.getWidth(INITIAL_SUBMESSAGE)/ 2.0;
            font.drawString(INITIAL_SUBMESSAGE, initialSubmessageX, INSTRUCTION_MESSAGE_HEIGHT + INSTRUCTION_SUBMESSAGE_HEIGHT_BELOW_MESSAGE);
        }

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

    }

}

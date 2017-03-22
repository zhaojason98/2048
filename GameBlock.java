package uwaterloo.lab4_201_09;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Jason on 2017-03-07.
 */

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class GameBlock extends GameBlockTemplate {
    private float IMAGE_SCALE = 0.585f; //Constant image scale for block picture size
    private int myCoordX; //Variable to track block X coordinate
    private int myCoordY; //Variable to track block Y coordinate
    private TextView number;
    private int starter;
    boolean isMove = false;
    private int targetCoordX; //Target value for block X coordinate as it moves
    private int targetCoordY; //Target value for block Y coordinate as it moves
    private int velocity = 0; //Set an initial velocity of 0 to move in any direction
    private int acceleration; //Accelerometer variable to speed up the block as it moves
    private GamerLoopTask.gameDirection myDir; //Set movement direction for the block
    private GamerLoopTask GLT;
    boolean remove;
    //Create constants for each destination
    private int X1 = -80;
    private int X2 = 595;
    private int Y1 = -80;
    private int Y2 = 595;
    private RelativeLayout myRL;

    //Install honeycomb API
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    //Constructor of gameblock to set initial variables
    public GameBlock(Activity myA, Context myContext, RelativeLayout myRL, int coordX, int coordY, int startNum,GamerLoopTask gbGL){
        super(myContext);
        this.GLT = gbGL;
        remove = false;
        starter = startNum;
        this.myRL = myRL;
        number = new TextView(myContext);
        number.setText(String.valueOf(starter));
        number.setX(coordX+150);
        number.setY(coordY+90);
        number.setTextSize(50);
        number.setTextColor(Color.BLUE);
        this.setImageResource(R.drawable.gameblock);
        this.setScaleX(IMAGE_SCALE);
        this.setScaleY(IMAGE_SCALE);
        myCoordX = coordX;
        myCoordY = coordY;
        this.setX(coordX);
        this.setY(coordY);
        myRL.addView(this);
        myRL.addView(number);
        myRL.bringToFront();
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public void move(){
        //Move block left
        if (myDir == GamerLoopTask.gameDirection.LEFT){
            //setDestination();
            //targetCoordX = X1; //Set a new target X location
            if (myCoordX > targetCoordX){
                acceleration = -5; //Set acceleration to be negative to move to the left
                isMove = true;
            }
            //Call for stop if the current location is already at the target
            else{
                myCoordX = targetCoordX;
                velocity = 0;
                acceleration = 0;
                isMove = false;
            }
            setLeft();
        }
        //Move block right
        else if (myDir == GamerLoopTask.gameDirection.RIGHT){
          // setDestination();
            //targetCoordX = X2; //Set target to the right of the game diagram
            //Move block a positive acceleration
            if (myCoordX < targetCoordX){
                acceleration = 5;
                isMove = true;
            }
            //Stop block if already at the location
            else{
                myCoordX = targetCoordX;
                velocity = 0;
                acceleration = 0;
                isMove = false;
            }
            setRight();
        }
        else if (myDir == GamerLoopTask.gameDirection.UP){
            //Set target location to be the top and move block up
            //targetCoordY = Y1;
            //setDestination();
            if (myCoordY > targetCoordY){
                acceleration = -5; //Set y acceleration to be negative
                isMove = true;
            }
            //Don't move blcok if it's already at the location
            else{
                myCoordY = targetCoordY;
                velocity = 0;
                acceleration = 0;
                isMove = false;
            }
            setUp();
        }
        //Move block down
        else if (myDir == GamerLoopTask.gameDirection.DOWN){
            //setDestination();
            //targetCoordY = Y2; //Set target y to be at the bottom
            if (myCoordY < targetCoordY){
                acceleration = 5; //Set a positive acceleration
                isMove = true;
            }
            //Don't move block if it's already at target location
            else{
                myCoordY = targetCoordY;
                velocity = 0;
                acceleration = 0;
                isMove = false;
            }
            setDown();
        }
    }

    //Set game block direction
    public void setBlockDirection(GamerLoopTask.gameDirection newDir){
        myDir = newDir;
    }

    public void setBlockX(int X){
        targetCoordX = X;
    }

    public void setBlockY(int Y){
        targetCoordY = Y;
    }
    //Move block based on new coordinates
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void setLeft(){
        myCoordX += velocity;
        velocity += acceleration;
        this.setX(myCoordX);
        number.setX(myCoordX+150);
    }

    public void setRemove(boolean ifRemove){
        remove = ifRemove;
    }
    public void setViewRemove(){
        myRL.removeView(number);
        myRL.removeView(this);
    }
    //Move block right based on velocity
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void setRight(){
        myCoordX += velocity;
        velocity += acceleration;
        this.setX(myCoordX);
        number.setX(myCoordX+150);
    }

    //Move blcok up based on velocity
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void setUp(){
        myCoordY += velocity;
        velocity += acceleration;
        this.setY(myCoordY);
        number.setY(myCoordY+90);
    }

    //Move block down based on velocity
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void setDown(){
        myCoordY += velocity;
        velocity += acceleration;
        this.setY(myCoordY);
        number.setY(myCoordY+90);
    }

    public void setInitialDirection(){
        if (myDir == GamerLoopTask.gameDirection.LEFT){
            setBlockX(X1);
            setBlockY(myCoordY);
        }
        else if (myDir == GamerLoopTask.gameDirection.RIGHT){
            setBlockX(X2);
            setBlockY(myCoordY);
        }
        else if (myDir == GamerLoopTask.gameDirection.UP){
            setBlockY(Y1);
            setBlockX(myCoordX);
        }
        else if (myDir == GamerLoopTask.gameDirection.DOWN){
            setBlockY(Y2);
            setBlockX(myCoordX);
        }
    }

    public void setDestination(){

        int testCoord;
        int numOfOccupants;
        boolean merge = false;
        GameBlock firstBlock;
        switch(myDir){
            case LEFT:
                testCoord = myCoordX -225;
                numOfOccupants = 0;
                firstBlock = null;
                while(testCoord >= X1){
                    //Log.d("Game Block Test Point", String.format("%d", testCoord));
                    if(GLT.isOccupied(testCoord, myCoordY) != null){
                        if (firstBlock == null && GLT.isOccupied(testCoord,myCoordY).getRemove() == false) {
                            firstBlock = GLT.isOccupied(testCoord, myCoordY);
                        }
                        numOfOccupants++;
                    }
                    testCoord -= 225;
                }

                targetCoordX =X1 + numOfOccupants * 225;
                if (firstBlock != null){
                    if (firstBlock.getNumber() == this.getNumber()){
                        //Log.d(firstBlock.getNumber() + " and " + this.getNumber());
                        GLT.createSecondBlock(this.getNumber() + firstBlock.getNumber(), targetCoordX - 225, targetCoordY,this,firstBlock);
                        //GLT.removeBlock(this,firstBlock);
                        setRemove(true);
                        setViewRemove();
                        firstBlock.setViewRemove();
                        firstBlock.setRemove(true);
                    }
                }

                break;
            case RIGHT:
                testCoord = myCoordX + 225;
                numOfOccupants = 0;
                firstBlock = null;
                while(testCoord <= X2){
                    //Log.d("Game Block Test Point", String.format("%d", testCoord));
                    if(GLT.isOccupied(testCoord, myCoordY) != null){
                        if (firstBlock == null && GLT.isOccupied(testCoord,myCoordY).getRemove() == false) {
                            firstBlock = GLT.isOccupied(testCoord, myCoordY);
                        }
                        numOfOccupants++;
                    }
                    testCoord += 225;
                }
                targetCoordX = X2 - numOfOccupants * 225;
                if (firstBlock != null){
                    if ((firstBlock.getNumber() == this.getNumber()) && (firstBlock.getTargetX() != 0) && (firstBlock.getTargetY() != 0)){
                        //Log.d(firstBlock.getNumber() + " and " + this.getNumber());
                        GLT.createSecondBlock(this.getNumber() + firstBlock.getNumber(), targetCoordX + 225, targetCoordY,this,firstBlock);
                        //GLT.removeBlock(this,firstBlock);
                        setViewRemove();
                        firstBlock.setViewRemove();
                        setRemove(true);
                        firstBlock.setRemove(true);
                    }
                }

                break;
            case UP:
                testCoord = myCoordY-225;
                numOfOccupants = 0;
                firstBlock = null;
                while(testCoord >= Y1){
                    //Log.d("Game Block Test Point", String.format("%d", testCoord));
                    if(GLT.isOccupied(myCoordX, testCoord) != null){
                        if (firstBlock == null && GLT.isOccupied(myCoordX,testCoord).getRemove() == false) {
                            firstBlock = GLT.isOccupied(myCoordX, testCoord);
                        }
                        numOfOccupants++;
                    }
                    testCoord -= 225;
                }
                targetCoordY =Y1 + numOfOccupants * 225;

                if (firstBlock != null){
                    if ((firstBlock.getNumber() == this.getNumber()) && (firstBlock.getTargetX() != 0) && (firstBlock.getTargetY() != 0)){
                        //Log.d(firstBlock.getNumber() + " and " + this.getNumber());
                        GLT.createSecondBlock(this.getNumber() + firstBlock.getNumber(), targetCoordX , targetCoordY-225,this,firstBlock);
                        //GLT.removeBlock(this,firstBlock);
                        setViewRemove();
                        firstBlock.setViewRemove();
                        setRemove(true);
                        firstBlock.setRemove(true);
                    }
                }

                //Log.d("Game Block Report: ", String.format("Target X Coord: %d", targetCoordX));
                break;
            case DOWN:
                testCoord = myCoordY + 225;
                numOfOccupants = 0;
                firstBlock = null;
                while(testCoord <= Y2){
                   // Log.d("Game Block Test Point", String.format("%d", testCoord));
                    if(GLT.isOccupied(myCoordX, testCoord) != null){
                        if (firstBlock == null && GLT.isOccupied(myCoordX,testCoord).getRemove() == false) {
                            firstBlock = GLT.isOccupied(myCoordX, testCoord);
                        }
                        numOfOccupants++;
                    }
                    testCoord += 225;
                }
                targetCoordY =Y2- numOfOccupants * 225;
                //Log.d("Game Block Report: ", String.format("Target X Coord: %d", targetCoordX));

                if (firstBlock != null){
                    if ((firstBlock.getNumber() == this.getNumber()) && (firstBlock.getTargetX() != 0) && (firstBlock.getTargetY() != 0)){
                        System.out.println(firstBlock.getNumber() + " and " + this.getNumber());
                        GLT.createSecondBlock(this.getNumber() + firstBlock.getNumber(), targetCoordX , targetCoordY+225,this,firstBlock);
                        setViewRemove();
                        firstBlock.setViewRemove();
                        this.setRemove(true);
                        firstBlock.setRemove(true);
                        //GLT.removeBlock(this,firstBlock);
                    }
                }

                break;
            default:
                break;
        }
        isMove = true;
    }

    //GET Methods

    public float getX(){
        return myCoordX;
    } //Get current X-coordinate

    public float getY(){
        return myCoordY;
    } //Get current Y-coordinate
    public int getVelocity(){return velocity;}
    public int getNumber(){return starter;}
    public int getTargetX(){
        return targetCoordX;
    }
    public int getTargetY(){
        return targetCoordY;
    }
    public boolean getIsMove(){return isMove;}
    public boolean getRemove(){return remove;}
    public GamerLoopTask.gameDirection getDirection(){
        return myDir;
    }
}
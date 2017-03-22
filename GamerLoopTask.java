package uwaterloo.lab4_201_09;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

/**
 * Created by Jason on 2017-03-07.
 */

public class GamerLoopTask extends TimerTask {
    TextView winText;
    TextView loseText;
    private Activity myActivity; //Create an activity
    private Context myContext; //Context of app
    private RelativeLayout myRL; //Layout for display
    private GameBlock newBlock; //New game object
    private GameBlock secondnewBlock = null;
    enum gameDirection{UP, DOWN, LEFT, RIGHT, NO_MOVEMENT}; //Set new block movement direction possibilties
    ArrayList<GameBlock> blockList = new ArrayList<GameBlock>();
    ArrayList<GameBlock> toAdd = new ArrayList<GameBlock>();
    ArrayList<GameBlock>toRemove = new ArrayList<GameBlock>();
    boolean moving = false;
    boolean isMove = false;
    private boolean endGame = false;
    private boolean win = false;
    private boolean lose = false;
    private int spawnLocationX;
    private int spawnLocationY;
    private int number;
    Random rand = new Random();
    gameDirection currentGameDirection; //Set new game directions

    //Call method run() of TimerTask
    public void run(){
        myActivity.runOnUiThread(
                new Runnable(){
                    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
                    public void run() {
                        isMove = false;
                        if (endGame == false) {
                            for (GameBlock b : blockList) {
                                b.move();
                                if ((b.getTargetX() != (int)b.getX()) && (b.getTargetY() != (int)b.getY())){
                                    isMove = true;
                                }

                                if (b.getNumber() == 32) {
                                    win = true;
                                    endGame = true;
                                }
                            }
                            if (moving == true && isMove == false){
                                moving = false;
                                createBlock();
                            }
                        }
                        if (endGame) {
                            winText = new TextView(myContext);
                            if (win){
                                winText.setText("WIN");
                            }
                            else{
                                winText.setText("LOSE");
                            }
                            winText.setX(200);
                            winText.setY(200);
                            winText.setTextColor(Color.BLACK);
                            winText.setTextSize(100);
                            myRL.addView(winText);
                            myRL.bringToFront();
                            Log.d("End Game Report","Game Ended");
                        }
                    }
                } //Update block to move
        );
    }

    //Use honeycomb api for android version update
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void createBlock(){
        spawnLocationX = rand.nextInt(4);
        spawnLocationY = rand.nextInt(4);
        number = (rand.nextInt(2)+1)*2;
        spawnLocationX = (225*spawnLocationX)-80;
        spawnLocationY = (225*spawnLocationY)-80;
        while (isTarget(spawnLocationX,spawnLocationY)) {
            spawnLocationX = rand.nextInt(4);
            spawnLocationY = rand.nextInt(4);
            number = (rand.nextInt(2)+1)*2;
            spawnLocationX = (225*spawnLocationX)-80;
            spawnLocationY = (225*spawnLocationY)-80;
        }
        final boolean[] moving = {false};
        newBlock = new GameBlock(myActivity,myContext,myRL,spawnLocationX, spawnLocationY,number,this); //Create a block at the top left of the screen
        addBlock(newBlock);
        if (!toAdd.isEmpty()){
            blockList.addAll(toAdd);
            toAdd.clear();
        }
        if (!toRemove.isEmpty()){
            blockList.removeAll(toRemove);
            toRemove.clear();
        }
        if (blockList.size() == 16){
            lose = true;
            endGame = true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public void createSecondBlock(int blockNum, int spawnX, int spawnY,GameBlock blockA, GameBlock blockB){
        //System.out.println("Block Number:" + blockNum + "spawnX: " + spawnX + "spawnY: " + spawnY);
        secondnewBlock = new GameBlock(myActivity,myContext,myRL,spawnX, spawnY,blockNum,this); //Create a block at the top left of the screen

        toRemove.add(blockA);
        toRemove.add(blockB);
        toAdd.add(secondnewBlock);
        secondnewBlock = null;
    }
    //Constructor for GamerLoopTask, receives main activity, layout, and application context
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public GamerLoopTask(Activity myActivity, RelativeLayout myRL, Context myContext){
        this.myActivity = myActivity;
        this.myRL = myRL;
        this.myContext = myContext;
        currentGameDirection = gameDirection.NO_MOVEMENT;
        createBlock();
    }

    //Receives direction from accelerometer event handler
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public void setDirection(gameDirection newDirection){
        currentGameDirection = newDirection;
        if (currentGameDirection != gameDirection.NO_MOVEMENT && endGame == false) {
            for (GameBlock b: blockList){
                b.setBlockDirection(currentGameDirection);
                b.setInitialDirection();
            }
            switch (currentGameDirection){
                case LEFT:
                    for (int x = -80; x <= 595; x+= 225){
                        for (int y = -80; y <= 595; y+= 225){
                            if (isOccupied(x,y) != null){
                                isOccupied(x,y).setDestination();
                            }
                        }
                    }
                    break;
                case RIGHT:
                    for (int x = 595; x>= -80; x-=225){
                        for (int y = -80; y <= 595; y+= 225){
                            if (isOccupied(x,y) != null){
                                isOccupied(x,y).setDestination();
                            }
                        }
                    }
                    break;
                case UP:
                    for (int x = -80; x <= 595; x+= 225){
                        for (int y = -80; y <= 595; y+= 225){
                            if (isOccupied(x,y) != null){
                                isOccupied(x,y).setDestination();
                            }
                        }
                    }
                    break;
                case DOWN:
                    for (int x = -80; x <= 595; x+= 225){
                        for (int y = 595; y >= -80; y-= 225){
                            if (isOccupied(x,y) != null){
                                isOccupied(x,y).setDestination();
                            }
                        }
                    }
                    break;
                default:
                    break;
            }

            //createBlock();
            moving = true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public GameBlock isOccupied(int x, int y){
        for (GameBlock b: blockList){
            if ((b.getX() == x) && (b.getY() == y)){
                return b;
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public boolean isTarget(int x, int y){
        for (GameBlock b: blockList){
            if ((b.getTargetX() == x) && (b.getTargetY() == y)){
                return true;
            }
        }
        return false;
    }

    public void addBlock(GameBlock block){
        blockList.add(block);
    }



}


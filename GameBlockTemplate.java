package uwaterloo.lab4_201_09;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by Jason on 2017-03-07.
 */

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by keith on 2017-03-07.
 */

public abstract class GameBlockTemplate extends ImageView{

    public GameBlockTemplate(Context gbCTX){
        super(gbCTX);
    }

    public abstract void setDestination();

    public abstract void move();

}
package uwaterloo.lab4_201_09;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    RelativeLayout r1;//Use relative layout for frame
    int fileCount;//Files

    TextView tvAccelerometer; //Acceleratometer direction text
    AccelerometerEventListener accListener; //Event listener for accelerometer
    Timer myGameLoop; //Create timer for updates
    GamerLoopTask myGameLoopTask; //Create gamerlooptask object

    //onCreate method for initial launch
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main); //Set main view to relative layout


        r1 = (RelativeLayout)findViewById(R.id.layout1); //Find from XML
        r1.getLayoutParams().width = 900; //Set width to 900 pixels
        r1.getLayoutParams().height =900; //Set height to 900 pixels
        r1.setBackgroundResource(R.drawable.gameboard); //Set background to image of board

        //Create Timer
        myGameLoopTask = new GamerLoopTask(this,r1,getApplicationContext()); //Call gamer loop task and set view to r1
        myGameLoop = new Timer(); //Create a new timer
        myGameLoop.schedule(myGameLoopTask,20,20);//50ms periodic timer-20 fps

        //Create accelerometer event listener
        tvAccelerometer = new TextView(getApplicationContext());
        //Display readings at the top
        tvAccelerometer.setText("Accelerometer Instantaneous Readings");
        tvAccelerometer.setTextColor(Color.BLACK);
        tvAccelerometer.setTextSize(40.0f);

        //Create a sensor manager to regesiter sensors
        SensorManager sensorManager =(SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor Accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        accListener = new AccelerometerEventListener(tvAccelerometer, myGameLoopTask);
        sensorManager.registerListener(accListener, Accelerometer,  SensorManager.SENSOR_DELAY_GAME);


        //Set file
        fileCount = 0;

        //Add accelerometer text to layout
        r1.addView(tvAccelerometer);

    }

}

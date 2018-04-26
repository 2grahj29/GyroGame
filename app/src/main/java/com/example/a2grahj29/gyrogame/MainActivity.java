package com.example.a2grahj29.gyrogame;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ShapeDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import static com.example.a2grahj29.gyrogame.R.drawable.ball;
import static com.example.a2grahj29.gyrogame.R.drawable.enemy;

public class MainActivity extends Activity implements SensorEventListener {

    CustomDrawableView mCustomDrawableView = null;
    public float xPosition, xAcceleration,xVelocity = 0.0f;
    public float yPosition, yAcceleration,yVelocity = 0.0f;
    public float xmax,ymax;
    private Bitmap bBitmap;
    private Bitmap eBitmap;
    private SensorManager sensorManager = null;
    public float frameTime = 0.666f;
    int counter = 0;
    int updateScore = 0;
    Paint p;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        //Set FullScreen & portrait
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);

        // Get a reference to a SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);

        mCustomDrawableView = new CustomDrawableView(this);
        setContentView(mCustomDrawableView);

        //Calculate Boundry
        Display display = getWindowManager().getDefaultDisplay();
        xmax = (float)display.getWidth() - 50;
        ymax = (float)display.getHeight() - 50;
    }

    // This method will update the UI on new sensor events
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
                //Set sensor values as acceleration
                yAcceleration = sensorEvent.values[1];
                xAcceleration = sensorEvent.values[2];
                updateBall();
            }
        }
    }

    private void updateBall() {
        //Calculate new speed
        xVelocity += (xAcceleration * frameTime);
        yVelocity += (yAcceleration * frameTime);

        //Calc distance travelled in that time
        float xS = (xVelocity/2)*frameTime;
        float yS = (yVelocity/2)*frameTime;

        //Add to position negative due to sensor
        //readings being opposite to what we want!
        xPosition -= xS;
        yPosition -= yS;

        if (xPosition > xmax) {
            xPosition = xmax;
        } else if (xPosition < 0) {
            xPosition = 0;
        }
        if (yPosition > ymax) {
            yPosition = ymax;
        } else if (yPosition < 0) {
            yPosition = 0;
        }
    }

    /*
    private void scoreUpdate(){
        if(yPosition == 200){
            if(xPosition == 900){
                updateScore ++;
            }
        }
        if(yPosition == 300){
            if(xPosition == 100){
                updateScore++;
            }
        }
        if(yPosition == 500){
            if(xPosition == 500){
                updateScore++;
            }
        }
    }
    */

    // I've chosen to not implement this method
    public void onAccuracyChanged(Sensor arg0, int arg1)
    {
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop()
    {
        // Unregister the listener
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    public class CustomDrawableView extends View
    {
        public CustomDrawableView(Context context)
        {
            super(context);
            Bitmap ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
            final int dstWidth = 50;
            final int dstHeight = 50;
            bBitmap = Bitmap.createScaledBitmap(ball, dstWidth, dstHeight, true);

            Bitmap enemy = BitmapFactory.decodeResource(getResources(), R.drawable.enemy);
            final int dsWidth = 50;
            final int dsHeight = 50;
            eBitmap = Bitmap.createScaledBitmap(enemy, dsWidth, dsHeight, true);

            p = new Paint();
            p.setColor(Color.BLUE);
            p.setTextSize(35);

        }

        protected void onDraw(Canvas canvas)
        {
            final Bitmap bitmap = bBitmap;
            canvas.drawBitmap(bBitmap, xPosition, yPosition, null);

            final Bitmap bitmap1 = eBitmap;
            canvas.drawBitmap(eBitmap, 500, 500, null);

            final Bitmap bitmap2 = eBitmap;
            canvas.drawBitmap(eBitmap, 200, 900, null);

            final Bitmap bitmap3 = eBitmap;
            canvas.drawBitmap(eBitmap, 300, 100, null);

            final Bitmap bitmap4 = eBitmap;
            canvas.drawBitmap(eBitmap, 200, 400, null);

            counter++;
            canvas.drawText(String.valueOf(counter),0,1150,p);

            canvas.drawText(String.valueOf(xPosition),600,30,p);
            canvas.drawText(String.valueOf(yPosition),600,60,p);

            if(xPosition < 470 == xPosition > 530){
                if(yPosition < 470 == yPosition > 530)
                    updateScore+= 10;
            }

            if(xPosition < 170 == xPosition > 230){
                if(yPosition < 850 == yPosition > 950)
                    updateScore+= 10;
            }

            if(xPosition < 270 == xPosition > 330){
                if(yPosition < 70 == yPosition > 130)
                    updateScore+= 10;
            }

            if(xPosition < 170 == xPosition > 230){
                if(yPosition < 370 == yPosition > 430)
                    updateScore+= 10;
            }

            canvas.drawText("Score :",0,30,p);
            canvas.drawText(String.valueOf(updateScore),120,30,p);

            invalidate();
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

}

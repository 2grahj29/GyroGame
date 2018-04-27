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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

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
    int updateTime = 1000;
    int updateVisit1 = 0;
    int updateVisit2 = 0;
    int updateVisit3 = 0;
    int updateVisit4 = 0;
    int updateFinished = 0;
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


    // Method not used.
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
            Bitmap ball = BitmapFactory.decodeResource(getResources(), R.drawable.playerufo);
            final int dstWidth = 50;
            final int dstHeight = 50;
            bBitmap = Bitmap.createScaledBitmap(ball, dstWidth, dstHeight, true);

            Bitmap enemy = BitmapFactory.decodeResource(getResources(), R.drawable.planet);
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

            updateTime--;

            //counter++;
            //canvas.drawText(String.valueOf(counter),0,1150,p);

            //canvas.drawText(String.valueOf(xPosition),600,30,p);
            //canvas.drawText(String.valueOf(yPosition),600,60,p);


            if(xPosition < 480 == xPosition > 520){
                if(yPosition < 480 == yPosition > 520) {
                    updateVisit1 += 1;
                }
            }

            if(xPosition < 180 == xPosition > 220) {
                if (yPosition < 880 == yPosition > 920) {
                    updateVisit2 += 1;
                }
            }

            if(xPosition < 280 == xPosition > 320){
                if(yPosition < 80 == yPosition > 120) {
                    updateVisit3 += 1;
                }
            }

            if(xPosition < 180 == xPosition > 220){
                if(yPosition < 380 == yPosition > 420) {
                    updateVisit4 += 1;
                }
            }

            canvas.drawText("Time :",0,30,p);
            canvas.drawText(String.valueOf(updateTime),120,30,p);

            if(updateVisit1 > 1){
                if(updateVisit2 > 1){
                    if(updateVisit3 > 1){
                        if(updateVisit4 > 1){
                            if(updateTime > 0){
                                canvas.drawText("You have conquered the planets",0,700,p);
                                updateFinished += 1;
                            }
                        }
                    }

                }
            }

            if(updateTime < 0){
                if(updateFinished == 1){
                    canvas.drawText("GAME OVER", 0, 740, p);
                }
            }

            invalidate();
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

}

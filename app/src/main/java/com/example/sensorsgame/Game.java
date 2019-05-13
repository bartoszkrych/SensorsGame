package com.example.sensorsgame;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Random;

public class Game extends Activity implements SensorEventListener{

    final int DEFAULTT_BALL_SIZE = 75;

    CustomDrawableView mCustomDrawableView = null;
    ShapeDrawable mDrawable = new ShapeDrawable();
    public float xPosition, xAcceleration,xVelocity = 0.10f;
    public float yPosition, yAcceleration,yVelocity = 0.10f;
    public float xmax,ymax;
    private Bitmap mBall;
    private SensorManager sensorManager = null;
    public float frameTime = 0.3333f;
    public int xMeta,yMeta;
    int count =0;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);

        mCustomDrawableView = new CustomDrawableView(this);
        setContentView(mCustomDrawableView);
        // setContentView(R.layout.main);

        getWindow().getDecorView().setBackground(getDrawable(R.drawable.milkway));

        Display display = getWindowManager().getDefaultDisplay();
        xmax = (float)display.getWidth() - DEFAULTT_BALL_SIZE;
        ymax = (float)display.getHeight() - DEFAULTT_BALL_SIZE;

        Random rand = new Random();
        xMeta = rand.nextInt((int) xmax);
        yMeta = rand.nextInt((int) ymax);
    }

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
//        xVelocity += (xAcceleration * frameTime);
//        yVelocity += (yAcceleration * frameTime);

        xVelocity += (xAcceleration);
        yVelocity += (yAcceleration);
        //Calc distance travelled in that time
        float xS = (xVelocity/2)*frameTime/4;
        float yS = (yVelocity/2)*frameTime/4;
//        float xS = (xVelocity/2)*frameTime/4;
//        float yS = (yVelocity/2)*frameTime/4;

        //Add to position negative due to sensor
        xPosition -= xS;
        yPosition -= yS;

        if((xPosition>=xMeta && xPosition<=xMeta+(DEFAULTT_BALL_SIZE/2)) && (yPosition >= yMeta && yPosition<= yMeta+(DEFAULTT_BALL_SIZE/2)))
        { if(count==0){
            finishAndRemoveTask();
            Intent intent = new Intent(this,FinishActivity.class);
            startActivity(intent);
            count++;
        }
        }

        if (xPosition >= xmax) {
            xPosition = xmax;
        } else if (xPosition < 0) {
            xPosition = 0;
        }
        if (yPosition >= ymax) {
            yPosition = ymax;
        } else if (yPosition < 0) {
            yPosition = 0;
        }
    }

    public void onAccuracyChanged(Sensor arg0, int arg1)
    {
        // TODO Auto-generated method stub
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
            Bitmap ball = BitmapFactory.decodeResource(getResources(), R.drawable.bk_black);
            final int dstWidth = DEFAULTT_BALL_SIZE;
            final int dstHeight = DEFAULTT_BALL_SIZE;
            mBall = Bitmap.createScaledBitmap(ball, dstWidth, dstHeight, true);
            mDrawable = new ShapeDrawable(new OvalShape());
            mDrawable.getPaint().setColor(Color.WHITE);
            mDrawable.setBounds(xMeta, yMeta, xMeta + dstWidth, yMeta + dstHeight);

        }

        protected void onDraw(Canvas canvas)
        {
            RectF oval = new RectF(xMeta, yMeta, xMeta + DEFAULTT_BALL_SIZE, yMeta + DEFAULTT_BALL_SIZE);
            Paint p = new Paint();
            p.setColor(Color.BLUE);
            final Bitmap bitmap = mBall;
            canvas.drawOval(oval, p);
            canvas.drawBitmap(bitmap, xPosition, yPosition, null);
            invalidate();
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
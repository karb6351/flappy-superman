package com.example.david.testproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable.Callback;
import android.util.Log;

import java.util.ArrayList;

public class Superman {
    private double x;
    private double y;
    private double velocityX;
    private double velocityY;
    private double gravity;
    private double lift;

    private AnimationDrawable drawable;
    private Activity activity;

    private Runnable startFlyTask = new Runnable() {
        @Override
        public void run() {
            drawable.start();
        }
    };

    private Runnable stopFlyTask = new Runnable() {
        @Override
        public void run() {
            drawable.stop();
        }
    };

    public Superman(Callback callback, Context context, Activity activity){
        this.x = 100;
        this.y = 100;

        this.drawable = (AnimationDrawable) context.getResources().getDrawable(R.drawable.flying_superman);
        this.drawable.setCallback(callback);

        this.activity = activity;

        reset();
    }

    private void reset(){

        this.x = this.activity.getWindowManager().getDefaultDisplay().getWidth() / 2 - this.drawable.getIntrinsicWidth();
        this.y = this.activity.getWindowManager().getDefaultDisplay().getHeight() / 2 - this.drawable.getIntrinsicHeight() / 2;

        this.velocityX = 0;
        this.velocityY = 0;

        this.gravity = 1.9;
        this.lift = -28;
    }

    synchronized public void move(float screenHeight, Rect building){

        activity.runOnUiThread(startFlyTask);

        double saveX = x;
        double saveY = y;

        x += velocityX;
        y += velocityY;

        // increase the speed over the time
        velocityY += gravity;

        // stop the ball when it hit the ground
        if (y + this.drawable.getIntrinsicHeight() > screenHeight) {
            y = screenHeight - this.drawable.getIntrinsicHeight();
            velocityY = 0;
        }

        // stop the ball when it hit the ceiling
        if (y < 0) {
            y = 0;
            velocityY = 0;
        }
//        isCharaterOnOrBelowBuilding(building);

    }

    public boolean isCharaterOnOrBelowBuilding(Rect building){
        if (building != null) {
            //determine it is a top building or bottom building
            if (building.top > 0){
                if (y < building.top ){
                    y = building.top - drawable.getIntrinsicHeight() + 55.5;
//                    velocityY = lift * 0.4;
                    velocityY = 0;
                    return true;
                }

            }else {
                if (y < building.bottom && y + drawable.getIntrinsicHeight() > building.bottom){
                    y = building.bottom - 55.5;
                    velocityY = 0;
                    return true;
                }
                return false;
            }
        }
        return false;
    }




    synchronized public void fly(){
        velocityY += lift * 0.9;
    }

    public void drawOn(Canvas canvas){
        drawable.setBounds((int)x, (int)y, (int)(x + drawable.getIntrinsicWidth()), (int)(y + drawable.getIntrinsicHeight()));
        canvas.save();
        drawable.draw(canvas);
        canvas.restore();
    }

    public Bitmap getBitmap(){
        Bitmap temp = BitmapFactory.decodeResource(activity.getApplicationContext().getResources(), R.drawable.superman1);
        return temp;
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public AnimationDrawable getDrawable() {
        return drawable;
    }

    public void setDrawable(AnimationDrawable drawable) {
        this.drawable = drawable;
    }
}

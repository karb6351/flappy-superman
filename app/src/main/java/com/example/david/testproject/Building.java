package com.example.david.testproject;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;

public class Building {
    private float x, y;
    private float dx;
    private Bitmap topImage;
    private Bitmap bottomImage;
    private float screenHeight;

    private Context context;

    private float topImageY;
    private float bottomImageY;

    public static int imageList[] = { R.drawable.building, R.drawable.building1, R.drawable.building2, R.drawable.building3 };
    public static int imageReveserList[] = { R.drawable.building_revserse, R.drawable.building1_revserse, R.drawable.building2_revserse, R.drawable.building3_revserse };

    public Building(Bitmap topImage, Bitmap bottomImage, int x, int y, Context context){
        this.context = context;

        this.x = x;
        this.y = y;
        this.topImage = topImage;
        this.bottomImage = bottomImage;
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        reset();
    }

    private void reset(){
        dx = 10;
        topImageY = 0;
        bottomImageY = 0;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(topImage, x, getCorrectTopImageY(), null);
        canvas.drawBitmap(bottomImage, x, getCorrectBottomImageY(), null);
    }

    synchronized public void update() {
        x -= GameView.VELOCITY;
    }

    public Rect hit(Superman s) {
        Rect supermanRect = s.getDrawable().getBounds();
        Rect topBuildingRect = new Rect((int)x, (int)getCorrectTopImageY(), (int)(x + topImage.getWidth()), (int)(getCorrectTopImageY() + topImage.getHeight()));
        Rect bottomBuildingRect = new Rect((int)x, (int)getCorrectBottomImageY(), (int)(x + bottomImage.getWidth()), (int)getCorrectBottomImageY() + bottomImage.getHeight());
        Bitmap supermanBitmap = s.getBitmap();
        Rect isHitTopBuilding = ImageHelper.collidePixel(supermanRect, topBuildingRect, supermanBitmap, topImage);
        Rect isHitBottomBuilding = ImageHelper.collidePixel(supermanRect, bottomBuildingRect, supermanBitmap, bottomImage);

        if (isHitTopBuilding != null){
            topImageY = -2000;
            return  topBuildingRect;
        }else if (isHitBottomBuilding != null){
            bottomImageY = -2000;
            return bottomBuildingRect;
        }else {
            return null;
        }
    }

    public float getCorrectTopImageY(){
        return topImageY != 0 ? topImageY : -(GameView.GAP_HEIGHT / 2) + y;
    }

    public float getCorrectBottomImageY(){
        return bottomImageY != 0 ? bottomImageY : ((screenHeight / 2) + (GameView.GAP_HEIGHT / 2)) + y;
    }


    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getDx() {
        return dx;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public Bitmap getTopImage() {
        return topImage;
    }

    public void setTopImage(Bitmap topImage) {
        this.topImage = topImage;
    }

    public Bitmap getBottomImage() {
        return bottomImage;
    }

    public void setBottomImage(Bitmap bottomImage) {
        this.bottomImage = bottomImage;
    }

    public float getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(float screenHeight) {
        this.screenHeight = screenHeight;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

package com.example.david.testproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class GameView extends SurfaceView {
    public final static int DEFAULT_VELOCITY = 15;
    public final static int STOP_VELOCITY = 0;

    public final static int CIRCLE_DELAY = 30;
    public static int VELOCITY = DEFAULT_VELOCITY;
    public final static int GAP_HEIGHT = 260;
    public final static int GAP_WIDTH = 500;

    public final static int MIN_Y_COOR = -400;
    public final static int MAX_Y_COOR = 400;


    public final static int HIT_BUIDLING_SCORE = 50;
    public final static int PASS_BUILDING_SCORE = 50 * 2;

    private GameActivity activity;

    private Timer timer;
    public boolean start;
    private float offset;
    public boolean running;
    public boolean killThread;

    private int timeLimit;
    public TimeLimitThread timeLimitThread;

    private int space;

    private boolean isRestart = false;

    private Superman superman;

    private ArrayList<Building> buildings;

    private HashSet<Building> hitBuildings;
    private HashSet<Building> passBuildings;


    private Bitmap topBuilding;
    private Bitmap bottomBuilding;


    private class GameTimerTask extends TimerTask {
        @Override
        public void run() {
            if (GameView.this.start){
                if(GameView.this.isEndGame()){
                    timer.cancel();
                    timer.purge();
                    timer = null;
                    running = false;
                    start = false;
                    isRestart = true;
                    Log.i("EndGame", hitBuildings.toString());
                    GameView.this.post(new Runnable() {
                        @Override
                        public void run() {
                            GameView.this.doEndGameProcess();
                        }
                    });
                }else{
                    GameView.this.update();
                }
            }
            Canvas canvas = GameView.this.getHolder().lockCanvas();
            if (canvas != null){
                canvas.drawRGB(255,255,255);
                GameView.this.drawBackground(canvas);
                superman.drawOn(canvas);
                for ( Building building: buildings) {
                    building.draw(canvas);
                }
                drawTime(canvas);
                GameView.this.getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    private class TimeLimitThread extends Thread{

        @Override
        public void run() {
            killThread = false;
            while (timeLimit > 0) {
                if (killThread){
                    break;
                }
                try {
                    if (running){
                        decreaseTime();
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public GameView(Context context, GameActivity activity) {
        super(context);

        this.activity = activity;

        reset();

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (GameView.this.start) {
//                    timeLimitThread.start();
                    superman.fly();
                }

                return false;
            }
        });

        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (GameView.this.start) {
//                    timeLimitThread.start();
                    superman.fly();
                }

                return false;
            }
        });
    }

    public void startCounting(){
        killThread = false;
        timeLimitThread.start();
    }

    public void resume(){
        if (start){
            running = true;
        }
        if (timer == null){
            timer = new Timer();
        }
        timer.schedule(new GameTimerTask(), 0, CIRCLE_DELAY);
    }

    public void pause(){
        running = false;
        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }

    public void reset(){
        buildings = new ArrayList<>();
        hitBuildings = new HashSet<>();
        passBuildings = new HashSet<>();

        superman = new Superman( this, this.getContext(), this.activity);


        start = false;
        offset = 0;
        running = false;
        killThread = true;

        space = 0;
        timeLimit = 30;

        timeLimitThread = new TimeLimitThread();

        if (isRestart){
            resume();
        }

    }

    synchronized public boolean isEndGame(){
        return timeLimit <= 0;
    }


    public void doEndGameProcess(){
        int score = passBuildings.size() * PASS_BUILDING_SCORE - hitBuildings.size() * HIT_BUIDLING_SCORE;
        activity.endGame(score);
    }


    public void update(){

        boolean isHit = false;
        Rect hitBuilding = null;
        Rect rect;
        for(Building building: buildings){
            rect = building.hit(superman);
            if (rect != null){
                isHit = true;
                hitBuilding = rect;
                hitBuildings.add(building);
            }
            if (building.getX() < superman.getX()){
                passBuildings.add(building);
            }
        }

//        if (isHit){
//            if (!superman.isCharaterOnOrBelowBuilding(hitBuilding)){
//                VELOCITY = STOP_VELOCITY;
//            }
//            else{
//                VELOCITY = DEFAULT_VELOCITY;
//            }
//
//        }else{
//            VELOCITY = DEFAULT_VELOCITY;
//        }

        superman.move(GameView.this.getHeight(), hitBuilding);
        GameView.this.increaseBackgroundOffset();
        if (space >= GAP_WIDTH) {
            addBuilding();
            space = 0;
        }
        space += VELOCITY;
        if (buildings.size() > 5) {
            buildings.remove(0);
        }
        for (Building building: buildings){
            building.update();
        }


    }

    public void addBuilding(){

        topBuilding = ImageHelper.getResizedBitmap(BitmapFactory.decodeResource
                        (getResources(), Building.imageReveserList[randomInt(0, 3)]), 250,
                Resources.getSystem().getDisplayMetrics().heightPixels / 2);
        bottomBuilding = ImageHelper.getResizedBitmap(BitmapFactory.decodeResource
                        (getResources(), Building.imageList[randomInt(0, 3)]), 250,
                (int) (Resources.getSystem().getDisplayMetrics().heightPixels / 1.8));

        buildings.add(new Building(topBuilding, bottomBuilding, Resources.getSystem().getDisplayMetrics().widthPixels + 250, randomInt(MIN_Y_COOR, MAX_Y_COOR), this.getContext()));
    }


    public void drawBackground(Canvas canvas){
        canvas.save();
        Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        int cameraStartPos = (int)offset;
        int cameraEndPos = (int)(canvas.getWidth() + offset);
        canvas.drawBitmap(background, new Rect(cameraStartPos,0, cameraEndPos, background.getHeight()), new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), new Paint());
        canvas.restore();

    }

    public void drawTime(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#a5a5a5"));
        paint.setTextSize(90);
        canvas.drawText(timeLimit + "", 30, 110, paint);
    }

    synchronized public void increaseBackgroundOffset(){
        offset += 0.5;
    }

    public int randomInt(int min, int max) {
        return min + new Random().nextInt(max);
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        super.verifyDrawable(who);
        return who == superman.getDrawable();
    }

    @Override
    public void invalidateDrawable(Drawable drawable) {
    }

    synchronized private void decreaseTime(){
        timeLimit--;
    }

}

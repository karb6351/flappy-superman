package com.example.david.testproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import java.util.Timer;

public class GameView extends SurfaceView {

    private static final String TAG = "Game View";

    private final static int CIRCLE_DELAY = 30;
    private Timer timer;

    private CharacterSprite characterSprite;

    public boolean start;

    private float offset;

    public GameView(Context context, Activity activity) {
        super(context);

        characterSprite = new CharacterSprite( this, this.getContext(), activity);

        start = false;

        offset = 0;

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!GameView.this.start) {
                    GameView.this.start = true;
                }
                characterSprite.fly();
                return false;
            }
        });

        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (!GameView.this.start) {
                    GameView.this.start = true;
                }
                characterSprite.fly();
                return false;
            }
        });

    }

    public void resume(){
        if (timer == null){
            timer = new Timer();
        }
        timer.schedule(new GameTimerTask(this, characterSprite, this.getContext()), 0, CIRCLE_DELAY);
    }

    public void pause(){
        timer.cancel();
        timer = null;
    }

    public void drawBackground(Canvas canvas){
        canvas.save();
        Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        int cameraStartPos = (int)offset;
        int cameraEndPos = (int)(canvas.getWidth() + offset);
        canvas.drawBitmap(background, new Rect(cameraStartPos,0, cameraEndPos, background.getHeight()), new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), new Paint());
        canvas.restore();

    }

    synchronized public void increaseBackgroundOffset(){
        offset += 1;
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        super.verifyDrawable(who);
        return who == characterSprite.getDrawable();
    }

    @Override
    public void invalidateDrawable(Drawable drawable) {
    }
}

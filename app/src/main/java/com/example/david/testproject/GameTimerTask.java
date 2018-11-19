package com.example.david.testproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;

import java.util.TimerTask;

public class GameTimerTask extends TimerTask {

    private GameView gameView;


    private Context context;

    private CharacterSprite characterSprite;

    @Override
    public void run() {
        if (gameView.start){
            characterSprite.move(gameView.getHeight());
            gameView.increaseBackgroundOffset();
        }

        Canvas canvas = gameView.getHolder().lockCanvas();
        if (canvas != null){
            canvas.drawRGB(255,255,255);
            gameView.drawBackground(canvas);
            characterSprite.drawOn(canvas);
            gameView.getHolder().unlockCanvasAndPost(canvas);
        }
    }

    public GameTimerTask(GameView gameView, CharacterSprite characterSprite, Context context) {
        this.characterSprite = characterSprite;
        this.gameView = gameView;
        this.context = context;
    }

}

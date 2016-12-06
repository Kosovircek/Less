package com.mitja.gracar.less;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import static android.R.attr.bitmap;
import static android.R.attr.logo;
import static android.R.attr.src;

/**
 * Created by mitja on 12/6/16.
 */

public class OverlayM {

    Canvas canvas;
    Bitmap logo;
    Rect srcLogo;
    Rect dstlogo;

    Rect topRect;
    Rect bottomRect;
    float topCoverPos;
    float bottomCoverPos;

    Paint coverPaint;

    ButtonM playButton;




    public OverlayM(Canvas canvas, Bitmap bitmap, Bitmap btnbitmap){
        this.canvas = canvas;

        topCoverPos = canvas.getHeight()/2;
        topRect = new Rect(0,0,canvas.getWidth(), (int) topCoverPos);

        bottomCoverPos = canvas.getHeight()/2;
        bottomRect = new Rect(0, (int) bottomCoverPos,canvas.getWidth(),canvas.getHeight());

        coverPaint = new Paint();
        coverPaint.setColor(Color.WHITE);
        coverPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        logo = bitmap;
        float logoWidth = (float) (canvas.getWidth()*1.2);
        float logoheight = (logoWidth * logo.getHeight()) / logo.getWidth();
        srcLogo = new Rect(0,0,logo.getWidth(),logo.getHeight());
        dstlogo = new Rect((int)(canvas.getWidth()/2-logoWidth/2),(int)(canvas.getHeight()*0.3 - logoheight/2),(int)(canvas.getWidth()/2+logoWidth/2),(int)(canvas.getHeight()*0.3 + logoheight/2) );

        playButton = new ButtonM(btnbitmap);
        playButton.setPosition((float) (canvas.getHeight()*0.7),canvas);
    }

    public void draw(){
        canvas.drawRect(topRect,coverPaint);
        canvas.drawRect(bottomRect,coverPaint);

        canvas.drawBitmap(logo,srcLogo,dstlogo,null);

        playButton.draw();
    }

    public void fadeOut(){
        topRect.bottom -= 300;
        canvas.drawRect(topRect,coverPaint);
        bottomRect.top += 300;
        canvas.drawRect(bottomRect,coverPaint);

        dstlogo.bottom -= 250;
        dstlogo.top -= 250;
        canvas.drawBitmap(logo,srcLogo,dstlogo,null);
    }

    public boolean animationOn(){
        return dstlogo.bottom > 0;
    }

    public boolean playPressed(float x, float y){
        return playButton.getTouch(x,y);
    }

    public void resetMenu(){
        topRect.bottom = canvas.getHeight()/2;

        bottomRect.top = canvas.getHeight()/2;

        float logoWidth = (float) (canvas.getWidth()*1.2);
        float logoheight = (logoWidth * logo.getHeight()) / logo.getWidth();
        srcLogo = new Rect(0,0,logo.getWidth(),logo.getHeight());
        dstlogo = new Rect((int)(canvas.getWidth()/2-logoWidth/2),(int)(canvas.getHeight()*0.3 - logoheight/2),(int)(canvas.getWidth()/2+logoWidth/2),(int)(canvas.getHeight()*0.3 + logoheight/2) );
    }


}

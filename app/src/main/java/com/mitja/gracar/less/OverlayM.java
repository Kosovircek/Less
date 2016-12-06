package com.mitja.gracar.less;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import static android.R.attr.bitmap;
import static android.R.attr.logo;

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


    public OverlayM(Canvas canvas, Bitmap bitmap){
        this.canvas = canvas;

        topCoverPos = canvas.getHeight()/2;
        topRect = new Rect(0,0,canvas.getWidth(), (int) topCoverPos);

        bottomCoverPos = canvas.getHeight()/2;
        bottomRect = new Rect(0, (int) bottomCoverPos,canvas.getWidth(),canvas.getHeight());

        coverPaint = new Paint();
        coverPaint.setColor(Color.WHITE);
        coverPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        logo = bitmap;
        srcLogo = new Rect(0,0,logo.getWidth(),logo.getHeight());
        //dstlogo = new Rect(canvas.getWidth()*)
    }

    public void draw(){
        canvas.drawRect(topRect,coverPaint);
        canvas.drawRect(bottomRect,coverPaint);

        //canvas.drawBitmap();
    }

    public void fadeOut(){
        topRect.bottom -= 100;
        canvas.drawRect(topRect,coverPaint);
        bottomRect.top += 100;
        canvas.drawRect(bottomRect,coverPaint);
    }


}

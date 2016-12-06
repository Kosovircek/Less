package com.mitja.gracar.less;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by mitja on 12/6/16.
 */

public class ButtonM {

    Bitmap graphic;
    Canvas canvas;
    float x;
    float y;
    Rect src;
    Rect dest;
    float buttonWidth;
    float buttonHeight;

    public ButtonM(Bitmap bitmap){
        graphic = bitmap;
        src = new Rect(0,0,graphic.getWidth(),graphic.getHeight());
    }

    public boolean getTouch(float touchX, float touchY){
        return ((touchX > x) && (touchX < x+buttonWidth) && (touchY > y) && (touchY < y+buttonHeight));
    }

    public void draw(){
        canvas.drawBitmap(graphic,src,dest,null);
    }

    public void setPosition(float posY, Canvas canvas){
        this.canvas = canvas;
        buttonWidth = (float) (canvas.getWidth()*0.7);
        buttonHeight = (buttonWidth * graphic.getHeight()) / graphic.getWidth();
        x = canvas.getWidth()/2 - buttonWidth/2;
        y = posY - buttonHeight/2;
        dest = new Rect((int) x,(int) y,(int) (x+buttonWidth),(int) (y+buttonHeight));
    }


}

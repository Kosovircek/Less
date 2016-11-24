package com.mitja.gracar.less;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Drawable {

    int posX;
    int posY;
    Bitmap bmp;
    Rect src;
    Rect dest;
    int drawWidth;
    int yOffset;
    Canvas c;


    public Drawable(Canvas canvas, int positionX, int positionY, Bitmap bitmap){

        c = canvas;
        bmp = bitmap;
        posX = positionX;
        posY = positionY;

        drawWidth = canvas.getWidth()/3;
        yOffset = (int) (canvas.getHeight()/2 - drawWidth*1.5);

        src = new Rect(0,0,bmp.getWidth(),bmp.getHeight());
        dest = new Rect(posX*drawWidth,yOffset+(posY*drawWidth),(posX*drawWidth)+drawWidth,yOffset+(posY*drawWidth)+drawWidth);

    }

    public void onDraw(){
        c.drawBitmap(bmp,src,dest,null);
    }


}

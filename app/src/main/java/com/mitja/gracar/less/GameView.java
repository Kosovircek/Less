package com.mitja.gracar.less;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;


public class GameView extends SurfaceView {


    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;

    private ArrayList<Bitmap> cards;
    private boolean first;

    private int cardWidth;
    private float originY;

    private int playerWidth;
    private int plOriginY;
    private Bitmap one;
    private Bitmap two;
    private Rect plSrc;


    private ArrayList<Rect> cardsPos;
    private Rect src;

    private LessEngine less;
    private int[] choosenCards;
    private int[][] positions;

    private int[] from;
    private int[] to;
    private boolean firsttouch;

    private Paint textPaint;

    private String statusReturn;

    private long lastClick;


    public GameView(Context context) {
        super(context);
        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });


        less = new LessEngine();
        choosenCards = new int[9];
        choosenCards = less.getChoosenCards();

        first = true;
        cards = new ArrayList<Bitmap>();
        cards.add(BitmapFactory.decodeResource(getResources(), R.drawable.a0));
        cards.add(BitmapFactory.decodeResource(getResources(), R.drawable.a1));
        cards.add(BitmapFactory.decodeResource(getResources(), R.drawable.a2));
        cards.add(BitmapFactory.decodeResource(getResources(), R.drawable.a3));
        cards.add(BitmapFactory.decodeResource(getResources(), R.drawable.a4));
        cards.add(BitmapFactory.decodeResource(getResources(), R.drawable.a5));
        cards.add(BitmapFactory.decodeResource(getResources(), R.drawable.a6));
        cards.add(BitmapFactory.decodeResource(getResources(), R.drawable.a7));
        cards.add(BitmapFactory.decodeResource(getResources(), R.drawable.a8));
        cards.add(BitmapFactory.decodeResource(getResources(), R.drawable.a9));
        cards.add(BitmapFactory.decodeResource(getResources(), R.drawable.a10));
        cards.add(BitmapFactory.decodeResource(getResources(), R.drawable.a11));

        cardWidth = 100;
        originY = 0;

        cardsPos = new ArrayList<Rect>();
        src = new Rect(0,0,cards.get(1).getWidth(),cards.get(1).getWidth());

        positions = new int[6][6];
        positions = less.getPositionField();

        one = BitmapFactory.decodeResource(getResources(), R.drawable.one);
        two = BitmapFactory.decodeResource(getResources(), R.drawable.two);
        plSrc = new Rect(0,0,one.getWidth(),one.getWidth());

        from = new int[2];
        to = new int[2];
        firsttouch = true;

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextSize(50);

        statusReturn = "TEST TEST TEST";

        lastClick = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.CYAN);

        //canvas.drawBitmap(cards.get(2),new Rect(0,0,cards.get(1).getWidth(),cards.get(1).getWidth()),new Rect(0,(int)originY,cardWidth,(int)(originY+cardWidth)),null);



        if(first){
            first = false;
            cardWidth = canvas.getWidth()/3;
            originY = (float) (canvas.getHeight()/2 -(cardWidth*1.5));

            playerWidth = canvas.getWidth()/6;
            plOriginY = canvas.getHeight()/2 -(playerWidth*3);

            for(int i=0; i<3; i++){
                for(int j=0; j<3; j++) {
                    cardsPos.add(new Rect(j*cardWidth,(int)(originY+(i*cardWidth)),(j*cardWidth)+cardWidth,(int) (originY+(i*cardWidth)+cardWidth)));
                }
            }
        }

        for(int i=0; i<9; i++){
            canvas.drawBitmap(cards.get(choosenCards[i]),src,cardsPos.get(i),null);
        }

        for(int i=0; i<6;i++){
            for(int j=0; j<6; j++){
                if(positions[i][j] == 1){
                    canvas.drawBitmap(one,plSrc,new Rect(i*playerWidth,plOriginY+(j*playerWidth),i*playerWidth+playerWidth,plOriginY+(j*playerWidth)+playerWidth),null);
                }else if(positions[i][j] == 2){
                    canvas.drawBitmap(two,plSrc,new Rect(i*playerWidth,plOriginY+(j*playerWidth),i*playerWidth+playerWidth,plOriginY+(j*playerWidth)+playerWidth),null);
                }
            }
        }

        canvas.drawText("TURN: "+less.getTurn(),50,(int) (canvas.getHeight()*0.1),textPaint);
        canvas.drawText("MOVES: "+less.getMoves(),(int)(canvas.getWidth()/2),(int) (canvas.getHeight()*0.1),textPaint);
        canvas.drawText("Status: "+statusReturn,50,(int) (canvas.getHeight()*0.15),textPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - lastClick > 100) {
            lastClick = System.currentTimeMillis();
            if(firsttouch){
                from = new int[] {(int) (event.getX()/playerWidth), (int) ((event.getY()-plOriginY)/playerWidth)};
                firsttouch = false;
            }else{
                to = new int[] {(int) (event.getX()/playerWidth), (int) ((event.getY()-plOriginY)/playerWidth)};
                statusReturn = less.move(from,to);
                firsttouch = true;
            }
        }
        return true;
    }
}
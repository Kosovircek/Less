package com.mitja.gracar.less;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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

    private boolean reset;
    private long lastRefresh;
    private int timesRefresh;

    private boolean pieceSelected;
    private Rect selectRect;
    private Paint selectPaint;

    private boolean running;
    private boolean playing; //za touch evente da lahk klikas cez playing field

    private ButtonM resetButton;
    private OverlayM overlayM;

    boolean menuUp;

    ButtonM menuButton;

    Bitmap playerTileBit;
    Rect tileSrc;
    Rect tileDst1;
    Rect tileDst2;

    Rect whiteRect;
    Rect blackRect;

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
                gameLoopThread = new GameLoopThread(GameView.this);
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
                running = true;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });


        running = true;

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
        textPaint.setColor(Color.BLUE);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextSize(100);

        statusReturn = "TEST TEST TEST";

        lastClick = 0;

        reset = false;
        lastRefresh = 0;
        timesRefresh = 0;

        pieceSelected = false;
        selectPaint = new Paint();
        selectPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        selectPaint.setARGB(100,100,10,0);
        selectRect = new Rect(0,0,0,0);

        playing = false;
        menuUp = true;
        //resetButton = new ButtonM(BitmapFactory.decodeResource(getResources(),R.drawable.reseton));
        menuButton = new ButtonM(BitmapFactory.decodeResource(getResources(),R.drawable.reseton));


        playerTileBit = BitmapFactory.decodeResource(getResources(),R.drawable.playertile);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.rgb(201, 216, 225));

        positions = less.getPositionField();

        if (reset) {
            //first = true;
            if (System.currentTimeMillis() - lastRefresh > 1) {
                lastRefresh = System.currentTimeMillis();
                timesRefresh += 1;

                less.reset();
                choosenCards = less.getChoosenCards();


                if (timesRefresh == 10) {
                    reset = false;
                    timesRefresh = 0;
                }
            }


        }


        if (first) {
            first = false;
            cardWidth = canvas.getWidth() / 3;
            originY = (float) (canvas.getHeight() / 2 - (cardWidth * 1.5));

            playerWidth = canvas.getWidth() / 6;
            plOriginY = canvas.getHeight() / 2 - (playerWidth * 3);

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    cardsPos.add(new Rect(j * cardWidth, (int) (originY + (i * cardWidth)), (j * cardWidth) + cardWidth, (int) (originY + (i * cardWidth) + cardWidth)));
                }
            }

            //resetButton.setPosition((float) (originY + cardWidth * 3.5), canvas);
            overlayM = new OverlayM(canvas, BitmapFactory.decodeResource(getResources(),R.drawable.lesslogo),BitmapFactory.decodeResource(getResources(),R.drawable.playon));
            menuButton.setPosition(10, canvas);

            tileDst1 = new Rect(0,0,canvas.getWidth(),(int) (canvas.getHeight()/2-cardWidth*1.5));
            tileDst2 = new Rect(0,(int) (canvas.getHeight()/2 +cardWidth*1.5),canvas.getWidth(),canvas.getHeight());
            tileSrc = new Rect(0,0,playerTileBit.getWidth(),playerTileBit.getHeight());

            whiteRect = new Rect(0,0,(int) (canvas.getHeight()/2-cardWidth*1.5),(int) (canvas.getHeight()/2-cardWidth*1.5));
            blackRect = new Rect((int) (canvas.getWidth()-(canvas.getHeight()/2-cardWidth*1.5)),(int) (canvas.getHeight()/2+cardWidth*1.5),canvas.getWidth(),canvas.getHeight());
        }

        if(playing) {
            if (running) {


                //canvas.drawBitmap(cards.get(2),new Rect(0,0,cards.get(1).getWidth(),cards.get(1).getWidth()),new Rect(0,(int)originY,cardWidth,(int)(originY+cardWidth)),null);



                for (int i = 0; i < 9; i++) {
                    canvas.drawBitmap(cards.get(choosenCards[i]), src, cardsPos.get(i), null);
                }

                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 6; j++) {
                        if (positions[i][j] == 1) {
                            canvas.drawBitmap(one, plSrc, new Rect(i * playerWidth, plOriginY + (j * playerWidth), i * playerWidth + playerWidth, plOriginY + (j * playerWidth) + playerWidth), null);
                        } else if (positions[i][j] == 2) {
                            canvas.drawBitmap(two, plSrc, new Rect(i * playerWidth, plOriginY + (j * playerWidth), i * playerWidth + playerWidth, plOriginY + (j * playerWidth) + playerWidth), null);
                        }
                    }
                }



                if (pieceSelected) {
                    canvas.drawRect(selectRect, selectPaint);
                }

                canvas.drawBitmap(playerTileBit,tileSrc, tileDst1,null);
                canvas.drawBitmap(playerTileBit,tileSrc, tileDst2,null);

                canvas.drawBitmap(one,plSrc,whiteRect,null);
                canvas.drawBitmap(two,plSrc,blackRect,null);

                if(less.getTurn() == 1) {
                    canvas.drawText("MOVES: " + less.getMoves(), (int) (canvas.getHeight() / 2 - cardWidth * 1.5) + 150, (int) (canvas.getHeight() / 2 - cardWidth * 1.5) / 2 + 50, textPaint);
                    canvas.drawText("MOVES: " + "0", 250, (int) (canvas.getHeight() - ((canvas.getHeight() / 2 - cardWidth * 1.5) / 2)) + 50, textPaint);
                }
                if(less.getTurn() == 2
                        ) {
                    canvas.drawText("MOVES: " + "0", (int) (canvas.getHeight() / 2 - cardWidth * 1.5) + 150, (int) (canvas.getHeight() / 2 - cardWidth * 1.5) / 2 + 50, textPaint);
                    canvas.drawText("MOVES: " + less.getMoves(), 250, (int) (canvas.getHeight() - ((canvas.getHeight() / 2 - cardWidth * 1.5) / 2)) + 50, textPaint);
                }
                //canvas.drawText("Status: " + statusReturn, 50, (int) (canvas.getHeight() * 0.15), textPaint);

                //resetButton.draw();
                //menuButton.draw();
                if(statusReturn == "Black is Winner!"){
                    less.setTurn(1);
                }
                if(statusReturn == "White is winner"){
                    less.setTurn(2);
                }
            }
        }
        if(overlayM.animationOn() && !menuUp){
            overlayM.fadeOut();
        }
        if(menuUp){
            overlayM.draw();
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - lastClick > 100) {
            lastClick = System.currentTimeMillis();

            //Touch within playing field
            if(playing) {
                if (event.getY() > originY && event.getY() < (originY + cardWidth * 3)) {
                    if (firsttouch) {
                        from = new int[]{(int) (event.getX() / playerWidth), (int) ((event.getY() - plOriginY) / playerWidth)};
                        firsttouch = false;

                        pieceSelected = true;
                        selectRect = new Rect(from[0] * playerWidth, plOriginY + (from[1] * playerWidth), from[0] * playerWidth + playerWidth, plOriginY + (from[1] * playerWidth) + playerWidth);

                    } else {

                        to = new int[]{(int) (event.getX() / playerWidth), (int) ((event.getY() - plOriginY) / playerWidth)};

                        statusReturn = less.move(from, to);
                        firsttouch = true;

                        pieceSelected = false;
                    }
                }
            }
            //Touch reset button
            /*
            if(resetButton.getTouch(event.getX(),event.getY())){
                reset = true;
            }
            */

            if((overlayM.playPressed(event.getX(),event.getY())) && menuUp){
                playing = true;
                menuUp = false;
                reset = true;
            }
            /*
            if(menuButton.getTouch(event.getX(),event.getY())){
                playing = false;
                menuUp = true;
                overlayM.resetMenu();
            }
            */
        }
        return true;
    }

    //Da z ustavitvijo Activitja ustavim tudi poseben Thread za risanje SurfaceViewa (Drugače aplikacija zablokira zara null reference)
    public void pause(){
        running = false;
        gameLoopThread.setRunning(false);
    }

    public void resume(){
        gameLoopThread.setRunning(true);

    }

    public void onBackPressed(){
        if(!menuUp){
            playing = false;
            menuUp = true;
            overlayM.resetMenu();
        }else{
            playing = true;
            menuUp = false;
        }
    }

}
package com.mitja.gracar.less;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Game extends Activity {

    GameView mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainView = new GameView(this);
        setContentView(mainView);
    }

    @Override
    protected void onPause() {
        mainView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mainView.resume();
        super.onResume();
    }
}

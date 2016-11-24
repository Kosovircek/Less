package com.mitja.gracar.less;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;

public class MainActivity extends Activity {

    LessEngine less;
    TextView viewOut;
    EditText editIn;
    TextView textRe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        less = new LessEngine();


        viewOut = (TextView)findViewById(R.id.viewOut);
        textRe = (TextView)findViewById(R.id.textReturn);
        editIn = (EditText)findViewById(R.id.editIn);
        viewOut.setText(less.getGameState());

    }

    public void onNext(View v){
        /* FOR TESTING ROTATE
        viewOut.setText(Arrays.toString(new int[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,1}}));

        String text = "";

        int[][] testArray = less.rotateRight(new int[][]{{1,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,1}});

        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                text = text + testArray[i][j];
            }
        }

        viewOut.setText(text);
        */

        /*FOR TESTING RETURNFIELD
        String textTest = "";
        int[][][] arr = less.retunField();

        for(int i=0;i<9;i++){
            for(int j=0;j<4;j++){
                for(int k=0;k<4;k++){
                    textTest += arr[i][j][k];
                }
                textTest += ", ";
            }
            textTest += "\n";
        }
        viewOut.setText(textTest);
        */

        /*TEST GET CELL POSITION
        String textTest = "";

        int[] neki = less.getCellPosition(new int[]{1,2});
        textTest = neki[0] +", "+neki[1];

        viewOut.setText(textTest);
        */


        /*GAME TEST
        String comm = editIn.getText().toString();
        int[] from = {Character.getNumericValue(comm.charAt(0)),Character.getNumericValue(comm.charAt(1))};
        int[] to = {Character.getNumericValue(comm.charAt(2)),Character.getNumericValue(comm.charAt(3))};
        comm = less.move(from, to);
        textRe.setText(comm);
        viewOut.setText(less.getGameState());
        editIn.setText("");
        */
        Intent i = new Intent(getBaseContext(), Game.class);
        startActivity(i);
    }

}

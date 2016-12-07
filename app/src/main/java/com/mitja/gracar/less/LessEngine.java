package com.mitja.gracar.less;


import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

//TODO(mitja): SPREMEN NAZAJ DA SO KARTE LAHK ROTATED!!!!

public class LessEngine {

    private final int[][][] cards = {
        {{0,0,0,0},{0,0,0,0},{0,0,1,1},{0,0,0,0}},
        {{0,1,0,0},{0,0,1,0},{0,0,0,0},{0,0,0,0}},
        {{0,1,1,0},{1,0,0,0},{0,0,0,0},{0,0,0,0}},
        {{0,0,0,0},{0,0,0,0},{0,0,1,0},{0,0,0,0}},
        {{0,0,0,0},{0,0,1,0},{0,1,0,0},{0,0,0,0}},
        {{0,0,0,0},{0,0,1,0},{0,0,0,1},{0,0,0,0}},
        {{0,0,0,0},{0,1,1,0},{0,0,0,0},{0,0,0,0}},
        {{1,1,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}},
        {{0,0,0,0},{0,0,0,0},{0,0,0,1},{0,0,1,0}},
        {{0,1,1,0},{1,0,0,0},{0,0,0,0},{0,0,0,0}},
        {{0,0,0,0},{0,0,0,0},{0,0,1,1},{0,0,1,0}},
        {{1,0,0,0},{1,0,0,0},{0,0,0,0},{0,0,0,0}}
    };


    private int[][][] wallField; //Stores postion of walls for current playing field
    private int[][] positionField; //Stores player pieces positions for current playing field
    private int movesBlack;
    private int movesWhite;
    private int whosTurn; //1 = white, 2 = black
    private int movesLeft;

    private int[] cardsChoosen;


    public LessEngine(){
        reset();
    }

    public String move(int[] from, int[] to){
        if(from[0] == 9){
            reset();
            return "reset";
        }

        //DEFINE 0 = Y-os     1 = X-os


        if((to[0] < 0)||(to[0] > 6)||(to[1] < 0)||(to[1] > 6)){
            return "ERROR: Move out of bounds";
        }
        if((from[0] < 0)||(from[0] > 6)||(from[1] < 0)||(from[1] > 6)){
            return "ERROR: Out of bounds";
        }
        if(positionField[from[0]][from[1]] == 0){
            return "ERROR: No piece on that position";
        }
        if((positionField[from[0]][from[1]] - whosTurn) != 0){
            return "ERROR: Not Your turn";
        }
        if((from[1] == to[1]) && (from[0] == to[0])){
            return "ERROR: Cant move to the same spot";
        }
        if((Math.abs(to[0]-from[0])+ Math.abs(to[1]-from[1])) >=2){
            return "ERROR: Invalid move";
        }

        //If the "to" field is empty (0)
        if(positionField[to[0]][to[1]] == 0){
            return checkEmptySpot(from, to);
        }

        //If te "to" field is a playing piece(!=0)
        if(positionField[to[0]][to[1]] != 0){
            int[] tempTo = to.clone();
            int[] tempFrom = from.clone();
            //move RIGHT
            if(from[1] < to[1]){

                tempTo[1] += 1;
                tempFrom[1] += 1;
                return checkFullSpot(from ,to , tempFrom, tempTo);
            }
            //move LEFT
            if(from[1] > to[1]){

                tempTo[1] -= 1;
                tempFrom[1] -= 1;
                return checkFullSpot(from ,to , tempFrom, tempTo);
            }
            //move UP
            if(from[0] > to[0]){

                tempTo[0] -= 1;
                tempFrom[0] -= 1;
                return checkFullSpot(from ,to , tempFrom, tempTo);
            }
            //move DOWN
            if(from[0] < to[0]){

                tempTo[0] += 1;
                tempFrom[0] += 1;
                return checkFullSpot(from ,to , tempFrom, tempTo);
            }
        }

        return "Nothing happend";
    }

    private String checkEmptySpot(int[] from, int[] to){

        int tempMoves = movesLeft;

        movesLeft -=1;
        movesLeft -= checkForWall(from ,to); //To check if ther is a wall on she "to" cell side
        movesLeft -= checkForWall(to ,from); //To cehck if there is a wall on the "from" cell side
        if(movesLeft < 0) {
            movesLeft = tempMoves;
            return "Not enough moves";
        }

        positionField[from[0]][from[1]] = 0;
        positionField[to[0]][to[1]] = whosTurn;



        if(movesLeft == 0){
            if(whosTurn == 1){
                whosTurn = 2;
            }else if(whosTurn == 2){
                whosTurn = 1;
            }
            movesLeft = 3;

            if((positionField[0][0]+positionField[1][0]+positionField[0][1]+positionField[1][1]) == 8){
                return "Black is Winner!";
            }
            else if((positionField[5][5] == 1) && (positionField[5][4] == 1) && (positionField[4][5] == 1) && (positionField[4][4] == 1)){
                return "White is winner";
            }
            else{
                return "Turn end";
            }
        }

        if((positionField[0][0]+positionField[1][0]+positionField[0][1]+positionField[1][1]) == 8){
            return "Black is Winner!";
        }
        else if((positionField[5][5] == 1) && (positionField[5][4] == 1) && (positionField[4][5] == 1) && (positionField[4][4] == 1)){
            return "White is winner";
        }else{
            return "move sucsessfull";
        }


    }

    private String checkFullSpot(int[] from, int[] to, int[] tempFrom, int[] tempTo){
        //Da ne mores skočt izven igralnega polja
        if((tempTo[0]<0)||(tempTo[0]>5)||(tempTo[1]<0)||(tempTo[1]>5)){
            return "ERROR: Out of bounds";
        }

        //Če je na jump mestu piece
        if(positionField[tempTo[0]][tempTo[1]] != 0){
            return "ERROR: Cant jump 2 pieces";
        }
        //Če je jump mesto prazno
        if(positionField[tempTo[0]][tempTo[1]] == 0){
            //Če skačeš čez zid
            if((checkForWall(tempFrom ,tempTo) != 0) || (checkForWall(tempTo ,tempFrom) != 0) || (checkForWall(from ,to) != 0) || (checkForWall(to ,from) != 0)){
                return "ERROR: Cant jup wall AND piece";
            }
            //Če NE skačeš čez zid
            if((checkForWall(tempFrom ,tempTo) == 0) || (checkForWall(tempTo ,tempFrom) == 0) || (checkForWall(from ,to) == 0) || (checkForWall(to ,from) == 0)){
                movesLeft -= 1;
                positionField[from[0]][from[1]] = 0;
                positionField[tempTo[0]][tempTo[1]] = whosTurn;

                if(movesLeft == 0){
                    if(whosTurn == 1){
                        whosTurn = 2;
                    }else if(whosTurn == 2){
                        whosTurn = 1;
                    }
                    movesLeft = 3;

                    if((positionField[0][0]+positionField[1][0]+positionField[0][1]+positionField[1][1]) == 8){
                        return "Black is Winner!";
                    }
                    else if((positionField[5][5] == 1) && (positionField[5][4] == 1) && (positionField[4][5] == 1) && (positionField[4][4] == 1)){
                        return "White is winner";
                    }else{
                        return "Turn end";
                    }
                }

                return "move sucsessfull";
            }
        }

        if((positionField[0][0]+positionField[1][0]+positionField[0][1]+positionField[1][1]) == 8){
            return "Black is Winner!";
        }
        else if((positionField[5][5] == 1) && (positionField[5][4] == 1) && (positionField[4][5] == 1) && (positionField[4][4] == 1)){
            return "White is winner";
        }else{
            return "ERROR: FULLCheck";
        }
    }

    private int checkForWall(int[] from, int[] to){
        int moveCost = 0;
        Collections.reverse(Arrays.asList(from));
        Collections.reverse(Arrays.asList(to));

        int[] pos = getCellPosition(to);
        //move RIGHT
        if(from[0] < to[0]){
            moveCost = wallField[pos[0]][pos[1]][3];
        }
        //move LEFT
        else if(from[0] > to[0]){
            moveCost = wallField[pos[0]][pos[1]][1];
        }
        //move UP
        else if(from[1] > to[1]){
            moveCost = wallField[pos[0]][pos[1]][2];
        }
        //move DOWN
        else if(from[1] < to[1]){
            moveCost = wallField[pos[0]][pos[1]][0];
        }

        return moveCost;
    }

    public int[] getCellPosition(int[] pos){

        int cardNum = (((int) (pos[1])/2)*3)+(Math.round(pos[0]/2));
        int cellNum = 0;


        if((pos[0]%2 == 0) && (pos[1]%2 == 0)){
            cellNum = 0;
        }
        if((pos[0]%2 != 0) && (pos[1]%2 == 0)){
            cellNum = 1;
        }
        if((pos[0]%2 == 0) && (pos[1]%2 != 0)){
            cellNum = 3;
        }
        if((pos[0]%2 != 0) && (pos[1]%2 != 0)){
            cellNum = 2;
        }

        Log.d("MItja test", ""+cardNum+","+cellNum);
        return new int[]{cardNum , cellNum};
    }

    public void reset(){
        cardsChoosen = new int[9];
        wallField = generateWallField();
        positionField = new int[][]{
                {1,1,0,0,0,0},
                {1,1,0,0,0,0},
                {0,0,0,0,0,0},
                {0,0,0,0,0,0},
                {0,0,0,0,2,2},
                {0,0,0,0,2,2},
        };
        movesBlack = 0;
        movesWhite = 0;
        whosTurn = 1;
        movesLeft = 3;
    }

    private int[][][] generateWallField(){

        int[][][] field = new int[9][4][4];
        ArrayList<Integer> alreadyUsed = new ArrayList<>();
        int cardSelected = 0;
        int cardOrientation = 0;

        Random rand = new Random();
        cardSelected = rand.nextInt(9);


        for(int i=0;i<9;i++){
            //randomly select one of the cards

            //Discards cards already used

            while(alreadyUsed.contains(cardSelected)){
                cardSelected = rand.nextInt(12);
            }
            alreadyUsed.add(cardSelected);//so this card cant be used again

            //cardSelected = i;

            cardsChoosen[i] = cardSelected;


            //Selects the orientation of the card
            //cardOrientation = rand.nextInt(4);
            cardOrientation = 0;



            switch (cardOrientation){
                case 0:
                    field[i] = cards[cardSelected];
                    break;
                case 1:
                    field[i] = rotateRight(cards[cardSelected]);
                    break;
                case 2:
                    field[i] = rotateRight(rotateRight(cards[cardSelected]));
                    break;
                case 3:
                    field[i] = rotateRight(rotateRight(rotateRight(cards[cardSelected])));
                    break;
            }


        }

        return field;
    }

    private int[][] rotateRight(int[][] card){
        int[][] field = new int[4][4];

        field[0] = new int[]{card[3][3], card[3][0], card[3][1], card[3][2]};
        field[1] = new int[]{card[0][3], card[0][0], card[3][1], card[0][2]};
        field[2] = new int[]{card[1][3], card[1][0], card[1][1], card[1][2]};
        field[3] = new int[]{card[2][3], card[2][0], card[2][1], card[2][2]};

        return field;
    }

    public int[][][] retunField(){
        return wallField;
    }

    public String getGameState(){

        String state = "\n\nTURN: "+whosTurn+"  Moves:"+movesLeft+"\n\n\n";

        for(int i=0; i<6;i++){
            state += "\n";
            for(int j=0; j<6;j++){
                state += positionField[i][j]+", ";
            }
        }

        return state;
    }

    public int[] getChoosenCards(){
        return cardsChoosen;
    }

    public int[][] getPositionField(){
        return positionField;
    }

    public int getMoves(){
        return movesLeft;
    }

    public int getTurn(){
        String temp = "";

        for(int i=0;i<4;i++){
            temp += "{";
            for(int j=0;j<4;j++){
                temp += wallField[0][i][j]+",";
            }
            temp += "}";
        }

        return whosTurn;
        //return temp;
    }

    public void setTurn(int turn){
        whosTurn = turn;
        movesLeft = 3;
    }


}




























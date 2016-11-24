package com.mitja.gracar.less;



public class OutOfMovesException extends Exception {

    @Override
    public String getMessage() {
        return "ERROR: Out of moves";
    }
}

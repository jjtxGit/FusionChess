package com.keshi.chess;

import android.graphics.Point;

import java.util.Stack;

public class FusionChess {
    // Set static member about piece.
    public static final byte BLANK=0;       // 空白
    public static final byte KING=1;        // 将
    public static final byte ADVISER =2;    // 士
    public static final byte ELEPHANT=3;    // 象
    public static final byte HORSE=4;       // 马
    public static final byte CHARIOT=5;     // 车
    public static final byte CANNON=6;      // 炮
    public static final byte PAWN=7;        // 卒
    // Set static member about typeColor.
    public static final byte TYPE_RED=0;    // 红
    public static final byte TYPE_BLACK=1;  // 黑
    // Set static default chessboard.
    private static final byte defaultBoard[][]= {
            {   CHARIOT,    HORSE,      ELEPHANT,   ADVISER,    KING},
            {   BLANK,      CANNON,     BLANK,      BLANK,      BLANK},
            {   PAWN,       BLANK,      PAWN,       BLANK,      PAWN}};
    // ChessBoard is used to save values about chess information.
    private byte chessBoard[][]=new byte[10][9];
    // The Stack is for Saving data of records.
    Stack pieceRecordsStack=new Stack<ChessRecord>();
    // Initialize class FusionChess.
    public FusionChess(){
        IntiChessBoard(TYPE_RED);
    }
    public FusionChess(byte myType){
        IntiChessBoard(myType);

    }

    public byte getChessBoard(int x,int y,byte myType) {
        if(myType==TYPE_BLACK){
            x=10-x; y=9-y;
        }
        return chessBoard[x][y];
    }

    /**
     * This function is used to set pieces on chessboard.
     * @param x The location.x of piece
     * @param y The location.y of piece
     * @param piece The type of piece.
     * @param type  The type of color. (TYPE_RED or TYPE_BLACK)
     */
    public void setChessBoard(int x,int y,byte piece,byte type) {
        if(type==TYPE_BLACK){
            x=10-x; y=9-y;
            piece=(byte)(-piece);
        }
        chessBoard[x][y] = piece;
    }

    public void IntiChessBoard(byte myType){
        byte enemyType=myType==TYPE_RED? TYPE_BLACK:TYPE_RED;
        for(int i=0;i<10;i++){
            setChessBoard(0,i,myType,defaultBoard[0][Math.abs(Math.abs(5-i)-5)]);
            setChessBoard(0,i,enemyType,defaultBoard[0][Math.abs(Math.abs(5-i)-5)]);
            setChessBoard(2,i,myType,defaultBoard[1][Math.abs(Math.abs(5-i)-5)]);
            setChessBoard(2,i,enemyType,defaultBoard[1][Math.abs(Math.abs(5-i)-5)]);
            setChessBoard(5,i,myType,defaultBoard[2][Math.abs(Math.abs(5-i)-5)]);
            setChessBoard(5,i,enemyType,defaultBoard[2][Math.abs(Math.abs(5-i)-5)]);
        }
    }
}

// This class is used to save records.
class ChessRecord{
    private Point from;
    private Point to;
    private byte fromPiece,toPiece;

    public ChessRecord(Point from,Point to,byte fromPiece,byte toPiece){
        setFrom(from);
        setTo(to);
        setFromPiece(fromPiece);
        setToPiece(fromPiece);
    }
//    public ChessRecord(int fromX,int fromY,int toX,int toY,byte fromPiece,byte toPiece){
//
//    }

    public Point getFrom() {
        return from;
    }

    public void setFrom(Point from) {
        this.from = from;
    }

    public Point getTo() {
        return to;
    }

    public void setTo(Point to) {
        this.to = to;
    }

    public byte getFromPiece() {
        return fromPiece;
    }

    public void setFromPiece(byte fromPiece) {
        this.fromPiece = fromPiece;
    }

    public byte getToPiece() {
        return toPiece;
    }

    public void setToPiece(byte toPiece) {
        this.toPiece = toPiece;
    }
}
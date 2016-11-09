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
    public static final byte HORSE_HORSE=8;     // 双马
    public static final byte HORSE_CHARIOT=9;   // 马车
    public static final byte HORSE_CANNON=10;   // 马炮
    public static final byte HORSE_PAWN=11;     // 马卒
    public static final byte CHARIOT_CHARIOT=12;// 双车
    public static final byte CHARIOT_PAWN=13;   // 车卒
    public static final byte CANNON_CHARIOT=14; // 炮车
    public static final byte CANNON_CANNON=15;  // 双炮
    public static final byte CANNON_PAWN=16;    // 炮卒
    public static final byte PAWN_PAWN=17;      // 双卒
    // Set static default chessboard.
    private static final byte defaultBoard[][]= {
            {   CHARIOT,    HORSE,      ELEPHANT,   ADVISER,    KING},
            {   BLANK,      CANNON,     BLANK,      BLANK,      BLANK},
            {   PAWN,       BLANK,      PAWN,       BLANK,      PAWN}};
    // Set static fusion-pieces array.(x,y)->HORSE,CHARIOT,CANNON,PAWN
    private static final byte fusionPieces[][]={
            {HORSE_HORSE},      {HORSE_CHARIOT},    {HORSE_CANNON},     {HORSE_PAWN},
            {HORSE_CHARIOT},    {CHARIOT_CHARIOT},  {CANNON_CHARIOT},   {CHARIOT_PAWN},
            {HORSE_CANNON},     {HORSE_PAWN},       {CANNON_CANNON},    {CANNON_PAWN},
            {HORSE_PAWN},       {CHARIOT_PAWN},     {CANNON_PAWN},      {PAWN_PAWN}};
    // Set static divide-pieces array.(x=x-8:HORSE_CHARIOT...)
    private static final byte dividePieces[][]={
            {HORSE,HORSE},      // x=0=HORSE_HORSE
            {HORSE,CHARIOT},    // x=1=HORSE_CHARIOT ...
            {HORSE,CANNON},{HORSE,PAWN},{CHARIOT,CHARIOT},
            {CHARIOT,PAWN},{CANNON,PAWN},{CANNON,CANNON},{CANNON,PAWN},{PAWN,PAWN}};
    // ChessBoard is used to save values about chess information.
    private byte chessBoard[][]=new byte[10][9];
    // The Stack is for Saving data of records.
    Stack<ChessRecord> chessRecordsStack= new Stack<ChessRecord>();
    // Initialize class FusionChess.
    public FusionChess(){
        initChessBoard();
    }

    /**
     * This function is used to get pieces on chessboard.
     * @param x The location.x of piece
     * @param y The location.y of piece
     */
    public byte getChessBoard(int x,int y) {
        return chessBoard[x][y];
    }

    /**
     * This function is used to set pieces on chessboard.
     * @param x The location.x of piece
     * @param y The location.y of piece
     * @param piece The type of piece.
     */
    public void setChessBoard(int x,int y,byte piece) {
        chessBoard[x][y] = piece;
    }

    /**
     * This function is used to initialize Chessboard.
     */
    public void initChessBoard(){
        for(int i=0;i<9;i++){
            setChessBoard(0,i,defaultBoard[0][Math.abs(Math.abs(5-i)-5)]);
            setChessBoard(1,i,BLANK);
            setChessBoard(2,i,defaultBoard[1][Math.abs(Math.abs(5-i)-5)]);
            setChessBoard(3,i,defaultBoard[2][Math.abs(Math.abs(5-i)-5)]);
            setChessBoard(4,i,BLANK);
            setChessBoard(5,i,BLANK);
            setChessBoard(6,i,(byte)-defaultBoard[2][Math.abs(Math.abs(5-i)-5)]);
            setChessBoard(7,i,(byte)-defaultBoard[1][Math.abs(Math.abs(5-i)-5)]);
            setChessBoard(8,i,BLANK);
            setChessBoard(9,i,(byte)-defaultBoard[0][Math.abs(Math.abs(5-i)-5)]);
        }
        chessRecordsStack.clear(); // Clear stack.
    }

    /**
     * This function is used to judge if a moving is legal.
     * @param from The location of piece that from.
     * @param to The Location of piece that to.
     * @return YES or NOT
     */

    /**
     * This function is to move pieces.
     * @param from The location of piece that from.
     * @param to The Location of piece that to.
     * @param choosePiece The piece which you want to move.
     */
    public void movePiece(Point from,Point to,byte choosePiece){
        byte fromPiece=getChessBoard(from.x,from.y);
        byte toPiece=getChessBoard(to.x,to.y);
        chessRecordsStack.push(new ChessRecord(from,to,choosePiece,toPiece));

        if(choosePiece==fromPiece)  // Move pieces
            setChessBoard(from.x,from.y,BLANK);
        else {   //Divide pieces
            fromPiece-=8;
            setChessBoard(from.x, from.y, dividePieces[fromPiece][0] != choosePiece ?
                    dividePieces[fromPiece][0] : dividePieces[fromPiece][1]);
        }
        if(choosePiece*toPiece<=0)    // Eat pieces
            setChessBoard(to.x,to.y,choosePiece);
        else{   //Fusion pieces
            choosePiece-=4; toPiece-=4;
            setChessBoard(to.x,to.y,fusionPieces[choosePiece-4][toPiece-4]);
        }

    }

    /**
     * This function is to undo pieces.
     */
    public void undoPiece(){
        if(chessRecordsStack.empty())
            return; // Return when the stack is empty.

        ChessRecord record=chessRecordsStack.pop();
        Point from=record.getFrom();
        Point to=record.getTo();
        byte fromPiece=getChessBoard(from.x,from.y);
        byte choosePiece=record.getChoosePiece();
        byte toPiece=record.getToPiece();

        setChessBoard(to.x,to.y,toPiece);
        if(fromPiece==BLANK)
            setChessBoard(from.x,from.y,choosePiece);
        else{   //Fusion pieces
            choosePiece-=4; fromPiece-=4;
            setChessBoard(from.x,from.y,fusionPieces[choosePiece-4][fromPiece-4]);
        }
    }
}

// This class is used to save records.
class ChessRecord{
    private Point from;
    private Point to;
    private byte choosePiece,toPiece;

    public ChessRecord(Point from,Point to,byte fromPiece,byte toPiece){
        setFrom(from);
        setTo(to);
        setChoosePiece(fromPiece);
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

    public byte getChoosePiece() {
        return choosePiece;
    }

    public void setChoosePiece(byte fromPiece) {
        this.choosePiece = fromPiece;
    }

    public byte getToPiece() {
        return toPiece;
    }

    public void setToPiece(byte toPiece) {
        this.toPiece = toPiece;
    }
}
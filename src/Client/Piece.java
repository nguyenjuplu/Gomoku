/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.awt.Color;
import java.awt.Rectangle;

/**
 * The pieces that player places as well as the piece that appears when hovering
 * a spot. Each hold a color to determine who placed the piece. The hover piece 
 * will hold reference to its x and y coordinates to validate when it needs to 
 * be redrawn on the board.
 * @author John
 */
public class Piece{
    private Color pieceColor;
    private int x;
    private int y;
    
    public Piece(Color pieceColor) {
        this.pieceColor = pieceColor;
    }

    //Only used for hoverPiece
    public void setX(int x) {
        this.x = x;
    }
    
    //Only used for hoverPiece
    public void setY(int y) {
        this.y = y;
    }
    
    //Return the team
    public Color getColor(){
        return pieceColor;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
}

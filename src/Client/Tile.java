/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Tiles that populate the board. Each one is represented by a cross section on 
 * the board and can hold a single piece inside of it.
 * @author John
 */
public class Tile extends Rectangle
{

    private Piece piece;
    private int row;
    private int col;
    private Board board;

    public Piece getPiece()
    {
        return piece;
    }

    public void setPiece(Piece piece)
    {
        this.piece = piece;
    }

    public boolean hasPiece()
    {
        return piece != null;
    }

    public Tile(int tile_size, int row, int col)
    {

        //Set location and size
        this.row = row;
        this.col = col;
        
        updateSize(tile_size);        
    }

    public void updateSize(int tile_size){

        x = row * tile_size;
        y = col * tile_size;

        width = tile_size;
        height = tile_size;
    }
}

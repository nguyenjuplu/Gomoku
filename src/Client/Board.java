/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * The board on which players play the game. Consists of BOARD_SIZE * BOARD_SIZE
 tiles, each one is tile_size * tile_size pixels. The player can click on a
 single tile and place a piece there. Once placed it cannot be removed. There
 is also a hoverPiece that displays what a users move will look like. This is
 updated as the user moves their mouse across the screen. Additionally, there is
 a method commented out that allows for the users move to only be previewed
 when they click and drag. To implement this, change the mouseMoved method to
 mouseDragged and uncomment the mousePressed method. The class also displays
 opponent moves on the board in player2Color.
 *
 * @author John
 */
public class Board extends JComponent
{

    public int tile_size;

    private Color player1Color;
    private Color player2Color;

    private static final Color HOVER_COLOR = Color.LIGHT_GRAY;
    private static final Color FILL_COLOR = Color.WHITE;
    private static final Color LINE_COLOR = Color.BLACK;
    private final Tile[][] tiles;

    private BufferedImage boardImage;
    private BufferedImage whiteImage;
    private BufferedImage blackImage;
    private BufferedImage hoverImage;

    public static final String BOARD_IMAGE_PATH = "src/Client/Images/GameBoard.png";
    public static final String WHITE_IMAGE_PATH = "src/Client/Images/whitePiece.png";
    public static final String BLACK_IMAGE_PATH = "src/Client/Images/blackPiece.png";
    public static final String HOVER_IMAGE_PATH = "src/Client/Images/hoverPiece.png";

    /**
     * The last mouse x position
     */
    private int prevMouseX;
    /**
     * The last mouse y position
     */
    private int prevMouseY;
    
    private MouseAdapter mouseHandler;

    public Color getPlayer1Color()
    {
        return player1Color;
    }

    public Color getPlayer2Color()
    {
        return player2Color;
    }
    
    public int getTileSize(){
        return tile_size;
    }

    public int getBoardSize() {
        return boardSize;
    }
    
    /**
     * The view object
     */
    private GameView view;

    /**
     * The size of the board
     */
    private int boardSize;

    /**
     * Creates a game board
     * @param view the view containing the board
     * @param player1Color player 1's color
     * @param player2Color player 2's color
     * @param boardSize the size of the board
     */
    public Board(GameView view, Color player1Color, Color player2Color, int boardSize)
    {
        tile_size = 30;
        tiles = new Tile[boardSize][boardSize];
        this.view = view;
        this.player1Color = player1Color;
        this.player2Color = player2Color;
        this.boardSize = boardSize;
        
        //Set Size
        this.setPreferredSize(getBoardDimension());
        
        revalidate();
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                tiles[x][y] = new Tile(tile_size, x, y);
            }
        }
        view.repaint();
        prevMouseX = -1;
        prevMouseY = -1;

        mouseHandler = new MouseAdapter()
        {
            public void mouseReleased(MouseEvent me)
            {
                int x = me.getX() / tile_size;
                int y = me.getY() / tile_size;
                GameController c = view.getController();
                c.makeMove(x, y);
                // TODO: Highlight the last move

            }

            public void mouseMoved(MouseEvent me)
            {

                int x = me.getX() / tile_size;
                int y = me.getY() / tile_size;
                if (prevMouseX == x && prevMouseY == y) {
                    return;
                }

                try{
                    if (prevMouseX != -1 && prevMouseY != -1
                            && tiles[prevMouseX][prevMouseY].hasPiece()
                            && tiles[prevMouseX][prevMouseY].getPiece().getColor() == HOVER_COLOR) {
                        tiles[prevMouseX][prevMouseY].setPiece(null);
                        drawTile(prevMouseX, prevMouseY, (Graphics2D) getGraphics());
                    }

                    prevMouseX = x;
                    prevMouseY = y;
                    synchronized (tiles) {
                        if (!tiles[x][y].hasPiece()) {
                            tiles[x][y].setPiece(new Piece(HOVER_COLOR));
                            drawTile(x, y, (Graphics2D) getGraphics());
                        }
                    }
                }catch(ArrayIndexOutOfBoundsException e){
                    //When the user moves their cursor off the screen
                }

            }
        };

        boardImage = null;
        
        try {
            boardImage = ImageIO.read(new File(BOARD_IMAGE_PATH));
            whiteImage = ImageIO.read(new File(WHITE_IMAGE_PATH));
            blackImage = ImageIO.read(new File(BLACK_IMAGE_PATH));
            hoverImage = ImageIO.read(new File(HOVER_IMAGE_PATH));
        } catch (IOException e) {
            // TODO: Properly handle the exception
        }
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);

    }

    /**
     * Returns the color for the player or the hover color if the player is
     * invalid
     *
     * @param player the player
     * @return the player's color
     */
    private Color colorFor(int player)
    {
        switch (player) {
            case GameModel.PLAYER_1_FLAG:
                return player1Color;
            case GameModel.PLAYER_2_FLAG:
                return player2Color;
            default:
                return HOVER_COLOR;
        }
    }

    /**
     * Draws a move within the board
     *
     * @param x the x coordinate of the move
     * @param y the y coordinate of the move
     * @param player the player flag of the player making the move
     */
    public void drawMove(int x, int y, int player)
    {
        synchronized (tiles) {
            tiles[x][y].setPiece(new Piece(colorFor(player)));
        }
        repaint();
    }

    /**
     * Draws a tile
     *
     * @param x the x coordinate of the tile
     * @param y the y coordinate of the tile
     * @param g2 the graphics to use for drawing
     */
    private void drawTile(int x, int y, Graphics2D g2)
    {
        // Fill the tile
        if (boardImage != null) {
            int x1 = x * tile_size;
            int y1 = y * tile_size;
            int x2 = x1 + tile_size;
            int y2 = y1 + tile_size;
            g2.drawImage(boardImage, x1, y1, x2, y2, x1, y1, x2, y2, null);
        } else {
            // Placeholder in case the board image could not be loaded
            g2.setColor(FILL_COLOR);
            g2.fillRect(x * (tile_size), y * (tile_size), tile_size, tile_size);
        }
        g2.setColor(LINE_COLOR);
        // Vertical line
        g2.drawLine(x * (tile_size) + (tile_size / 2), y * tile_size, x * (tile_size) + (tile_size / 2), y * (tile_size) + (tile_size));
        // Horizontal line
        g2.drawLine(x * (tile_size), y * (tile_size) + (tile_size / 2), x * (tile_size) + (tile_size), y * (tile_size) + (tile_size / 2));
        // Draw the piece
        synchronized (tiles) {
            if (tiles[x][y].hasPiece()) {
                
                Color read = tiles[x][y].getPiece().getColor();
                if(read==player1Color){
                    g2.drawImage(whiteImage, (int)tiles[x][y].getX(), (int)tiles[x][y].getY(), (tile_size), (tile_size), null);
                }
                else if(read==player2Color){
                    g2.drawImage(blackImage, (int)tiles[x][y].getX(), (int)tiles[x][y].getY(), (tile_size), (tile_size), null);
                }
                else if(read==HOVER_COLOR){                
                    g2.drawImage(hoverImage, (int)tiles[x][y].getX(), (int)tiles[x][y].getY(), (tile_size), (tile_size), null);
                }
                else if(read==Color.RED){
                    g2.setColor(Color.RED);
                    g2.fillOval( (int)tiles[x][y].getX(), (int)tiles[x][y].getY(), (tile_size), (tile_size));
                }

            }
        }

    }

    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g);
        //Populate board with tiles
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                drawTile(x, y, g2);
            }
        }

        repaint();
    }
    
    /**
     * Returns the dimension of the board
     * @return the dimension of the board
     */
    public Dimension getBoardDimension()
    {
        return new Dimension(tile_size*boardSize, tile_size*boardSize);
    }

    /**
     * Sets the tile size
     * @param value the new tile size
     */
    public void setTileSize(int value) {
        tile_size = value;
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                tiles[x][y].updateSize(tile_size);
            }
        }
        this.setPreferredSize(getBoardDimension());
        revalidate();
        repaint();
    }
    
    
    public void gameDone(int x, int y, boolean win){
        removeMouseListener(mouseHandler);
        removeMouseMotionListener(mouseHandler);
        
        if( x != -1 && y != -1){
            synchronized (tiles) {
                tiles[x][y].setPiece(new Piece(Color.RED));
            }
        }
        repaint();        
    }

}

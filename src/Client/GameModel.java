package Client;

import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jeremy
 * 
 * Edited 5/4/2018 by Jimmy Nguyen: Added a helper method to be called by 
 * NetworkPlayer which could then pass on a Win-case for when another
 * user disconnects from the game. Essentially, it is a short circuit
 * win caused by receiving a resign message from the other user. More 
 * detail of the procedure can be seen in the new method displayWin();
 * EDIT: 5.9.18 Jeremy Pallwitz - Added WinSender functionality, changed 
 * constructor to take in opponent string and lobby model, added checkDraw()
 */
public class GameModel
{

    // Game over results
    /**
     * Result flag for a win
     */
    public static final int RESULT_WIN = 1;
    /**
     * Result flag for a loss
     */
    public static final int RESULT_LOSS = -1;
    /**
     * Result flag for a draw
     */
    public static final int RESULT_DRAW = 0;

    // Tile flags
    /**
     * Flag for empty tile
     */
    public static final int EMPTY_FLAG = 0;
    /**
     * Flag for player 1's tile
     */
    public static final int PLAYER_1_FLAG = 1;
    /**
     * Flag for player 2's tile
     */
    public static final int PLAYER_2_FLAG = 2;

    /**
     * Flag for draw results
     */
    public static final int DRAW_FLAG = 0;

    /**
     * Flag for unknown players
     */
    public static final int UNKNOWN_PLAYER_FLAG = -1;

    /**
     * The size of the regin checked when checking for wins
     */
    private static final int REGION_SIZE = 9;

    /**
     * Offset where to star the region for checking for wins
     */
    private static final int REGION_START = -4;

    /**
     * Offset where to stop the region for checking for wins
     */
    private static final int REGION_STOP = 4;

    private PlayerController player1;
    private PlayerController player2;
    public boolean winSent;
    /**
     * A matrix of ints storing flags to indicate which tiles are occupied by
     * which player's pieces
     */
    private int[][] board;
    /**
     * Whose turn it is (Use %1 or %2)
     */
    private int turn;
    /**
     * String to keep track of Opponent for win sending
     */
    private String opponent;
    /**
     * LobbyModel to send win when game is over
     */
    private LobbyModel winSend;
    /**
     * Incremented every turn
     */
    private int moves;

    /**
     * When to stop making moves and declare the game a draw
     */
    private int maxTurn;

    //TODO Later: Implement time
    /**
     * Creates a GameModel with the specified board size
     *
     * @param size the size of the board
     * @param p1 player 1's controller
     * @param p2 player 2's controller
     */
    public GameModel(int size, PlayerController p1, PlayerController p2, LobbyModel lmodel,  String opp)
    {
        opponent = opp;
        winSend = lmodel;
        player1 = p1;
        player2 = p2;
        turn = 0;
        moves = 0;
        winSent = false;
        maxTurn = size * size;
        board = new int[size][size];
        System.out.println("Constructor: " + player1);
        System.out.println("Constructor: " + player2);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = EMPTY_FLAG;
            }
        }
        p1.yourTurn();
    }

    /**
     * Returns the player controller for the given player
     *
     * @param player the player flag
     * @return the player's player controller
     */
    private PlayerController playerControllerFor(int player)
    {
        switch (player) {
            case PLAYER_1_FLAG:
                return player1;
            case PLAYER_2_FLAG:
                return player2;
            default:
                return null;
        }
    }

    /**
     * Retrieves the player flag for the given player controller
     *
     * @param player the player controller
     * @return the player flag
     */
    public int playerFlagFor(PlayerController player)
    {
        if (player == player1) {
            return PLAYER_1_FLAG;
        } else if (player == player2) {
            return PLAYER_2_FLAG;
        } else {
            return UNKNOWN_PLAYER_FLAG;
        }
    }

    /**
     * Makes a move Just commented out the validateMove for now
     *
     * @param row
     * @param col
     * @param player
     * @return
     */
    public boolean makeMove(int row, int col, PlayerController player)
    {
        if(checkDraw(turn)){
            winSend.sendDraw(opponent);
            player1.gameOver(RESULT_DRAW);
            player2.gameOver(RESULT_DRAW);
        }
        if (turn % 2 + 1 != playerFlagFor(player)) {
            return false;
        }
        if (validateMove(row, col)) {
            turn++;
            int pFlag = playerFlagFor(player);
           
            board[row][col] = pFlag;
            player1.moveMade(row, col, pFlag);
            player2.moveMade(row, col, pFlag);
            if (checkWin(row, col, pFlag)) {
                   
                    if(!winSent && player1 instanceof GameController){
                         win(pFlag);
                        winSend.sendWin(opponent);
                    } 
                    else if( !winSent && player1 instanceof NetworkPlayer){
                         win(pFlag);
                    }
                    
                    player1.model.setWinSent(true);
                    player2.model.setWinSent(true);
                    
                
                
            }           
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if the move is valid and able to be placed by the user.
     *
     * @param x the row
     * @param y the column
     * @return true if move is valid, false if not
     */
    public boolean validateMove(int x, int y)
    {
        try {
            return (board[x][y] == EMPTY_FLAG);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Retrieves the tile at the indicated position
     *
     * @param x the row
     * @param y the column
     * @return the flag of the corresponding tile or EMPTY_FLAG if the tile is
     * out of bounds
     */
    public int getTile(int x, int y)
    {
        try {
            return board[x][y];
        } catch (ArrayIndexOutOfBoundsException e) {
            return EMPTY_FLAG;
        }
    }

    //Board has no piece there, move is in bounds
    /**
     * Check for win on board
     *
     * @param x The Row
     * @param y The Column
     * @param player the player checking for wins
     * @return true if there is a win
     */
    public boolean checkWin(int x, int y, int player)
    {
        int[][] area = new int[REGION_SIZE][REGION_SIZE];
        // Copy region of board to do win checking
        for (int i = REGION_START; i <= REGION_STOP; i++) {
            for (int j = REGION_START; j <= REGION_STOP; j++) {
                area[i - REGION_START][j - REGION_START] = getTile(x + i, y + j);
            }
        }
        
        //Check Horizontal
        int count = 0;
        for (int i = 0; i < REGION_SIZE; i++) {
            if (area[4][i] == player) {
                count++;
            } else {
                count = 0;
            }
            if (count == 5) {
                return true;
            }
        }
        //Check Vertical
        count = 0;
        for (int i = 0; i < REGION_SIZE; i++) {
            if (area[i][4] == player) {
                count++;
            } else {
                count = 0;
            }
            if (count == 5) {
                return true;
            }
        }
        //Check Diagonal '/'
        count = 0;
        for (int i = 0; i < REGION_SIZE; i++) {
            if (area[REGION_SIZE - 1 - i][i] == player) {
                count++;
            } else {
                count = 0;
            }
            if (count == 5) {
                return true;
            }
        }
        //Check Diagonal '\'
        count = 0;
        for (int i = 0; i < REGION_SIZE; i++) {
            if (area[i][i] == player) {
                count++;
            } else {
                count = 0;
            }
            if (count == 5) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sends a win to the players
     *
     * @param winner the winning player's flag
     */
    private void win(int winner)
    {
        switch (winner) {
            case PLAYER_1_FLAG:
                player1.gameOver(RESULT_WIN);
                player2.gameOver(RESULT_LOSS);
                winSend.close();
                break;
            case PLAYER_2_FLAG:
                player1.gameOver(RESULT_LOSS);
                player2.gameOver(RESULT_WIN);
                
                winSend.close();
                break;
        }
    }

    /**
     * Resigns for a given player
     *
     * @param player the player resigning
     */
    public void resign(PlayerController player)
    {
        if(player instanceof GameController){
            winSend.sendWin(opponent);
            setWinSent(false);
        }
        if (player1 == player) {
            win(PLAYER_2_FLAG);
        } else if (player2 == player) {
            win(PLAYER_1_FLAG);
        }
    }
    
    /**
     * Helper method for when the other user has resigned and this user
     * receives a win message while NOT on their turn.
     * 
     * This method is specifically used in a short-circuit case when the
     * other user has resigned.
     * 
     * This method is called from the NetworkPlayer and calls the
     * GameController to call the GameView to display a win message
     */
    public void displayWin()
    {
        if(player1 instanceof GameController){
            player1.gameOver(1);
        } else {
            player2.gameOver(1);
        }
    }
    
    public void setTurn(PlayerController player){
        if(player1 instanceof GameController){
            player1.yourTurn();
        }
    }

    private boolean checkDraw(int t) {
        int size = board.length;
        size = size * size;
        if(turn == size)
            return true;
        else
            return false;
    }
    
   
    public void setWinSent(boolean ws){
        winSent = ws;
    }
    public void setDraw(){
        if(player1 instanceof GameController){
            player1.gameOver(RESULT_DRAW);
        }else
            player2.gameOver(RESULT_DRAW);
    }
}

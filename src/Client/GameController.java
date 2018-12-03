package Client;

/**
 * GameController class Created when the peer-to-peer connection is made and is
 * used to establish communication between the GameView and the GameModel. The
 * Controller's responsibility is to relay move commands from the View to the
 * Model and also notify the Model/View if the game is ever finished or if one
 * user resigned/disconnected.
 * 
 * Edited 4/30 by Jimmy Nguyen: Updated comments for methods to allow users to 
 *      read the flow easily. MakeMove(...) is currently unfinished because
 *      it no longer needs to return a boolean since gameView checks if the
 *      move is possible.
 *
 * @author JimmyTN
 */
public class GameController extends PlayerController
{
    /**
     * The 2D array of the board, 1 means taken and 0 means empty
     * Not implemented anymore *
     */
    private int[][] board;
    
    /**
     * The size of the board (Number of rows & columns)
     */
    private int size;
    
    /**
     * The view that the user sees, one per game
     */
    private final GameView gameView;

    /**
     * The last move's X coordinate
     */
    private int lastMoveX;
    
    /**
     * The last move's Y coordinate
     */
    private int lastMoveY;
    
    /**
     * The last player to make a move
     */
    private int lastMoveP;

    /**
     * Default constructor, needs a board size specified to create the board for
     * the game.
     * Instantiates last moves as -1 since there has been no last move.
     * The gameView is also created.
     *
     * @param size The number of rows and columns in the board
     */
    public GameController(int size)
    {
        newGame(size);
        gameView = new GameView(this, size);
        myTurn = true;
        lastMoveX = -1;
        lastMoveY = -1;
        lastMoveP = -1;
    }
    
    /**
     * Returns the gameView that this game is representing
     * @return the View that the user sees for this game
     */
    public GameView getView()
    {
        return gameView;
    }

    /**
     * The last move's X coordinate
     * @return An int that represents the X value
     */
    public int getLastMoveX()
    {
        return lastMoveX;
    }

    /**
     * The last move's Y coordinate
     * @return An int that represents the Y value
     */
    public int getLastMoveY()
    {
        return lastMoveY;
    }

    /**
     * The last player to make a move
     * @return An int that represents which player last moved
     */
    public int getLastMoveP()
    {
        return lastMoveP;
    }

    /**
     * Instantiates the size of the board and the board to be of a certain size
     *
     * @param size The size of the board for the game
     */
    private void newGame(int size)
    {
        this.size = size;
        board = new int[size][size];
    }

    /**
     * Is called by the GameView/BoardView The game is passed an x and y
     * coordinate to indicate that the player has specified a need to place a
     * piece at the certain location. This then calls the makeMove inside the
     * GameModel, along with THIS passed.
     *
     * @param x The x coordinate for the piece to be placed
     * @param y The y coordinate for the piece to be placed.
     */
    public void makeMove(int x, int y)
    {
        boolean tryMove = model.makeMove(x, y, this);
        myTurn = false;
        gameView.setTurn(myTurn);

        if (!tryMove) 
        {   //if the spot is filled, tell the view to notify the user

        } else {    //The spot is empty, Display the move to the User!

        }
    }

    /**
     * This is called when the User has made or received a move. The board is
     * updated with the spot specified being replaced with a playerFlag.
     *
     * @param x The x coordinate of the piece to update
     * @param y The y coordinate of the piece to update
     * @param playerFlag The int depending on which player made the move
     */
    @Override
    public void moveMade(int x, int y, int playerFlag)
    {
        lastMoveX = x;
        lastMoveY = y;
        lastMoveP = playerFlag;
        gameView.drawMove(x, y, playerFlag);
        myTurn = true;
        this.setViewTurn(myTurn);
    }

    /**
     * Relay to the gameView that the game has ended with the appropriate 
     * game result
     * 
     * @param resultFlag An int to represent who has won
     *      1 == This user has won
     *      -1 == This user has lost
     *      0 == The game ended in a draw
     */
    @Override
    public void gameOver(int resultFlag)
    {
        switch (resultFlag) {
            case 1:
                gameView.displayWin(lastMoveX, lastMoveY);
                System.out.println("Game Over Win");
                break;
            case -1:
                gameView.displayLoss(lastMoveX, lastMoveY);
                System.out.println("GameOver Lose");
                
                break;
            case 0:
                gameView.displayDraw(lastMoveX, lastMoveY);
                System.out.println("GameOver Draw");
                break;
            default:
                System.out.println("Invalid number has been passed to GameController's gameOver()");
                break;
        }
    }

    /**
     * If the user clicks quit from the View, the Controller sends a resign
     * message to the Model by calling model's resign() method.
     */
    public void quitClicked()
    {
        model.resign(this);
    }

    /**
     * Called from gameView or gameModel. Returns the boolean value of myTurn.
     * 
     * If it is not your turn, it returns false.
     * If it is your turn, it returns true.
     * @return 
     */
    public boolean getMyTurn()
    {
        return myTurn;
    }
    
    public void endGame()
    {
        //TODO: close sockets and stuff
    }
    
    /**
     * This method can be called from the Model to tell the view whether or not
     * it is the player's turn. Essentially a helper method.
     * 
     * @param myOrOpp 
     */
    public void setViewTurn(boolean myOrOpp)
    {
        gameView.setTurn(myOrOpp);
    }

    /**
     * Overriding the yourTurn method in PlayerController to also be able to
     * update the view with whose turn it is.
     */
    @Override
    public void yourTurn()
    {
        myTurn = true;
        setViewTurn(myTurn);
    }
 

    @Override
    public void setModel(GameModel m) {
        model = m;
    }
}

package Client;

/**
 *
 * @author JimmyTN
 * This is an abstract class to be implemented by the other 
 * classes used in the gamePlay (Network, Controller, AI).
 * The class has reference to the model of the game and classes
 * that will execute messages of gameplay.
 */
public abstract class PlayerController {
    
    //Fields
    protected GameModel model;    //The model to connect and send information to
    boolean myTurn = true;
    
    //Methods to implement
    
    /**
     * Must be implemented. Each class must handle receiving
     * a move that was made.
     * @param x
     * @param y
     * @param playerFlag 
     */
    public abstract void moveMade(int x, int y, int playerFlag);
    
    /**
     * Notifies the user that the game is over
     * Called from the GameModel and notifies the View of who won
     * @param resultFlag An int that represents who won 
     *         1 == user won
     *         -1 == user lost
     *         0 == draw
     */
    public abstract void gameOver(int resultFlag);
    
    /**
     * Sets the model for this game
     * @param m the game model
     */
    public void setModel(GameModel m)
    {
        this.model = m;
    }
    
    /**
     * Called from the Controllers and tells the GameView/BoardView
     * that it is the User's turn
     */
    public void yourTurn(){
        myTurn = true;
    }
}

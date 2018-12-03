package Client;

import java.util.ArrayList;

/**
 *  This is the AIPlayerController.
 *  The class is yet to be implemented still. It will be fully functional
 *      by Iteration 4.
 * @author JimmyTN
 */


/**
 * Okay so I used this document exclusively: https://blog.theofekfoundation.org/artificial-intelligence/2015/12/11/minimax-for-gomoku-connect-five/
 * 
 * I would suggest fully reading it to understand whats going on.
 * What I have working is everything for a single depth (easy mode) game except
 * for the score assessment of the horizontal, vertical and diagonals.
 * 
 * Right now, it does not compute an accurate score for vertical and I would 
 * suggest working off of the code in the document above. Unfortunately, that
 * does not have the complete method since we must also account for the other
 * players score and subtract it. You can see initial attempts at that below, 
 * though I don't believe they are correct. Additionally, we should try and find
 * some way to make the first move that is made by the AI to be put as close to
 * the center as possible. Let me know if you have questions.
 * 
 * @author John
 */


public class AIPlayerMedium extends AI{


    /**
     * 
     * @param size
     * @param difficulty 
     */
    public AIPlayerMedium(int size){
        board = new int[size][size];
        this.size = size;
    }
        
    /**
     * Analyzes the total score of a potential move
     * @param move an int[] containing the x coordinate, the y coordinate,
     * and the score (defaults to 0)
     * @return the score
     */
    private int analyze(int row, int col) {
    
        int aiPoints = Integer.max(vertical(col, model.playerFlagFor(this), row, true), horizontal(row, model.playerFlagFor(this), col, true));
        int playerPoints = Integer.max(vertical(col, getOpponentFlag(model.playerFlagFor(this)), row, false), horizontal(row, getOpponentFlag(model.playerFlagFor(this)), col, false)); 

        return aiPoints + playerPoints;

        //return upLeftDiagonal(row, col, model.playerFlagFor(this), true);
        
        //return vertical(col, getOpponentFlag(), row, false);

    }
    
    
    /**
     * Evaluates score of a potential move
     * @return 
     */
    private int evaluate(int consecutive, int openEnds, boolean currentTurn){
        if( openEnds == 0 )
            return 0;
        switch(consecutive){
            case 4:
                switch(openEnds){
                    case 1:
                        if(currentTurn)
                            return 100000000;
                        return 50;
                    case 2:
                        if(currentTurn)
                            return 100000000;
                        return 500000;
                }
            case 3:
                switch (openEnds){
                    case 1:
                        if(currentTurn)
                            return 14;
                        return 10;
                    case 2:
                        if (currentTurn)
                            return 10000;
                        return 50;
                }
            case 2:
                switch (openEnds) {
                    case 1:
                        return 4;
                    case 2:
                        return 10;
                }
            case 1:
                switch (openEnds) {
                    case 1:
                        return 1;
                    case 2:
                        return 2;
                }
            default:
                return 200000000;
        }
    }
    
    /**
     * The vertical score
     * @param x x coordinate of move
     * @return Total vertical score
     */
    private int vertical(int col, int playerFlag, int moveRow, boolean currentTurn){
        int consecutive = 0;
        int openEnds = 0;
        int score = 0;
        int blocking = 0;
        
            for(int row = 0; row < size; row++){
                //If the tile has AI piece in it
                if( board[row][col] == playerFlag){
                    consecutive++;
                    //System.out.println("board[" + row + "][" + col + "]" + "own piece");
                }
                //If the tile to the right has an opponents piece in it
                else if( board[row][col] == getOpponentFlag(playerFlag) && consecutive > 0 ){
                    if(openEnds == 0)
                        consecutive = 0;

                    //System.out.println("board[" + row + "][" + col + "]" + "blocking on right");
                }
                //If there is an opponents piece on the left
                else if( board[row][col] == getOpponentFlag(playerFlag)){
                    if(moveRow < row)
                        blocking++;
                    openEnds = 0;
                    consecutive = 0;
                    //System.out.println("board[" + row + "][" + col + "]" + "blocking on left");
                }
                //If tile piece is empty and AI pieces are in a row
                else if( board[row][col] == 0 && consecutive > 0 ){
                    openEnds++;
                    score += evaluate(consecutive, openEnds, currentTurn);
                    consecutive = 0;
                    openEnds = 1;
                    //System.out.println("board[" + row + "][" + col + "]" + "empty + reset");
                }
                //If tile is empty
                else if( board[row][col] == 0 ){
                    openEnds = 1;
                    //System.out.println("board[" + row + "][" + col + "]" + "empty");
                }
                else if( consecutive > 0 ){
                    score += evaluate(consecutive, openEnds, currentTurn);
                    consecutive = 0;
                    openEnds = 0;
                    //System.out.println("board[" + row + "][" + col + "]" + "consecutive");
                }
                else{
                    openEnds = 0;
                    //System.out.println("board[" + row + "][" + col + "]" + "other");
                }
                    
            }
            if (blocking > 0){
                //System.out.println("board[][" + col + "]" + "blocked");
                return 0;
            }
            if (consecutive > 0){
                score += evaluate(consecutive, openEnds, currentTurn);
                consecutive = 0;
                openEnds = 0;
            }
        
        return score;
    }
    
    /**
     * The score for horizontal moves
     * @param x The x param of a move
     * @return The total (sum) horizontal score
     */
    private int horizontal(int row, int playerFlag, int moveCol, boolean currentTurn){
        int consecutive = 0;
        int openEnds = 0;
        int score = 0;
        int blocking = 0;
        
            for(int col = 0; col < size; col++){
                //If the tile has AI piece in it
                if( board[row][col] == playerFlag){
                    consecutive++;
                    //System.out.println("board[" + row + "][" + col + "]" + "own piece");
                }
                //If the tile to the right has an opponents piece in it
                else if( board[row][col] == getOpponentFlag(playerFlag) && consecutive > 0 ){
                    if(openEnds == 0)
                        consecutive = 0;

                    //System.out.println("board[" + row + "][" + col + "]" + "blocking on right");
                }
                //If there is an opponents piece on the left
                else if( board[row][col] == getOpponentFlag(playerFlag)){
                    if(moveCol < col)
                        blocking++;
                    openEnds = 0;
                    consecutive = 0;
                    //System.out.println("board[" + row + "][" + col + "]" + "blocking on left");
                }
                //If tile piece is empty and AI pieces are in a row
                else if( board[row][col] == 0 && consecutive > 0 ){
                    openEnds++;
                    score += evaluate(consecutive, openEnds, currentTurn);
                    consecutive = 0;
                    openEnds = 1;
                    //System.out.println("board[" + row + "][" + col + "]" + "empty + reset");
                }
                //If tile is empty
                else if( board[row][col] == 0 ){
                    openEnds = 1;
                    //System.out.println("board[" + row + "][" + col + "]" + "empty");
                }
                else if( consecutive > 0 ){
                    score += evaluate(consecutive, openEnds, currentTurn);
                    consecutive = 0;
                    openEnds = 0;
                    //System.out.println("board[" + row + "][" + col + "]" + "consecutive");
                }
                else{
                    openEnds = 0;
                    //System.out.println("board[" + row + "][" + col + "]" + "other");
                }
                    
            }
            if (blocking > 0){
                //System.out.println("board[][" + col + "]" + "blocked");
                return 0;
            }
            if (consecutive > 0){
                score += evaluate(consecutive, openEnds, currentTurn);
                consecutive = 0;
                openEnds = 0;
            }
        
        return score;
    }
    
    /**
     * The score for Diagonal from UpperRight to LowerLeft
     *            *
     *             *   UpperHalf
     *              *      like so
     *               *
     *  Bottom Half   *
     * @param x The x coordinate for a move
     * @param y The y coordinate for a move
     * @return 
     */
    private int upLeftDiagonal(int moveRow, int moveCol, int playerFlag, boolean currentTurn){
        int consecutive = 0;
        int openEnds = 0;
        int score = 0;
        int blocking = 0;
        
        int bound;
        boolean boundRow = false;
        int maxIndex;
        int boardValue;
        
        //check which dimension limits it
        if( moveRow < moveCol ){
            bound = moveRow;
            boundRow = true;
        }
        else{
            bound = moveCol;
        }
        
        //check total necessary iterations
        if(bound % 2 == 0)
            maxIndex = 2 * bound;
        else
            maxIndex = (2 * bound) -1;
        
        //Begin traversal
        for(int i = 0; i < maxIndex; i++){
            
            //Get value in diagonal
            if(boundRow)
                boardValue = board[i][i + moveCol - bound];
            else
                boardValue = board[i + moveRow - bound][i];
            
            
         //If the tile has AI piece in it
            if( boardValue == playerFlag){
                consecutive++;
                System.out.println("own piece");
            }
            //If the tile to the right has an opponents piece in it
            else if( boardValue == getOpponentFlag(playerFlag) && consecutive > 0 ){
                if(openEnds == 0)
                    consecutive = 0;

                System.out.println("blocking on right");
            }
            //If there is an opponents piece on the left
            else if( boardValue == getOpponentFlag(playerFlag)){

                if(moveRow > i && moveCol > i)
                    return 0;

                openEnds = 0;
                consecutive = 0;
                System.out.println("blocking on left");
            }
            //If tile piece is empty and AI pieces are in a row
            else if( boardValue == 0 && consecutive > 0 ){
                score += evaluate(consecutive, openEnds, currentTurn);
                consecutive = 0;
                openEnds = 1;
                System.out.println("empty + reset");
            }
            //If tile is empty
            else if( boardValue == 0 ){
                openEnds = 1;
                System.out.println("empty");
            }
            else if( consecutive > 0 ){
                score += evaluate(consecutive, openEnds, currentTurn);
                consecutive = 0;
                openEnds = 0;
                //System.out.println("board[" + row + "][" + col + "]" + "consecutive");
            }
            else{
                openEnds = 0;
                //System.out.println("board[" + row + "][" + col + "]" + "other");
            }

        }
        if (blocking > 0){
            //System.out.println("board[][" + col + "]" + "blocked");
            return 0;
        }
        if (consecutive > 0){
            score += evaluate(consecutive, openEnds, currentTurn);
            consecutive = 0;
            openEnds = 0;
        }

        return score;          
    }
    
    
    /**
     * The score for Diagonal from UpperRight to LowerLeft
     * IMPORTANT NOTE: THIS METHOD STILL STARTS AT ORIGIN (0,0)
     *            *
     * Upper Half*   
     *          *      like so
     *         *
     *        *  Bottom Half
     * @param x The x coordinate for a move
     * @param y The y coordinate for a move
     * @return 
     */
    private int upRightDiagonal(int moveRow, int moveCol, int playerFlag, boolean currentTurn){
        int consecutive = 0;
        int openEnds = 0;
        int score = 0;
        int blocking = 0;
        
        int bound;
        boolean boundRow = false;
        int boardValue;
        
        //check which dimension limits it
        if( moveRow < (size-moveCol) ){
            bound = moveRow;
            boundRow = true;
        }
        else{
            bound = size-moveCol;
        }
        

        
        //Begin traversal
        for(int i = size-1; i > 0; i--){
            
            //Get value in diagonal
            if(boundRow)
                boardValue = board[i][i + moveCol - bound];
            else
                boardValue = board[i + moveRow - bound][i];
            
            
         //If the tile has AI piece in it
            if( boardValue == playerFlag){
                consecutive++;
                System.out.println("own piece");
            }
            //If the tile to the right has an opponents piece in it
            else if( boardValue == getOpponentFlag(playerFlag) && consecutive > 0 ){
                if(openEnds == 0)
                    consecutive = 0;

                System.out.println("blocking on right");
            }
            //If there is an opponents piece on the left
            else if( boardValue == getOpponentFlag(playerFlag)){

                if(moveRow > i && moveCol > i)
                    blocking++;

                openEnds = 0;
                consecutive = 0;
                System.out.println("blocking on left");
            }
            //If tile piece is empty and AI pieces are in a row
            else if( boardValue == 0 && consecutive > 0 ){
                score += evaluate(consecutive, openEnds, currentTurn);
                consecutive = 0;
                openEnds = 1;
                System.out.println("empty + reset");
            }
            //If tile is empty
            else if( boardValue == 0 ){
                openEnds = 1;
                System.out.println("empty");
            }
            else if( consecutive > 0 ){
                score += evaluate(consecutive, openEnds, currentTurn);
                consecutive = 0;
                openEnds = 0;
                //System.out.println("board[" + row + "][" + col + "]" + "consecutive");
            }
            else{
                openEnds = 0;
                //System.out.println("board[" + row + "][" + col + "]" + "other");
            }

        }
        if (blocking > 0){
            //System.out.println("board[][" + col + "]" + "blocked");
            return 0;
        }
        if (consecutive > 0){
            score += evaluate(consecutive, openEnds, currentTurn);
            consecutive = 0;
            openEnds = 0;
        }

        return score;  
    }
    
     /**
     * Returns an ArrayList containing all, non-taken moves
     * @return 
     */
    ArrayList<int[]> getAvailableMoves(){
        ArrayList<int[]> empty = new ArrayList<int[]>();
        
//        for(int row = 0; row < size; row++){
//            for(int col = 0; col < size; col++){
//                if(board[row][col] == 0){
//                    empty.add(new int[]{row,col,0});
//                }
//            }
//        }
        for(int row = 0; row < size; row++){
            for(int col = 0; col < size; col++){
                if(board[row][col] != 0){
                    addSurrounding(row, col, empty);
                }
            }
        }
        
        return empty;
    }
    
    private void addSurrounding(int initRow, int initCol, ArrayList<int[]> empty) {
        for(int row = -1; row < 2; row++){
            for(int col = -1; col < 2; col++){
                if((initRow + row < size && initRow + row > 0) && (initCol + col < size && initCol + col > 0)){
                    if (board[initRow + row][initCol + col] == 0) {
                        empty.add(new int[]{initRow + row, initCol + col, 0});
                    }
                }
            }
        }
    }
    
    /**
     * Finds the best scoring move possible
     * @return the best move
     */
    int[] predictHelper(){
        int[] topScore = new int[3];
        int[] singleScore = new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE};
        topScore = singleScore;
        //topscore = check4inarow
        //if topscore!=null
            //return topscore;
        for(int[] move : (ArrayList<int[]>) availableMoves){
            singleScore = new int[]{move[0], move[1], analyze(move[0], move[1])};
            System.out.println();
            if(topScore[2] < singleScore[2]){
                topScore = singleScore;
            }
        }

        System.out.println(topScore[0]+ ", " + topScore[1] + ", Score: " + topScore[2]);
        return topScore;
    }
    
}
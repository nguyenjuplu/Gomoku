package Client;

import java.util.ArrayList;

/**
 * This class is the medium version of the AI. It will trace the board and
 * try to connect five-in-a-row but also play defensively and block the User's
 * attempt at winning
 * 
 * @author JimmyTN
 */
public class AIPlayerHard extends AI {

    ArrayList<int[] > movesPlaced = new ArrayList<int[]>(50);
//    /**
//     * Constructor for AIPlayerHard
//     * Instantiates board with size that is passed
//     * @param size 
//     */
//    AIPlayerHard(int size) {
//        board = new int[size][size];
//        this.size = size;
//    }
//
//    @Override
//    int[] predictHelper() {
//        int[] move = null;
//        
//        //Check if there is threeInARow and make optimum move
//        move = checkNumInARow(3, move);
//            
//        //Check if there is fourInARow and make optimum move
//        move = checkNumInARow(4, move);
//        
//        //If neither of these happen, pick a random move
//        if(move == null){
//            for(int[] temp : (ArrayList<int[]>) availableMoves){
//                move = temp;
//                break;
//            }
//        }
//        
//        return move;
//    }
//    
//    /**
//     * Iterates over the set and checks if there are any combinations of
//     * four in a row. If there are, then the method returns a move to stop 
//     * the five-in-a-row win condition
//     * 
//     * @param limit An Integer that represents the number of pieces in a row to 
//     * check. For example, limit = 4 means check for FourInARow
//     * @return 
//     */
    int[] checkNumInARow(int limit){
        int range = limit - 1;
        int[] move = null;
        int count = 0;
        head:
        for(int[] temp : movesPlaced){
            //System.out.println("Iterate to next move");
            
            int tempX = temp[0];
            int tempY = temp[1];
            int tempPlayerFlag = temp[2];
            int blockX = 0;
            int blockY = 0;
            
            try{
                
            //Check Vertical
            for(int i = (0 - range) ; i < range; i++){
                //System.out.printf("Checking move: x %d, y %d, flag %d\n", tempX, tempY, tempPlayerFlag);
                
                if(board[tempX][tempY + i] == tempPlayerFlag){
                    //System.out.println(count);
                    blockX = tempX;
                    blockY = tempY + i;
                    count++;
                } else {
                    count = 0;
                }
                
                if(count == limit){ //There's <limit> in a row, block it!!
                    //System.out.printf("Blocking x %d, y %d count %d\n", blockX, blockY+1, count);
                    if(board[blockX][blockY + 1] == 0){   //Try one open end
                        move = new int[]{ blockX, blockY + 1, Integer.MAX_VALUE};
                        return move;
                    } else if(board[blockX][blockY - limit] == 0){    //Try other end
                        move = new int[]{blockX, blockY - limit, Integer.MAX_VALUE};
                        return move;
                    } else{         //You cannot win, so quit
                        move = null;
                    }
                    break;
                }
            }
            count = 0;
            
            //Check Horizontal
            for(int i = (0 - range) ; i < range; i++){
                if(board[tempX+i][tempY] == tempPlayerFlag){
                    blockX = tempX + i;
                    blockY = tempY;
                    count++;
                    //System.out.printf("Checking move: x %d, y %d, flag %d, count %d\n", tempX, tempY, tempPlayerFlag, count);
                } else {
                    count = 0;
                }
                if(count == limit){ //There's <limit> in a row, block it!!
                    if(board[blockX + 1][blockY ] == 0){   //Try one open end
                        move = new int[]{ blockX + 1, blockY, Integer.MAX_VALUE};
                        return move;
                    } else if(board[blockX - limit][blockY] == 0){    //Try other end
                        move = new int[]{blockX - limit, blockY, Integer.MAX_VALUE};
                        return move;
                    } else{         //You cannot win, so quit
                        move = null;
                    }
                    break;
                }
            }
            count = 0;
            
            //Check Diagonal "\"
            for(int i = (0 - range) ; i < range; i++){
                if(board[tempX + i][tempY + i] == tempPlayerFlag){
                    blockX = tempX + i;
                    blockY = tempY + i;
                    count++;
                } else {
                    count = 0;
                }
                if(count == limit){ //There's <limit> in a row, block it!!
                    if(board[blockX + 1][blockY + 1] == 0){   //Try one open end
                        //System.out.printf("Diagonal found for %d at x %d y %d\n", count, blockX, blockY);
                        move = new int[]{ blockX + 1, blockY + 1, Integer.MAX_VALUE};
                        //System.out.printf("Trying to block at x %d, y %d\n", blockX+1, blockY+1);
                        return move;
                    } else if(board[blockX - limit][blockY - limit] == 0){    //Try other end
                        move = new int[]{blockX - limit, blockY - limit, Integer.MAX_VALUE};
                        return move;
                    } else{         //You cannot win, so quit
                        move = null;
                    }
                    break;
                }
            }
            count = 0;
            
            //Check other Diagonal "/"
            for(int i = (0 - range) ; i < range; i++){
                if(board[tempX - i][tempY + i] == tempPlayerFlag){
                    blockX = tempX - i;
                    blockY = tempY + i;
                    count++;
                } else {
                    count = 0;
                }
                if(count == limit){ //There's <limit> in a row, block it!!
                    if(board[blockX - 1][blockY + 1] == 0){   //Try one open end
                        move = new int[]{ blockX - 1, blockY + 1, Integer.MAX_VALUE};
                        return move;
                    } else if(board[blockX + limit][blockY - limit] == 0){    //Try other end
                        move = new int[]{blockX + limit, blockY - limit, Integer.MAX_VALUE};
                        return move;
                    } else{         //You cannot win, so quit
                        move = null;
                    }
                    break;
                }
            }
            count = 0;
            
            } catch (ArrayIndexOutOfBoundsException e){
                //Array was checked outside of bounds, move onto next move
            }
        }
        
        return move;
    }
//
//    /**
//     * Get all available empty spots on the board and return a move to AI class
//     * in order to make a move
//     * 
//     * Checks the whole board to add available moves around spots that exist
//     * @return 
//     */
//    @Override
//    ArrayList<int[]> getAvailableMoves() {
//        ArrayList<int[]> emptySpots = new ArrayList<int[]>();
//        
//        for(int i = 0; i < size; i++){
//            for(int j = 0; j < size; j++){
//                if(board[i][j] != 0){
//                    addSurrounding(i, j, emptySpots);
//                    addMovePlaced(i, j, board[i][j]);
//                }
//            }
//        }
//        
//        return emptySpots;
//    }  
//    
    /**
     * If a specific tile has a piece on it, place it on this map
     * @param row
     * @param col
     * @param playerFlag 
     */
    private void addMovePlaced(int row, int col, int playerFlag){
        movesPlaced.add(new int[]{row, col, playerFlag});
    }
//    
//    private void addSurrounding(int row, int col, ArrayList<int[]> emptySpots){
//        //Iterate in a square around the piece that has been placed and add it 
//        //to the ArrayList of empty spots for further movement
//        
//        //Written by John
//        for(int i = -1; i < 2; i++){
//            for(int j = -1; j < 2; j++){
//                if((row + i < size && row + i > 0) && (col + j < size && col + j > 0)){
//                    if (board[row + i][col + j] == 0) {
//                        emptySpots.add(new int[]{row + i, col + j,0});
//                    }
//                }
//            }
//        }       
//    }
    
//AIPlayerHard copied on 5/15
    /**
     * 
     * @param size
     * @param difficulty 
     */
    public AIPlayerHard(int size){
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
        //int playerPoints = Integer.max(vertical(col, getOpponentFlag(model.playerFlagFor(this)), row, false), horizontal(row, getOpponentFlag(model.playerFlagFor(this)), col, false)); 

        return aiPoints;

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
                    addMovePlaced(row, col, board[row][col]);
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
        
        
        topScore = checkNumInARow(4);
        if(topScore != null){
            System.out.println("4inarow");
            return topScore;
        }
        
        topScore = checkNumInARow(3);
        if(topScore != null){
            System.out.println("3inarow");
            return topScore;
        }
        topScore = singleScore;
        for(int[] move : (ArrayList<int[]>) availableMoves){
            singleScore = new int[]{move[0], move[1], analyze(move[0], move[1])};
            //System.out.println();
            if(topScore[2] < singleScore[2]){
                topScore = singleScore;
            }
        }
        
        System.out.println(topScore[0]+ ", " + topScore[1] + ", Score: " + topScore[2]);
        return topScore;
    }
}

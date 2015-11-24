package game;

/**
 * Created by Carlo on 09/10/2015.
 */
public class Game {
    public enum GAME_STATES {NORMAL, WON, TIE}


    private final char player1;
    private final char player2;
    private Board board;
    private char currentPlayer;

    private GAME_STATES gameState;
    private char winner;

    public Game(char player1, char player2) throws GameException {
        if (player1 == player2)
            throw new GameException("Players cannot have the same char");

        this.player1 = player1;
        this.player2 = player2;

        board = new Board();

        gameState = GAME_STATES.NORMAL;
        currentPlayer = player1;
    }

    private void checkWin() {
        char[][] boardArray = board.getBoardArray();

        //Check horizontal
        for (int y = 0; y < boardArray.length; y++) {
            if (boardArray[y][0] != '\u0000' && boardArray[y][0] == boardArray[y][1] && boardArray[y][1] == boardArray[y][2]) {
                gameState = GAME_STATES.WON;
                winner = boardArray[y][0] == player1 ? player1 : player2;
                return;
            }
        }

        //Check vertical
        for (int x = 0; x < boardArray[0].length; x++) {
            if (boardArray[0][x] != '\u0000' && boardArray[0][x] == boardArray[1][x] && boardArray[1][x] == boardArray[2][x]) {
                gameState = GAME_STATES.WON;
                winner = boardArray[0][x] == player1 ? player1 : player2;
                return;
            }
        }

        //Check diagonal left->right
        if (boardArray[0][0] != '\u0000' && boardArray[0][0] == boardArray[1][1] && boardArray[1][1] == boardArray[2][2]) {
            gameState = GAME_STATES.WON;
            winner = boardArray[0][0] == player1 ? player1 : player2;
            return;
        }

        //Check diagonal right->left
        if (boardArray[0][2] != '\u0000' && boardArray[0][2] == boardArray[1][1] && boardArray[1][1] == boardArray[2][0]) {
            gameState = GAME_STATES.WON;
            winner = boardArray[0][2] == player1 ? player1 : player2;
            return;
        }

        //Check for tie
        boolean emptySpot = false;
        for (int y = 0; y < boardArray.length; y++) {
            for (int x = 0; x < boardArray[y].length; x++)
                if (boardArray[y][x] == '\u0000')
                    emptySpot = true;
        }
        if (emptySpot) {
            gameState = GAME_STATES.NORMAL;
        } else {
            gameState = GAME_STATES.TIE;
        }

    }

    public char getPlayer1() {
        return player1;
    }

    public char getPlayer2() {
        return player2;
    }

    public GAME_STATES getGameState() {
        return gameState;
    }

    public char getWinner() {
        return winner;
    }

    public Board getBoard() {
        return board;
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public void makeMove(int pos) throws GameException {
        if (pos >= board.getBoardSize() || pos < 0) {
            throw new GameException("The selected position is not within the board");
        } else if (!board.isFree(pos)) {
            throw new GameException("The selected position is taken.");
        } else {
            board.setPlayer(pos, currentPlayer);
            try {
                winner = WinChecker.FIND_WINNER(board);
                if (winner == '\u0000') gameState = GAME_STATES.TIE;
                else gameState = GAME_STATES.WON;
            } catch (NoWinnerException e) {
                currentPlayer = currentPlayer == player1 ? player2 : player1;
            }
        }
    }

    @Override
    public String toString() {
        return "Player's " + currentPlayer + " turn \n" + board;
    }
}
package ai.minimax;

import ai.AI;
import game.Board;
import game.NoWinnerException;
import game.WinChecker;

import java.util.Enumeration;

/**
 * Created by Carlo on 17/10/2015.
 */
public class Minimax implements AI {
    private final static String NAME = "Minimax";
    private static final int MINIMAX_TREE_DEPTH = 9;

    private final char aiPlayer;
    private final char opponent;

    private int currentMove;

    public Minimax(char aiPlayer, char opponent) {
        this.aiPlayer = aiPlayer;
        this.opponent = opponent;
    }

    @Override
    public int move(Board board) {
        TicTacToeNode node = makeTree(new TicTacToeNode(board, aiPlayer, opponent), MINIMAX_TREE_DEPTH);

        int bestScore = 0;
        TicTacToeNode bestNode = null;
        Enumeration enumeration = node.children();
        while (enumeration.hasMoreElements()) {
            TicTacToeNode curNode = (TicTacToeNode) enumeration.nextElement();
            int childScore = alphaBeta(curNode, Integer.MIN_VALUE, Integer.MAX_VALUE, true, 1);

            if (bestNode == null || bestScore < childScore) {
                bestNode = curNode;
                bestScore = childScore;
            }
        }

        currentMove = Board.boardsToMove(board.getBoardArray(), bestNode.getBoard().getBoardArray());
        return currentMove;
    }

    private int alphaBeta(TicTacToeNode node, int alpha, int beta, boolean maximizing, int depth) {
        int bestScore;
        if (!node.children().hasMoreElements()) {
            bestScore = node.calcScore() / depth;
            if (!maximizing) bestScore = -bestScore;
        } else if (maximizing) {
            bestScore = alpha;

            Enumeration enumeration = node.children();
            while (enumeration.hasMoreElements()) {
                TicTacToeNode childNode = (TicTacToeNode) enumeration.nextElement();
                int childScore = alphaBeta(childNode, bestScore, beta, false, depth + 1);
                bestScore = Math.max(bestScore, childScore);
                if (beta <= bestScore) break;
            }
        } else {
            bestScore = beta;

            Enumeration enumeration = node.children();
            while (enumeration.hasMoreElements()) {
                int childScore = alphaBeta((TicTacToeNode) enumeration.nextElement(), alpha, bestScore, true, depth + 1);
                bestScore = Math.min(bestScore, childScore);
                if (bestScore <= alpha) break;
            }
        }
        node.setScore(bestScore);
        return bestScore;
    }

    private TicTacToeNode makeTree(TicTacToeNode root, int depth) {
        Board rootBoard = root.getBoard();
        if (depth == 0) return root;

        for (int pos = 0; pos < rootBoard.getBoardSize(); pos++) {
            try {
                WinChecker.FIND_WINNER(rootBoard);
            } catch (NoWinnerException e) {
                if (rootBoard.isFree(pos)) {
                    TicTacToeNode newNode = new TicTacToeNode(rootBoard, root.getOtherPlayer(), root.getCurrentPlayer());
                    newNode.getBoard().setPlayer(pos, newNode.getCurrentPlayer());
                    root.add(makeTree(newNode, depth - 1));
                }
            }
        }
        return root;
    }

    @Override
    public int getTimeSpend() {
        return 0;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public char getPlayer() {
        return 0;
    }
}

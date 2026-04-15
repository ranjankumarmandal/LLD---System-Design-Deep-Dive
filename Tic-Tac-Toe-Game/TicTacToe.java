import java.util.Scanner;

class Board {
    private char[][] board;
    private int size;

    public Board(int size) {
        this.size = size;
        board = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = ' ';
            }
        }
    }

    public void printBoard() {
        System.out.println("Current board:");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(board[i][j]);
                if (j < size - 1) System.out.print(" | ");
            }
            System.out.println();
            if (i < size - 1) {
                for (int k = 0; k < size; k++) {
                    System.out.print("--");
                    if (k < size - 1) System.out.print("+");
                }
                System.out.println();
            }
        }
    }

    public boolean isValidMove(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size && board[row][col] == ' ';
    }

    public boolean placeMark(int row, int col, char mark) {
        if (isValidMove(row, col)) {
            board[row][col] = mark;
            return true;
        }
        return false;
    }

    public boolean isFull() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == ' ') return false;
            }
        }
        return true;
    }

    public boolean checkWin(char mark) {
        for (int i = 0; i < size; i++) {
            boolean rowWin = true;
            for (int j = 0; j < size; j++) {
                if (board[i][j] != mark) {
                    rowWin = false;
                    break;
                }
            }
            if (rowWin) return true;
        }

        for (int j = 0; j < size; j++) {
            boolean colWin = true;
            for (int i = 0; i < size; i++) {
                if (board[i][j] != mark) {
                    colWin = false;
                    break;
                }
            }
            if (colWin) return true;
        }

        boolean diagWin = true;
        for (int i = 0; i < size; i++) {
            if (board[i][i] != mark) {
                diagWin = false;
                break;
            }
        }
        if (diagWin) return true;

        diagWin = true;
        for (int i = 0; i < size; i++) {
            if (board[i][size - 1 - i] != mark) {
                diagWin = false;
                break;
            }
        }
        if (diagWin) return true;

        return false;
    }
}

public class TicTacToe {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Board board = new Board(3);
        char currentPlayer = 'X';
        boolean gameEnded = false;

        System.out.println("Welcome to Tic-Tac-Toe!");
        board.printBoard();

        while (!gameEnded) {
            System.out.println("Player " + currentPlayer + "'s turn.");
            System.out.print("Enter row (0, 1, or 2): ");
            int row = sc.nextInt();
            System.out.print("Enter column (0, 1, or 2): ");
            int col = sc.nextInt();

            if (board.placeMark(row, col, currentPlayer)) {
                board.printBoard();
                if (board.checkWin(currentPlayer)) {
                    System.out.println("Player " + currentPlayer + " wins!");
                    gameEnded = true;
                } else if (board.isFull()) {
                    System.out.println("Game is a draw!");
                    gameEnded = true;
                } else {
                    currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                }
            } else {
                System.out.println("Invalid move! Try again.");
            }
        }
        sc.close();
    }
}
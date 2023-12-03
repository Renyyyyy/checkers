public class Board {
    private Piece[][] board;

    public Board() {
        board = new Piece[8][8];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 1 && i < 3) {
                    board[i][j] = new Piece('b'); // Black piece
                } else if ((i + j) % 2 == 1 && i > 4) {
                    board[i][j] = new Piece('w'); // White piece
                } else {
                    board[i][j] = null; // Empty cell
                }
            }
        }
    }

    public Piece getPiece(int row, int col) {
        return board[row][col];
    }

    public void setPiece(int row, int col, Piece piece) {
        board[row][col] = piece;
    }

//    public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
//        Piece piece = board[fromRow][fromCol];
//        board[fromRow][fromCol] = null;
//        board[toRow][toCol] = piece;
//    }

    public boolean isValidCell(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
}

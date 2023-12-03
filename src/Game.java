import java.util.Scanner;

public class Game {
    private Board board;
    private Player[] players;
    private boolean gameOver;

    public Game() {
        board = new Board();
        players = new Player[]{new Player('b', true), new Player('w', false)};
        gameOver = false;
    }

    public void playGame() {
        Scanner scanner = new Scanner(System.in);
        boolean blackPiecesLeft = true;
        boolean whitePiecesLeft = true;

        while (!gameOver) {
            printBoard();

            char playerType = currentPlayer().getType();
            System.out.println("Player " + playerType + "'s turn.");

            System.out.println("Enter the coordinates of the piece you want to move (e.g., a2): ");
            String fromInput = scanner.next();
            int fromCol = fromInput.charAt(0) - 'a';
            int fromRow = Character.getNumericValue(fromInput.charAt(1)) - 1;

            System.out.println("Enter the coordinates to move the piece to (e.g., b3): ");
            String toInput = scanner.next();
            int toCol = toInput.charAt(0) - 'a';
            int toRow = Character.getNumericValue(toInput.charAt(1)) - 1;

            if (isValidMove(fromRow, fromCol, toRow, toCol)) {
                makeMove(fromRow, fromCol, toRow, toCol);

                blackPiecesLeft = false;
                whitePiecesLeft = false;

                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (board.getPiece(i, j) != null) {
                            char pieceType = board.getPiece(i, j).getType();
                            if (pieceType == 'b' || pieceType == 'B') {
                                blackPiecesLeft = true;
                            } else if (pieceType == 'w' || pieceType == 'W') {
                                whitePiecesLeft = true;
                            }
                        }
                    }
                }

                if (!blackPiecesLeft || !whitePiecesLeft) {
                    gameOver = true;
                }

                switchPlayerTurn();
            } else {
                System.out.println("Invalid move. Try again.");
            }

            if (gameOver) {
                break;
            }
        }

        printBoard();
        System.out.println("Game Over!");
        if (!blackPiecesLeft) {
            System.out.println("White player wins!");
        } else {
            System.out.println("Black player wins!");
        }
        scanner.close();
    }

    private void printBoard() {
        System.out.println("  a b c d e f g h");
        for (int i = 0; i < 8; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < 8; j++) {
                Piece piece = board.getPiece(i, j);
                if (piece == null) {
                    System.out.print("  ");
                } else {
                    System.out.print(piece.getType() + " ");
                }
            }
            System.out.println(i + 1);
        }
        System.out.println("  a b c d e f g h");
    }

    private Player currentPlayer() {
        for (Player player : players) {
            if (player.isTurn()) {
                return player;
            }
        }
        return null;
    }

    private void switchPlayerTurn() {
        for (Player player : players) {
            player.setTurn(!player.isTurn());
        }
    }

    private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        Piece fromPiece = board.getPiece(fromRow, fromCol);
        Piece toPiece = board.getPiece(toRow, toCol);

        // Проверка на выход за пределы доски
        if (!board.isValidCell(fromRow, fromCol) || !board.isValidCell(toRow, toCol)) {
            return false;
        }

        // Проверка наличия фигуры для перемещения
        if (fromPiece == null) {
            return false;
        }

        // Проверка наличия фигуры на конечной позиции
        if (toPiece != null) {
            return false;
        }

        // Дополнительная логика проверки валидности хода для взятия шашки противника
        int rowDiff = Math.abs(fromRow - toRow);
        int colDiff = Math.abs(fromCol - toCol);

        if (rowDiff == 2 && colDiff == 2) {
            int captureRow = (fromRow + toRow) / 2;
            int captureCol = (fromCol + toCol) / 2;
            char opponentType = (fromPiece.getType() == 'b') ? 'w' : 'b'; // Определение типа противника

            if (board.getPiece(captureRow, captureCol) != null &&
                    board.getPiece(captureRow, captureCol).getType() == opponentType) {
                // В случае взятия шашки противника, удаляем её с доски
                board.setPiece(captureRow, captureCol, null);
                return true;
            } else {
                return false; // Недопустимый ход (взятие шашки не удалось)
            }
        }


        // Дополнительная логика проверки валидности хода для превращения в дамку
        if (fromPiece.getType() == 'b' && toRow == 7) { // Черные фигуры достигли нижнего края
            // Проверяем, что ход происходит на одну клетку по диагонали
            if (Math.abs(fromRow - toRow) == 1 && Math.abs(fromCol - toCol) == 1) {
                return true;
            }
        } else if (fromPiece.getType() == 'w' && toRow == 0) { // Белые фигуры достигли верхнего края
            // Проверяем, что ход происходит на одну клетку по диагонали
            if (Math.abs(fromRow - toRow) == 1 && Math.abs(fromCol - toCol) == 1) {
                return true;
            }
        }

        // Обычные правила для ходов шашек
        if (Math.abs(fromRow - toRow) == 1 && Math.abs(fromCol - toCol) == 1) {
            // Ход на одну клетку вперед допустим только если фигура перемещается по диагонали
            if (fromPiece.getType() == 'b' && toRow > fromRow // Черные фигуры ходят вверх
                    || fromPiece.getType() == 'w' && toRow < fromRow) { // Белые фигуры ходят вниз
                return true;
            }
        }

        // Проверка возможности взятия шашки
        if (Math.abs(fromRow - toRow) == 2 && Math.abs(fromCol - toCol) == 2) {
            int captureRow = (fromRow + toRow) / 2;
            int captureCol = (fromCol + toCol) / 2;
            Piece capturedPiece = board.getPiece(captureRow, captureCol);

            // Проверка на наличие фигуры противника между начальной и конечной клетками
            if (capturedPiece != null && capturedPiece.getType() != fromPiece.getType()) {
                return true;
            }
        }

        return false; // Ход недопустим
    }


    private void makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = board.getPiece(fromRow, fromCol);

        // Проверяем, что на конечной позиции нет фигуры (если есть, она будет съедена)
        if (board.getPiece(toRow, toCol) != null) {
            int captureRow = (fromRow + toRow) / 2;
            int captureCol = (fromCol + toCol) / 2;
            board.setPiece(captureRow, captureCol, null); // Убираем съеденную шашку с доски
        }

        board.setPiece(fromRow, fromCol, null);
        board.setPiece(toRow, toCol, piece);
    }

}

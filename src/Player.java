public class Player {
    private char type;
    private boolean isTurn;

    public Player(char type, boolean isTurn) {
        this.type = type;
        this.isTurn = isTurn;
    }

    public char getType() {
        return type;
    }

    public boolean isTurn() {
        return isTurn;
    }

    public void setTurn(boolean turn) {
        isTurn = turn;
    }

//    public boolean isPlayerPiece(char pieceType) {
//        return type == pieceType || Character.toUpperCase(type) == pieceType;
//    }

}

public class King extends Piece{
	public King(PieceColor color, Position position) {
		super(color,position);
	}
	@Override
	public boolean isValidMove(Position newPosition, Piece[][] board) {
		if (newPosition.equals(this.position)) {
			return false; // Cannot move to the same position
		}
		int rowDiff = Math.abs(this.position.getRow() - newPosition.getRow());
	    int colDiff = Math.abs(this.position.getColumn() - newPosition.getColumn());
	    // Kings can move one square in any direction.
	    boolean isOneSquareMove = rowDiff <= 1 && colDiff <= 1 && !(rowDiff == 0 && colDiff == 0);
	    if (!isOneSquareMove) {
	    	return false; // Move is not within one square.
	    }
	    // The move is valid if the destination is empty or contains an opponent's piece
	    Piece destinationPiece = board[newPosition.getRow()][newPosition.getColumn()];
	    return destinationPiece == null || destinationPiece.getColor() != this.getColor();
	}
}

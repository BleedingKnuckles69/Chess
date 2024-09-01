public class Queen extends Piece{
	public Queen(PieceColor color, Position position) {
		super(color,position);
	}
	@Override
	public boolean isValidMove(Position newPosition, Piece[][] board) {
		if (newPosition.equals(this.position)) {
			return false; // Cannot move to the same position
		}
		int rowDiff = Math.abs(this.position.getRow() - newPosition.getRow());
	    int colDiff = Math.abs(this.position.getColumn() - newPosition.getColumn());
	    boolean straightLine = this.position.getRow() == newPosition.getRow() || this.position.getColumn() == newPosition.getColumn();
	    boolean diagonal = rowDiff == colDiff;
	    if (!straightLine && !diagonal) {
	    	return false; // The move is neither straight nor diagonal
	    }
	    // Calculate direction of movement
	    int rowDirection = Integer.compare(newPosition.getRow(), this.position.getRow());
	    int colDirection = Integer.compare(newPosition.getColumn(), this.position.getColumn());
	    int currentRow = this.position.getRow() + rowDirection;
	    int currentCol = this.position.getColumn() + colDirection;
	    while (currentRow != newPosition.getRow() || currentCol != newPosition.getColumn()) {
	    	if (board[currentRow][currentCol] != null) {
	    		return false; // Path is blocked
	    	}
	    	currentRow += rowDirection;
	        currentCol += colDirection;
	    }
	    // The move is valid if the destination is empty or contains an opponent's piece
	    //Piece destinationPiece = board[newPosition.getRow()][newPosition.getColumn()];
	    //return destinationPiece == null || destinationPiece.getColor() != this.getColor();
	    Piece destination = board[newPosition.getRow()][newPosition.getColumn()];
		if(destination==null) {
			return true;
		}
		else if(destination.getColor()!=this.getColor()) {
			return true;
		}
		else {
			return false;
		}
	}
}

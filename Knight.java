public class Knight extends Piece {
	public Knight(PieceColor color, Position position) {
		super(color,position);
	}
	@Override
	public boolean isValidMove(Position newPosition, Piece[][] board) {
		if (newPosition.equals(this.position)) {
			return false; // Cannot move to the same position
		}
		int rowDiff = Math.abs(this.position.getRow() - newPosition.getRow());
	    int colDiff = Math.abs(this.position.getColumn() - newPosition.getColumn());
	    boolean isValidLMove = (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
	    if (!isValidLMove) {
	    	return false; // Not a valid knight move
	    }
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

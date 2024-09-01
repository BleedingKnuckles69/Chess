public class Bishop extends Piece{
	public Bishop(PieceColor color, Position position) {
		super(color,position);
	}
	@Override
	public boolean isValidMove(Position newPosition, Piece[][] board) {
		if (newPosition.equals(this.position)) {
			return false; // Cannot move to the same position
		}
		int rowDiff = Math.abs(this.position.getRow() - newPosition.getRow());
	    int colDiff = Math.abs(this.position.getColumn() - newPosition.getColumn());
	    if(rowDiff!=colDiff) {
	    	return false;
	    }
	    int rowStep = newPosition.getRow() > position.getRow() ? 1 : -1;
	    int colStep = newPosition.getColumn() > position.getColumn() ? 1 : -1;
	    int steps = rowDiff - 1; // Number of squares to check for obstruction
	    for(int i=1;i<=steps;i++) {
	    	if(board[position.getRow()+i*rowStep][position.getColumn()+i*colStep]!=null) {
	    		return false;
	    	}
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

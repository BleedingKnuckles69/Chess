public class Rook extends Piece{
	public Rook(PieceColor color, Position position) {
		super(color,position);
	}
	@Override
	public boolean isValidMove(Position newPosition, Piece[][] board) {
		//Rooks can move horizontally or vertically any number of squares, can't jump over pieces
		if (newPosition.equals(this.position)) {
			return false; // Cannot move to the same position
		}
		if(position.getRow()==newPosition.getRow()) {
			int colStart = Math.min(position.getColumn(),newPosition.getColumn())+1;
			int colEnd = Math.max(position.getColumn(),newPosition.getColumn());
			for(int i=colStart;i<colEnd;i++) {
				if(board[position.getRow()][i]!=null) {
					return false;
				}
			}
		}
		else if(position.getColumn()==newPosition.getColumn()) {
			int rowStart = Math.min(position.getRow(),newPosition.getRow())+1;
			int rowEnd = Math.max(position.getRow(),newPosition.getRow());
			for(int i=rowStart;i<rowEnd;i++) {
				if(board[i][position.getColumn()]!=null) {
					return false;
				}
			}
		}
		else {
			return false;
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

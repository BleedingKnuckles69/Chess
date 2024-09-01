import java.util.List;
import java.util.ArrayList;

public class ChessGame {
	private ChessBoard board;
	private boolean whiteTurn = true; //white starts
	
	public ChessGame() {
		this.board = new ChessBoard();
	}
	
	//Handling Moves
	public boolean makeMove(Position start, Position end) {
		Piece movingPiece = board.getPiece(start.getRow(), start.getColumn());
		
		if (movingPiece == null || movingPiece.getColor() != (whiteTurn ? PieceColor.WHITE : PieceColor.BLACK)) {
			return false; // No piece at the start position or not the player's turn
		}
		if (wouldBeInCheckAfterMove(movingPiece.getColor(), start, end)) {
	        return false; // The move leaves the king in check, so it's illegal
	    }
		
		if (movingPiece.isValidMove(end, board.getBoard())) { 		// Execute the move
		    board.movePiece(start, end);
		    whiteTurn = !whiteTurn; // Switch turns
		    return true;
		}
		return false;
	}
	//Checking for Check
	public boolean isInCheck(PieceColor kingColor) {
		Position kingPosition = findKingPosition(kingColor);
		for (int row = 0; row < board.getBoard().length; row++) {
			for (int col = 0; col < board.getBoard()[row].length; col++) {
				Piece piece = board.getPiece(row, col);
				if (piece != null && piece.getColor() != kingColor) {
					if (piece.isValidMove(kingPosition, board.getBoard())) {
						return true; // An opposing piece can capture the king
	                }
	            }
	        }
	    }
	    return false;
	}
	//Find The King’s Position
	private Position findKingPosition(PieceColor color) {
		for (int row = 0; row < board.getBoard().length; row++) {
			for (int col = 0; col < board.getBoard()[row].length; col++) {
				Piece piece = board.getPiece(row, col);
				if (piece instanceof King && piece.getColor() == color) {
					System.out.println("Found king at: (" + row + ", " + col + ")");
					return new Position(row, col);
	            }
			}
		}
	    throw new RuntimeException("King not found, which should never happen.");
	}
	//Checking for Checkmate
	public boolean isCheckmate(PieceColor kingColor) {
		if (!isInCheck(kingColor)) {
			return false; 
	    }
		Position kingPosition = findKingPosition(kingColor);
	    King king = (King) board.getPiece(kingPosition.getRow(), kingPosition.getColumn());
	    for(int rowOffset=-1;rowOffset <= 1; rowOffset++) {
	    	for (int colOffset = -1; colOffset <= 1; colOffset++) {
	    		if (rowOffset == 0 && colOffset == 0) {
	    			continue; // Skip the current position of the king
	            }
	    		Position newPosition = new Position(kingPosition.getRow() + rowOffset,kingPosition.getColumn() + colOffset);
	    		if (isPositionOnBoard(newPosition) && king.isValidMove(newPosition, board.getBoard())
	                      && !wouldBeInCheckAfterMove(kingColor, kingPosition, newPosition)) {
	    			return false;
	    		}
	    	}
	    }
	    // Check if the check can be blocked or the threatening piece can be captured
	    for (int row = 0; row < board.getBoard().length; row++) {
	        for (int col = 0; col < board.getBoard()[row].length; col++) {
	            Piece piece = board.getPiece(row, col);
	            if (piece != null && piece.getColor() == kingColor) {
	                List<Position> legalMoves = getLegalMovesForPieceAt(new Position(row, col));
	                for (Position move : legalMoves) {
	                    if (canBlockOrCaptureThreat(new Position(row, col), move)) {
	                        return false; // Threat can be blocked or captured
	                    }
	                }
	            }
	        }
	    }
	    return true;
	}
	private boolean canBlockOrCaptureThreat(Position currentPosition, Position move) {
	    // Simulate moving the piece from currentPosition to move
	    Piece movingPiece = board.getPiece(currentPosition.getRow(), currentPosition.getColumn());
	    Piece capturedPiece = board.getPiece(move.getRow(), move.getColumn());

	    // Execute the move
	    board.setPiece(move.getRow(), move.getColumn(), movingPiece);
	    board.setPiece(currentPosition.getRow(), currentPosition.getColumn(), null);

	    boolean threatBlockedOrCaptured = !isInCheck(getCurrentPlayerColor());

	    // Undo the move
	    board.setPiece(currentPosition.getRow(), currentPosition.getColumn(), movingPiece);
	    board.setPiece(move.getRow(), move.getColumn(), capturedPiece);

	    return threatBlockedOrCaptured;
	}
	//Validating Board Position
	private boolean isPositionOnBoard(Position position) {
		return position.getRow() >= 0 && position.getRow() < board.getBoard().length &&
	              position.getColumn() >= 0 && position.getColumn() < board.getBoard()[0].length;

	}
	//Simulating Moves For Check Position
	private boolean wouldBeInCheckAfterMove(PieceColor kingColor, Position from, Position to) {
		// Simulate the move temporarily
	    Piece temp = board.getPiece(to.getRow(), to.getColumn());
	    board.setPiece(to.getRow(), to.getColumn(), board.getPiece(from.getRow(), from.getColumn()));
	    board.setPiece(from.getRow(), from.getColumn(), null);
	    boolean inCheck = isInCheck(kingColor);
	    // Undo the move
	    board.setPiece(from.getRow(), from.getColumn(), board.getPiece(to.getRow(), to.getColumn()));
	    board.setPiece(to.getRow(), to.getColumn(), temp);
	    return inCheck;
	}
	public ChessBoard getBoard() {
		return this.board;
	}
	public void resetGame() {
		this.board = new ChessBoard(); // Re-initialize the board
		this.whiteTurn = true; // Reset turn to white
	}
	public PieceColor getCurrentPlayerColor() {
		return whiteTurn ? PieceColor.WHITE : PieceColor.BLACK;
	}
	private Position selectedPosition; // Tracks the currently selected piece's position
	public boolean isPieceSelected() {
		return selectedPosition != null;
	}
	public boolean handleSquareSelection(int row, int col) {
		System.out.println("Handling selection at: (" + row + ", " + col + ")");
		if (selectedPosition == null) {
			// Attempt to select a piece
		    Piece selectedPiece = board.getPiece(row, col);
		    if (selectedPiece != null
		    		&& selectedPiece.getColor() == (whiteTurn ? PieceColor.WHITE : PieceColor.BLACK)) {
		    	selectedPosition = new Position(row, col);
		        return false; // Indicate a piece was selected but not moved
		    }
		} 
		else {
		    // Attempt to move the selected piece
		    boolean moveMade = makeMove(selectedPosition, new Position(row, col));
		    selectedPosition = null; // Reset selection regardless of move success
		    return moveMade; // Return true if a move was successfully made
		}
		return false; // Return false if no piece was selected or move was not made
	}
	//Updating ChessGame to Highlight Possible Moves
	public List<Position> getLegalMovesForPieceAt(Position position) {
	    Piece selectedPiece = board.getPiece(position.getRow(), position.getColumn());
	    if (selectedPiece == null) return new ArrayList<>();
	    List<Position> legalMoves = new ArrayList<>();
	    List<Position> possibleMoves = new ArrayList<>();
	    switch (selectedPiece.getClass().getSimpleName()) {
	        case "Pawn":
	        	addPawnMoves(position, selectedPiece.getColor(), legalMoves);
	            break;
	        case "Rook":
	            addLineMoves(position, new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}}, legalMoves);
	            break;
	        case "Knight":
	            addSingleMoves(position, new int[][]{{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {-1, 2}, {1, -2}, {-1, -2}}, legalMoves);
	            break;
	        case "Bishop":
	            addLineMoves(position, new int[][]{{1, 1}, {-1, -1}, {1, -1}, {-1, 1}}, legalMoves);
	            break;
	        case "Queen":
	            addLineMoves(position, new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}}, legalMoves);
	            break;
	        case "King":
	            addSingleMoves(position, new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}}, legalMoves);
	            break;
	            
	    }
	    for (Position move : legalMoves) {
	        if (!wouldBeInCheckAfterMove(selectedPiece.getColor(), position, move)) {
	            possibleMoves.add(move);
	        }
	    }
	    return possibleMoves;
	}
	private void addLineMoves(Position position, int[][] directions, List<Position> legalMoves) {
		for (int[] d : directions) {
			Position newPos = new Position(position.getRow() + d[0], position.getColumn() + d[1]);
		    while (isPositionOnBoard(newPos)) {
		        if (board.getPiece(newPos.getRow(), newPos.getColumn()) == null) {
		        	legalMoves.add(new Position(newPos.getRow(), newPos.getColumn()));
		            newPos = new Position(newPos.getRow() + d[0], newPos.getColumn() + d[1]);
		          } 
		        else {
		        	if (board.getPiece(newPos.getRow(), newPos.getColumn()).getColor() != board.getPiece(position.getRow(), position.getColumn()).getColor()) {
		        		legalMoves.add(newPos);
		            }
		            break;
		        }
		    }
		}
	}
	private void addSingleMoves(Position position, int[][] moves, List<Position> legalMoves) {
		for (int[] move : moves) {
			Position newPos = new Position(position.getRow() + move[0], position.getColumn() + move[1]);
		    if (isPositionOnBoard(newPos) && (board.getPiece(newPos.getRow(), newPos.getColumn()) == null ||
		        board.getPiece(newPos.getRow(), newPos.getColumn()).getColor() != board.getPiece(position.getRow(), position.getColumn()).getColor())) {
		    	legalMoves.add(newPos);
		    }
		}
	}
	private void addPawnMoves(Position position, PieceColor color, List<Position> legalMoves) {
		int direction = color == PieceColor.WHITE ? -1 : 1;
		// Standard single move
		Position newPos = new Position(position.getRow() + direction, position.getColumn());
		if (isPositionOnBoard(newPos) && board.getPiece(newPos.getRow(), newPos.getColumn()) == null) {
			legalMoves.add(newPos);
		}
		// Double move from starting position
		if ((color == PieceColor.WHITE && position.getRow() == 6) || (color == PieceColor.BLACK && position.getRow() == 1)) {
			newPos = new Position(position.getRow() + 2 * direction, position.getColumn());
		    Position intermediatePos = new Position(position.getRow() + direction, position.getColumn());
		    if (isPositionOnBoard(newPos) && board.getPiece(newPos.getRow(), newPos.getColumn()) == null && board.getPiece(intermediatePos.getRow(), intermediatePos.getColumn()) == null) {
		    	legalMoves.add(newPos);
		    }
		}
		// Captures
		int[] captureCols = {position.getColumn() - 1, position.getColumn() + 1};
		for (int col : captureCols) {
			newPos = new Position(position.getRow() + direction, col);
		    if (isPositionOnBoard(newPos) && board.getPiece(newPos.getRow(), newPos.getColumn()) != null &&
		        board.getPiece(newPos.getRow(), newPos.getColumn()).getColor() != color) {
		    	legalMoves.add(newPos);
		    }
		}
	}	
	
}

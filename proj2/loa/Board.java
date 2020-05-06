/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;

import java.util.regex.Pattern;

import static loa.Piece.*;
import static loa.Square.*;

/** Represents the state of a game of Lines of Action.
 *  @author Jilin He
 */
class Board {

    /** Default number of moves for each side that results in a draw. */
    static final int DEFAULT_MOVE_LIMIT = 60;

    /** Pattern describing a valid square designator (cr). */
    static final Pattern ROW_COL = Pattern.compile("^[a-h][1-8]$");

    /** A Board whose initial contents are taken from INITIALCONTENTS
     *  and in which the player playing TURN is to move. The resulting
     *  Board has
     *        get(col, row) == INITIALCONTENTS[row][col]
     *  Assumes that PLAYER is not null and INITIALCONTENTS is 8x8.
     *
     *  CAUTION: The natural written notation for arrays initializers puts
     *  the BOTTOM row of INITIALCONTENTS at the top.
     */
    Board(Piece[][] initialContents, Piece turn) {
        initialize(initialContents, turn);
    }

    /** A new board in the standard initial position. */
    Board() {
        this(INITIAL_PIECES, BP);
    }

    /** A Board whose initial contents and state are copied from
     *  BOARD. */
    Board(Board board) {
        this();
        copyFrom(board);
    }

    /** Set my state to CONTENTS with SIDE to move. */
    void initialize(Piece[][] contents, Piece side) {
        int len = 0;
        for (int i = 0; i < contents.length; i++) {
            for (int j = 0; j < contents[i].length; j++) {
                _board[len + j] = contents[i][j];
            }
            len += contents[i].length;
        }
        _turn = side;
        _moveLimit = DEFAULT_MOVE_LIMIT;
        _winner = null;
        _subsetsInitialized = true;
        _winnerKnown = false;
        _moves.clear();
        _blackRegionSizes.clear();
        _whiteRegionSizes.clear();
    }

    /** Set me to the initial configuration. */
    void clear() {
        initialize(INITIAL_PIECES, BP);
    }

    /** Set my state to a copy of BOARD. */
    void copyFrom(Board board) {
        if (board == this) {
            return;
        }
        for (int i = 0; i < board._board.length; i++) {
            this._board[i] = board._board[i];
        }
        this._subsetsInitialized = board._subsetsInitialized;
        this._turn = board._turn;
        this._moves.addAll(board._moves);
        this._winner = board._winner;
        this._winnerKnown = board._winnerKnown;
    }

    /** Return the contents of the square at SQ. */
    Piece get(Square sq) {
        return _board[sq.index()];
    }

    /** Set the square at SQ to V and set the side that is to move next
     *  to NEXT, if NEXT is not null. */
    void set(Square sq, Piece v, Piece next) {
        _board[sq.index()] = v;
        if (next != null) {
            _turn = next;
        }
    }

    /** Set the square at SQ to V, without modifying the side that
     *  moves next. */
    void set(Square sq, Piece v) {
        set(sq, v, null);
    }

    /** Set limit on number of moves by each side that results in a tie to
     *  LIMIT, where 2 * LIMIT > movesMade(). */
    void setMoveLimit(int limit) {
        if (2 * limit <= movesMade()) {
            throw new IllegalArgumentException("move limit too small");
        }
        _moveLimit = 2 * limit;
    }

    /** Assuming isLegal(MOVE), make MOVE. Assumes MOVE.isCapture()
     *  is false. */
    void makeMove(Move move) {
        assert isLegal(move);
        Square from = move.getFrom();
        Square to = move.getTo();
        Piece temp = get(from);
        Piece tempt = get(to);
        set(from, EMP);
        set(to, temp, temp.opposite());
        if (temp == tempt.opposite()) {
            move = Move.mv(from, to).captureMove();
        }
        _moves.add(move);
        _subsetsInitialized = false;
    }

    /** Retract (unmake) one move, returning to the state immediately before
     *  that move.  Requires that movesMade () > 0. */
    void retract() {
        assert movesMade() > 0;
        Move move = _moves.remove(_moves.size() - 1);
        Square from = move.getFrom();
        Square to = move.getTo();
        Piece temp = get(to);
        if (move.isCapture()) {
            set(to, temp.opposite());
            set(from, temp, temp);
        } else {
            set(to, EMP);
            set(from, temp, temp);
        }
        _subsetsInitialized = false;
    }

    /** Return the Piece representing who is next to move. */
    Piece turn() {
        return _turn;
    }

    /** Return true iff FROM - TO is a legal move for the player currently on
     *  move. */
    boolean isLegal(Square from, Square to) {
        if (!from.isValidMove(to)) {
            return false;
        }
        int dir = from.direction(to);
        int dis = from.distance(to);
        Piece tempf = get(from);
        Piece tempt = get(to);
        if (tempf == tempt) {
            return false;
        }
        if (blocked(from, to)) {
            return false;
        }
        return dis == numberHelper(dir, from);
    }

    /** Return true iff MOVE is legal for the player currently on move.
     *  The isCapture() property is ignored. */
    boolean isLegal(Move move) {
        return isLegal(move.getFrom(), move.getTo());
    }

    /** Return a sequence of all legal moves from this position. */
    List<Move> legalMoves() {
        List<Move> allMoves = new ArrayList<Move>();
        for (int i = 0; i < _board.length; i++) {
            Piece temp1 = _board[i];
            if (temp1 == this._turn) {
                Square temp2 = ALL_SQUARES[i];
                for (int j = 0; j < ALL_SQUARES.length; j++) {
                    Piece nextP = _board[j];
                    if (nextP != this._turn) {
                        Square temp3 = ALL_SQUARES[j];
                        if (isLegal(temp2, temp3)) {
                            allMoves.add(Move.mv(temp2, temp3));
                        }
                    }
                }
            }
        }
        return allMoves;
    }

    /** Return true iff the game is over (either player has all his
     *  pieces continguous or there is a tie). */
    boolean gameOver() {
        return winner() != null;
    }

    /** Return true iff SIDE's pieces are continguous. */
    boolean piecesContiguous(Piece side) {
        return getRegionSizes(side).size() == 1;
    }

    /** Return the winning side, if any.  If the game is not over, result is
     *  null.  If the game has ended in a tie, returns EMP. */
    Piece winner() {
        if (!_winnerKnown) {
            if (_moves.size() < _moveLimit) {
                if (!piecesContiguous(_turn)
                        && !piecesContiguous(_turn.opposite())) {
                    return null;
                }
                if (piecesContiguous(_turn)
                        && piecesContiguous(_turn.opposite())) {
                    _winner = _turn.opposite();
                } else if (piecesContiguous(_turn)) {
                    _winner = _turn;
                } else if (piecesContiguous(_turn.opposite())) {
                    _winner = _turn.opposite();
                }
            } else if (_moves.size() == _moveLimit) {
                if (!piecesContiguous(_turn)
                        && !piecesContiguous(_turn.opposite())) {
                    return EMP;
                }
                if (piecesContiguous(_turn)
                        && piecesContiguous(_turn.opposite())) {
                    _winner = _turn.opposite();
                } else if (piecesContiguous(_turn)) {
                    _winner = _turn;
                } else if (piecesContiguous(_turn.opposite())) {
                    _winner = _turn.opposite();
                }
            } else {
                return EMP;
            }
            _winnerKnown = true;
        }
        return _winner;
    }

    /** Return the total number of moves that have been made (and not
     *  retracted).  Each valid call to makeMove with a normal move increases
     *  this number by 1. */
    int movesMade() {
        return _moves.size();
    }

    @Override
    public boolean equals(Object obj) {
        Board b = (Board) obj;
        return Arrays.deepEquals(_board, b._board) && _turn == b._turn;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(_board) * 2 + _turn.hashCode();
    }

    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("===%n");
        for (int r = BOARD_SIZE - 1; r >= 0; r -= 1) {
            out.format("    ");
            for (int c = 0; c < BOARD_SIZE; c += 1) {
                out.format("%s ", get(sq(c, r)).abbrev());
            }
            out.format("%n");
        }
        out.format("Next move: %s%n===", turn().fullName());
        return out.toString();
    }

    /** Return true if a move from FROM to TO is blocked by an opposing
     *  piece or by a friendly piece on the target square. */
    private boolean blocked(Square from, Square to) {
        Piece pfrom = get(from);
        Piece pto = get(to);
        if (pto == pfrom) {
            return true;
        }
        int dis = from.distance(to);
        int dir = from.direction(to);
        for (int i = 0; i < dis; i++) {
            Square temp1 = from.moveDest(dir, i);
            if (temp1 != null) {
                if (get(temp1) == pfrom.opposite()) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Return the size of the as-yet unvisited cluster of squares
     *  containing P at and adjacent to SQ.  VISITED indicates squares that
     *  have already been processed or are in different clusters.  Update
     *  VISITED to reflect squares counted. */
    private int numContig(Square sq, boolean[][] visited, Piece p) {
        if (p == EMP) {
            return 0;
        }
        if (get(sq) != p) {
            return 0;
        }
        if (visited[sq.row()][sq.col()]) {
            return 0;
        }
        visited[sq.row()][sq.col()] = true;
        int counter = 1;
        for (int i = 0; i < 8; i++) {
            Square temp = sq.moveDest(i, 1);
            if (temp != null) {
                counter += numContig(temp, visited, p);
            }
        }
        return counter;
    }

    /** Set the values of _whiteRegionSizes and _blackRegionSizes. */
    private void computeRegions() {
        if (_subsetsInitialized) {
            return;
        }
        _whiteRegionSizes.clear();
        _blackRegionSizes.clear();
        boolean[][] visited = new boolean[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                visited[i][j] = false;
            }
        }
        for (int i = 0; i < _board.length; i++) {
            int col = i, row = 0;
            if (i > 7) {
                col = i % BOARD_SIZE;
                row = i / BOARD_SIZE;
            }
            if (_board[i] == BP && !visited[row][col]) {
                _blackRegionSizes.add(numContig(ALL_SQUARES[i], visited, BP));
            }
            if (_board[i] == WP && !visited[row][col]) {
                _whiteRegionSizes.add(numContig(ALL_SQUARES[i], visited, WP));
            }
        }
        Collections.sort(_whiteRegionSizes, Collections.reverseOrder());
        Collections.sort(_blackRegionSizes, Collections.reverseOrder());
        _subsetsInitialized = true;
    }

    /** Return the sizes of all the regions in the current union-find
     *  structure for side S. */
    List<Integer> getRegionSizes(Piece s) {
        computeRegions();
        if (s == WP) {
            return _whiteRegionSizes;
        } else {
            return _blackRegionSizes;
        }
    }

    /** Helper function.
     * @param dir is the direction.
     * @param from is the from square.
     * @return the number of the pieces on that direction.
     * */
    int numberHelper(int dir, Square from) {
        int count = 1;
        int dir1 = 0;
        if (dir >= 4) {
            dir1 = dir - 4;
        } else {
            dir1 = dir + 4;
        }
        for (int i = 0; i < BOARD_SIZE; i++) {
            Square temp1 = from.moveDest(dir, i);
            if (temp1 != null) {
                if (get(temp1) != EMP) {
                    count += 1;
                }
            }
            Square temp2 = from.moveDest(dir1, i);
            if (temp2 != null) {
                if (get(temp2) != EMP) {
                    count += 1;
                }
            }
        }
        return count;
    }

    /** Helper function to get the distance between each contiguous regions.
     * @param piece is input value.
     * @return return value is the sum of distance.
     * */
    public int regionDist(Piece piece) {
        int dist = 0;
        boolean[][] visited = new boolean[BOARD_SIZE][BOARD_SIZE];
        ArrayList<Square> visiSq = new ArrayList<Square>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                visited[i][j] = false;
            }
        }
        for (int i = 0; i < 64; i++) {
            int col = i, row = 0;
            if (i > 7) {
                col = i % BOARD_SIZE;
                row = i / BOARD_SIZE;
            }
            Square temp1 = ALL_SQUARES[i];
            if (_board[i] == piece && !visited[row][col]) {
                visiSq.add(temp1);
            }
            numContig(temp1, visited, piece);
        }
        for (Square temp: visiSq) {
            for (Square temp1: visiSq) {
                dist += temp.distance(temp1);
            }
        }
        return dist;
    }

    /** The standard initial configuration for Lines of Action (bottom row
     *  first). */
    static final Piece[][] INITIAL_PIECES = {
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };

    /** Current contents of the board.  Square S is at _board[S.index()]. */
    private final Piece[] _board = new Piece[BOARD_SIZE  * BOARD_SIZE];

    /** List of all unretracted moves on this board, in order. */
    private final ArrayList<Move> _moves = new ArrayList<>();
    /** Current side on move. */
    private Piece _turn;
    /** Limit on number of moves before tie is declared.  */
    private int _moveLimit;
    /** True iff the value of _winner is known to be valid. */
    private boolean _winnerKnown;
    /** Cached value of the winner (BP, WP, EMP (for tie), or null (game still
     *  in progress).  Use only if _winnerKnown. */
    private Piece _winner;

    /** True iff subsets computation is up-to-date. */
    private boolean _subsetsInitialized;

    /** List of the sizes of continguous clusters of pieces, by color. */
    private final ArrayList<Integer>
        _whiteRegionSizes = new ArrayList<>(),
        _blackRegionSizes = new ArrayList<>();
}

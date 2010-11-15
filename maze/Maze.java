// maze/Maze.java
// MATH 539 Statistics Final Project

package maze;

import java.awt.Point;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Random;

/** Represents a 10x10 maze with walls.  The user may move from an initial
 * point in any direction, although the user will not succeed if there is a wall
 * in his way.  The maze may be read from an input file.
 */
public class Maze
{
  public static final int HSIZE = 10;
  public static final int VSIZE = 10;

  /** Matrix in column major order */
  private final boolean[][] _matrix = new boolean[HSIZE][VSIZE];

  /** Direction */
  public enum Direction {
    NORTH {
      public Point step(Point p) {
        Point copy = new Point(p);
        assert(p != copy);
        assert p.equals(copy);
        copy.translate(0, -1);
        return copy;
      }
    }, SOUTH {
      public Point step(Point p) {
        Point copy = new Point(p);
        assert(p != copy);
        assert p.equals(copy);
        copy.translate(0, 1);
        return copy;
      }
    }, EAST {
      public Point step(Point p) {
        Point copy = new Point(p);
        assert(p != copy);
        assert p.equals(copy);
        copy.translate(1, 0);
        return copy;
      }
    }, WEST {
      public Point step(Point p) {
        Point copy = new Point(p);
        assert(p != copy);
        assert p.equals(copy);
        copy.translate(-1, 0);
        return copy;
      }
    };

    private static final Random _rng = new Random();

    /** Steps one step from p.  Does not consider the maze at all. */
    public abstract Point step(Point p);

    /** Gets a random direction. */
    public static Direction getRandomDir()
    {
      int r = _rng.nextInt(4);
      switch (r) {
        case 0:
          return NORTH;
        case 1:
          return EAST;
        case 2:
          return SOUTH;
        case 3:
          return WEST;
        default:
          throw new AssertionError("the impossible happened");
      }
    }
  };

  /** Constructs an empty maze. */
  public Maze()
  {
  }

  /** Constructs a maze from the existing maze.
   *
   * @throws IllegalArgumentException The maze is not size 10x10.
   */
  public Maze(boolean[][] matrix)
  {
    if (matrix.length != VSIZE || matrix[0].length != HSIZE) {
      throw new IllegalArgumentException("Maze is not 10x10");
    }
    for (int i = 0; i < HSIZE; ++i) {
      for (int j = 0; j < HSIZE; ++j) {
        _matrix[i][j] = matrix[i][j];
      }
    }
  }

  /** Inputs the maze from the given Reader.  The input is expected to be in a
   * 10x10 matrix of 0's and 1's separated by whitespace.  0's represent empty
   * space, and 1's represent a wall.
   */
  public Maze(Reader r) throws IOException
  {
    try {
      Scanner s = new Scanner(r);
      for (int i = 0; i < HSIZE; ++i) {
        for (int j = 0; j < VSIZE; ++j) {
          int n = s.nextInt();
          if (n == 0) {
            _matrix[i][j] = false;
          } else if (n == 1) {
            _matrix[i][j] = true;
          } else {
            throw new IOException(
                "unexpected value in maze " + Integer.valueOf(n).toString());
          }
        }
      }
    } catch (NoSuchElementException e) {
      throw new IOException(e);
    }
  }

  @Override public String toString()
  {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < HSIZE; ++i) {
      for (int j = 0; j < VSIZE; ++j) {
        builder.append(_matrix[i][j] ? "1 " : "0 ");
      }
      builder.append("\n");
    }
    return builder.toString();
  }

  /** Moves the given direction from the given point.
   *
   * @param point The point that the user is coming from.
   * @param dir The direction that the user is trying to go.
   * @return The point that the user got to, which will be the same point if the
   * user walked into a wall.
   * @throws IllegalArgumentException if the point is outside the range
   * (0,0)-(9,9).
   */
  public Point move(Point point, Direction dir)
  {
    if (point.x < 0 || point.y < 0 || point.x > 9 || point.y > 9) {
      throw new IllegalArgumentException(
          "Point " + point.toString() + " out of range.");
    }

    Point newPoint = dir.step(point);
    return isWall(newPoint) ? point : newPoint;
  }

  /** Returns true if the given point in the maze is a wall. */
  private boolean isWall(Point p)
  {
    return _matrix[p.y][p.x];
  }

  public static void main(String[] args) throws IOException {
    String str =
      "0 0 0 0 0 0 0 0 0 0\n" +
      "1 0 0 0 0 0 0 0 0 0\n" +
      "0 0 0 0 0 0 0 0 0 0\n" +
      "0 0 0 0 0 0 0 0 0 0\n" +
      "0 0 0 0 0 0 0 0 0 0\n" +
      "0 0 0 0 0 0 0 0 0 0\n" +
      "0 0 0 0 0 0 0 0 0 0\n" +
      "0 0 0 0 0 0 0 0 0 0\n" +
      "0 0 0 0 0 0 0 0 0 0\n" +
      "0 0 0 0 0 0 0 0 0 0\n";
    Maze m = new Maze(new StringReader(str));
    Point home = new Point(0,0);
    Point east = new Point(1,0);
    Point test = m.move(home, Direction.SOUTH);
    assert test.equals(home) : "Point should be " + home.toString() + " but is " + test.toString();
    test = m.move(home, Direction.EAST);
    assert test.equals(east) : "Point should be " + east.toString() + " but is " + test.toString();
    System.out.println(m.toString());
  }
}

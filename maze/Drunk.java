// maze/Drunk.java
// MATH 539 Statistics Final Project

package maze;

import java.awt.Point;

/** Represents a drunk college student stumbling around a dark maze. */
public class Drunk
{
  private final Maze _maze;
  private Point _p;

  /** Constructs a drunk at the specified point on the specified maze. */
  public Drunk(Point p, Maze m)
  {
    _maze = m;
    _p = new Point(p);
  }

  /** Tries to move in the given direction.
   * @return {@code true} if the drunk successfully moved; {@code false} if the
   * drunk bumped into a wall
   */
  public boolean move()
  {
    Maze.Direction d = Maze.Direction.getRandomDir();
    Point newPoint = _maze.move(_p, d);
    if (newPoint.equals(_p)) {
      return false;
    } else {
      _p = newPoint;
      return true;
    }
  }
  
  /**
   * Returns the current position of the drunk.
   */
  public Point getPosition()
  {
	  return _p;
  }
}

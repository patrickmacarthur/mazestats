/**
 * maze/Driver.java
 * MATH 539 Statistics Final Project
 */

package maze;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import maze.Drunk;
import maze.Maze;

public class Driver
{
    ArrayList<Integer> list = new ArrayList<Integer>();
    Maze maze;
    Drunk drunk;
    
    //--------------------
    /**
     * Initializes the new Maze object.
     * @param filename of the maze file to load from.
     */
    private void initializeMaze(String filename)
    {
        FileReader r;
        try
        {
            r = new FileReader(filename);
            maze = new Maze(r);
            System.err.println(maze.toString());
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Incorrect filename passed.");
            e.printStackTrace();
            System.exit(1);
        }
        catch (IOException e)
        {
            System.err.println("Error reading maze from file.");
            e.printStackTrace();
            System.exit(1);
        }
        
    }
    
    //--------------------
    /**
     * Runs the simulation.
     * @param n the number of times the simulation is run.
     */
    private void run(int n)
    {
        for(int i = 0; i < n; i++)
        {
            //store the number of steps
            int steps = 0;
            
            //create Drunk at initial position
            drunk = new Drunk(new Point(5,5), maze);
            
            while(!maze.outsideMaze(drunk.getPosition()))
            {
                drunk.move();
                steps++;
            }
            
            //store the number of steps into the arrayList
            list.add(steps);
        }
    }
    
    //--------------------
    /**
     * Prints the contents of the list to file.
     * @throws IOException 
     */
    private void printResults()
    {
        //print out total number of steps to standard output on its own line
        //(should be first n lines of the file, where n is the number of times we
        //are repeating the experiment), possibly store in array or other data
        //structure (needed only if doing the stats at the end)
        for(int i = 0; i < list.size(); i++)
            System.out.println(list.get(i).toString());
    }
    
    
    public static void main(String[] args)
    {
        if(args.length != 1)
        {
            System.err.println("Please provide a maze-file.");
            System.exit(1);
        }
        
        Driver d = new Driver();
        
        //create maze (loaded from file supplied as command-line parameter)
        d.initializeMaze(args[0]);
    
        d.run(1000);
        
        d.printResults();
        
        //optionally: print mean/standard deviation and/or five number summary
        //(min,1st quartile,median,3rd quartile,max) 
    }
}

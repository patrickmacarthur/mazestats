/**
 * maze/Driver.java
 * MATH 539 Statistics Final Project
 */

package maze;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

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
     * @param filename the name of the output file
     * @throws IOException
     */
    private void printResults(String filename)
    {
        FileWriter fw = null;
        BufferedWriter output = null;
        //print out total number of steps to standard output on its own line
        //(should be first n lines of the file, where n is the number of times we
        //are repeating the experiment), possibly store in array or other data
        //structure (needed only if doing the stats at the end)
        try
        {
            fw = new FileWriter(filename);
            output = new BufferedWriter(fw);

            for(Integer i : list)
            {
                //System.err.println(i.toString());
                output.write(i.toString() + "\n");
            }
        }
        catch(IOException e)
        {
            System.err.println("Error writing to results file.");
            e.printStackTrace();
            System.exit(1);
        }
        finally
        {
            try
            {
                if(output != null)    
                    output.close();
            }
            catch(IOException e){}
        }

    }

    //--------------------
    /**
     * Chooses a file using a JFileChooser.
     * @return the chosen filename
     */
    private String chooseFile()
    {
        JFileChooser chooser = new JFileChooser(".");
        FileNameExtensionFilter filter =
            new FileNameExtensionFilter("Maze Files", "txt");
        chooser.setFileFilter(filter);
        int retVal = chooser.showOpenDialog(null);
        if(retVal == JFileChooser.APPROVE_OPTION)
            return chooser.getSelectedFile().getPath();
        else
            return "";
    }

    //--------------------
    /**
     * The main method.
     */
    public static void main(String[] args)
    {
        String filename = "";
        Driver d = new Driver();

        if(args.length == 0)
        {
            filename = d.chooseFile();

            if(filename.equals(""))
            {
                System.err.println("Error selecting file.");
                System.exit(1);
            }
        }
        else if(args.length == 1)
        {
            filename = args[0];
        }
        else
        {
            System.err.println("Incorrect number of arguments passed.");
            System.exit(1);
        }

        //create maze (loaded from file supplied as command-line parameter)
        d.initializeMaze(filename);

        //1000 is the default value
        d.run(1000);

        d.printResults(filename + "-results.txt");

        //optionally: print mean/standard deviation and/or five number summary
        //(min,1st quartile,median,3rd quartile,max)
        
        //This isn't pretty, but it closes the GUI thread.
        System.exit(0);
    }
}

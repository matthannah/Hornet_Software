import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Matt on 15/07/2015.
 */
public class UserInterface {

    /* TOOO DOOOO:


        PUT MORE FUNCTIONALISTY FROM THIS CLASS INTO LIDAR MAP AND FRONT VIEW
        FOR INSTANCE DRAWABLE POINTS SHOULD BE A MEMBER OF THOSE CLASSES
     */




    private static ArrayList<Point3D> drawablePoints = new ArrayList<>();
    private static UserInterface u = new UserInterface();

    public static void main(String [] args) {
        JFrame frame = new JFrame("User Interface");
        frame.setSize(1600, 1600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FrontView panel = new FrontView();

        Container pane = frame.getContentPane();
        pane.setLayout(new GridLayout(1, 1));

        pane.add(panel);
        frame.setVisible(true);

        u.readData();
        panel.draw(drawablePoints);
    }

    public void readData() {
        //THIS WORKS
        // The name of the file to open.
        String fileName = "test2.txt";

        // This will reference one line at a time
        String line = null;

        //testing

        //testing

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                //reads 1 ine at a time then moves to the next
                drawablePoints.add(new Point3D(line));
            }
            // Always close files.
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
    }
}
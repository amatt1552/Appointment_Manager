package scheduleapplication;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.awt.geom.Rectangle2D;

/**
 * Sets up the splash screen.
 * @author Austin Matthews
 */
public class Splash 
{
     
    private static SplashScreen splashScreen;               // splashscreen
    private static Graphics2D splashGraphics;               // graphics context for overlay of the splash image
    private static Rectangle2D.Double splashTextArea;       // area where we draw the text
    private static Rectangle2D.Double splashProgressArea;   // area where we draw the progress bar
    private static Font font;                               // font.


    //I never would have figured this out without this:
    //https://www.howtobuildsoftware.com/index.php/how-do/cuK/java-netbeans-jar-splash-screen-not-display-when-execute-by-jar-file-but-it-works-when-execute-from-netbeans-ide
    
    /**
     * Initialize SplashScreen.
     * @param message
     * @param progress 
     */
    protected static void splashInit(String message, int progress)
    {
    
        // the splash screen object is created by the JVM, if it is displaying a splash image

        splashScreen = SplashScreen.getSplashScreen();
        // if there are any problems displaying the splash image
        // the call to getSplashScreen will return null

        if (splashScreen != null) 
        {
            // get the size of the image now being displayed
            Dimension splashDimensions = splashScreen.getSize();
            int height = splashDimensions.height;
            int width = splashDimensions.width;

            // picking an area for our loading information
            splashTextArea = new Rectangle2D.Double(20, height * 0.90, width * 0.95, 20);
            splashProgressArea = new Rectangle2D.Double(20, height * .87, width * 0.95, 5);

            // create the Graphics environment for diplaying the loading info
            splashGraphics = splashScreen.createGraphics();
            font = new Font("Arial", Font.BOLD, 14);
            
            //add font to graphic
            splashGraphics.setFont(font);

            // initialize the status info
            splashText(message);
            splashProgress(progress);
        }
    }
    
   
    /**
     * Display text in status area of Splash.
     * @param text
     */
    protected static void splashText(String text) 
    {
        if (splashScreen != null && splashScreen.isVisible()) 
        {

           // sets background to gray
           splashGraphics.setPaint(new Color(235,235,235));
           splashGraphics.fill(splashTextArea);

           // draw the text
           splashGraphics.setPaint(Color.BLACK);
           splashGraphics.drawString(text, (int) (splashTextArea.getX() + 10), (int) (splashTextArea.getY() + 15));

           // make sure it's displayed
           splashScreen.update();
        }
    }

    
    /**
     * Display a basic progress bar.
     * @param percent 
     */
    protected static void splashProgress(int percent) 
    {
        if (splashScreen != null && splashScreen.isVisible()) 
        {
            // draw an outline
            splashGraphics.setPaint(Color.WHITE);
            splashGraphics.fill(splashProgressArea);

            //position
            int x = (int) splashProgressArea.getMinX();
            int y = (int) splashProgressArea.getMinY();
            //scale
            int width = (int) splashProgressArea.getWidth();
            int height = (int) splashProgressArea.getHeight();

            //makes percent into 0 to 1 then multiples by width
            //width is 500 percent is 50. returns 250
            int doneWidth = Math.round(percent * width / 100.f);
            //math.max returns the greater of 2 values. Min vice versa. 
            //thus it will always pick the largest value between 0 and min(donewidth, width -1)
            doneWidth = Math.max(0, Math.min(doneWidth, width - 1));

            //fill the progress bar
            splashGraphics.setPaint(Color.ORANGE);
            splashGraphics.fillRect(x, y, doneWidth, height);

            //make sure it's displayed
            splashScreen.update();
        }
    }
    
    /**
     * Calls splashText and splashProgress.
     * @param message
     * @param progress 
     */
    protected static void splashUpdate(String message, int progress)
    {
        splashText(message);
        splashProgress(progress);
    }
    
    /**
     * Closes SplashScreen.
     */
    protected static void closeSplash()
    {
        if (splashScreen != null) 
        {
            splashScreen.close();
        }
    }
}

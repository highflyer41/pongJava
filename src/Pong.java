import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

//extends jpanel instead of canvas to use paintcomponent
@SuppressWarnings("serial")
public class Pong extends JPanel
{
	Point delta;
	Ellipse2D.Double ball;
    Rectangle paddle1;
    Rectangle paddle2;
    int score1 = 0;
	int score2 = 0;
	
	public static void main( String[] args )
	{
		JFrame win = new JFrame("Pong");
		win.setSize(1010,735);
		win.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		win.add( new Pong() );
		win.setVisible(true);
	}

	public Pong()
	{
		enableEvents(java.awt.AWTEvent.KEY_EVENT_MASK);
		requestFocus();
		
		ball = new Ellipse2D.Double(500,350,20,20);
		delta = new Point(-5,5);
        paddle1 = new Rectangle(50,450,20,200);
        paddle2 = new Rectangle(940,250,20,200);

        //action listener for swing timer
        ActionListener task = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doStuff();
				repaint();

				//stops the game once a player wins
				if(score1 == 5 || score2 == 5) {
					((javax.swing.Timer)e.getSource()).stop(); //stops the swing timer
				}
				
            }
        };

        //swing timer instead of util timer. better for gui related repaints
        javax.swing.Timer timer = new javax.swing.Timer(10, task);
		timer.start();
		
		//old code, java util timer
        // Timer t = new Timer(true);
		// t.schedule( new java.util.TimerTask()
		// {
		// 	public void run()
		// 	{
		// 		doStuff();
		// 		repaint();
		// 	}
		// }, 10, 10);

    }

    //paintcomponent instead of paint. solves flickering issue
	public void paintComponent( Graphics g )
	{
        //paints the components: ball, paddles, text
        super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setColor(Color.black);
		g2.fill(ball);
		
		g2.setColor(Color.blue);
        g2.fill(paddle1);
        
        g2.setColor(Color.red);
		g2.fill(paddle2);
		
		//draws title on screen
		g2.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		g2.drawString("JAVA PONG", 415, 50);

        //updates the score and draws to the screen
		g2.setColor(Color.black);
		g2.setFont(new Font("TimesRoman", Font.PLAIN, 15));
        g2.drawString("Player 1 Score: " + score1, 300, 100);
		g2.drawString("Player 2 Score: " + score2, 600, 100);
		
		//prints the winner on screen
		if(score1 == 5) {
			g2.setColor(Color.red);
			g2.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			g2.drawString("Player 1 WINS", 450, 300);
		}
		if(score2 == 5) {
			g2.setColor(Color.red);
			g2.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			g2.drawString("Player 2 WINS", 450, 300);
		}
	}

    //process key presses to move paddles
	public void processKeyEvent(KeyEvent e)
	{
		if ( e.getID() == KeyEvent.KEY_PRESSED )
		{
			if ( e.getKeyCode() == KeyEvent.VK_W )
			{
				paddle1.y -= 10; 
			}
			if ( e.getKeyCode() == KeyEvent.VK_S )
			{
				paddle1.y += 10;
            }
            if ( e.getKeyCode() == KeyEvent.VK_UP )
			{
				paddle2.y -= 10;
            }
            if ( e.getKeyCode() == KeyEvent.VK_DOWN )
			{
				paddle2.y += 10;
			}
		}
	}
	
	public void doStuff()
	{
		ball.x += delta.x;
        ball.y += delta.y;
        
        //sets boundaries for paddles
        if(paddle1.y < 5) 
            paddle1.y = 5;
        if(paddle1.y > 495)
            paddle1.y = 495;
        if(paddle2.y < 5) 
            paddle2.y = 5;
        if(paddle2.y > 495)
            paddle2.y = 495;

		// and bounce if we hit a wall
		if ( ball.y < 0 || ball.y+20 > 700 )
			delta.y = -delta.y;
			
		// check if the ball is hitting the paddle
		if ( ball.intersects(paddle1) )
            delta.x = -delta.x;
        if ( ball.intersects(paddle2) )
			delta.x = -delta.x;
			
		// check for scoring
		if ( ball.x > 1000 )
		{
			score1++;
			ball.x = 500;
			ball.y = 350;
			delta = new Point(0,0); //pauses the ball after each point
			
			ActionListener task = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					delta = new Point(-5,5); //sets ball movement increments
				}
			};
	
			//swing timer to pause then start game after 1 second
			javax.swing.Timer timer = new javax.swing.Timer(1000, task);
			timer.setRepeats(false);
			timer.start();
            
        }
        if (ball.x < 10 ) {
			score2++;
            ball.x = 500;
			ball.y = 350;
			delta = new Point(0,0); 
			
			ActionListener task = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					delta = new Point(5,5);
				}
			};
	
			javax.swing.Timer timer = new javax.swing.Timer(2000, task);
			timer.setRepeats(false);
			timer.start();
        }
			
	}
	
	public boolean isFocusable() { return true;	}
}
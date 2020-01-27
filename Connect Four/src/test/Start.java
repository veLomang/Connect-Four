package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

//////// Connect Four //////// 

// Ascending and Descending methods need work

//////////////////////////////
public class Start extends JPanel implements Runnable,KeyListener {
    public static final long serialVersionUID = 1L;

    public static final int ROWS = 7, COLS = 6;
    public static final int SIZE = 50, GAP = 10;
    public static final int YGAP = SIZE+(GAP*2);
    public static final int WIDTH = (ROWS*SIZE)+(ROWS*GAP)+GAP;
    public static final int HEIGHT = (COLS*SIZE)+(COLS*GAP)+GAP+YGAP;

    private Thread thread;

    Color c0 = new Color(20,143,239);
    Color c1 = new Color(225,218,0);
    Color c2 = Color.RED;

    Color[][] grid;
    
    int counter = 0;
    int selection = 0;

    boolean complete = false;
    boolean turn = false;
    boolean running;

    public Start() {
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        this.setFocusable(true);
        this.addKeyListener(this);

        init();
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Connect Four");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new Start());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void init() {
    	grid = new Color[ROWS][COLS];
    	
        for(int x=0;x<ROWS;x++)        	
        	for(int y=0;y<COLS;y++)
        		grid[x][y] = c0;
        
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public void run() {
        while(true) {

            tick();
            repaint();

            try {
                Thread.sleep(100);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void tick() {
    	if(counter == COLS*ROWS)
    		complete = true;
    	
    	if(!running) complete = true;
    	
    	if(checkWinner(c1)) 
    		running = false;
    	else if(checkWinner(c2)) 
    		running = false;
    }

    public void paintComponent(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.clearRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(c0);
        g.fillRect(0, 0, WIDTH, YGAP);

        for(int x=0;x<ROWS;x++)
            for(int y=0;y<COLS;y++) {
                g.setColor(grid[x][y]);
                g.fillOval((x*SIZE)+(x*GAP)+GAP, (y*SIZE)+(y*GAP)+GAP+YGAP, SIZE, SIZE);
            }

        if(turn)
            g.setColor(c1);
        else
            g.setColor(c2);

        if(!complete)
        	g.fillOval((selection*SIZE)+(selection*GAP)+GAP, GAP, SIZE, SIZE);
        else {
        	g.setColor(Color.BLACK);
        	g.setFont(new Font("Verdana",Font.BOLD,60));
        	
        	if(turn)
        		g.drawString("Red Wins!", 50, 60);
        	else
        		g.drawString("Yellow Wins!", 10, 60);
        }
    }
    
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();

        if(!running)
        	return;
        
        if(k == KeyEvent.VK_RIGHT) {
            selection++;

            if(selection >= ROWS) selection = ROWS-1;
        } else if(k == KeyEvent.VK_LEFT) {
            selection--;

            if(selection < 0) selection = 0;
        } else if(k == KeyEvent.VK_SPACE) {
            drop(selection);
            turn = !turn;
        }
    }

    public void drop(int x) {
        for(int y=COLS-1;y>=0;y--) {
            if(grid[x][0] != c0) {
                turn = !turn;
                return;
            } else if(grid[x][y] == c0) {
                if(turn)
                    grid[x][y] = c1;
                else
                    grid[x][y] = c2;
                
                counter++;
                return;
            }
        }
    }
    
    public boolean checkWinner(Color player) {
    	int count=0;
    	
    	// Descending Check    	
    	for(int x1=0;x1<ROWS;x1++) {
    		for(int y1=0;y1<COLS;y1++) {
    			for(int x=0,y=0;x<ROWS&&y<COLS;x++,y++) {
    				try {    					
	    				if(grid[x1+x][y1+y] == player)
	        				count++;
	    				else
	    					count=0;
	            			
	            		if(count==4) 
	            			return true;
    				} catch(Exception e) {
    					count = 0;
    				}
    			}
    		}
    	}
    	
    	// Ascending Check    	
    	for(int x1=0;x1<ROWS;x1++) {
    		for(int y1=0;y1<COLS;y1++) {
    			for(int x=0,y=0;x<ROWS&&y<COLS;x++,y++) {
    				try {    					
	    				if(grid[x1+x][y1-y] == player)
	        				count++;
	    				else
	    					count=0;
	            			
	            		if(count==4) 
	            			return true;
    				} catch(Exception e) {
    					count = 0;
    				}
    			}
    		}
    	}
		
    	// Horizontal Check
    	for(int x=0;x<ROWS;x++) {
    		for(int y=0;y<COLS;y++) {
    			if(grid[x][y] == player)
    				count++;
    			else
    				count=0;
    			
    			if(count==4) 
    				return true;
    		}
    		count=0;
    	}
    			
    	// Vertical Check
    	for(int y=0;y<COLS;y++) {
    		for(int x=0;x<ROWS;x++) {
    			if(grid[x][y] == player)
    				count++;
    			else
    				count=0;
    			
    			if(count==4) 
    				return true;
    		}
    		count=0;
    	}
    	return false;
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

}
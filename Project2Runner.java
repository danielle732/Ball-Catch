import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
public class Project2Runner extends JPanel implements ActionListener {
    /*
     * Name: Danielle Eng
     * Student ID: 501303114
     * 
     ******** Project Description ********
     * 
     * This project is an interactive game called "Catch the Balls". Players control a basket at the bottom
     * of the screen using the left and right arrow keys to catch the falling coloured balls. 
     * Successfully catching a ball increases the score, while missing a ball reduces the player's lives. 
     * Players are given 3 lives and the game ends when all lives are lost at which point they have the option to
     * restart or exit the game. The game speeds up as the score increases, adding a challenage as the game continues. 
     * 
     ******** Swing Requirement ********
     * 
     * 1. JLabel (scoreText): Displays the score and remaining lives.
     * 2. JButton (pause button): used to pause the game.
     * 3. JFrame: main game window 
     * 
     * 
     * 
     ******** 2D Graphics Requirement ********
     *
     * The game involves using a JPanel for 2D graphics. The game panel extends JPanel 
     * and overrises the paintCompnents method to handle the rendering of the basket, falling
     * balls andupdates teh game visuas 
     * The paintCompoent method uses the Graphics object to draw the elements of the game on the screen.
     * 
     * 
     * 
     ******** Event Listener Requirement ********
     *
     * The program implements an actionPerformed method which controls the game logic
     * and updates the positioning of the falling balls, checking for collisions with the basket and 
     * managing the score and lives. 
     * 
     * The keyPressed method inside a keyAdapter is the event listener so players can control the basket
     * using their left and right arrow keys. 
    */

        //Game vairbales 
         private int basketX = 250;
         private int basketY = 450;
         private int basketWidth = 100;
         private int basketHeight = 40;
         private int ballX, ballY;
         private int ballSize = 20;
         private int score = 0;
         private int lives = 3;
         private Timer timer;
         private Random rand = new Random();
         private JLabel scoreText;
         private JButton pause;
         private boolean isPaused = false;
         private int speed = 10;
         private Color ballColour;

     
         public Project2Runner() {
            //window features
             setPreferredSize(new Dimension(500, 500));
             setBackground(Color.BLACK);
             setFocusable(true);
             setLayout(new BorderLayout());
     
             JPanel topPanel = new JPanel();
             topPanel.setBackground(Color.BLACK);
            
             //label to display score and lives
             scoreText = new JLabel("Score: 0 | Lives: 3");
             scoreText.setForeground(Color.WHITE);
             topPanel.add(scoreText);
     
             add(topPanel, BorderLayout.NORTH);
            
             //pause buttom
             pause = new JButton("Pause");
             pause.addActionListener(e -> togglePause()); 
             topPanel.add(pause);
             
             add(topPanel, BorderLayout.NORTH);
             //key listense to move the basket with left and right arrow keys
             addKeyListener(new KeyAdapter() {
                 @Override
                 public void keyPressed(KeyEvent e) {
                     if (e.getKeyCode() == KeyEvent.VK_LEFT && basketX > 0) {
                         basketX -= 10;
                     } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && basketX < getWidth() - basketWidth) {
                         basketX += 10;
                     }
                     repaint();
                 }
             });
             
             //sets up the game timer to update the game regularly
             timer = new Timer(10, this);
             timer.start();
             resetBall();
         }
         //toggles the game between pause and resume 
         private void togglePause() {
                // If the game is running, stop it and show the dialog
                timer.stop();
                int choice = JOptionPane.showConfirmDialog(this, "Game is paused. Do you want to exit?", "Paused", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    //exits game if user chooses yes
                    System.exit(0);
                } 
                else {
                    // Resume the game immediately after clicking 'No'
                    timer.start();
                    pause.setText("Pause");
                }
            // Toggle the pause state
            isPaused = false;  
        }
        
        //resets the ball's position
         private void resetBall() {
             int panelWidth = getWidth();
             if (panelWidth <= 0) {
                 panelWidth = 500;
             }
             ballX = rand.nextInt(Math.max(1, panelWidth - ballSize));
             //gets it to start at the top of the panel
             ballY = 0;
             //chooses the colour of the ball randomly
             Color[] colours = {Color.RED, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PINK, Color.MAGENTA, Color.CYAN};
             ballColour = colours[rand.nextInt(colours.length)];
         }
         //restarts the game
         private void restartGame() {
             score = 0;
             lives = 3;
             speed = 5;
             resetBall();
             scoreText.setText("Score: " + score + " | Lives: " + lives);
     
             // Ensure the timer is properly restarted
             if (timer != null) {
                 timer.stop();
             }
              // Shorter timer interval for faster gameplay
             timer = new Timer(10, this);
             timer.start();
     
             requestFocusInWindow();
             repaint();
         }
         //method for when game is over 
         private void gameOver() {
             timer.stop();
             int choice = JOptionPane.showConfirmDialog(this, "Game Over! You dropped 3 fruits.\nYour score: " + score + "\nDo you want to play again?",
                     "Game Over", JOptionPane.YES_NO_OPTION);
             if (choice == JOptionPane.YES_OPTION) {
                 restartGame();
             } 
             else {
                 System.exit(0);
             }
         }
     
         @Override
         public void actionPerformed(ActionEvent e) {
             if (isPaused) return;
             requestFocusInWindow();
         
             // Move the ball down
             ballY += speed;
         
             // Increase speed based on score
             speed = 5 + score / 5;
         
             // If ball reaches the basket 
             if (ballY + ballSize >= basketY && ballX >= basketX && ballX <= basketX + basketWidth) {
                //increases score
                 score++;
                //increases speed depending on score
                 speed = 5 + score / 5;
                 scoreText.setText("Score: " + score + " | Lives: " + lives);
                 // Generate a new falling ball
                 resetBall();  
             } 
             // If the ball falls past the basket 
             else if (ballY > basketY + basketHeight) {
                 lives--;
                 // Update the scoreText when lives are reduced
                 scoreText.setText("Score: " + score + " | Lives: " + lives);
                 
                 if (lives == 0) {
                     gameOver();
                     return;
                 }
                 // Generate a new falling ball
                 resetBall();  
             }
         
             // Update the screen
             repaint();
         }
         

     
         @Override
         protected void paintComponent(Graphics g) {
             super.paintComponent(g);
            //draws the falling ball
             g.setColor(ballColour);
             g.fillOval(ballX, ballY, ballSize, ballSize);
            //draws the basket 
             g.setColor(new Color(139, 69, 19));
             g.fillRect(basketX, basketY, basketWidth, basketHeight);
            //draws the lines of basket
             g.setColor(new Color(100, 50, 0));
             for (int i = 0; i < basketWidth; i += 10) {
                 g.drawLine(basketX + i, basketY, basketX + i, basketY + basketHeight);
             }
             for (int i = 0; i < basketHeight; i += 5) {
                 g.drawLine(basketX, basketY + i, basketX + basketWidth, basketY + i);
             }
         }
     
         public static void main(String[] args) {
             SwingUtilities.invokeLater(() -> {
                 JFrame frame = new JFrame("Catching Fruits");
                 Project2Runner game = new Project2Runner();
                 frame.add(game);
                 frame.pack();
                 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                 frame.setVisible(true);
             });
         }
     }
     
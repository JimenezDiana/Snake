package app;


import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics;





public class Snake extends JFrame{

    int widht = 640;
    int height = 480;
    int lenght = 2;
    Point snake;
    Point comida;


    
    boolean gameOver = false;

    List<Point> positionList = new ArrayList<Point>();

    int widhtPoint = 10;
    int heightPoint = 10;

    String direction = "RIGHT";
    long frecuency = 40;
    ImagenSnake imagenSnake;

    public Snake(){
        setTitle("Snake");
        
        startGame();

        imagenSnake = new ImagenSnake();
        this.getContentPane().add(imagenSnake);
        
        setSize(widht, height);

        this.addKeyListener(new Teclas());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFrame.setDefaultLookAndFeelDecorated(false);
        setUndecorated(true);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    
        setVisible(true);

        Moment momento = new Moment();
        Thread trid = new Thread(momento);
        trid.start();

        //Teclas teclas = new Teclas();
        //this.addKeyListener(teclas);
    }

    public void startGame(){
        comida = new Point(200,100);
        snake = new Point(320, 240);
        positionList = new ArrayList<Point>();
        positionList.add(snake);
        lenght = positionList.size();
    }

    public void generateMeal(){
        Random rnd = new Random();
        comida.x = (rnd.nextInt(widht)) + 5;
        if((comida.x % 5) > 0){
            comida.x = comida.x - (comida.x %5);
        }
        if(comida.x < 5){
            comida.x = comida.x + 10;
        }
        if(comida.x  > widht){
            comida.x = comida.x - 10;
        }
        comida.y = (rnd.nextInt(height)) + 5;
        if((comida.y % 5) > 0){
            comida.y = comida.y - (comida.y % 5);
        }
        if(comida.y > height){
            comida.y = comida.y - 10;
        }
        if(comida.y < 0){
            comida.y = comida.y + 10;
        }
    }

    public void actualizar(){
        
        positionList.add(0, new Point(snake.x, snake.y));
        positionList.remove(positionList.size()-1);

        for(int i = 1; i<positionList.size(); i++){
            Point point = positionList.get(i);
            if(snake.x == point.x && snake.y == point.y){
                gameOver = true;
            }
        }

        if((snake.x > (comida.x - 10) && snake.x < (comida.x + 10)) && (snake.y > (comida.y - 10) && snake.y < (comida.y + 10))){
            positionList.add(0, new Point(snake.x, snake.y));
            System.out.println(positionList.size());
            generateMeal();
        }
        imagenSnake.repaint();
        speed();
    }

    public void speed(){
        if(positionList.size()< 15){
            frecuency = this.frecuency;
        }else if(positionList.size()> 15){
            frecuency = 40;
        }else if(positionList.size() > 22){
            frecuency = 50;
        }else if(positionList.size() > 30){
            frecuency = 60;
        }else if(positionList.size() > 40){
            frecuency = 70;
        }
    }

    public static void main(String[] args) throws Exception {
        Snake snake = new Snake();
    }

    public class ImagenSnake extends JPanel{
        public void paintComponent(Graphics g){
            super.paintComponent(g);

            if(gameOver){
                g.setColor(new Color(0,0,0));
            }else{
                g.setColor(new Color(255,255,255));
            }
            g.fillRect(0,0,widht,height);
            g.setColor(new Color(0,0,255));

            if(positionList.size() > 0){
            for(int i = 0; i < positionList.size(); i++){
                Point point = (Point)positionList.get(i); 
                g.fillRect(point.x, point.y, widhtPoint, heightPoint);
            }
        }
            

            g.setColor(new Color(255,0,0));
            g.fillRect(comida.x, comida.y, widhtPoint, heightPoint);

            if(gameOver){
                //g.drawString("GAME OVER", 200,320);
                g.setFont(new Font("TimesRoman", Font.BOLD, 40));
                g.drawString("GAME OVER", 300, 200);
                g.drawString("SCORE "+(positionList.size()-1), 300, 240);

                g.setFont(new Font("TimesRoman", Font.BOLD, 20));
                g.drawString("N to Start New Game", 100, 320);
                g.drawString("ESC to Exit", 100, 340);
            }
        }
    }

    public class Teclas extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                System.exit(0);
            }else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                if(direction != "LEFT"){
                    direction ="RIGHT";
                }
            }else if(e.getKeyCode() == KeyEvent.VK_LEFT){   
                if(direction != "RIGHT"){
                    direction = "LEFT";
                }
            }else if(e.getKeyCode() == KeyEvent.VK_UP){
                if(direction != "DOWN"){
                    direction = "UP";
                }
            }else if(e.getKeyCode() == KeyEvent.VK_DOWN){
                if(direction != "UP"){
                    direction = "DOWN";
                }
            }else if(e.getKeyCode() == KeyEvent.VK_N){
                gameOver = false;
                startGame();
            }
    }
}

    public class Moment extends Thread{
        private long last = 0;

        public Moment(){};

        public void run(){
            while(true){
                if((java.lang.System.currentTimeMillis() - last) > frecuency){
                    if(!gameOver){
                        if(direction == "RIGHT"){
                            snake.x = snake.x + widhtPoint;
                            if(snake.x > widht){
                                snake.x = 0;
                            }
                        }else if(direction == "LEFT"){
                            snake.x = snake.x - widhtPoint;
                            if(snake.x < 0){
                                snake.x = widht - widhtPoint;
                            }
                        }else if(direction == "UP"){
                            snake.y = snake.y - heightPoint;
                            if(snake.y < 0){
                                snake.y = height;
                            }
                        }else if(direction == "DOWN"){
                            snake.y = snake.y + heightPoint;
                            if(snake.y > height){
                                snake.y = 0;
                            }
                        }
                    }
                    actualizar();
                   last = java.lang.System.currentTimeMillis();
                }
            }
        }
    }
}
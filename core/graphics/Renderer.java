
package core.graphics;

import core.graphics.backend.Canvas;
import core.graphics.backend.ColorRgb;
import core.Game;
import core.Tennis4x4;
import core.objects.*;

/**
 *
 * @author gekoncze
 */
public class Renderer {
    private static final ColorRgb BACKGROUND_COLOR = new ColorRgb(0, 0, 0);
    private static final ColorRgb BALL_COLOR = new ColorRgb(192, 192, 192);
    private static final ColorRgb PLAYER_COLOR = new ColorRgb(64, 64, 255);
    private static final ColorRgb CURRENT_PLAYER_COLOR = new ColorRgb(255, 64, 64);
    private static final ColorRgb WAVE_COLOR = new ColorRgb(255, 0, 0);
    private static final ColorRgb OBSTACLE_COLOR = new ColorRgb(128, 128, 128);
    
    public static void draw(Canvas c, Game g, int playerId){
        drawBackground(c);
        
        int x = 0;
        int y = 0;
        int size = 0;
        int width = c.getWidth();
        int height = c.getHeight();
        
        if(width > height){
            size = height;
            x = (width - size) / 2;
        } else {
            size = width;
            y = (height - size) / 2;
        }
        
        c.clipRectangle(x, y, size, size);
        c.move(x, y);
        
        if(g == null){
            drawTitleScreen(c);
            return;
        }
        
        c.rotate(playerId * 90);
        drawGame(c, g, playerId);
        
        c.loadIdentity();
        c.move(x, y);
        drawScore(c, g, playerId);
    }
    
    private static void drawGame(Canvas c, Game g, int playerId){
        int i = 0;
        for(Player player : g.players){
            drawPlayer(c, player, i == playerId);
            i++;
        }
        
        for(Ball ball : g.balls){
            drawBall(c, ball);
        }
        
        for(Obstacle obstacle : g.obstacles){
            drawObstacle(c, obstacle);
        }
    }
    
    private static void drawScore(Canvas c, Game g, int playerId){
        int ii = 1;
        for(Player player : g.players){
            if((ii-1) == playerId){
                c.setColor(new ColorRgb(255, 128, 128));
            } else {
                c.setColor(new ColorRgb(255, 255, 255));
            }
            int size = Math.min(c.getWidth(), c.getHeight());
            c.drawTextCentered(player.score + "", size * ii / 5, 32, 24);
            ii++;
        }
    }
    
    private static void drawTitleScreen(Canvas c){
        c.setColor(new ColorRgb(255, 255, 255));
        c.drawTextCentered("Tennis4x4", c.getWidth()/2, c.getHeight()/2 - 48, 32);
        c.drawTextCentered("Created by Daniel Tichy", c.getWidth()/2, c.getHeight()/2, 16);
        c.drawTextCentered("A - move left", c.getWidth()/2, c.getHeight()/2 + 16 + 32, 24);
        c.drawTextCentered("D - move right", c.getWidth()/2, c.getHeight()/2 + 16 + 64, 24);
        c.drawTextCentered("SPACE - wave", c.getWidth()/2, c.getHeight()/2 + 16 + 96, 24);
    }
    
    private static void drawBackground(Canvas c){
        c.setColor(BACKGROUND_COLOR);
        c.drawRectangle(0, 0, c.getWidth(), c.getHeight());
    }
    
    private static void drawPlayer(Canvas c, Player player, boolean current){
        c.setColor(WAVE_COLOR);
        lineGameCircle(c, player.position.x, player.position.y, player.wave);
        if(current)
            c.setColor(CURRENT_PLAYER_COLOR);
        else
            c.setColor(PLAYER_COLOR);
        drawGameCircle(c, player.position.x, player.position.y, player.radius);
    }
    
    private static void drawBall(Canvas c, Ball ball){
        c.setColor(BALL_COLOR);
        drawGameCircle(c, ball.position.x, ball.position.y, ball.radius);
    }
    
    private static void drawObstacle(Canvas c, Obstacle obstacle){
        c.setColor(OBSTACLE_COLOR);
        drawGameCircle(c, obstacle.position.x, obstacle.position.y, obstacle.radius);
    }
    
    private static void drawGameCircle(Canvas c, long x, long y, long r){
        int size = Math.min(c.getWidth(), c.getHeight());
        c.drawCircle(r2v(x, size), r2v(y, size), r2v(r, size));
    }
    
    private static void lineGameCircle(Canvas c, long x, long y, long r){
        int size = Math.min(c.getWidth(), c.getHeight());
        c.lineCircle(r2v(x, size), r2v(y, size), r2v(r, size));
    }
    
    private static double r2v(long value, int width){
        return (int) ((value * width) / Tennis4x4.PLAYGROUND_SIZE);
    }
    
    private static long v2r(long value, int width){
        return (value * Tennis4x4.PLAYGROUND_SIZE) / width;
    }
}

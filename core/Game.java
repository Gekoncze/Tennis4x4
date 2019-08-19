
package core;

import core.objects.*;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author gekoncze
 */
public class Game {
    public final Player[] players = new Player[Tennis4x4.PLAYER_COUNT];
    public final Obstacle[] obstacles = new Obstacle[Tennis4x4.OBSTACLE_COUNT];
    public final LinkedList<Ball> balls = new LinkedList<>();
    private int stepId = 0;

    public Game(){
        createPlayers();
        createObstacles();
    }

    public int getStepId() {
        return stepId;
    }
    
    private void createPlayers(){
        int ps = Tennis4x4.PLAYGROUND_SIZE;
        int center = ps / 2;
        int borderSize = -ps / 30;
        
        players[0] = new Player(new Point2L(center, ps - borderSize),
                Player.PlayerDirection.BOTTOM);
        players[1] = new Player(new Point2L(ps - borderSize, center),
                Player.PlayerDirection.RIGHT);
        players[2] = new Player(new Point2L(center, borderSize),
                Player.PlayerDirection.TOP);
        players[3] = new Player(new Point2L(borderSize, center),
                Player.PlayerDirection.LEFT);
    }
    
    private void createObstacles(){
        int ps = Tennis4x4.PLAYGROUND_SIZE;
        obstacles[0] = new Obstacle(new Point2L(0, 0));
        obstacles[1] = new Obstacle(new Point2L(0, ps));
        obstacles[2] = new Obstacle(new Point2L(ps, ps));
        obstacles[3] = new Obstacle(new Point2L(ps, 0));
    }
    
    public void step(PlayerActions actions) throws GameException {
        if(actions.stepId != stepId){
            throw new GameException(
                    "Wrong step id." +
                    " Expected " + stepId +
                    " got " + actions.stepId
            );
        }
        
        for(int i = 0; i < 4; i++){
            players[i].step(actions.player[i], this);
        }
        
        createBalls();
        
        Iterator<Ball> iterator = balls.iterator();
        for(;iterator.hasNext();){
            Ball ball = iterator.next();
            ball.step(this);
            
            if(ball.position.x < 0){
                players[3].score++;
                iterator.remove();
            } else if(ball.position.x > Tennis4x4.PLAYGROUND_SIZE){
                players[1].score++;
                iterator.remove();
            } else if(ball.position.y < 0){
                players[2].score++;
                iterator.remove();
            } else if(ball.position.y > Tennis4x4.PLAYGROUND_SIZE){
                players[0].score++;
                iterator.remove();
            }
        }
        
        stepId++;
    }
    
    private void createBalls(){
        if(stepId % Tennis4x4.NEW_BALL_STEP_DELAY == 0 && stepId != 0){
            long sx = 0;
            long sy = 0;
            int modulo = (stepId / Tennis4x4.NEW_BALL_STEP_DELAY) % 4;
            if(modulo == 0){
                sy = Tennis4x4.BALL_DEFAULT_SPEED;
            } else if(modulo == 1){
                sx = Tennis4x4.BALL_DEFAULT_SPEED;
            } else if(modulo == 2){
                sy = -Tennis4x4.BALL_DEFAULT_SPEED;
            } else {
                sx = -Tennis4x4.BALL_DEFAULT_SPEED;
            }
            
            balls.add(new Ball(
                    new Point2L(
                            Tennis4x4.PLAYGROUND_SIZE / 2,
                            Tennis4x4.PLAYGROUND_SIZE / 2
                    ), new Point2L(
                            sx,
                            sy
                    ))
            );
        }
    }
}

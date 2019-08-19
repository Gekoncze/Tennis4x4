
package core.objects;

import core.Game;
import core.PlayerAction;
import core.Point2L;
import core.Tennis4x4;

/**
 *
 * @author gekoncze
 */
public class Player extends GameObject {
    
    public enum PlayerDirection {
        BOTTOM,
        RIGHT,
        TOP,
        LEFT
    }
    
    public static final int radius = Tennis4x4.PLAYGROUND_SIZE / 12;
    private static final int ACCELERATION = 1000;
    private static final int ENVIRONMENT_RESISTANCE = 400;
    private static final int MAX_SPEED = 10000;
    private static final int WAVE_SPEED = 8000;
    public static final int WAVE_SIZE_LIMIT = 200000;
    
    private final PlayerDirection direction;
    public int score = 0;
    public int wave = 0;
    
    public Player(Point2L position, PlayerDirection direction){
        this.position = position;
        this.direction = direction;
    }

    @Override
    public int getRadius() {
        return radius;
    }
    
    private static long bringValueToZero(long value, int speed){
        if(value > 0){
            value -= ENVIRONMENT_RESISTANCE;
            if(value < 0) value = 0;
        } else if(value < 0){
            value += ENVIRONMENT_RESISTANCE;
            if(value > 0) value = 0;
        }
        
        return value;
    }
    
    private static void speedPerformResistance(Point2L speed, int resistance){
        speed.x = bringValueToZero(speed.x, resistance);
        speed.y = bringValueToZero(speed.y, resistance);
    }
    
    private static long valueUp(long value, int amount, int max){
        value += amount;
        if(value > max) value = max;
        return value;
    }
    
    private static long valueDown(long value, int amount, int min){
        value -= amount;
        if(value < min) value = min;
        return value;
    }
    
    public void step(PlayerAction action, Game game){
        Point2L oldPosition = new Point2L(position);
        boolean moveLeft = action.action[PlayerAction.MOVE_LEFT] && !action.action[PlayerAction.MOVE_RIGHT];
        boolean moveRight = !action.action[PlayerAction.MOVE_LEFT] && action.action[PlayerAction.MOVE_RIGHT];
        boolean left = direction == PlayerDirection.LEFT;
        boolean right = direction == PlayerDirection.RIGHT;
        boolean top = direction == PlayerDirection.TOP;
        boolean bottom = direction == PlayerDirection.BOTTOM;
        
        speedPerformResistance(speed, ENVIRONMENT_RESISTANCE);
        
        if(moveLeft){
            if(bottom){
                speed.x = valueDown(speed.x, ACCELERATION, -MAX_SPEED);
            } else if(right) {
                speed.y = valueUp(speed.y, ACCELERATION, MAX_SPEED);
            } else if(top){
                speed.x = valueUp(speed.x, ACCELERATION, MAX_SPEED);
            } else if(left){
                speed.y = valueDown(speed.y, ACCELERATION, -MAX_SPEED);
            }
        } else if(moveRight){
            if(bottom){
                speed.x = valueUp(speed.x, ACCELERATION, MAX_SPEED);
            } else if(right) {
                speed.y = valueDown(speed.y, ACCELERATION, -MAX_SPEED);
            } else if(top){
                speed.x = valueDown(speed.x, ACCELERATION, -MAX_SPEED);
            } else if(left){
                speed.y = valueUp(speed.y, ACCELERATION, MAX_SPEED);
            }
        }
        
        position.plus(speed);
        
        for(Obstacle obstacle : game.obstacles){
            if(collide(obstacle)){
                speed.x = 0;
                speed.y = 0;
                break;
            }
        }
        
        if(top || bottom){
            position.y = oldPosition.y;
        } else {
            position.x = oldPosition.x;
        }
        
        if(action.action[PlayerAction.WAVE]) wave = 1;
        updateWave();
    }
    
    private void updateWave(){
        if(wave > 0){
            wave += WAVE_SPEED;
            if(wave > WAVE_SIZE_LIMIT){
                wave = 0;
            }
        }
    }
}

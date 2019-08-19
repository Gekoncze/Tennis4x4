
package core;

/**
 *
 * @author gekoncze
 */
public class Tennis4x4 {
    public static final int PLAYER_COUNT = 4;
    public static final int OBSTACLE_COUNT = 4;
    public static final int PLAYGROUND_SIZE = 1000000;
    public static final int STEP_TIME = 10;
    public static final int INPUT_TIME = STEP_TIME * 4;
    public static final int DEFAULT_PORT = 56789;
    public static final int STEP_INPUT_SKIP_COUNT = 10;
    public static final int NEW_BALL_STEP_DELAY = 100;
    public static final int BALL_DEFAULT_SPEED = 2500;
    
    public static long getCurrentTime(){
        return System.currentTimeMillis();
    }
    
    public static long getExpectedStep(long startupTime){
        long delta = getCurrentTime() - startupTime;
        long expectedStepId = delta / STEP_TIME;
        return expectedStepId;
    }
    
    public static boolean isTimeForStep(long startupTime, int stepId){
        long delta = getCurrentTime() - startupTime;
        long expectedStepId = delta / STEP_TIME;
        return expectedStepId >= stepId;
    }
    
    public static void checkPlayerBounds(int playerId) throws GameException {
        if(playerId < 0 || playerId >= PLAYER_COUNT){
            throw new GameException("Player id out of bounds: " + playerId + " out of " + PLAYER_COUNT);
        }
    }
}

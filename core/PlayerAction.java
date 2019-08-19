
package core;

/**
 *
 * @author gekoncze
 */
public class PlayerAction {
    public static final byte UNUSED = 0;
    public static final byte MOVE_LEFT = 1;
    public static final byte MOVE_RIGHT = 2;
    public static final byte WAVE = 3;
    
    public static final byte ACTION_COUNT = 4;

    public boolean[] action = new boolean[ACTION_COUNT];
    
    public PlayerAction copyNoWave(){
        PlayerAction rval = new PlayerAction();
        for(int i = 0; i < ACTION_COUNT; i++){
            if(i != WAVE)
                rval.action[i] = action[i];
        }
        return rval;
    }
}
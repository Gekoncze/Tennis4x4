
package core;

/**
 *
 * @author gekoncze
 */
public class PlayerActions {
    public int stepId;
    public PlayerAction[] player = new PlayerAction[Tennis4x4.PLAYER_COUNT];

    public PlayerActions(int stepId) {
        this.stepId = stepId;
        for(int i = 0; i < player.length; i++){
            player[i] = new PlayerAction();
        }
    }
    
    public PlayerActions copyNoWave(int newStepId){
        PlayerActions rval = new PlayerActions(newStepId);
        for(int i = 0; i < player.length; i++){
            rval.player[i] = player[i].copyNoWave();
        }
        return rval;
    }
}

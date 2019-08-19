
package core.objects;

import core.Game;
import core.Point2D;
import core.Point2L;
import core.Tennis4x4;

/**
 *
 * @author gekoncze
 */
public class Ball extends GameObject {
    public static final int radius = Tennis4x4.PLAYGROUND_SIZE / 40;

    public Ball(Point2L position, Point2L speed){
        this.position = position;
        this.speed = speed;
    }
    
    @Override
    public int getRadius() {
        return radius;
    }

    public void step(Game game){
        position.plus(speed);
        
        for(Obstacle obstacle : game.obstacles){
            if(collide(obstacle)){
                bounce(obstacle);
            }
        }
        
        for(Player player : game.players){
            if(collide(player)){
                bounce(player);
            }
            if(player.wave > 0 && collide(player.position, player.wave)){
                setWaveSpeedVector(player);
            }
        }
        
        for(Ball ball : game.balls){
            if(this == ball) continue;
            if(collide(ball)){
                bounce2(ball);
            }
        }
    }
    
    private void setWaveSpeedVector(Player player){
        Point2D sp = new Point2D(speed.x, speed.y);
        Point2D pos = new Point2D(position.x, position.y);
        Point2D ppos = new Point2D(player.position.x, player.position.y);
        Point2D speedVector = Point2D.minus(pos, ppos);
        double svLenght = speedVector.getLength();
        double newLength = Player.WAVE_SIZE_LIMIT - svLenght;
        newLength /= 4;
        if(newLength < 1) newLength = 1;
        newLength = Math.max(sp.getLength(), newLength);
        speedVector.divide(svLenght);
        speedVector.multiply(newLength);
        speed.x = (long) speedVector.x;
        speed.y = (long) speedVector.y;
    }
    
    private void bounce(GameObject o){
        Point2D cs = new Point2D(speed.x, speed.y);
        Point2D cso = new Point2D(o.speed.x, o.speed.y);
        Point2D cp = new Point2D(position.x, position.y);
        Point2D cpo = new Point2D(o.position.x, o.position.y);
        
        double angle = Point2D.minus(cpo, cp).getAngle();
        cs.rotate(-angle);
        cs.x = -cs.x;
        cs.rotate(angle);
        
        Point2D speedup = new Point2D(cso);
        speedup.multiply(Point2D.cosAngle(cs, cso));
        speedup.divide(2);
        cs = Point2D.plus(cs, speedup);
        
        speed.x = (long) cs.x;
        speed.y = (long) cs.y;
    }
    
    private void bounce2(GameObject o){
        Point2D cs = new Point2D(speed.x, speed.y);
        Point2D cso = new Point2D(o.speed.x, o.speed.y);
        Point2D cp = new Point2D(position.x, position.y);
        Point2D cpo = new Point2D(o.position.x, o.position.y);
        
        double angle = Point2D.minus(cpo, cp).getAngle();
        cs.rotate(-angle);
        cs.x = -cs.x;
        cs.rotate(angle);
        
        cso.rotate(-angle);
        cso.x = -cs.x;
        cso.rotate(angle);
        
        /*Point2D speedup = new Point2D(cso);
        speedup.multiply(Point2D.cosAngle(cs, cso));
        speedup.divide(2);
        cs = Point2D.plus(cs, speedup);*/
        
        speed.x = (long) cs.x;
        speed.y = (long) cs.y;
    }
}


package core.objects;

import core.Point2D;
import core.Point2L;

/**
 *
 * @author gekoncze
 */
public abstract class GameObject {
    public Point2L position = new Point2L(0, 0);
    public Point2L speed = new Point2L(0, 0);
    
    public abstract int getRadius();
    
    public boolean collide(GameObject o){
        return collide(o.position, o.getRadius());
    }
    
    public boolean collide(Point2L oposition, int radius){
        Point2D cp = new Point2D(position.x, position.y);
        Point2D cpo = new Point2D(oposition.x, oposition.y);
        
        double centerDistance = Point2D.distance(cp, cpo);
        double distance = centerDistance - (getRadius() + radius);
        if(distance < 0){
            Point2D mdir = Point2D.minus(cp, cpo);
            cp = Point2D.minus(cp, mdir.normalize().multiply(distance));
            position.x = (long) cp.x;
            position.y = (long) cp.y;
            return true;
        } else {
            return false;
        }
    }
}

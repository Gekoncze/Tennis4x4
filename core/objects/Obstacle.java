
package core.objects;

import core.Point2L;
import core.Tennis4x4;

/**
 *
 * @author gekoncze
 */
public class Obstacle extends GameObject {
    public static final int radius = Tennis4x4.PLAYGROUND_SIZE / 10;

    public Obstacle(Point2L position) {
        this.position = position;
    }
    
    @Override
    public int getRadius() {
        return radius;
    }
}

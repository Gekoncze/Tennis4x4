
package core;

/**
 *
 * @author gekoncze
 */
public class Point2L {
    public long x, y;
    
    public Point2L(long x, long y){
        this.x = x;
        this.y = y;
    }
    
    public Point2L(Point2L p){
        x = p.x;
        y = p.y;
    }
    
    public void plus(Point2L p){
        x += p.x;
        y += p.y;
    }
    
    public void minus(Point2L p){
        x -= p.x;
        y -= p.y;
    }
    
    /*public static Point2L plus(Point2L p1, Point2L p2){
        return new Point2L(p1.x + p2.x, p1.y + p2.y);
    }
    
    public static Point2L minus(Point2L p1, Point2L p2){
        return new Point2L(p1.x - p2.x, p1.y - p2.y);
    }*/
}

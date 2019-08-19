
package core;

/**
 *
 * @author gekoncze
 */
public class Point2D {
    public double x, y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public Point2D(Point2D p){
        x = p.x;
        y = p.y;
    }
    
    public static double distance(Point2D p1, Point2D p2){
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        return Math.sqrt(dx*dx + dy*dy);
    }
    
    public double getAngle(){
        double temp = Math.abs(Math.atan(y/x));
        
        if(x >= 0 && y >= 0){
            return temp;
        } else if(x < 0 && y >= 0){
            return Math.PI - temp;
        } else if(x < 0 && y < 0){
            return Math.PI + temp;
        } else {
            return 2*Math.PI - temp;
        }
    }
    
    public Point2D rotate(double angle){
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        
        double px = x;
        double py = y;
        
        x = px*c - py*s;
        y = px*s + py*c;
        
        return this;
    }
    
    public double getLength(){
        return Math.sqrt(x*x + y*y);
    }
    
    public static Point2D normalized(Point2D p){
        double length = p.getLength();
        return new Point2D(p.x / length, p.y / length);
    }
    
    public Point2D normalize(){
        return divide(getLength());
    }
    
    public Point2D divide(double value){
        x /= value;
        y /= value;
        return this;
    }
    
    public Point2D multiply(double value){
        x *= value;
        y *= value;
        return this;
    }
    
    public Point2D plus(Point2D p){
        x += p.x;
        y += p.y;
        return this;
    }
    
    public static Point2D plus(Point2D p1, Point2D p2){
        return new Point2D(p1.x + p2.x, p1.y + p2.y);
    }
    
    public static Point2D minus(Point2D p1, Point2D p2){
        return new Point2D(p1.x - p2.x, p1.y - p2.y);
    }
    
    public static double dotProduct(Point2D p1, Point2D p2){
        return p1.x * p2.x + p1.y * p2.y;
    }
    
    public static double cosAngle(Point2D p1, Point2D p2){
        double l1 = p1.getLength();
        double l2 = p2.getLength();
        if(Math.abs(l1*l2) < 0.0001) return 0;
        return dotProduct(p1, p2) / (l1 * l2);
    }
}

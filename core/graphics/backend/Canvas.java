
package core.graphics.backend;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Objects;

/**
 *
 * @author gekoncze
 */
public class Canvas {
    private final Graphics2D g;
    private final int width;
    private final int height;
    private final AffineTransform identity;
    
    public Canvas(Graphics2D g, int width, int height) {
        Objects.requireNonNull(g);
        this.g = g;
        this.width = width;
        this.height = height;
        identity = g.getTransform();
    }
    
    public void setColor(ColorRgb c){
        g.setColor(c.c);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    public void drawCircle(double x, double y, double r){
        g.fillOval((int)(x-r), (int)(y-r), (int)(2*r), (int)(2*r));
    }
    
    public void lineCircle(double x, double y, double r){
        g.drawOval((int)(x-r), (int)(y-r), (int)(2*r), (int)(2*r));
    }
    
    public void drawRectangle(double x, double y, double width, double height){
        g.fillRect((int)(x), (int)(y), (int)(width), (int)(height));
    }
    
    public void drawRectangle(int x, int y, int width, int height){
        g.fillRect(x, y, width, height);
    }
    
    public void drawTextCentered(String text, int x, int y, int size){
        g.setFont(new Font("Arial", Font.BOLD, size));
        int xx = (int) g.getFontMetrics().getStringBounds(text, g).getWidth();
        g.drawString(text, x - xx / 2, y);
    }
    
    public void loadIdentity(){
        g.setTransform(identity);
    }
    
    public void rotate(int angle){
        g.rotate(angle * Math.PI / 180.0, width / 2.0, height / 2.0);
    }
    
    public void move(int x, int y){
        g.translate(x, y);
    }
    
    public void clipRectangle(int x, int y, int w, int h){
        g.clipRect(x, y, w, h);
    }
}

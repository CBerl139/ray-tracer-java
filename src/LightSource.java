import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

public class LightSource extends Point3D{
    private Color color;
    public LightSource(Point3D center, Color color) {
        super(center.getX(),center.getY(),center.getZ());
        this.color = color;
    }
    @Override
    public LightSource multiply(double v){
        return new LightSource(super.multiply(1.0 / 50), color);
    }
    @Override
    public LightSource add(Point3D other){
        return new LightSource(super.add(other),color);
    }
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }
}

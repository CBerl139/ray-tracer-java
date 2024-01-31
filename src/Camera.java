import javafx.geometry.Point3D;

public class Camera {
    private Point3D position;
    private Point3D direction;
    private double distanceToScreen;
    public Camera(Point3D position, double distanceToScreen){
        this.position = position;
        this.distanceToScreen = distanceToScreen;
        this.direction = new Point3D(0,0,1).normalize();
    }
    public double getDistanceToScreen(){
        return distanceToScreen;
    }
    public Point3D getDirection(){
        return direction;
    }
    public Point3D getPosition(){
        return position;
    }
    public void setPosition(Point3D position){
        this.position = position;
    }

    public void setDirection(Point3D direction) {
        this.direction = direction;
    }

    public void setDistanceToScreen(double distanceToScreen) {
        this.distanceToScreen = distanceToScreen;
    }
}

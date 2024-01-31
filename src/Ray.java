import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Vector;

public class Ray {
    private Point3D origin;
    private Point3D direction;
    private Point3D hitPoint;
    private Object3D objectHit;
    private Color color;
    public Ray(Point3D origin, Point3D direction){
        this.origin = origin;
        this.direction = direction;

        this.color = null;
        this.hitPoint = null;
        this.objectHit = null;
    }

    public Ray(Point3D origin, Point3D direction, Color color){
        this.origin = origin;
        this.direction = direction;
        this.color = color;

        this.hitPoint = null;
        this.objectHit = null;
    }
    public Point3D getOrigin() {
        return origin;
    }

    public Point3D getDirection() {
        return direction;
    }
    public Point3D pointAtParameter(double t){
        return origin.add(direction.multiply(t));
    }
    @Override
    public String toString(){
        return "Ray(Point3D(" + origin.getX() + "," + origin.getY() + "," + origin.getZ() + "),Point3D("
                 + direction.getX() + "," + direction.getY() + "," + direction.getZ() + "))";
    }

    public Point3D getHitPoint() {
        return hitPoint;
    }

    public void setHitPoint(Point3D hitPoint) {
        this.hitPoint = hitPoint;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Object3D getObjectHit() {
        return objectHit;
    }

    public void setObjectHit(Object3D objectHit) {
        this.objectHit = objectHit;
    }

    public void setOrigin(Point3D origin) {
        this.origin = origin;
    }

    public void setDirection(Point3D direction) {
        this.direction = direction;
    }

    public void reset(Point3D origin, Point3D direction){
        this.origin = origin;
        this.direction = direction;
        this.hitPoint = null;
        this.color = null;
        this.objectHit = null;
    }

}

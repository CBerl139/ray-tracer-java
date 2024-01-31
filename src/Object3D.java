import javafx.geometry.Point3D;
import javafx.scene.paint.Color;


public abstract class Object3D {
    // spheres, cubes, eventually triangle meshes ?
    Point3D center;
    private RayTracingMaterial rayTracingMaterial;

    public Object3D(Point3D center, RayTracingMaterial rayTracingMaterial){
        this.center = center;
        this.rayTracingMaterial = rayTracingMaterial;
    }
    public abstract Point3D intersectsAndClosestPoint(Ray ray);
    public abstract int numberOfIntersections(Ray ray);
    public abstract Object3D normalized(double factor);
    public abstract Point3D normalVectorAtPoint(Point3D point3D);
    public Point3D getCenter(){
        return center;
    }
    public void setCenter(Point3D newCenter){
        this.center = newCenter;
    }
    public RayTracingMaterial getRayTracingMaterial(){
        return rayTracingMaterial;
    }
}

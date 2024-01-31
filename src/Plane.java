import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

public class Plane extends Object3D{
    private double width;
    private double height;
    private Point3D normal;
    public Plane(Point3D center, double width, double height, Point3D normal, RayTracingMaterial rayTracingMaterial) {
        super(center, rayTracingMaterial);
        this.width = width;
        this.height = height;
        this.normal = normal;
    }

    @Override
    public Point3D intersectsAndClosestPoint(Ray ray) {
        // Check if the ray is parallel to the plane
        double dotProduct = ray.getDirection().dotProduct(normal);
        if (Math.abs(dotProduct) < 1e-6) { // The ray is parallel to the plane
            return null; // No intersection
        }

        // Calculate the distance along the ray to the plane
        double t = (normal.dotProduct(center.subtract(ray.getOrigin()))) / dotProduct;

        if (t < 0) { // Intersection behind the ray's origin
            return null;
        }

        // Calculate the point of intersection
        Point3D intersectionPoint = ray.pointAtParameter(t);
        // Check if the intersection point is within the plane's bounds
        //TODO : the width and height thing doesn't work for planes whose normal isn't (0,-1,0)
        // maybe if i create a second plane of the same width and height that i rotate so that its normal is (0,-1,0),
        // and rotate the intersection point with the same angles as the plane, then do the check of width and height on this new point
        if (Math.abs(intersectionPoint.getX() - center.getX()) <= width / 2 &&
                Math.abs(intersectionPoint.getZ() - center.getZ()) <= height / 2) {
            return intersectionPoint;
        } else {
            return null; // Intersection outside the plane's bounds
        }

    }
    public int numberOfIntersections(Ray ray){
        // Check if the ray is parallel to the plane
        double dotProduct = ray.getDirection().dotProduct(normal);
        if (Math.abs(dotProduct) < 1e-6) { // The ray is parallel to the plane
            return 0; // No intersection
        }

        // Calculate the distance along the ray to the plane
        double t = (normal.dotProduct(center.subtract(ray.getOrigin()))) / dotProduct;

        if (t < 0) { // Intersection behind the ray's origin
            return 0;
        }

        // Calculate the point of intersection
        Point3D intersectionPoint = ray.pointAtParameter(t);
        // Check if the intersection point is within the plane's bounds
        if (Math.abs(intersectionPoint.getX() - center.getX()) <= width / 2 &&
                Math.abs(intersectionPoint.getZ() - center.getZ()) <= height / 2) {
            return 1;
        } else {
            return 0; // Intersection outside the plane's bounds
        }
    }

    @Override
    public Object3D normalized(double factor) {
        return new Plane(center.multiply(1.0 / factor), width / factor, height / factor, normal,getRayTracingMaterial());
    }
    @Override
    public Point3D normalVectorAtPoint(Point3D point3D){
        return normal;
    }
    @Override
    public String toString(){
        return "Plane(Point3D(" + center.getX() + "," + center.getY() + "," + center.getZ() + "),"
                + width + "," + height + "," + getRayTracingMaterial() + ")";
    }
}

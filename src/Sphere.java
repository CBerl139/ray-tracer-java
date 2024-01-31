import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

public class Sphere extends Object3D {
    private double radius;
    public Sphere(Point3D center, double radius, RayTracingMaterial rayTracingMaterial){
        super(center, rayTracingMaterial);
        this.radius = radius;
    }
    @Override
    public Point3D intersectsAndClosestPoint(Ray ray) {
        Point3D rayOriginToSphere = this.center.subtract(ray.getOrigin());
        double b = ray.getDirection().dotProduct(rayOriginToSphere);
        double c = rayOriginToSphere.dotProduct(rayOriginToSphere) - (this.radius * this.radius);

        double discriminant = b * b - c;

        if (discriminant >= 0){
            double t1 =  b - Math.sqrt(discriminant);
            double t2 =  b + Math.sqrt(discriminant);

            //double t = Math.min(t1,t2);
            double t = b - Math.sqrt(discriminant);
            // Make sure the ball is in front of the camera
            if (t >= 0){
                return ray.pointAtParameter(t);
            }
            return null;
        } else{
            return null;
        }
    }
    //@Override
    //public Point3D intersectsAndClosestPoint(Ray ray) {
    //    Point3D rayOriginToSphere = this.center.subtract(ray.getOrigin());
    //    double a = ray.getDirection().dotProduct(ray.getDirection());
    //    double b = 2 * ray.getDirection().dotProduct(rayOriginToSphere);
    //    double c = rayOriginToSphere.dotProduct(rayOriginToSphere) - (this.radius * this.radius);
//
    //    double discriminant = (b * b) - (4 * a * c);
//
    //    if (discriminant >= 0) {
    //        double t1 = (-b + Math.sqrt(discriminant)) / (2 * a);
    //        double t2 = (-b - Math.sqrt(discriminant)) / (2 * a);
//
    //        double t = Math.max(t1, t2); // Get the maximum positive value of t for some reason
//
    //        Point3D intersectionPoint = ray.pointAtParameter(t);
    //        return intersectionPoint;
    //    }
//
    //    // If no intersection or negative discriminant, return null or a default point
    //    return null; // Or return a default Point3 indicating no intersection
    //}
    public int numberOfIntersections(Ray ray){
        Point3D rayOriginToSphere = this.center.subtract(ray.getOrigin());
        double a = ray.getDirection().dotProduct(ray.getDirection());
        double b = 2 * ray.getDirection().dotProduct(rayOriginToSphere);
        double c = rayOriginToSphere.dotProduct(rayOriginToSphere) - (this.radius * this.radius);

        double discriminant = (b * b) - (4 * a * c);

        if (discriminant > 0) {
            return 2;
        } else if(discriminant == 0){
            return 1;
        }

        return 0;
    }

    public Sphere normalized(double factor){
        Point3D center = this.center.multiply(1.0 / factor);
        double radius = this.radius / factor;
        return new Sphere(center,radius,this.getRayTracingMaterial());
    }
    @Override
    public Point3D normalVectorAtPoint(Point3D point3D){
        return point3D.subtract(center).normalize();
    }
    public double getRadius() {
        return radius;
    }
    @Override
    public String toString(){
        return "Sphere(Point3D(" + center.getX() + "," + center.getY() + "," + center.getZ() + "),"
                + radius + "," + getRayTracingMaterial() + ")";
    }
}


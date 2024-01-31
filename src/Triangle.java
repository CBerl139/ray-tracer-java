import javafx.geometry.Point3D;

public class Triangle extends Object3D{
    private Point3D A;
    private Point3D B;
    private Point3D C;
    public Triangle(Point3D A, Point3D B, Point3D C, RayTracingMaterial rayTracingMaterial) {
        super(A, rayTracingMaterial);
        this.A = A;
        this.B = B;
        this.C = C;
    }

    @Override
    public Point3D intersectsAndClosestPoint(Ray ray) {
        Point3D edgeAB = B.subtract(A);
        Point3D edgeAC = C.subtract(A);
        Point3D normal = edgeAB.crossProduct(edgeAC);
        Point3D ao = ray.getOrigin().subtract(A);
        Point3D dirAO = ao.crossProduct(ray.getDirection());

        double determinant = - ray.getDirection().dotProduct(normal);
        double invDeterminant = 1.0 / determinant;

        double dst = ao.dotProduct(normal) * invDeterminant;
        double u = edgeAC.dotProduct(dirAO) * invDeterminant;
        double v = - edgeAB.dotProduct(dirAO) * invDeterminant;
        double w = 1 - u - v;

        if (determinant >= 1e-6 && dst >= 0 && u >= 0 && v >= 0 && w >= 0){
            return ray.pointAtParameter(dst);
        }
        return null;
    }

    @Override
    public int numberOfIntersections(Ray ray) {
        return 0;
    }
    @Override
    public void setCenter(Point3D newCenter) {
        Point3D edgeAB = B.subtract(A);
        Point3D edgeAC = C.subtract(A);
        this.center = newCenter;
        this.A = newCenter;
        this.B = newCenter.add(edgeAB);
        this.C = newCenter.add(edgeAC);
    }

    @Override
    public Object3D normalized(double factor) {
        return new Triangle(A.multiply(1.0 / factor),C.multiply(1.0 / factor),C.multiply(1.0 / factor),getRayTracingMaterial());
    }

    @Override
    public Point3D normalVectorAtPoint(Point3D point3D) {
        Point3D edgeAB = B.subtract(A);
        Point3D edgeAC = C.subtract(A);
        return edgeAB.crossProduct(edgeAC).normalize();
    }
}

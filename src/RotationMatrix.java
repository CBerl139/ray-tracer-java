import javafx.geometry.Point3D;


public class RotationMatrix {
    private Point3D point;
    private double alphaDegrees;
    private double betaDegrees;
    private double gammaDegrees;
    private double[][] matrix;
    public RotationMatrix(Point3D point, double alphaDegrees, double betaDegrees, double gammaDegrees){
        this.point = point;
        this.alphaDegrees = alphaDegrees;
        this.betaDegrees = betaDegrees;
        this.gammaDegrees = gammaDegrees;

        double ca = Math.cos(alphaDegrees * Math.PI / 180.0);
        double sa = Math.sin(alphaDegrees * Math.PI / 180.0);
        double cb = Math.cos(betaDegrees * Math.PI / 180.0);
        double sb = Math.sin(betaDegrees * Math.PI / 180.0);
        double cg = Math.cos(gammaDegrees * Math.PI / 180.0);
        double sg = Math.sin(gammaDegrees * Math.PI / 180.0);

        matrix = new double[][]{
                new double[]{cb * cg,sa * sb * cg - ca * sg ,ca * sb * cg + sa * sg },
                new double[]{cb * sg,sa * sb * sg + ca * cg ,ca * sb * sg - sa * cg },
                new double[]{- sb   ,sa * cb                ,ca * cb                }
        };
    }
    public Point3D getRotatedPoint3D(){
        return multiply(matrix,point);
    }
    private Point3D multiply(double[][] matrix, Point3D point3D){
        double[] inputPoint = new double[]{point3D.getX(),point3D.getY(),point3D.getZ()};
        double x = 0;
        double y = 0;
        double z = 0;
        for (int i = 0; i < 3; i++){
            x += matrix[0][i] * inputPoint[i];
            y += matrix[1][i] * inputPoint[i];
            z += matrix[2][i] * inputPoint[i];
        }
        return new Point3D(x,y,z);
    }
}

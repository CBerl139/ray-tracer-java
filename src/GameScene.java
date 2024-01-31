import javafx.geometry.Point3D;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class GameScene extends Scene {
    private int width;
    private int height;
    private WritableImage writableImage;
    private PixelWriter pixelWriter;
    private ArrayList<Object3D> objectsInTheScene;
    // light sources are Object3D to make use of polymorphism. In practice, they are spheres.
    private ArrayList<ArrayList<Color>> frame;
    private boolean enableShadows;
    private int numberOfReflections;
    private int rayPerPixel;
    private Camera camera;
    public GameScene(Pane pane, double v, double v1, boolean b) {
        super(pane, v, v1, b);
        this.setFill(Color.BLACK);
        this.width = (int) v;
        this.height = (int) v1;
        this.enableShadows = false;
        this.numberOfReflections = 4;
        this.rayPerPixel = 10;

        writableImage = new WritableImage((int) v, (int) v1);
        pixelWriter = writableImage.getPixelWriter();
        ImageView imageView = new ImageView(writableImage);
        pane.getChildren().add(imageView);

        camera = new Camera(new Point3D(0,0,0), 1);

        //Sphere sun = new Sphere(new Point3D( 250,-250,5000),4500,false,new RayTracingMaterial(Color.WHITE,Color.WHITE,1,1));
        //objectsInTheScene.add(sun);

        objectsInTheScene = new ArrayList<>();
        objectsInTheScene.add(new Sphere(new Point3D( - 200,0,300),50, new RayTracingMaterial(Color.GREEN,Color.GREEN,0,0, 0)));
        objectsInTheScene.add(new Sphere(new Point3D( - 70,80,200),60, new RayTracingMaterial(Color.RED,Color.RED,0,0, 0)));
        objectsInTheScene.add(new Sphere(new Point3D( 150,60,160),50,new RayTracingMaterial(Color.BLUE,Color.BLUE,0,0.5, 0)));
        objectsInTheScene.add(new Sphere(new Point3D( 100,0,120),10, new RayTracingMaterial(Color.YELLOW,Color.YELLOW,0,0, 0)));
        objectsInTheScene.add(new Sphere(new Point3D( 0,-50,350),100, new RayTracingMaterial(Color.GREY,Color.GREY,0,0.85, 0)));
        objectsInTheScene.add(new Sphere(new Point3D( -300,-250,650),50, new RayTracingMaterial(Color.WHITE,Color.WHITE,0,0.5, 0)));
        objectsInTheScene.add(new Sphere(new Point3D( -150,-250,650),50, new RayTracingMaterial(Color.WHITE,Color.WHITE,0,0.625, 0)));
        objectsInTheScene.add(new Sphere(new Point3D( 0,-250,650),50, new RayTracingMaterial(Color.WHITE,Color.WHITE,0,0.75, 0)));
        objectsInTheScene.add(new Sphere(new Point3D( 150,-250,650),50, new RayTracingMaterial(Color.WHITE,Color.WHITE,0,0.875, 0)));
        objectsInTheScene.add(new Sphere(new Point3D( 300,-250,650),50, new RayTracingMaterial(Color.WHITE,Color.WHITE,0,1, 0)));
        objectsInTheScene.add(new Sphere(new Point3D( -650,-200,200),150, new RayTracingMaterial(Color.WHITE,Color.WHITE,0,1, 1)));
        //objectsInTheScene.add(new Triangle(new Point3D(-200,0,300),new Point3D(-70,80,200), new Point3D(0,-50,350),new RayTracingMaterial(Color.BLACK,Color.BLACK,0,0,0)));
        // bottom plane
        objectsInTheScene.add(new Plane(new Point3D(0,100,500),2000,1000,new Point3D(0,-1,0).normalize(), new RayTracingMaterial(Color.ORANGE,Color.ORANGE,0,0,0)));
        // right plane
        objectsInTheScene.add(new Plane(new Point3D(400,100,500),200,1000,new Point3D(-1,0,0).normalize(), new RayTracingMaterial(Color.RED,Color.RED,1,0,0)));
        // mirror
        objectsInTheScene.add(new Plane(new Point3D(-300,0,100),100,100,new Point3D(1,0,0).normalize(), new RayTracingMaterial(Color.GREY,Color.GREY,0,1,0)));
        // small white plane
        Plane whitePlane = new Plane(new Point3D(0,70,90),50,50,new Point3D(1,-1,0).normalize(), new RayTracingMaterial(Color.WHITE,Color.WHITE,0,0,0));
        objectsInTheScene.add(whitePlane);

        // test to see if I can get a plane with a normal of (0,-1,0) out of a random plane : it doesn't work :(
        Point3D whitePlaneNormalVector = whitePlane.normalVectorAtPoint(whitePlane.getCenter());
        Point3D upVector = new Point3D(0,-1,0);
        System.out.println(upVector.angle(whitePlaneNormalVector));
        double angle = upVector.angle(whitePlaneNormalVector);
        double[] angles = xyzAngles(whitePlaneNormalVector.normalize(),upVector.normalize());
        System.out.println(angles[0] + " , " + angles[1] + " , " + angles[2]);
        //TODO : determine the right angles in order to implement the plane rotation technique so that the planes can have the correct size
        RotationMatrix rotationMatrix = new RotationMatrix(whitePlaneNormalVector, - angles[2] ,- angles[1],- angles[0]);
        objectsInTheScene.add(new Plane(whitePlane.getCenter(),50,50,rotationMatrix.getRotatedPoint3D().normalize(), new RayTracingMaterial(Color.PURPLE,Color.PURPLE,0,0,0)));

        //double[] testAngles = xyzAngles(new Point3D(0,1,0),upVector);
        //System.out.println(testAngles[0] + " , " + testAngles[1] + " , " + testAngles[2]);
        // end test

        for (Object3D object3D : objectsInTheScene){
            objectsInTheScene.set(objectsInTheScene.indexOf(object3D),object3D.normalized(50));
        }

        frame = new ArrayList<>();
        for (int j = 0; j < v; j++){
            ArrayList<Color> line = new ArrayList<>();
            frame.add(line);
            for (int i = 0; i < v1; i++){
                line.add((Color) this.getFill());
            }
        }
    }
    public void compute(){
        // move the objects closer / away from the camera :
        Point3D offset = new Point3D(- camera.getPosition().getX(),- camera.getPosition().getY(),- camera.getPosition().getZ());
        moveSceneRelativeToCamera(offset);

        double[] angles = xyzAngles(camera.getDirection(),new Point3D(0,0,1));
        //System.out.println(angles[0] + " , " + angles[1] + " , " + angles[2]);
        //System.out.println(camera.getDirection());
        //System.out.println(new Point3D(0,0,1).angle(new Point3D(0,0,-1)));
        //System.out.println((new Point3D(0,0,-1).dotProduct(new Point3D(1,0,0))/new Point3D(0,0,-1).subtract(new Point3D(1,0,0)).magnitude()));
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double screenX = (x + 0.5) / 500 - width/500.0/2; // Normalize x-coordinate
                double screenY = (y + 0.5) / 500 - 0.5; // Normalize y-coordinate
                double angleY = camera.getDirection().angle(new Point3D(0,0,1));
                RotationMatrix rotationMatrix = new RotationMatrix(new Point3D(screenX,screenY, camera.getDistanceToScreen()),angles[0],angles[1], 0);

                Point3D rayOrigin = new Point3D(camera.getPosition().getX(),camera.getPosition().getY(),camera.getPosition().getZ());
                Point3D rayDirection = rotationMatrix.getRotatedPoint3D().normalize();

                Ray ray = new Ray(rayOrigin,rayDirection, (Color) this.getFill());

                if (enableShadows){
                    double[] totalIncomingLight = new double[]{0,0,0};
                    for (int i = 0; i < rayPerPixel; i++){
                        totalIncomingLight = addColorUncapped(totalIncomingLight,traceRayPath(ray));
                        ray.reset(rayOrigin,rayDirection);
                    }
                    Color finalColor = Color.color(totalIncomingLight[0]/rayPerPixel,totalIncomingLight[1]/rayPerPixel,totalIncomingLight[2]/rayPerPixel);

                    frame.get(x).set(y,finalColor);
                } else{
                    checkRayIntersectionWithObjects(ray,objectsInTheScene);
                    frame.get(x).set(y,ray.getColor());
                }
                //// test : drawing a circle
                //if ((x - 250) * (x - 250) + (y - 250) * (y - 250) <= 100 * 100) {
                //    //frame.get(x).set(y,Color.rgb((int) ((x-150)/250.0 * 255),0,(int) ((y-150)/250.0 * 255)));
                //    double randomNumber = Math.random();
                //    //frame.get(x).set(y,Color.BLACK.interpolate(Color.WHITE,0.6));
                //    frame.get(x).set(y,linearInterpolationColor(Color.BLUE,Color.WHITE,(y - 150)/250.0));
                //}
            }
        }
        // replace the objects at their correct position :
        for (Object3D object3D : objectsInTheScene) object3D.setCenter(object3D.getCenter().add(offset.multiply(-1)));
    }
    public Color traceRayPath(Ray ray){
        Color incomingLight = Color.color(0,0,0);
        Color rayColor = Color.color(1,1,1);
        for (int i = 0; i < 1 + numberOfReflections; i++){
            checkRayIntersectionWithObjects(ray,objectsInTheScene);
            if (ray.getHitPoint() != null){
                double epsilon = 1e-4;
                Point3D normal = ray.getObjectHit().normalVectorAtPoint(ray.getHitPoint());

                RayTracingMaterial material = ray.getObjectHit().getRayTracingMaterial();

                Color objectEmittedLight = multiplyColor(material.getEmissionColor(),material.getEmissionStrength());
                incomingLight = addColor(incomingLight,multiplyColor(objectEmittedLight,rayColor));

                rayColor = multiplyColor(rayColor,material.getColor());

                Point3D diffuseDirection = normal.add(randomDirection()).normalize();
                Point3D specularDirection = ray.getDirection().subtract(normal.multiply(2 * ray.getDirection().dotProduct(normal)));
                // refraction :
                Point3D refractedDirection = calculateRefractedDirection(ray,normal);
                Point3D finalDirection = linearInterpolationDirection(diffuseDirection,specularDirection,material.getSmoothness()).normalize();
                if (material.getTransparency() == 1){
                    ray.reset(ray.getHitPoint().add(normal.multiply(-epsilon)),refractedDirection);
                } else{
                    ray.reset(ray.getHitPoint().add(normal.multiply(epsilon)),finalDirection);
                }
            } else{
                incomingLight = addColor(incomingLight,multiplyColor(rayColor,getSkyColor(ray)));
                break;
            }
        }
        return incomingLight;
    }
    public Color getSkyColor(Ray ray){
        double dot = ray.getDirection().dotProduct(new Point3D(0,-1,0));
        double angleToSun = ray.getDirection().angle(new Point3D(-1,-1,1));
        if (angleToSun <= 20.0){
            dot = (0.5 + dot)/1.5;
            Color sunColor = addColor(linearInterpolationColor(Color.WHITE,Color.SKYBLUE,Math.min(2*dot,0.8)),multiplyColor(linearInterpolationColor(Color.ORANGE,Color.BLACK,angleToSun/20.0),0.5));
            return sunColor;
        }
        if (dot >= - 0.5){
            dot = (0.5 + dot)/1.5;
            return linearInterpolationColor(Color.WHITE,Color.SKYBLUE,Math.min(2*dot,0.8));
            //return Color.color(Math.min(Color.SKYBLUE.getRed(),2*(0.62 * dot)),Math.min(Color.SKYBLUE.getGreen(),2*(0.77 * dot)),Math.min(Color.SKYBLUE.getBlue(),2*(0.91 * dot)));
        } else{
            return Color.GREY;
        }
    }
    public void render(){
        // render all the pixels on the screen (done) (=change their color)
        int width = (int) writableImage.getWidth();
        int height = (int) writableImage.getHeight();

        for (int j = 0; j < height; j++){
            for (int i = 0; i < width; i++){
                Color pixelColor = frame.get(i).get(j);
                pixelWriter.setColor(i,j,pixelColor);
            }
        }
    }
    public void move(double distanceStep, KeyCode keyPressed){
        switch (keyPressed){
            case Z -> camera.setPosition(camera.getPosition().add(camera.getDirection().multiply(distanceStep)));
            case S -> camera.setPosition(camera.getPosition().add(camera.getDirection().multiply(- distanceStep)));
            case Q -> {
                Point3D rotatedDirection90Degrees = new Point3D(camera.getDirection().getZ(),camera.getDirection().getY(),- camera.getDirection().getX());
                camera.setPosition(camera.getPosition().add(rotatedDirection90Degrees.multiply(- distanceStep)));
            }
            case D -> {
                Point3D rotatedDirection90Degrees = new Point3D(camera.getDirection().getZ(),camera.getDirection().getY(),- camera.getDirection().getX());
                camera.setPosition(camera.getPosition().add(rotatedDirection90Degrees.multiply(distanceStep)));
            }
            case SPACE -> camera.setPosition(camera.getPosition().add(new Point3D(0,-1,0).multiply(distanceStep * 0.25)));
            case SHIFT -> camera.setPosition(camera.getPosition().add(new Point3D(0,-1,0).multiply(- distanceStep * 0.25)));
            case RIGHT -> {
                RotationMatrix rotationMatrix = new RotationMatrix(camera.getDirection(),0,10,0);
                camera.setDirection(rotationMatrix.getRotatedPoint3D().normalize());
            }
            case LEFT -> {
                RotationMatrix rotationMatrix = new RotationMatrix(camera.getDirection(),0,- 10,0);
                camera.setDirection(rotationMatrix.getRotatedPoint3D().normalize());
            }
            case UP -> {
                RotationMatrix rotationMatrix = new RotationMatrix(camera.getDirection(),10,0,0);
                camera.setDirection(rotationMatrix.getRotatedPoint3D().normalize());
            }
            case DOWN -> {
                RotationMatrix rotationMatrix = new RotationMatrix(camera.getDirection(),- 10,0,0);
                camera.setDirection(rotationMatrix.getRotatedPoint3D().normalize());
            }
            case A -> camera.setDistanceToScreen(camera.getDistanceToScreen() * 2);
            case E -> camera.setDistanceToScreen(camera.getDistanceToScreen() * 0.5);
            case F -> camera.setDirection(camera.getDirection().multiply(-1.0));
            case K -> enableShadows = !enableShadows;
        }
    }
    public Point3D calculateRefractedDirection(Ray ray, Point3D normal){
        double n = 1.0 / 1.8;
        double cosI = - normal.dotProduct(ray.getDirection());
        double sinT2 = n * n * (1.0 - cosI * cosI);
        if (sinT2 > 1.0) System.out.println("invalid vector");
        double cosT = Math.sqrt(1.0 - sinT2);
        return ray.getDirection().multiply(n).add(normal.multiply(n * cosI - cosT));
    }
    public Point3D linearInterpolationDirection(Point3D dir1, Point3D dir2, double t){
        double x = lerp(dir1.getX(),dir2.getX(),t);
        double y = lerp(dir1.getY(),dir2.getY(),t);
        double z = lerp(dir1.getZ(),dir2.getZ(),t);
        return new Point3D(x,y,z);
    }
    public Color linearInterpolationColor(Color color1, Color color2, double t){
        return Color.color(
                lerp(color1.getRed(),color2.getRed(),t),
                lerp(color1.getGreen(),color2.getGreen(),t),
                lerp(color1.getBlue(),color2.getBlue(),t)
        );
    }
    public double lerp(double x0, double x1, double t){
        return (1 - t) * x0 + t * x1;
    }

    public double[] xyzAngles(Point3D vector, Point3D other){
        //double x = Math.acos(vector.getX()/vector.magnitude()) - Math.acos(other.getX()/other.magnitude());;
        //double y = Math.acos(vector.getY()/vector.magnitude()) - Math.acos(other.getY()/other.magnitude());
        //double z = Math.acos(vector.getZ()/vector.magnitude()) - Math.acos(other.getZ()/other.magnitude());
        //return new double[]{x * 180/Math.PI,y * 180/Math.PI,z * 180/Math.PI};

        double x = vector.getX(); // Replace with your x-coordinate
        double y = vector.getY(); // Replace with your y-coordinate
        double z = vector.getZ(); // Replace with your z-coordinate

        double otherx = other.getX(); // Replace with your x-coordinate
        double othery = other.getY(); // Replace with your y-coordinate
        double otherz = other.getZ(); // Replace with your z-coordinate

        double pitch = Math.atan2(y, Math.sqrt(x * x + z * z));
        double yaw = Math.atan2(x, z);
        double roll = Math.atan2(Math.sqrt(x * x + y * y), z);

        double otherpitch = Math.atan2(othery, Math.sqrt(otherx * otherx + otherz * otherz));
        double otheryaw = Math.atan2(otherx, otherz);
        double otherroll = Math.atan2(Math.sqrt(otherx * otherx + othery * othery), otherz);

        // Convert radians to degrees if needed
        double pitchDegrees = Math.toDegrees(pitch) - Math.toDegrees(otherpitch);
        double yawDegrees = Math.toDegrees(yaw) - Math.toDegrees(otheryaw);
        double rollDegrees = Math.toDegrees(roll) - Math.toDegrees(otherroll);

        return new double[]{pitchDegrees,yawDegrees,rollDegrees};
    }
    private void moveSceneRelativeToCamera(Point3D offset){
        for (Object3D object3D : objectsInTheScene) object3D.setCenter(object3D.getCenter().add(offset));
    }
    private Color multiplyColor(Color color1, Color color2){
        return Color.color(color1.getRed() * color2.getRed(), color1.getGreen() * color2.getGreen(), color1.getBlue() * color2.getBlue());
    }
    private Color multiplyColor(Color color1, double factor){
        return Color.color(Math.min(1,color1.getRed() * factor), Math.min(1,color1.getGreen() * factor), Math.min(1,color1.getBlue() * factor));
    }
    private Color addColor(Color color1, Color color2){
        return Color.color(Math.min(1,color1.getRed() + color2.getRed()),Math.min(1,color1.getGreen() + color2.getGreen()),Math.min(1,color1.getBlue() + color2.getBlue()));
    }
    private double[] addColorUncapped(double[] color1, Color color2){
        return new double[]{
                color1[0] + color2.getRed(),
                color1[1] + color2.getGreen(),
                color1[2] + color2.getBlue()
            };
    }
    private void checkRayIntersectionWithObjects(Ray ray, ArrayList<Object3D> listOfObjects){
        for (Object3D object3D : listOfObjects){
            Point3D potentialClosestPoint = object3D.intersectsAndClosestPoint(ray);
            if (ray.getHitPoint() == null){
                ray.setHitPoint(potentialClosestPoint);
            }
            if (potentialClosestPoint != null && potentialClosestPoint.subtract(ray.getOrigin()).magnitude() <= ray.getHitPoint().subtract(ray.getOrigin()).magnitude()){
                ray.setHitPoint(potentialClosestPoint);
                ray.setObjectHit(object3D);
                ray.setColor(object3D.getRayTracingMaterial().getColor());
            }
        }
    }
    private double randomNormallyDistributed(){
        double theta = 2 * Math.PI * Math.random();
        double rho = Math.sqrt(- 2 * Math.log(Math.random()));
        return rho * Math.cos(theta);
    }
    private Point3D randomDirection(){
        return new Point3D(randomNormallyDistributed(),randomNormallyDistributed(),randomNormallyDistributed()).normalize();
    }
    private Point3D randomHemisphereDirection(Point3D normal){
        Point3D dir = randomDirection();
        return dir.multiply(normal.dotProduct(dir)/(dir.magnitude() * normal.magnitude()));
    }
    public ArrayList<ArrayList<Color>> getFrame(){
        return frame;
    }

}

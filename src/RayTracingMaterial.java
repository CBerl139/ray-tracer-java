import javafx.scene.paint.Color;

public class RayTracingMaterial {
    private Color color;
    // emission strength is between 0 and 1
    private double emissionStrength;
    private Color emissionColor;
    private double smoothness;
    private double transparency;
    public RayTracingMaterial(Color color, Color emissionColor, double emissionStrength, double smoothness, double transparency){
        this.color = color;
        this.emissionColor = emissionColor;
        this.emissionStrength = emissionStrength;
        this.smoothness = smoothness;
        this.transparency = transparency;
    }

    public Color getColor() {
        return color;
    }

    public double getEmissionStrength() {
        return emissionStrength;
    }

    public Color getEmissionColor() {
        return emissionColor;
    }
    @Override
    public String toString(){
        return "RayTracingMaterial(" + color + "," + emissionColor + "," + emissionStrength + ")";
    }

    public double getSmoothness() {
        return smoothness;
    }

    public double getTransparency() {
        return transparency;
    }
}

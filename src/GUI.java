import com.sun.javafx.scene.shape.ArcHelper;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.ArrayList;

public class GUI extends Application {
    private GameScene gameScene;
    private AnimationTimer timer;
    private double lastUpdateTime;
    public void start(Stage stage) throws Exception{
        stage.setTitle("RAY TRACING");

        Group root = new Group();
        Pane pane = new Pane(root);

        gameScene = new GameScene(pane,1000,500,true);
        stage.setScene(gameScene);

        stage.show();
        gameScene.compute();
        gameScene.render();

        gameScene.setOnKeyPressed(keyEvent -> {
            lastUpdateTime = (double) System.nanoTime() / 1000000000.0;
            gameScene.move(1,keyEvent.getCode());
            gameScene.compute();
            gameScene.render();
            double computingTime = (System.nanoTime() / 1000000000.0 - lastUpdateTime);
            System.out.println("Computing time : " + computingTime + " s");
        });
        //timer = new AnimationTimer() {
        //    @Override
        //    public void handle(long time) {
        //        double deltaTimeSeconds = (double) time / 1000000000 - lastUpdateTime;
        //        if (deltaTimeSeconds > 1.0){
        //            System.out.println(deltaTimeSeconds);
        //            lastUpdateTime = (double) time / 1000000000;
        //        }
        //    }
        //};
////
        ////timer.start();

    }
    public static void main(String[] args){
        launch(args);
    }
}

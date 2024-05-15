import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PythagorasTree extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private GraphicsContext gc;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Pythagoras Tree");
        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();
        drawPythagorasTree(400, 600, 200, -Math.PI / 2, 12);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void drawPythagorasTree(double x, double y, double size, double angle, int depth) {
        if (depth == 0) {
            return;
        }
        double x2 = x + Math.cos(angle) * size;
        double y2 = y + Math.sin(angle) * size;

        gc.setStroke(Color.BLACK);
        gc.strokeLine(x, y, x2, y2);
        double angle1 = angle - Math.PI / 4;
        double angle2 = angle + Math.PI / 4;

        double size1 = size / Math.sqrt(2);

        drawPythagorasTree(x2, y2, size1, angle1, depth - 1);
        drawPythagorasTree(x2, y2, size1, angle2, depth - 1);
    }
}
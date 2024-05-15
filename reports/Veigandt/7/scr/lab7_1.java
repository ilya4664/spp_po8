import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class RotatingLineApplet extends Application {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private static final Color START_COLOR = Color.RED;
    private static final Color END_COLOR = Color.BLUE;
    private static final double ROTATION_SPEED = 0.01;

    private double angle = 0.0;
    private Color currentColor = START_COLOR;

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Group root = new Group();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root, WIDTH, HEIGHT);

        scene.setFill(Color.WHITE);

        primaryStage.setTitle("Rotating Line Applet");
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(0, 0, WIDTH, HEIGHT);

                double x1 = WIDTH / 2;
                double y1 = HEIGHT / 2;
                double x2 = x1 + 100 * Math.cos(angle);
                double y2 = y1 + 100 * Math.sin(angle);

                gc.setStroke(currentColor);
                gc.setLineWidth(2);
                gc.strokeLine(x1, y1, x2, y2);

                angle += ROTATION_SPEED;

                currentColor = interpolateColor(START_COLOR, END_COLOR, angle % (2 * Math.PI) / (2 * Math.PI));
            }
        };
        animationTimer.start();
    }

    private Color interpolateColor(Color startColor, Color endColor, double t) {
        double r = startColor.getRed() + t * (endColor.getRed() - startColor.getRed());
        double g = startColor.getGreen() + t * (endColor.getGreen() - startColor.getGreen());
        double b = startColor.getBlue() + t * (endColor.getBlue() - startColor.getBlue());
        double opacity = startColor.getOpacity() + t * (endColor.getOpacity() - startColor.getOpacity());
        return new Color(r, g, b, opacity);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
package com.codedotorg;

import com.codedotorg.modelmanager.CameraController;
import com.codedotorg.modelmanager.ModelManager;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GuessingGame {

    /** The main window of the app */
    private Stage window;

    /** The main scene of the app to display the game */
    private MainScene game;

    /** Manages the TensorFlow model used for image classification */
    private ModelManager model;

    /** Controls the camera capture and provides frames to the TensorFlow model for classification */
    private CameraController cameraController;

    /**
     * Constructor for the GuessingGame class.
     * Initializes a new CameraController, ModelManager, and MainScene.
     */
    public GuessingGame() {
        cameraController = new CameraController();
        model = new ModelManager();
        game = new MainScene();
    }
    
    /**
     * Starts the Guessing Game application.
     * Sets the title of the primary stage to "Guessing Game".
     * Adds a shutdown hook to stop the camera capture when the app is closed.
     * Calls the showMainScreen() method to display the main screen.
     *
     * @param primaryStage the primary stage of the application
     */
    public void startApp(Stage primaryStage) {
        this.window = primaryStage;
        window.setTitle("Guessing Game");

        // Shutdown hook to stop the camera capture when the app is closed
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            cameraController.stopCapture();
        }));

        showMainScreen();
    }

    
    /**
     * Displays the main screen of the game, which includes the camera view and the computer's guess.
     * Calls createMainScreen() to create the main screen to show, sets the scene to the window and shows it.
     * Starts capturing the webcam, initially hides the camera view and shows the loading animation.
     * Updates the prediction and computer's guess.
     */
    public void showMainScreen() {
        Scene mainScene = game.createMainScene(cameraController);
        window.setScene(mainScene);
        window.show();
        cameraController.captureCamera(game.getCameraView(), model);
        game.showLoadingAnimation();
        updateGameResults();
    }

    /**
     * Updates the game results by getting the predicted class and score from the CameraController,
     * removing the loading animation and showing the camera view, updating the label to prompt the user
     * to think of a number, getting the result of the user's response, updating the prediction label with
     * the predicted class and confidence score, and updating the computer guess label.
     */
    private void updateGameResults() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            // Get the predicted class and score from the CameraController
            String predictedClass = cameraController.getPredictedClass();
            double predictedScore = cameraController.getPredictedScore();

            if (predictedClass != null) {
                // Remove the loading animation and show the camera view
                game.hideLoadingAnimation();

                // Update the label to prompt the user to think of a number
                game.setPromptLabelText("Higher, Lower, or Correct?");

                // Get the result of the user's response
                String userResult = GameLogic.getUserResponse(predictedClass, predictedScore);

                // Update the prediction label with the predicted class and confidence score
                Platform.runLater(() -> game.setPredictionLabelText(userResult));

                updateComputerGuessLabel(predictedClass);
            }
        }));
        
        // Specify that the animation should repeat indefinitely
        timeline.setCycleCount(Timeline.INDEFINITE);

        // Start the animation
        timeline.play();
    }

    /**
     * Updates the computer guess label with the result of a binary search on the predicted class.
     * Waits for 3 seconds before updating the label to give the user time to see the previous guess.
     *
     * @param predictedClass the predicted class to search for
     */
    private void updateComputerGuessLabel(String predictedClass) {
        PauseTransition delay = new PauseTransition(Duration.seconds(3));

        delay.setOnFinished(event -> {
            // Get the result of the computer's guess
            String computerGuess = "Computer Guess: " + GameLogic.binarySearch(predictedClass);

            // Update the computer guess label with the number the computer guessed
            Platform.runLater(() -> game.setComputerGuessLabel(computerGuess));
        });

        delay.play();
    }

}

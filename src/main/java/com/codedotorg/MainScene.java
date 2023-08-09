package com.codedotorg;

import com.codedotorg.modelmanager.CameraController;

import javafx.geometry.Pos;
import javafx.scene.Camera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainScene {
    
    /** The root layout of the main scene */
    private VBox rootLayout;

    /** Displays the camera feed in the app */
    private ImageView cameraView;

    /** Displays the title of the app */
    private Label titleLabel;

    /** Displays the computer's guess */
    private Label computerGuessLabel;

    /** Displays the predicted class and confidence score */
    private Label predictionLabel;

    /** Displays a prompt to tell the user to think of a number */
    private Label promptLabel;

    /** Button to exit the app */
    private Button exitButton;

    /** The loading animation while the camera is loading */
    private Loading cameraLoading;

    /** Button to play the game again */
    private Button playAgainButton;

    /**
     * Constructs a new MainScene object.
     * Initializes the cameraView, progress, exitButton, titleLabel, computerGuessLabel,
     * predictionLabel, promptLabel, and cameraLoadingLabel.
     */
    public MainScene() {
        cameraView = new ImageView();
        cameraView.setId("camera");

        exitButton = new Button("Exit");
        playAgainButton = new Button("Play Again");
        
        titleLabel = new Label("Guessing Game");
        titleLabel.setId("titleLabel");

        computerGuessLabel = new Label("");
        predictionLabel = new Label("");
        promptLabel = new Label("Think of a number between 1 and 100!");

        cameraLoading = new Loading();   
    }

    /**
     * Returns the camera view ImageView object.
     *
     * @return the camera view ImageView object
     */
    public ImageView getCameraView() {
        return cameraView;
    }

    /**
     * Returns the root layout.
     * 
     * @return the VBox for the root layout
     */
    public VBox getRootLayout() {
        return rootLayout;
    }

    /**
     * Returns the loading animation
     * 
     * @return the Loading object for the loading animation
     */
    public Loading getLoadingAnimation() {
        return cameraLoading;
    }

    /**
     * Creates the main screen of the game.
     * @return the main scene of the game
     */
    public Scene createMainScene(CameraController cameraController) {
        // Sets the action for when the exit button is clicked
        createExitButtonAction(cameraController);

        // Initialize the root layout
        rootLayout = new VBox();
        rootLayout.setAlignment(Pos.CENTER);

        // Create spacers for above and below the cameraView
        Region cameraSpacer1 = createSpacer(20);
        Region cameraSpacer2 = createSpacer(20);

        // Create spacer for above the exit button
        Region buttonSpacer = createSpacer(10);

        // Add the title label, prompt label, loading animation, camera view, prediction label, and exit button to the layout
        rootLayout.getChildren().addAll(titleLabel, promptLabel, cameraLoading.getCameraLoadingLabel(),
            cameraSpacer1, cameraView, cameraSpacer2, cameraLoading.getProgressIndicator(), computerGuessLabel, predictionLabel, buttonSpacer, exitButton);

        // Creates a new scene and set the layout as its root
        Scene mainScene = new Scene(rootLayout, 600, 750);
        mainScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        // Returns the main scene
        return mainScene;
    }

    /**
     * Sets the text of the prompt label to the given text.
     * 
     * @param text the text to set as the prompt label's text
     */
    public void setPromptLabelText(String text) {
        promptLabel.setText(text);
    }

    /**
     * Sets the text of the prediction label to the given text.
     * 
     * @param text the text to set the prediction label to
     */
    public void setPredictionLabelText(String text) {
        predictionLabel.setText(text);
    }

    /**
     * Sets the text of the computerGuessLabel to the given text.
     * @param text the text to set the label to
     */
    public void setComputerGuessLabel(String text) {
        computerGuessLabel.setText(text);
    }

    public void resetGame(Stage primaryStage, CameraController cameraController) {
        // Reset the game logic
        GameLogic.resetGame();

        // Restore the game scene
        Scene mainScene = createMainScene(cameraController);
        primaryStage.setScene(mainScene);
    }

    public Scene correctGuessScene(int correctNumber, Stage primaryStage, CameraController cameraController) {
        // Sets the action for when the play again button is clicked
        createPlayAgainButtonAction(primaryStage, cameraController);

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
    
        Label correctNumberLabel = new Label("Correct Number: " + correctNumber);
        Label successMessage = new Label("The computer guessed the number!");
        
        layout.getChildren().addAll(correctNumberLabel, successMessage, playAgainButton);
        
        Scene correctGuessScene = new Scene(layout, 600, 750);
        correctGuessScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
    
        return correctGuessScene;
    }

    /**
     * Sets the action for the exit button. When clicked, it
     * stops the camera capture and exits the program.
     */
    private void createExitButtonAction(CameraController cameraController) {
        exitButton.setOnAction(event -> {
            cameraController.stopCapture();
            System.exit(0);
        });
    }

    private void createPlayAgainButtonAction(Stage primaryStage, CameraController cameraController) {
        playAgainButton.setOnAction(event -> {
            resetGame(primaryStage, cameraController);
        });
    }

    /**
     * Creates a spacer region with the specified height.
     * 
     * @param amount the preferred height of the spacer region
     * @return a Region object with the specified height
     */
    private Region createSpacer(double amount) {
        Region temp = new Region();
        temp.setPrefHeight(amount);
        return temp;
    }
    
}

package com.codedotorg;

public class GameLogic {
    
    /** The left boundary for the binary search (lowest number in the range) */
    private static int left = 0;

    /** The right boundary for the binary search (highest number in the range) */
    private static int right = 100;

    /** The computer's guess, which is the middle number between the lowest and highest number */
    private static int guess = (left + right) / 2;

    /**
     * Performs a binary search based on the user's response to a guess.
     * @param predictedClass the predicted class returned by the model
     * @return an integer representing the next guess to make (-1 if the user's response is invalid)
     */
    public static int binarySearch(String predictedClass) {
        // Get the predicted class without the leading number
        String userResponse = predictedClass.substring(predictedClass.indexOf(" ") + 1);

        if (!isGuessCorrect(predictedClass)) {
            if (userResponse.equals("thumbsup")) {
                return guessHigher();
            }
            else if (userResponse.equals("thumbsdown")) {
                return guessLower();
            }
            else if (userResponse.equals("neutral")) {
                return guess;
            }
            else {
                return -1;
            }
        }
        else {
            return guess;
        }
    }

    /**
     * Checks if the user's guess is correct.
     * @param predictedClass the user's predicted class
     * @return true if the user's guess is "stop", false otherwise
     */
    public static boolean isGuessCorrect(String predictedClass) {
        // Get the predicted class without the leading number
        String userResponse = predictedClass.substring(predictedClass.indexOf(" ") + 1);

        if (userResponse.equals("stop")) {
            return true;
        }
        
        return false;
    }

    /**
     * Returns the next guess by assuming the number is higher than the current guess.
     * Updates the left boundary of the search space to be the current guess.
     * The next guess is the number in the middle of the left and right boundaries.
     * @return the next guess
     */
    public static int guessHigher() {
        // Set the left boundary to the current guess
        left = guess;

        // Set guess to the value in the middle of the left and right boundaries
        guess = (left + right) / 2;

        // Return the guess
        return guess;
    }

    /**
     * Calculates the next guess by setting the current guess as the upper bound and
     * gets the middle of the lower and upper bounds as the new guess.
     *
     * @return the new guess
     */
    public static int guessLower() {
        // Set the right boundary to the current guess
        right = guess;

        // Set guess to the value in the middle of the left and right boundaries
        guess = (left + right) / 2;

        // Return the guess
        return guess;
    }

    /**
     * Returns the correct guess.
     * @return the correct guess
     */
    public static int guessCorrect() {
        return guess;
    }

    /**
     * Returns a String containing the predicted class and confidence score.
     * 
     * @param predictedClass the predicted class with a leading number
     * @param predictedScore the confidence score of the predicted class
     * @return a String containing the predicted class and confidence score
     */
    public static String getUserResponse(String predictedClass, double predictedScore) {
        // Get the predicted class without the leading number
        String userResponse = predictedClass.substring(predictedClass.indexOf(" ") + 1);

        // Convert the predicted score to an integer percentage
        int confidencePercentage = (int)(predictedScore * 100);

        // Create a String containing the result of the predicted class and confidence score
        String userResult = "User: " + userResponse + " (" + confidencePercentage + "% Confidence)";

        // Return the String
        return userResult;
    }

    public static void resetGame() {
        left = 0;
        right = 100;
        guess = (left + right) / 2;
    }

}

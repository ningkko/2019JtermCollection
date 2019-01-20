package com.google.engedu.ghost;

public class GameData {

    public static final String TAG="PRINT";
    // for storage when pause/stop
    public static String storedText = "storedText";
    public static String storedLabel = "storedLabel";

    public static boolean inGame = false;

    public static final String COMPUTER_TURN = "Computer's turn";
    public static final String USER_TURN = "Your turn";

    public static final int MIN_WORD_LENGTH=4;

    public static final int POINT_PER_LETTER = 10;
    public static final int POINT_PER_TURN = 500;
    public static boolean PCfirst;
    public static int life;
    public static int totalPoints;


}

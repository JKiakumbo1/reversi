package service;

public class AppService {

    public static final Owner AIPlayer = Owner.NONE;
    public static int localThinkTime;
    private static  GameService  gameService = new GameService();

    public static int getSecsThink() {
        return 0;
    }

    public static boolean getOwnerIsBlack() {
        return false;
    }
}

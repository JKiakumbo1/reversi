package service;

import AI.AIService;
import UI.SceneGameController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import network.ReceiveMessageService;
import network.SendMessageService;

import java.net.Socket;
import java.util.LinkedList;

final public class GameService {

    // 8 directions for flip search
    private static final int[] DR = {1, 1, 0, -1, -1, -1, 0, 1};
    private static final int[] DC = {0, 1, 1, 1, 0, -1, -1, -1};

    private GameType gameType;
    private Owner[][] board = new Owner[8][8];

    // Indicates whether current player can chess here

    private boolean[][] hintBoard = new boolean[8][8];
    private Owner currentPlayer;

    // When playing online game, the player user is.
    private Owner netselfPlayer;
    private Owner winner = Owner.NONE;
    private int blackCount;
    private int whiteCount;
    private SceneGameController UIController;
    private Timeline secondTimer;
    private int currentTimerLeft;

    private int whiteWithdrawCount = 0;
    private int blackWithdrawCount = 0;

    private Socket socket;
    private ReceiveMessageService receiveMessageService;
    private SendMessageService sendMessageService;
    private AIService aiService = new AIService();

    private LinkedList<HistoryStatus> historyStatusList = new LinkedList<>();
    private FileHandler fileHandler = new FileHandler();

    GameService(){
        secondTimer = new Timeline(new KeyFrame(Duration.seconds(1.0), event -> onOneSecondPassed()));
        secondTimer.setCycleCount(Animation.INDEFINITE);
    }

    void initialize(GameType gameType){
        secondTimer.stop();

        this.gameType = gameType;
        this.winner = Owner.NONE;
        this.whiteWithdrawCount = 0;
        this.blackWithdrawCount = 0;

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++){
                board[i][j] = Owner.NONE;
                hintBoard[i][j] = false;
            }

        board[3][3] = Owner.WHITE;
        board[4][4] = Owner.WHITE;
        board[3][4] = Owner.BLACK;
        board[4][3] = Owner.BLACK;

        currentPlayer = Owner.BLACK;
        blackCount = 2;
        whiteCount = 2;

        historyStatusList.clear();
        historyStatusList.add(new HistoryStatus(this.board,Owner.BLACK));

        updateHintBoard();

        if ((gameType == GameType.ONLINE_GAME && currentPlayer != netselfPlayer) || checkShouldAIMove())
            UIController.setHintEnabled(false);
        else
            UIController.setHintEnabled(true);

        UIController.updateBoard();

        if (gameType == GameType.ONLINE_GAME)
            currentTimerLeft = AppService.getSecsThink();
        else
            currentTimerLeft = AppService.localThinkTime;

        if(checkShouldAIMove())
            AIMove();

        secondTimer.playFromStart();

    }

    private void AIMove() {

    }

    private boolean checkShouldAIMove() {
        return (gameType == GameType.LOCAL_GAME_AI && currentPlayer == AppService.AIPlayer);
    }

    public int getBlackCount() {
        return blackCount;
    }

    public int getWhiteCount() {
        return whiteCount;
    }

    public void pauseCountDown(){
        secondTimer.pause();
    }

    public void resumeCountDown(){
        secondTimer.play();
    }

    private void onOneSecondPassed() {
    }

    void setNetselfPlayer(Owner netselfPlayer){
        this.netselfPlayer = netselfPlayer;
    }

    private void initializeWhithFile(GameType gameType, String filename){

    }

    void initializeNetService(Socket socket){
        this.socket = socket;
        receiveMessageService = new ReceiveMessageService(socket);

    }

    Owner getWinner(){
        return winner;
    }

    private void updateHintBoard() {
    }

}
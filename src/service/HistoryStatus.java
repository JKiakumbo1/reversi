package service;

public class HistoryStatus {
    private Owner[][] board = new Owner[8][8];
    private Owner player; //Who will deal with this situation

    public HistoryStatus(Owner[][] board, Owner player) {
        for (int i = 0; i < 8; ++i)
            System.arraycopy(board[i],0, this.board[i],0,8);
        this.player = player;
    }

    HistoryStatus(){}

    Owner getPlayer() {
        return player;
    }

    void setPlayer(Owner player){
        this.player = player;
    }

    void copyTo(Owner[][] destination){
        for (int i = 0; i < 8; ++i)
            System.arraycopy(this.board[i],0, destination[i],0,8);
    }


    final Owner get(int row, int col) {
        return board[row][col];
    }

    void set(int row, int col, Owner owner){
        board[row][col] = owner;
    }


}

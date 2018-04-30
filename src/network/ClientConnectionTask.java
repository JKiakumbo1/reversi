package network;

import javafx.concurrent.Task;

import java.net.Socket;

public class ClientConnectionTask extends Task<Socket> {
    // Default: connect to localhost

    private String hostName = "localhost";

    public ClientConnectionTask(String hostName){
        this.hostName = hostName;
    }

    @Override
    protected Socket call() throws Exception {
        return new Socket(hostName, NetConst.SERVER_PORT);
    }
}

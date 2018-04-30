package network;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class LocalServerService extends Service<Socket> {

    private ServerSocket tcpServer;
    private  BroadcastService broadcastService;

    @Override
    public boolean cancel(){
        broadcastService.cancel();
        try {
            tcpServer.close();
        }
        catch (IOException ignored){}
        return super.cancel();
    }

    @Override
    protected Task<Socket> createTask() {
        return new Task<Socket>() {
            @Override
            protected Socket call() throws Exception {
                broadcastService = new BroadcastService();
                broadcastService.start();

                tcpServer = new ServerSocket(NetConst.SERVER_PORT);
                Socket result = null;
                try {
                    result = tcpServer.accept();
                }
                catch (SocketException e){
                    // When the socket is closed from another thread.
                    if (isCancelled())
                        return null;
                }
                Platform.runLater(() -> {
                    broadcastService.cancel();
                    try {
                        tcpServer.close();
                    }
                    catch (IOException ignored){}
                });
                return result;
            }
        };

    }
}

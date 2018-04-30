package network;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReceiveMessageService extends ScheduledService<NetMessage> {

    private final  Socket socket;

    public ReceiveMessageService(Socket socket) {
        this.socket = socket;
        setRestartOnFailure(false);

    }

    @Override
    protected Task<NetMessage> createTask() {
        return new Task<NetMessage>() {
            @Override
            protected NetMessage call() throws Exception {
                // Socket close auto detect
                BufferedReader bufferedReaderIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String incomingMessage = bufferedReaderIn.readLine();
                return NetMessage.fromString(incomingMessage);
            }
        };
    }
}

package network;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.LinkedList;

public class SendMessageService extends ScheduledService {
    private final LinkedList<String> queuedMessage;
    private final Socket socket;

    public SendMessageService(Socket socket) {
        this.socket = socket;
        queuedMessage = new LinkedList<>();
        setRestartOnFailure(false);
    }

    public void sendMessage(NetMessage netMessage){
        synchronized (queuedMessage){
            queuedMessage.addLast(netMessage.getString());
            queuedMessage.notify();
        }
    }

    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                String messageToSend;
                synchronized (queuedMessage){
                    if (queuedMessage.size() == 0){
                        try {
                            queuedMessage.wait();
                        }
                        catch (InterruptedException ignored){}
                    }
                    messageToSend = queuedMessage.removeFirst();
                }
                // When exception was thrown, the service fails(indicates disconnection)
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeBytes(messageToSend + "\n");
                return null;
            }
        };
    }
}

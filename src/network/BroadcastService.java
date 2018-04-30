package network;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import service.AppService;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class BroadcastService extends Service {

    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                String packegeHeader = NetConst.BROADCAST_HEADER + " "
                        +((AppService.getOwnerIsBlack()) ? '1' : '0') + " "
                        +(String.valueOf(AppService.getSecsThink())) + " ";
                DatagramSocket udpSocket = new DatagramSocket();
                udpSocket.setBroadcast(true);
                while (true){
                    // Send a UDP package about this server
                    // ip address is updated every time because net env might change.
                    byte[] dataToSend = (packegeHeader + InetAddress
                            .getLocalHost()
                            .getHostAddress()).getBytes();
                    DatagramPacket udpPackat = new DatagramPacket(dataToSend, dataToSend.length);
                    udpPackat.setAddress(InetAddress.getByName(NetConst.getBroadcastName()));
                    udpPackat.setPort(NetConst.BROADCAST_PORT);

                    udpSocket.send(udpPackat);
                    // Every 0.5 seconds
                    try{
                        Thread.sleep(500);
                    }
                    catch (InterruptedException e){
                        if (isCancelled())
                            break;
                    }
                    if (isCancelled())
                        break;
                }
                return null;
            }
        };
    }
}

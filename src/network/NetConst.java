package network;

public class NetConst {

    static final String BROADCAST_HEADER = "REV-MUL1.0";
    static final int BROADCAST_PORT = 4466;
    static final int SERVER_PORT =4477;

    static String getBroadcastName(){
        return "255.255.255.255";
    }
}

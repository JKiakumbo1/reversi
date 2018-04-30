package network;

public class NetMessage {

    public NetEvent event;
    private String content;

    private NetMessage(){ }

    public NetEvent getEvent() {
        return event;
    }

    public void setEvent(NetEvent event) {
        this.event = event;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static NetMessage fromString(String rawMessage) {
        NetMessage result = new NetMessage();
        result.setContent(rawMessage.substring(1));
        result.setEvent(NetEvent.fromChar(rawMessage.charAt(0)));
        return result;
    }

    public String getString(){
        return NetEvent.toChar(event) + content;
    }
}

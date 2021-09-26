package io.github.wuare.hl.listener;

public class ListenerEvent {

    private Object source;

    public ListenerEvent() {
    }

    public ListenerEvent(Object source) {
        this.source = source;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
}

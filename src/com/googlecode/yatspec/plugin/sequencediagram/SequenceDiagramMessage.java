package com.googlecode.yatspec.plugin.sequencediagram;

public class SequenceDiagramMessage {
    private final String from;
    private final String to;
    private final String messageName;
    private final String messageId;

    public SequenceDiagramMessage(String from, String to, String messageName, String messageId) {
        this.from = from;
        this.to = to;
        this.messageName = messageName;
        this.messageId = messageId;
    }

    public String from() {
        return from;
    }

    public String to() {
        return to;
    }

    public String messageName() {
        return messageName;
    }

    public String messageId() {
        return messageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SequenceDiagramMessage that = (SequenceDiagramMessage) o;

        if (!from.equals(that.from)) return false;
        if (!messageId.equals(that.messageId)) return false;
        if (!messageName.equals(that.messageName)) return false;
        if (!to.equals(that.to)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = from.hashCode();
        result = 31 * result + to.hashCode();
        result = 31 * result + messageName.hashCode();
        result = 31 * result + messageId.hashCode();
        return result;
    }
}

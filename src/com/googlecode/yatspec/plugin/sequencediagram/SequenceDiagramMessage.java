package com.googlecode.yatspec.plugin.sequencediagram;

class SequenceDiagramMessage {
    private final String visibleName;
    private final String fullyQualifiedMessageName;

    SequenceDiagramMessage(String visibleName, String fullyQualifiedMessageName) {
        this.visibleName = visibleName;
        this.fullyQualifiedMessageName = fullyQualifiedMessageName;
    }

    public String getVisibleName() {
        return visibleName;
    }

    public String getFullyQualifiedMessageName() {
        return fullyQualifiedMessageName;
    }
}

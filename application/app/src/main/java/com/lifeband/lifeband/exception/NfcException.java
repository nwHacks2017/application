package com.lifeband.lifeband.exception;

public class NfcException extends LifebandException {

    private Reason reason;

    public NfcException(Reason reason) {
        super();
        this.reason = reason;
    }

    public Reason getReason() {
        return reason;
    }

    @Override
    public String getMessage() {
        return reason.getText();
    }

    public static enum Reason {
        INVALID_TAG_TYPE("Invalid tag type; NDEF is required"),
        INVALID_CONTENT_TYPE("Invalid content type; text/plain is required"),
        INVALID_ENCODING("Invalid text encoding");

        private String text;

        Reason(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}

package com.lifeband.lifeband.exception;

public class ServerException extends LifebandException {

    private Reason reason;
    private String details;

    public ServerException(Reason reason) {
        super();
        this.reason = reason;
    }

    public ServerException(Reason reason, String details) {
        this(reason);
        this.details = details;
    }

    public Reason getReason() {
        return reason;
    }

    @Override
    public String getMessage() {
        if(details != null) {
            return reason.getText() + ": " + details;
        }
        return reason.getText();
    }

    public static enum Reason {
        PARSE_JSON_FAILURE("Failed to parse JSON response from server"),
        UNHANDLED_ERROR("Unhandled error");

        private String text;

        private Reason(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}
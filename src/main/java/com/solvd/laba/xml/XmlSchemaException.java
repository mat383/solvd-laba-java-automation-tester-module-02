package com.solvd.laba.xml;

public class XmlSchemaException extends Exception {
    public XmlSchemaException() {
    }

    public XmlSchemaException(String message) {
        super(message);
    }

    public XmlSchemaException(String message, Throwable cause) {
        super(message, cause);
    }

    public XmlSchemaException(Throwable cause) {
        super(cause);
    }

    public XmlSchemaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

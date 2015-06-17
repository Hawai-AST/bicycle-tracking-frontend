package models.utility;

public enum ContentType {
    APP_JSON("application/json"),
    APP_FORM_ENC("application/x-www-form-urlencoded")
    ;

    private final String value;

    ContentType(String contentType) {
        this.value = contentType;
    }

    public String value() {
        return this.value;
    }

}

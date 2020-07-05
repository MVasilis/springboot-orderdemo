package eu.acme.demo.web.response;

public class ErrorResponse {

    private String ErrorCode;

    private String ErrorMessage;

    public ErrorResponse(String errorCode, String errorMessage) {
        ErrorCode = errorCode;
        ErrorMessage = errorMessage;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }
}

package io.github.wuare.admin.domain.common;

public class ApiResponse<T> {

    private int code;
    private String message;
    private T result;

    public ApiResponse(int code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(200, "success", null);
    }
    public static <T> ApiResponse<T> ok(T result) {
        return new ApiResponse<>(200, "success", result);
    }

    public static <T> ApiResponse<T> error() {
        return new ApiResponse<>(500, "error", null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(500, message, null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}

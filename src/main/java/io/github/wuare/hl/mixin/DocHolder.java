package io.github.wuare.hl.mixin;

public class DocHolder {

    private String url;
    private String methodName;

    public DocHolder() {
    }

    public DocHolder(String url, String methodName) {
        this.url = url;
        this.methodName = methodName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}

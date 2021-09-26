package io.github.wuare.hl.interceptor;

public class WebInterceptorHolder {

    public WebInterceptorHolder() {
    }

    public WebInterceptorHolder(WebInterceptor webInterceptor, int order) {
        this.webInterceptor = webInterceptor;
        this.order = order;
    }

    private WebInterceptor webInterceptor;
    private int order;

    public WebInterceptor getWebInterceptor() {
        return webInterceptor;
    }

    public void setWebInterceptor(WebInterceptor webInterceptor) {
        this.webInterceptor = webInterceptor;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}

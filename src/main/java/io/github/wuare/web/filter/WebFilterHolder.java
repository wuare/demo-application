package io.github.wuare.web.filter;

public class WebFilterHolder {

    private WebFilter webFilter;
    private int order;

    public WebFilterHolder() {
    }

    public WebFilterHolder(WebFilter webFilter, int order) {
        this.webFilter = webFilter;
        this.order = order;
    }

    public WebFilter getWebFilter() {
        return webFilter;
    }

    public void setWebFilter(WebFilter webFilter) {
        this.webFilter = webFilter;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}

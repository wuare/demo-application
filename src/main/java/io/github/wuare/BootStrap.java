package io.github.wuare;

import io.github.wuare.web.ServerContainer;

import java.io.IOException;

public class BootStrap {
    public static void main(String[] args) throws IOException {
        ServerContainer.instance.start();
    }
}

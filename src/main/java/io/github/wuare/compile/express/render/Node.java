package io.github.wuare.compile.express.render;

import java.util.ArrayList;
import java.util.List;

public class Node {

    public Node(String name) {
        this.name = name;
    }

    private String name;
    private List<Node> children = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }
}

package io.github.wuare.compile.express.ast;

import io.github.wuare.compile.express.visitor.ExpVisitor;

public class NameExpr implements Expr {

    private final String text;

    public NameExpr(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public void accept(ExpVisitor visitor) {
        visitor.visitNameExpr(this);
    }
}

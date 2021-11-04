package io.github.wuare.compile.express.ast;


import io.github.wuare.compile.express.ExpToken;
import io.github.wuare.compile.express.ExpTokenType;
import io.github.wuare.compile.express.visitor.ExpVisitor;

public class OperatorExpr implements Expr {

    private ExpToken token;
    private ExpTokenType type;
    private Expr left;
    private Expr right;

    public OperatorExpr(ExpToken token, ExpTokenType type, Expr left, Expr right) {
        this.token = token;
        this.type = type;
        this.left = left;
        this.right = right;
    }

    public ExpToken getToken() {
        return token;
    }

    public void setToken(ExpToken token) {
        this.token = token;
    }

    public ExpTokenType getType() {
        return type;
    }

    public void setType(ExpTokenType type) {
        this.type = type;
    }

    public Expr getLeft() {
        return left;
    }

    public void setLeft(Expr left) {
        this.left = left;
    }

    public Expr getRight() {
        return right;
    }

    public void setRight(Expr right) {
        this.right = right;
    }

    @Override
    public void accept(ExpVisitor visitor) {
        visitor.visitOperatorExpr(this);
    }
}

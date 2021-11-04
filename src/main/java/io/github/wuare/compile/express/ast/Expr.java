package io.github.wuare.compile.express.ast;

import io.github.wuare.compile.express.visitor.ExpVisitor;

public interface Expr extends AST {

    void accept(ExpVisitor visitor);
}

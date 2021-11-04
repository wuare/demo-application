package io.github.wuare.compile.express.visitor;

import io.github.wuare.compile.express.ast.Expr;
import io.github.wuare.compile.express.ast.NameExpr;
import io.github.wuare.compile.express.ast.OperatorExpr;
import io.github.wuare.compile.express.ast.PrefixExpr;

public interface ExpVisitor {

    void visitNameExpr(NameExpr nameExpr);
    void visitOperatorExpr(OperatorExpr operatorExpr);
    void visitPrefixExpr(PrefixExpr prefixExpr);
}

package io.github.wuare.compile.express.parser;

import io.github.wuare.compile.express.ExpParser;
import io.github.wuare.compile.express.ExpToken;
import io.github.wuare.compile.express.ast.Expr;

public interface InfixParseLet {
    Expr parse(ExpParser parser, Expr left, ExpToken token);

    int getPrecedence();
}

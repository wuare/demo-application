package io.github.wuare.compile.express.render;

import io.github.wuare.compile.express.ExpTokenType;
import io.github.wuare.compile.express.ast.Expr;
import io.github.wuare.compile.express.ast.NameExpr;
import io.github.wuare.compile.express.ast.OperatorExpr;
import io.github.wuare.compile.express.ast.PrefixExpr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TreeRender {

    public Node render(Expr expr) {
        if (expr == null) {
            return null;
        }
        if (expr instanceof NameExpr) {
            return new Node(((NameExpr) expr).getText());
        }
        if (expr instanceof PrefixExpr) {
            PrefixExpr prefixExpr = (PrefixExpr) expr;
            if (prefixExpr.getTokenType() == ExpTokenType.LPAREN) {
                return render(prefixExpr.getOperand());
            }
            Node node = new Node(prefixExpr.getTokenType().getText());
            node.getChildren().add(render(prefixExpr.getOperand()));
            return node;
        }
        if (expr instanceof OperatorExpr) {
            OperatorExpr operatorExpr = (OperatorExpr) expr;
            Node node = new Node(operatorExpr.getType().getText());
            node.getChildren().add(render(operatorExpr.getLeft()));
            node.getChildren().add(render(operatorExpr.getRight()));
            return node;
        }
        return null;
    }

    public Map<String, Object> render2(Expr expr) {
        if (expr == null) {
            return null;
        }
        if (expr instanceof NameExpr) {
            Map<String, Object> map = new HashMap<>();
            String text = ((NameExpr) expr).getText();
            map.put("name", text);
            map.put("value", text);
            return map;
        }
        if (expr instanceof PrefixExpr) {
            PrefixExpr prefixExpr = (PrefixExpr) expr;
            Map<String, Object> map = new HashMap<>();
            map.put("name", prefixExpr.getTokenType().getText());
            List<Map<String, Object>> list = new ArrayList<>();
            list.add(render2(prefixExpr.getOperand()));
            map.put("children", list);
            return map;
        }
        if (expr instanceof OperatorExpr) {
            OperatorExpr operatorExpr = (OperatorExpr) expr;
            Map<String, Object> map = new HashMap<>();
            map.put("name", operatorExpr.getType().getText());
            Node node = new Node(operatorExpr.getType().getText());
            List<Map<String, Object>> list = new ArrayList<>();
            list.add(render2(operatorExpr.getLeft()));
            list.add(render2(operatorExpr.getRight()));
            map.put("children", list);
            return map;
        }
        return null;
    }
}

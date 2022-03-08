package io.github.wuare.web.controller;

import io.github.wuare.compile.express.ExpParser;
import io.github.wuare.compile.express.ast.Expr;
import io.github.wuare.compile.express.render.Node;
import io.github.wuare.compile.express.render.TreeRender;
import io.github.wuare.web.anno.Controller;
import io.github.wuare.web.anno.PostMapping;
import io.github.wuare.web.anno.ResponseBody;
import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpResponse;

@Controller
public class ExprController {

    private final ExpParser parser = new ExpParser();
    private final TreeRender treeRender = new TreeRender();

    @ResponseBody
    @PostMapping("/expr/tree")
    public Node tree(HttpRequest request, HttpResponse response) {
        String body = request.getBody();
        Expr expr = parser.parse(body);
        return treeRender.render(expr);
    }
}

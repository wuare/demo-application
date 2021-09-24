package io.github.wuare.admin.controller.user;

import io.github.wuare.admin.domain.common.ApiResponse;
import io.github.wuare.admin.domain.req.LoginReq;
import io.github.wuare.admin.domain.vo.LoginVO;
import io.github.wuare.admin.mock.MockUser;
import io.github.wuare.hl.anno.Controller;
import io.github.wuare.hl.anno.GetMapping;
import io.github.wuare.hl.anno.PostMapping;
import top.wuare.http.define.HttpStatus;
import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpResponse;
import top.wuare.json.Wson;

@Controller
public class UserController {

    @PostMapping("/api/auth/login")
    public void login(HttpRequest request, HttpResponse response) {
        response.addHeader("Content-Type", "application/json;charset=UTF-8");
        String body = request.getBody();
        Wson wson = new Wson();
        LoginReq loginReq = wson.fromJson(body, LoginReq.class);
        if (loginReq != null && "admin".equals(loginReq.getUsername())
                && "21232f297a57a5a743894a0e4a801fc3".equals(loginReq.getPassword())) {
            response.setBody(wson.toJson(ApiResponse.ok(new LoginVO(loginReq.getUsername()))));
            return;
        }
        response.setStatus(HttpStatus.UNAUTHORIZED);
        response.setBody(wson.toJson(ApiResponse.error()));
    }

    @PostMapping("/api/auth/logout")
    public void logout(HttpRequest request, HttpResponse response) {
        response.addHeader("Content-Type", "application/json;charset=UTF-8");
        Wson wson = new Wson();
        response.setBody(wson.toJson(ApiResponse.ok()));
    }

    @GetMapping("/api/user/info")
    public void info(HttpRequest request, HttpResponse response) {
        response.addHeader("Content-Type", "application/json;charset=UTF-8");
        response.setBody(MockUser.userInfo);
    }

    @GetMapping("/api/user/nav")
    public void nav(HttpRequest request, HttpResponse response) {
        response.addHeader("Content-Type", "application/json;charset=UTF-8");
        response.setBody(MockUser.nav);
    }

    @GetMapping("/api/role")
    public void role(HttpRequest request, HttpResponse response) {
        response.addHeader("Content-Type", "application/json;charset=UTF-8");
        response.setBody(MockUser.role);
    }

    @GetMapping("/api/service")
    public void service(HttpRequest request, HttpResponse response) {
        response.addHeader("Content-Type", "application/json;charset=UTF-8");
        response.setBody(MockUser.service);
    }

    @GetMapping("/api/list/search/projects")
    public void projects(HttpRequest request, HttpResponse response) {
        response.addHeader("Content-Type", "application/json;charset=UTF-8");
        response.setBody(MockUser.projects);
    }

    @GetMapping("/api/workplace/activity")
    public void activity(HttpRequest request, HttpResponse response) {
        response.addHeader("Content-Type", "application/json;charset=UTF-8");
        response.setBody(MockUser.activity);
    }

    @GetMapping("/api/workplace/teams")
    public void teams(HttpRequest request, HttpResponse response) {
        response.addHeader("Content-Type", "application/json;charset=UTF-8");
        response.setBody(MockUser.teams);
    }

    @GetMapping("/api/workplace/radar")
    public void radar(HttpRequest request, HttpResponse response) {
        response.addHeader("Content-Type", "application/json;charset=UTF-8");
        response.setBody(MockUser.radar);
    }
}

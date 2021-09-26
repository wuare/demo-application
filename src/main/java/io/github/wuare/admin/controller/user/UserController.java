package io.github.wuare.admin.controller.user;

import io.github.wuare.admin.domain.bo.token.JWT;
import io.github.wuare.admin.domain.common.ApiResponse;
import io.github.wuare.admin.domain.req.LoginReq;
import io.github.wuare.admin.domain.req.user.RefreshTokenReq;
import io.github.wuare.admin.domain.vo.LoginVO;
import io.github.wuare.admin.domain.vo.user.RefreshTokenVO;
import io.github.wuare.admin.mock.MockUser;
import io.github.wuare.hl.anno.Controller;
import io.github.wuare.hl.anno.GetMapping;
import io.github.wuare.hl.anno.PostMapping;
import io.github.wuare.hl.anno.ResponseBody;
import io.github.wuare.hl.exception.JwtDecodeException;
import io.github.wuare.hl.util.JsonUtil;
import io.github.wuare.hl.util.JwtUtil;
import io.github.wuare.hl.util.Md5Util;
import io.github.wuare.hl.util.StringUtil;
import top.wuare.http.define.HttpStatus;
import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpResponse;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class UserController {

    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    @ResponseBody
    @PostMapping("/api/auth/login")
    public ApiResponse<?> login(HttpRequest request, HttpResponse response) {
        String body = request.getBody();
        LoginReq loginReq = JsonUtil.fromJson(body, LoginReq.class);
        if (loginReq != null && StringUtil.isNotEmpty(loginReq.getUsername())
                && Md5Util.md5(loginReq.getUsername().getBytes(StandardCharsets.UTF_8))
                .equals(loginReq.getPassword())) {
            return ApiResponse.ok(new LoginVO(JwtUtil.generateToken(loginReq.getUsername(), 1),
                    JwtUtil.generateToken(loginReq.getUsername(), 2)));
        }
        return ApiResponse.error("用户名或密码错误");
    }

    @PostMapping("/api/auth/logout")
    public void logout(HttpRequest request, HttpResponse response) {
        response.addHeader("Content-Type", "application/json;charset=UTF-8");
        response.setBody(JsonUtil.toJson(ApiResponse.ok()));
    }

    @ResponseBody
    @PostMapping("/api/auth/token/refresh")
    public ApiResponse<?> refreshToken(HttpRequest request, HttpResponse response) {
        String body = request.getBody();
        if (StringUtil.isEmpty(body)) {
            return ApiResponse.error("参数错误");
        }
        RefreshTokenReq refreshTokenReq = JsonUtil.fromJson(body, RefreshTokenReq.class);
        try {
            JWT jwt = JwtUtil.decodeToken(refreshTokenReq.getRefreshToken());
            Date now = new Date();
            if (now.getTime() > jwt.getExp()) {
                // TODO 过期，需要登录
                return ApiResponse.error();
            }
            // TODO 返回新token和refresh token
            String token = JwtUtil.generateToken(jwt.getUserId(), 1);
            String refreshToken = JwtUtil.generateToken(jwt.getUserId(), 2);
            RefreshTokenVO vo = new RefreshTokenVO();
            vo.setToken(token);
            vo.setRefreshToken(refreshToken);
            return ApiResponse.ok(vo);
        } catch (JwtDecodeException e) {
            logger.log(Level.SEVERE, "解析token错误", e);
            return ApiResponse.error("token解析错误");
        }
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

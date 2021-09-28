package io.github.wuare.admin.controller.doc;

import io.github.wuare.admin.domain.common.ApiResponse;
import io.github.wuare.hl.JavaHighLight;
import io.github.wuare.hl.anno.Controller;
import io.github.wuare.hl.anno.GetMapping;
import io.github.wuare.hl.anno.ResponseBody;
import io.github.wuare.hl.exception.MultiPartFormException;
import io.github.wuare.hl.mixin.DocHolder;
import io.github.wuare.hl.multipart.MultiPartFormData;
import io.github.wuare.hl.multipart.MultiPartFormDataHelper;
import top.wuare.http.proto.HttpRequest;

import java.util.List;

@Controller
public class DocumentController {

    @ResponseBody
    @GetMapping("/api/doc/list")
    public ApiResponse<?> docList() {
        List<DocHolder> docHolders = JavaHighLight.instance.getDocHolders();
        return ApiResponse.ok(docHolders);
    }

    @GetMapping("/api/doc/upload")
    public void upload(HttpRequest request) throws MultiPartFormException {
        MultiPartFormData formData = MultiPartFormDataHelper.handle(request);
        formData.clear();
    }
}

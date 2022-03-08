package io.github.wuare.admin.controller.doc;

import io.github.wuare.admin.domain.common.ApiResponse;
import io.github.wuare.web.ServerContainer;
import io.github.wuare.web.anno.Controller;
import io.github.wuare.web.anno.GetMapping;
import io.github.wuare.web.anno.ResponseBody;
import io.github.wuare.web.exception.MultiPartFormException;
import io.github.wuare.web.mixin.DocHolder;
import io.github.wuare.web.multipart.MultiPartFormData;
import io.github.wuare.web.multipart.MultiPartFormDataHelper;
import top.wuare.http.proto.HttpRequest;

import java.util.List;

@Controller
public class DocumentController {

    @ResponseBody
    @GetMapping("/api/doc/list")
    public ApiResponse<?> docList() {
        List<DocHolder> docHolders = ServerContainer.instance.getDocHolders();
        return ApiResponse.ok(docHolders);
    }

    @GetMapping("/api/doc/upload")
    public void upload(HttpRequest request) throws MultiPartFormException {
        MultiPartFormData formData = MultiPartFormDataHelper.handle(request);
        formData.clear();
    }
}

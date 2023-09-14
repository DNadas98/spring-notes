package com.dnadas.spring_notes.controller.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Controller
public class JSONErrorController implements ErrorController {

  private final ErrorAttributes errorAttributes;
  private final Logger logger;

  public JSONErrorController(ErrorAttributes errorAttributes) {
    this.errorAttributes = errorAttributes;
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  @RequestMapping("/error")
  public ResponseEntity<Map<String, Object>> handleError(WebRequest webRequest) {
    Map<String, Object> errorAttributesData = errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.defaults());
    int status = (int) errorAttributesData.get("status");
    Throwable e = errorAttributes.getError(webRequest);

    logger.error(status + "\t" + (e != null ? e.getMessage() : "Unknown Error"), e);

    if (status == 404) {
      return ResponseEntity.status(404).body(Map.of("error", "Not Found", "status", 404));
    } else {
      return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error", "status", 500));
    }
  }
}

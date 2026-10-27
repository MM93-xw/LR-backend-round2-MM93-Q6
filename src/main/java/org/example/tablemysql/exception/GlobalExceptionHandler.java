package org.example.tablemysql.exception;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 拦截参数异常 (400)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(BadRequestException e) {
        return ResponseEntity.badRequest().body(Map.of("code", 400, "message", e.getMessage()));
    }

    // 拦截业务异常 (400)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusiness(BusinessException e) {
        return ResponseEntity.badRequest().body(Map.of("code", 400, "message", e.getMessage()));
    }

    // 拦截数据库唯一索引冲突（高并发防重兜底）
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateKey(DuplicateKeyException e) {
        // 当并发导致数据库报错时，给前端返回统一的业务提示
        return ResponseEntity.badRequest().body(Map.of("code", 400, "message", "文章标题已存在，请勿重复提交"));
    }
}
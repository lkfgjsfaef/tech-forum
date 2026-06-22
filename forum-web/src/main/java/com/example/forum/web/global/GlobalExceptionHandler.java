package com.example.forum.web.global;

import com.example.forum.api.model.exception.ForumAdviceException;
import com.example.forum.api.model.vo.ResVo;
import com.example.forum.api.model.vo.constants.StatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ForumAdviceException.class)
    public ResVo<String> handleForumAdviceException(ForumAdviceException e) {
        log.warn("业务异常: {}", e.getMessage());
        return ResVo.fail(e.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResVo<String> handleValidationException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("参数校验失败: {}", msg);
        return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS, msg);
    }

    @ExceptionHandler(BindException.class)
    public ResVo<String> handleBindException(BindException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("参数绑定失败: {}", msg);
        return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS, msg);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResVo<String> handleConstraintViolation(ConstraintViolationException e) {
        String msg = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        log.warn("约束校验失败: {}", msg);
        return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS, msg);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResVo<String> handleMissingParam(MissingServletRequestParameterException e) {
        log.warn("缺少请求参数: {}", e.getParameterName());
        return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS, "缺少必填参数: " + e.getParameterName());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResVo<String> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String required = e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "";
        log.warn("参数类型错误: 参数={}, 期望类型={}", e.getName(), required);
        return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS,
                "参数「 + e.getName() + 」类型错误，期望: " + required);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResVo<String> handleMessageNotReadable(HttpMessageNotReadableException e) {
        log.warn("请求体解析失败: {}", e.getMessage());
        return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS, "请求参数格式错误，请检查JSON格式");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResVo<String> handleDataIntegrity(DataIntegrityViolationException e) {
        String msg = e.getMessage();
        log.error("数据完整性异常: {}", msg);

        if (msg != null && msg.contains("Duplicate entry")) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS, "数据已存在，请勿重复提交");
        }
        if (msg != null && msg.contains("cannot be null")) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS, "必填字段不能为空");
        }
        if (msg != null && msg.contains("Data too long")) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS, "输入内容过长");
        }
        return ResVo.fail(StatusEnum.UNEXPECT_ERROR, "数据操作异常，请稍后重试");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResVo<String> handleDuplicateKey(DuplicateKeyException e) {
        log.warn("数据重复: {}", e.getMessage());
        return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS, "数据已存在，请勿重复操作");
    }

    @ExceptionHandler(DataAccessException.class)
    public ResVo<String> handleDataAccess(DataAccessException e) {
        log.error("数据库访问异常", e);
        return ResVo.fail(StatusEnum.UNEXPECT_ERROR, "数据库异常，请稍后重试");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResVo<String> handleMaxUploadSize(MaxUploadSizeExceededException e) {
        log.warn("文件上传过大: {}", e.getMessage());
        return ResVo.fail(StatusEnum.UPLOAD_PIC_FAILED, "上传文件过大，请选择更小的文件");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResVo<String> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        log.warn("不支持的请求方法: {}", e.getMethod());
        return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS, "不支持的请求方式: " + e.getMethod());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResVo<String> handleIllegalState(IllegalStateException e) {
        log.warn("非法状态: {}", e.getMessage());
        return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResVo<String> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("非法参数: {}", e.getMessage());
        return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS, e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResVo<String> handleNPE(NullPointerException e) {
        log.error("空指针异常", e);
        return ResVo.fail(StatusEnum.UNEXPECT_ERROR, "系统内部错误，请联系管理员");
    }

    @ExceptionHandler(Exception.class)
    public ResVo<String> handleGeneric(Exception e) {
        log.error("未捕获的异常", e);
        return ResVo.fail(StatusEnum.UNEXPECT_ERROR, "系统繁忙，请稍后重试");
    }
}




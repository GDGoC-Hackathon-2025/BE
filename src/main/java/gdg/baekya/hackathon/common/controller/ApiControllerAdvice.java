package gdg.baekya.hackathon.common.controller;

import gdg.baekya.hackathon.common.response.ApiResponse;
import gdg.baekya.hackathon.common.error.CustomException;
import gdg.baekya.hackathon.common.error.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleValidationException(MethodArgumentNotValidException ex) {
        CustomException exception = new CustomException(ErrorCode.BAD_REQUEST);
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        exception.getErrorCode().changeMessage(message);
        return ApiResponse.fail(exception);
    }

    // 요청된 URL에 해당하는 핸들러가 없을 때 발생.
    // 지원되지 않는 HTTP 메서드 요청 시 발생.
    @ExceptionHandler(value = {NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public ApiResponse<?> handleNoPageFoundException(Exception e) {
        log.error("GLOBAL EXCEPTION {} 발생", e);
        return ApiResponse.fail(new CustomException(ErrorCode.NOT_FOUND_END_POINT));
    }

    // 요청 파라미터가 예상한 타입과 일치하지 않을 때 발생.
    @ExceptionHandler(value = TypeMismatchException.class)
    public ApiResponse<?> handleMismatchException(Exception e) {
        log.error("GLOBAL EXCEPTION {} 발생", e);
        return ApiResponse.fail(new CustomException(ErrorCode.BAD_REQUEST));
    }

    // NullPointException 일 때, 예외처리
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiResponse<?> handleNullPointException(NullPointerException e) {
        log.error("GLOBAL EXCEPTION {} 발생", e);
        return ApiResponse.fail(new CustomException(ErrorCode.BAD_REQUEST));
    }

    // NoSuchElementException, EntityNotFoundException 일 때, 예외처리
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {NoSuchElementException.class,EntityNotFoundException.class})
    public ApiResponse<?> handleNoSuchElementException(Exception e) {
        log.error("GLOBAL EXCEPTION {} 발생", e);
        return ApiResponse.fail(new CustomException(ErrorCode.NOT_FOUND_END_POINT));
    }

    // NoSuchElementException, EntityNotFoundException 일 때, 예외처리
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {IllegalStateException.class})
    public ApiResponse<?> handleIllegalStateException(Exception e) {
        log.error("GLOBAL EXCEPTION {} 발생", e);
        return ApiResponse.fail(new CustomException(ErrorCode.BAD_REQUEST));
    }
}

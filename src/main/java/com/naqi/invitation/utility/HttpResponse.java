package com.naqi.invitation.utility;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class HttpResponse {

    public HttpResponse(String requestUri) {
        this.timestamp = DateTimeUtil.currentTimestamp();
        this.path = requestUri;
    }

    private Date timestamp;
    private int status;
    private String error;
    private String message;
    private Object data;
    private String path;
    private String errorCode;




    /**
     * *
     * Sets success and message as reason phrase of provided status.
     *
     * @param status
     */
    public void setStatus(HttpStatus status) {
        this.status = status.value();
        this.message = status.getReasonPhrase();
    }

    public void setStatus(HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
    }

    public void setStatus(HttpStatus status, String message,String errorCode) {
        this.status = status.value();
        this.message = message;
        this.errorCode = errorCode;

    }
}


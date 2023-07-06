package com.naqi.invitation.utility;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class HttpResult {

    public int resultCode;
    public int httpResponseCode;
    public String responseBody;
    public String errorMessage;
}

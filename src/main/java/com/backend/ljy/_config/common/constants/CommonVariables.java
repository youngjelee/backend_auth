package com.backend.ljy._config.common.constants;

public class CommonVariables {


    //accessToken 유효시간 MIN(분) , MIL(밀리세컨즈)
    public static final int ACCESS_TOKEN_VALIDITY_SEC = 60*60;
    public static final int ACCESS_TOKEN_VALIDITY_MIL = ACCESS_TOKEN_VALIDITY_SEC*1000;

    public static final int REFRESH_TOKEN_VALIDITY_SEC = 60*60*24;
}

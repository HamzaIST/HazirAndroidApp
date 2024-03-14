package technology.innovate.haziremployee.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Constants {
    @Retention(RetentionPolicy.SOURCE)
    public @interface LOG_ACTION {
        int PUNCH_IN = 1;
        int PUNCH_OUT = 2;
        int BREAK_IN = 3;
        int BREAK_OUT = 4;
    }

    public @interface API_RESPONSE_CODE {
        String OK = "ok";
        String NOT_OK = "notok";
        int TOKEN_EXPIRED = 401;
    }


}
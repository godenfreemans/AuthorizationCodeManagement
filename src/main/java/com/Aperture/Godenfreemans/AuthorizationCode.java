package com.Aperture.Godenfreemans;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AuthorizationCode {
    private String code;
    private String totalNumber;
    private String AuthorizedNumber;
    private String remarks;
    private String data;

    AuthorizationCode(String code, String totalNumber, String authorizedNumber, String remarks) {
        this.data = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        this.code = code;
        this.totalNumber = totalNumber;
        this.AuthorizedNumber = authorizedNumber;
        this.remarks = remarks;
    }

    String getCode() {
        return code;
    }

    String getTotalNumber() {
        return totalNumber;
    }

    void setAuthorizedNumber(String authorizedNumber) {
        AuthorizedNumber = authorizedNumber;
    }

    String getRemarks() {
        return remarks;
    }

    String getData() {
        return data;
    }

    String getAuthorizedNumber() {
        return AuthorizedNumber;
    }
}

package com.ywwl.customcard.model;

import lombok.Data;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
public class RequestDownloadModel {
    @NotEmpty
    private String [] companyCodes;
    @NotNull
    private String effectTime;
}

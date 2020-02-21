package com.ywwl.customcard.model;

import lombok.Data;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
public class RequestDownloadModel {
    @NotEmpty
    private List<String> companyCodes;
    @NotNull
    private String effectTime;
}

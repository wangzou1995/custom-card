package com.ywwl.customcard.model;

import lombok.Data;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;


@Data
public class RequestDownloadModel {
    @NotEmpty(message = "companyCodes 不能为空")
    private List<String> companyCodes;
    @NotNull(message = "effectTime 不能为空")
    @Pattern(regexp = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|" +
            "(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$"
    , message = "effectTime 日期参数异常")
    private String effectTime;
}

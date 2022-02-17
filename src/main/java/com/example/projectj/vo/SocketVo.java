package com.example.projectj.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SocketVo {
    private Long roomId;

    private String userId;

    private String userName;

    private String content;

    private String date;

    private List<Map<String, Object>> userInfo;

    private String type;

    public SocketVo(String userName, String content, String date, String type) {
        this.userName = userName;
        this.content = content;
        this.date = date;
        this.type = type;
    }

    public SocketVo(String userName, String content, String date, List<Map<String, Object>> userInfo, String type) {
        this.userName = userName;
        this.content = content;
        this.date = date;
        this.userInfo = userInfo;
        this.type = type;
    }
}
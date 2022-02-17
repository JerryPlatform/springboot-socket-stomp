package com.example.projectj.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SocketVo {
    private Long roomId;

    private String userName;

    private String content;

    private String date;

    private String type;

    public SocketVo(String userName, String content, String date, String type) {
        this.userName = userName;
        this.content = content;
        this.date = date;
        this.type = type;
    }
}
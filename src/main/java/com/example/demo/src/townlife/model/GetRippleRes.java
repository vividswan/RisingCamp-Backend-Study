package com.example.demo.src.townlife.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class GetRippleRes {
    private String profileImageUrl;
    private String nickname;
    private String townAddress;
    private String updatedAt;
    private String content;
    private Integer likeView;
    private Integer nestedCount;
    private Boolean isWriter;
}

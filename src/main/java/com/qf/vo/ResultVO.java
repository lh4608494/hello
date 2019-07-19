package com.qf.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * create by 郑大仙丶
 * 2019/7/15 14:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultVO {

    private Integer code;

    private String msg;

    private Object data;

}

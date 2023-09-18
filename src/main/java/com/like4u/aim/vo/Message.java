package com.like4u.aim.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/19 14:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String sender;
    private String getter;
    private String src;
}

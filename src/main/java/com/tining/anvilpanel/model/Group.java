package com.tining.anvilpanel.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 自定义用户组
 * @author tinga
 */
@Data
public class Group {

    String name;

    List<String> users;
}

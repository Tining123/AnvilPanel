package com.tining.anvilpanel.model;

import com.tining.anvilpanel.gui.V1.DataV1;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 自定义用户组
 * @author tinga
 */
@Data
public class Group extends DataV1 {

    String name;

    List<String> users;
}

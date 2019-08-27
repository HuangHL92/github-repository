package com.ruoyi.area.demo.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = false)
@Data
@Accessors(chain = true)
@TableName("table_people")
public class TableAndPeople  {

    private String pId;
    private String tId;
}

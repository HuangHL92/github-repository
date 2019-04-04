package com.ruoyi.generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.base.BaseEntity;
import com.ruoyi.common.utils.StringUtils;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("gen_table_column")
public class GenTableColumn extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public static final String DEL_FLAG_NORMAL = "0"; // 未删除
    public static final String DEL_FLAG_DELETE = "1"; // 已删除
    public static final String YES = "1"; // 对
    public static final String NO = "0"; // 错


    @TableId(type = IdType.UUID)
    private String id;

    private String genTableId;

    private String name;

    private String oldName;

    private String comments;

    private String oldComments;

    private String jdbcType;

    private String oldJdbcType;

    private String isPk; //数据库默认为0

    private String isNull; // 数据库默认0

    private String oldIsPk;

    private Integer orderNum;

    @TableField(exist = false)
    private GenTable genTable;

    public GenTableColumn() {
    }

    public GenTableColumn(GenTable genTable) {
        this.genTable = genTable;
    }

    public String getNameAndComments() {
        return this.getName() + (this.comments == null ? "" : "  :  " + this.comments);
    }

    public String getDataLength() {
        String[] ss = StringUtils.split(StringUtils.substringBetween(this.getJdbcType(), "(", ")"), ",");
        return ss != null && ss.length == 1 ? ss[0] : "0";
    }

}

package com.ruoyi.system.domain;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.util.Date;
@Data
@Accessors(chain = true)
@TableName("sys_book")
@EqualsAndHashCode(callSuper = false)
public class SysBook extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @TableId
    private Integer bookId;
    //书籍Id
    private Integer bookNumber;
    //书籍数量
    private String bookName;
    //书籍名字
    private Integer bookPrice;
    //书籍价格
    private Date createDate;
    //创建时间
    private Date upDate;
    //更新时间




}

package com.ruoyi.common.enums;

import lombok.Getter;

/**
 * dataX 数据库类型
 *
 * @Author: Zen
 * @Date: 2019/04/26 11:25
 */
@Getter
public enum DataXSqlType {
    MYSQL(1, "jdbc:mysql://","?characterEncoding=utf8&serverTimezone=GMT%2B8","com.mysql.cj.jdbc.Driver"),
    ORACLE(2, "jdbc:oracle:thin:","","oracle.jdbc.driver.OracleDriver"),
    SQL_SERVER(3, "jdbc:sqlserver://","","com.microsoft.sqlserver.jdbc.SQLServerDriver"),
    ;

    private Integer type;
    private String prefix;
    private String suffix;
    private String driver;

    DataXSqlType(Integer type, String prefix,String suffix,String driver) {
        this.type = type;
        this.prefix = prefix;
        this.suffix = suffix;
        this.driver = driver;
    }
}

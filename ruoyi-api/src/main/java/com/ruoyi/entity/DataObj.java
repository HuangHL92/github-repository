package com.ruoyi.entity;

import com.alibaba.fastjson.annotation.JSONType;

/**
 *
 */
@JSONType(includes = {"result_status","result_id","result_oid","result_msg"})
public class DataObj {

	private DataSchema data_schema;

	private Object data;

    private String result_id;

    private String result_oid;

    private String result_status;

    private String result_msg;


    public String getResult_oid() {
        return result_oid;
    }

    public void setResult_oid(String result_oid) {
        this.result_oid = result_oid;
    }

    public String getResult_status() {
        return result_status;
    }

    public void setResult_status(String result_status) {
        this.result_status = result_status;
    }

    public String getResult_id() {
        return result_id;
    }

    public void setResult_id(String result_id) {
        this.result_id = result_id;
    }

    public String getResult_msg() {
        return result_msg;
    }

    public void setResult_msg(String result_msg) {
        this.result_msg = result_msg;
    }

    public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}


	public DataSchema getData_schema() {
		return data_schema;
	}

	public void setData_schema(DataSchema data_schema) {
		this.data_schema = data_schema;
	}

}






package com.njwd.rpc.monitor.web;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ResponseVo<T> implements Serializable {

    /**
     * 是否成功标志。
     * 需要特别说明的是，如果返回处理中，则此处为false
     */
    private boolean isSuccess;
    
    /**
     * 错误编码。参考{@link ErrorCodeEnum}
     */
    private int status;
    
    /**
     * 错误返回说明
     */
    private String msg;
    
    /**
     * 返回的实体类
     */
    private T data;
    
    /**
     * 分页总条数
     */
    private long totalProperty;
    
    private Date timestamp = new Date();

   
    public T getData() {
        return data;
    }


    public void setData(T data) {
        this.data = data;
    }


    public static ResponseVo getSuccessResponse(){
        return new ResponseVo(true, 200, null);
    }
    public static <T> ResponseVo<T> getSuccessResponse(T obj){
        return new ResponseVo<T>(obj);
    }
    public static <T> ResponseVo<T> getSuccessResponse(T obj,String msg){
        return new ResponseVo<T>(true,200,msg,obj);
    }
    public static <T> ResponseVo<T> getSuccessResponse(long totalProperty,T obj){
        return new ResponseVo (totalProperty,obj);
    }
    public ResponseVo(T obj) {
        super();
        this.isSuccess = true;
        this.status=200;
        this.data=obj;
    }
    
    public ResponseVo (long totalProperty,T obj) {
        super();
        this.isSuccess = true;
        this.status=200;
        this.data=obj;
        this.totalProperty = totalProperty;
    }

    public ResponseVo(boolean isSuccess, int errorCode, String msg) {
        super();
        this.isSuccess = isSuccess;
        this.status = errorCode;
        this.msg = msg;
    }
    public ResponseVo(boolean isSuccess, int errorCode, String msg, T date) {
        super();
        this.isSuccess = isSuccess;
        this.status = errorCode;
        this.msg = msg;
        this.data = date;
    }
    
    public ResponseVo(boolean isSuccess, int errorCode, String msg, T data,
			long totalProperty) {
		super();
		this.isSuccess = isSuccess;
		this.status = errorCode;
		this.msg = msg;
		this.data = data;
		this.totalProperty = totalProperty;
	}


	public ResponseVo setDataObj(T date){
        this.setData(date);
        return this;
    }
    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

   

    public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	public Date getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}


	public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    
    
    public long getTotalProperty() {
        return totalProperty;
    }


    public void setTotalProperty(long totalProperty) {
        this.totalProperty = totalProperty;
    }


    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
    
    
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    
   
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    
}

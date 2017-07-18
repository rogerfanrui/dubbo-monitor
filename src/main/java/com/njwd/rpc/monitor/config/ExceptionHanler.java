package com.njwd.rpc.monitor.config;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.njwd.rpc.monitor.web.R;


@RestControllerAdvice
public class ExceptionHanler {

	private Logger log = LoggerFactory.getLogger(ExceptionHanler.class);
	
	@ExceptionHandler
    public R handleSQLException(HttpServletRequest request, Exception ex) {  
		log.error("",ex);
		return new R(false, 500, "操作异常！");
    }
}

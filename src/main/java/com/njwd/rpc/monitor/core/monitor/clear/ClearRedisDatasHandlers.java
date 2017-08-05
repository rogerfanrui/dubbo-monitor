package com.njwd.rpc.monitor.core.monitor.clear;

import java.util.List;

/**
 * 该接口统一管理清理的key
 * @author fanrui
 *
 */
public interface ClearRedisDatasHandlers {

	public List<String> minutesKeysPattern();
	
	public List<String> hoursKeysPattern();
}

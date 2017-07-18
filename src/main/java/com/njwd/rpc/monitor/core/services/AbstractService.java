
package com.njwd.rpc.monitor.core.services;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.registry.RegistryService;
import com.njwd.rpc.monitor.core.dubbo.RegistryServerService;


public class AbstractService {

    @Autowired
    private RegistryServerService           sync;
    
    @Autowired
    protected RegistryService registryService;
    
    public ConcurrentMap<String, ConcurrentMap<String, Map<Long, URL>>> getRegistryCache(){
        return sync.getRegistryCache();
    }
    
    
}

package com.njwd.rpc.monitor.core.alarm;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.common.collect.Lists;
import com.njwd.rpc.monitor.config.SpringUtils;
import com.njwd.rpc.monitor.core.domain.StatisticsInfo;
import com.njwd.rpc.monitor.core.domain.alarm.AlarmRule;
import com.njwd.rpc.monitor.core.monitor.StaTotalInvokerServices;
import com.njwd.rpc.monitor.core.monitor.domain.StaAll;
import com.njwd.rpc.monitor.core.util.DateUtils;
import com.njwd.rpc.monitor.core.util.IdFactory;

@Service
public class AlarmRuleServices {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private Interner<String> pool = Interners.newWeakInterner();

	private List<AlarmRule> alarmRule = null;
	@Autowired
	StaTotalInvokerServices staTotalInvokerServices;

	@Autowired
	RedisTemplate<String, AlarmRule> redisTemplate;
	
	ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	public void addOrUpdateRule(AlarmRule rule) {
		if(StringUtils.isBlank(rule.getId()))
			rule.setId(IdFactory.getDateTimeRandomStr());
		redisTemplate.opsForHash().put("alarm-rule-users", key(rule), rule);
		load();
	}


	public void deleteRule(AlarmRule rule){
		redisTemplate.opsForHash().delete("alarm-rule-users", key(rule));
		load();
	}
	
	
	

	private String key(AlarmRule rule) {
		return rule.getId() + "_" + rule.getAppName() + "_"
				+ rule.getServicesName();
	}

	public List<AlarmRule> rules(final String id) {
		ReadLock rlock =lock.readLock();
		try {
			rlock.tryLock();
			if (id == null)
				return Lists.newLinkedList(alarmRule);
			else {
				return Lists.newLinkedList(Collections2.filter(alarmRule,
						new Predicate<AlarmRule>() {

							@Override
							public boolean apply(AlarmRule input) {

								return input.getId().equals(id);
							}

						}));
			}
		} finally {
			rlock.unlock();
		}
	}

	@Async
	@EventListener
	public void handlerEvent(AlarmEvent event) {
		final StatisticsInfo sinfo = event.getInfo();
		synchronized (pool.intern(sinfo.getApplication() + "_"
				+ sinfo.getService())) {
			List<AlarmRule> msgNotifyLists = Lists.newLinkedList();
			List<AlarmRule> rs = match(sinfo);
			if (rs != null && !rs.isEmpty()) {
				for (AlarmRule r : rs) {
					Double pencent = getSerSuPencent(sinfo.getService(),
							r.getMinutes());
					if (pencent < r.getSpercent()) {
						r.setCurSpencent(pencent);
						msgNotifyLists.add(r);

					}
				}

			}

			if (!msgNotifyLists.isEmpty()) {
				AlarmNotifyEvent nofiyEvent = new AlarmNotifyEvent(this, sinfo,
						msgNotifyLists);
				SpringUtils.getApplicationContext().publishEvent(nofiyEvent);
			}

		}

	}

	private Double getSerSuPencent(String servicesName, Integer minutes) {
		if (minutes == null || minutes == 0) {
			minutes = 30;
		}

		Date end = new Date();
		Set<StaAll> staAlls = staTotalInvokerServices.getReords(
				DateUtils.add(end, Calendar.MINUTE, 0 - minutes), end,
				servicesName);
		Long sumCounts = 0l;
		Long succesCounts = 0l;
		for (StaAll s : staAlls) {
			sumCounts += (s.getComErrorCount() + s.getComSuccessCount());
			succesCounts += s.getComSuccessCount();
		}
		return sumCounts==0?100d:(succesCounts / sumCounts) * 100d;
	}

	@PostConstruct
	public void load() {
		WriteLock wlock =lock.writeLock();
		
		try {
			wlock.tryLock();
			List values = redisTemplate.opsForHash().values("alarm-rule-users");
			if (values != null) {
				alarmRule = (List<AlarmRule>) values;
			} else {
				alarmRule = Lists.newLinkedList();
			}
		} finally {
			wlock.unlock();
		}
	}

	private List<AlarmRule> match(StatisticsInfo sinfo) {
		List<AlarmRule> rules = Lists.newLinkedList();
		for (AlarmRule r : alarmRule) {
			if ((r.getAppName().equals("*")
					|| r.getAppName().equals(sinfo.getApplication()) || sinfo
					.getApplication().matches(r.getAppName()))
					&& (r.getServicesName().equals("*") || r.getServicesName()
							.equals(sinfo.getService())|| sinfo.getService().matches(r.getServicesName()))) {
				rules.add(r);
			}
		}
		log.info("detach rules ,{} ,info:{}",rules,sinfo);
		return rules;
	}

}

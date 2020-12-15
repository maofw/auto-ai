package com.ai.generator.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: QIK
 * @CreateDate: 2019/4/22 20:13
 */
@Component
@Data
public class TaskConfig {
	@Value("${task.core_pool_size}")
	private int corePoolSize;
	@Value("${task.max_pool_size}")
	private int maxPoolSize;
	@Value("${task.name_prefix}")
	private String namePrefix;
	@Value("${task.queue_capacity}")
	private int queueCapacity;
	@Value("${task.keep_alive_seconds}")
	private int keepAliveSeconds;

	public int getCorePoolSize() {
		return corePoolSize;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public String getNamePrefix() {
		return namePrefix;
	}

	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}

	public int getQueueCapacity() {
		return queueCapacity;
	}

	public void setQueueCapacity(int queueCapacity) {
		this.queueCapacity = queueCapacity;
	}

	public int getKeepAliveSeconds() {
		return keepAliveSeconds;
	}

	public void setKeepAliveSeconds(int keepAliveSeconds) {
		this.keepAliveSeconds = keepAliveSeconds;
	}

}

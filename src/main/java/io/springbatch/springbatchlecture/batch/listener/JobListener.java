package io.springbatch.springbatchlecture.batch.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j

public class JobListener implements JobExecutionListener {
	@Override
	public void beforeJob(JobExecution jobExecution) {

	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		long time = jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime();
		log.info("총 소요시간  ={}" + time);
	}
}

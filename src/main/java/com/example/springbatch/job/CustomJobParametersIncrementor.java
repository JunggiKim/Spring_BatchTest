package com.example.springbatch.job;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

public class CustomJobParametersIncrementor implements JobParametersIncrementer {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-hhmmss");

	@Override
	public JobParameters getNext(JobParameters parameters) {

		String id = dateFormat.format(new Date());

		return new JobParametersBuilder()
			.addString("custom", id)
			.toJobParameters();
	}
}

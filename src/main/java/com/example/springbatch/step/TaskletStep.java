package com.example.springbatch.step;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Configuration;

import com.example.springbatch.config.CustomTasklet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TaskletStep {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	public Job helloJob(){
	    return jobBuilderFactory.get("job1")
			.start(step1())
			.incrementer(new RunIdIncrementer())
			.build();
	}

	private Step step1() {
		return stepBuilderFactory.get("step1")
			.tasklet(new CustomTasklet())
			.startLimit(3)
			.allowStartIfComplete(true)
			.build();
	}

}
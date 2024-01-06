package com.example.springbatch.step;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.job.DefaultJobParametersExtractor;
import org.springframework.batch.core.step.job.JobParametersExtractor;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JobStep {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	public Job helloJob() {
		return jobBuilderFactory.get("parentJob")
			.start(jobStep(null))
			.incrementer(new RunIdIncrementer())
			.build();
	}

	private Step jobStep(JobLauncher jobLauncher) {
		return stepBuilderFactory.get("jobStep")
			.job(childJob())
			.launcher(jobLauncher)
			.parametersExtractor(jobParametersExtractor())
			//리스너를 사용해서 step이 시작 전 step의 ExecutionContext에 name 키 밸류 삽입
			.listener(new StepExecutionListener() {
				@Override
				public void beforeStep(StepExecution stepExecution) {
					stepExecution.getExecutionContext().putString("name","kjg");
				}

				@Override
				public ExitStatus afterStep(StepExecution stepExecution) {
					return null;
				}
			})
			.build();
	}

	private JobParametersExtractor jobParametersExtractor() {
		DefaultJobParametersExtractor extractor = new DefaultJobParametersExtractor();
		extractor.setKeys(new String[]{"name"});
		return extractor;
	}

	private Job childJob() {
		return jobBuilderFactory.get("childJob")
			.start(step1())
			.build();
	}

	private Step step1() {
		return stepBuilderFactory.get("step1")
			.tasklet((contribution, chunkContext) -> {
				JobParameters jobParameters = contribution
											  .getStepExecution()
					                          .getJobExecution()
					                          .getJobParameters();
				System.out.println(jobParameters.getString("name"));
				return RepeatStatus.FINISHED;
			})
			.build();
	}

}
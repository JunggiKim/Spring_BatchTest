package io.springbatch.springbatchlecture.batch.job.api;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import io.springbatch.springbatchlecture.batch.chunk.processor.APiItemProcessor1;
import io.springbatch.springbatchlecture.batch.chunk.processor.APiItemProcessor2;
import io.springbatch.springbatchlecture.batch.chunk.processor.APiItemProcessor3;
import io.springbatch.springbatchlecture.batch.chunk.writer.APiItemWriter1;
import io.springbatch.springbatchlecture.batch.chunk.writer.APiItemWriter2;
import io.springbatch.springbatchlecture.batch.chunk.writer.APiItemWriter3;
import io.springbatch.springbatchlecture.batch.classifier.ProcessorClassifier;
import io.springbatch.springbatchlecture.batch.classifier.WriterClassifier;
import io.springbatch.springbatchlecture.batch.domain.ApiRequestVO;
import io.springbatch.springbatchlecture.batch.domain.ProductVO;
import io.springbatch.springbatchlecture.batch.partition.ProductPartitoner;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Configuration
@RequiredArgsConstructor
public class ApiStepConfiguration {

	private final StepBuilderFactory stepBuilderFactory;

	private final DataSource dataSource;

	private int chunkSize = 10;


	@SneakyThrows
	@Bean
	public Step masterStep() throws Exception {
	    return stepBuilderFactory.get("apiMasterStep")
			.partitioner(apiSlaveStep().getName(), partitioner())
			.step(apiSlaveStep())
			.gridSize(3)
			.taskExecutor(taskExecutor())
			.build();
	}

	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor =new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(3);
		taskExecutor.setMaxPoolSize(6);
		taskExecutor.setThreadNamePrefix("api-thread-");
		return taskExecutor;
	}


	public Step apiSlaveStep() throws Exception {
		return stepBuilderFactory.get("apiSlaveStep")
			.<ProductVO , ProductVO>chunk(chunkSize)
			.reader(itemReader(null))
			.processor(itemProcessor())
			.writer(itemWriter())
			.build();
	}

	@Bean
		public ProductPartitoner partitioner() {
			ProductPartitoner productPartitoner = new ProductPartitoner();
			productPartitoner.setDataSource(dataSource);
			return null;
	}



	@Bean
	@StepScope
	public ItemReader<ProductVO> itemReader(@Value( "#{stepExecutionContext['product']}") ProductVO productVO) throws Exception{

		JdbcPagingItemReader<ProductVO> reader = new JdbcPagingItemReader<>();

		reader.setDataSource(dataSource);
		reader.setPageSize(chunkSize);
		reader.setRowMapper(new BeanPropertyRowMapper(ProductVO.class));

		MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();

		queryProvider.setSelectClause("id, name, price, type");
		queryProvider.setFromClause("from product");
		queryProvider.setWhereClause("where type = :type");

		queryProvider.setSortKeys(Map.of("id", Order.DESCENDING));

		reader.setParameterValues(QueryGenerator.getParameterForQuery("type",productVO.getType()));
		reader.setQueryProvider(queryProvider);
		reader.afterPropertiesSet();

		return reader;
	}


	@Bean
	public ItemProcessor itemProcessor() {
		ClassifierCompositeItemProcessor<ProductVO, ApiRequestVO> processor =new ClassifierCompositeItemProcessor<>();
		//Classifier를 가보면 Serializable상속을 받고  Classifier<C,T>로 재네릭형태로 들어가있는데 첫번쨰의 값으로 분류를 하고 두번쨰의 값으로 반환을한다  첫번째 값으로
		//이 코드에서는  ProductVO 기준으로 분류를 하고 ItemProcessor반환을 한다
		ProcessorClassifier<ProductVO, ItemProcessor<?,? extends ApiRequestVO>> classifier =new ProcessorClassifier();

		Map<String,ItemProcessor<ProductVO,ApiRequestVO>> processorMap =new HashMap<>();
		processorMap.put("1",new APiItemProcessor1());
		processorMap.put("2",new APiItemProcessor2());
		processorMap.put("3",new APiItemProcessor3());



		classifier.setProcessorMap(processorMap);

		processor.setClassifier(classifier);

		return processor;
	}

	@Bean
	public ItemWriter itemWriter() {
		ClassifierCompositeItemWriter<ApiRequestVO> processor =new ClassifierCompositeItemWriter<>();
		WriterClassifier<ApiRequestVO, ItemWriter<? super ApiRequestVO>> classifier =new WriterClassifier();

		Map<String,ItemWriter<ApiRequestVO>> writerMap =new HashMap<>();
		writerMap.put("1",new APiItemWriter1());
		writerMap.put("2",new APiItemWriter2());
		writerMap.put("3",new APiItemWriter3());



		classifier.setWriterMap(writerMap);

		processor.setClassifier(classifier);

		return processor;
	}





}

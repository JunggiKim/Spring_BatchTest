package io.springbatch.springbatchlecture.batch.partition;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import io.springbatch.springbatchlecture.batch.domain.ProductVO;
import io.springbatch.springbatchlecture.batch.job.api.QueryGenerator;

public class ProductPartitoner implements Partitioner {

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {

		ProductVO[] productList = QueryGenerator.getProductList(dataSource);
		Map<String, ExecutionContext> result = new HashMap<>();
		int number = 0;

		for (ProductVO productVO : productList) {
			ExecutionContext value = new ExecutionContext();

			result.put("partition" + number, value);
			value.put("product",productVO);

			number++;
		}

		return null;
	}
}

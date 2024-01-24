package io.springbatch.springbatchlecture.batch.chunk.processor;

import org.springframework.batch.item.ItemProcessor;

import io.springbatch.springbatchlecture.batch.domain.ApiRequestVO;
import io.springbatch.springbatchlecture.batch.domain.ProductVO;

public class APiItemProcessor3 implements ItemProcessor<ProductVO, ApiRequestVO> {
	@Override
	public ApiRequestVO process(ProductVO item) throws Exception {
		return ApiRequestVO.builder()
			.id(item.getId())
			.productVO(item)
			.build();
	}
}

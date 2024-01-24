package io.springbatch.springbatchlecture.batch.chunk.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import io.springbatch.springbatchlecture.batch.domain.ApiRequestVO;

public class APiItemWriter2 implements ItemWriter<ApiRequestVO> {
	@Override
	public void write(List<? extends ApiRequestVO> items) throws Exception {

	}
}

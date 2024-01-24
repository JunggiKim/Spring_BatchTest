package io.springbatch.springbatchlecture.batch.chunk.processor;

import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;

import io.springbatch.springbatchlecture.batch.domain.Product;
import io.springbatch.springbatchlecture.batch.domain.ProductVO;




public class FileItemProcessor implements ItemProcessor<ProductVO, Product> {
	@Override
	public Product process(ProductVO item) throws Exception {
        ModelMapper modelMapper = new ModelMapper();
		return  modelMapper.map(item, Product.class);
	}
}

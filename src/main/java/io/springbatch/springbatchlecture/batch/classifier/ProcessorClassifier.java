package io.springbatch.springbatchlecture.batch.classifier;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.classify.Classifier;

import io.springbatch.springbatchlecture.batch.domain.ApiRequestVO;
import io.springbatch.springbatchlecture.batch.domain.ProductVO;
import lombok.Setter;

@Setter
public class ProcessorClassifier<C,T> implements Classifier<C,T> {

	private Map<String, ItemProcessor<ProductVO, ApiRequestVO>> processorMap = new HashMap<>();

	@Override
	public T classify(C classifiable) {
	return (T)processorMap.get(((ProductVO)classifiable).getType());
	}



}

package io.springbatch.springbatchlecture.batch.classifier;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;

import io.springbatch.springbatchlecture.batch.domain.ApiRequestVO;
import io.springbatch.springbatchlecture.batch.domain.ProductVO;
import lombok.Setter;

@Setter
public class WriterClassifier<C,T> implements Classifier<C,T> {

	private Map<String, ItemWriter<ApiRequestVO>> WriterMap = new HashMap<>();

	@Override
	public T classify(C classifiable) {
		return (T)WriterMap.get(((ApiRequestVO)classifiable).getProductVO().getType());
	}



}

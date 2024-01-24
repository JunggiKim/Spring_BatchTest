// package io.springbatch.springbatchlecture.service;
//
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestTemplate;
//
// import io.springbatch.springbatchlecture.batch.domain.ApiInfo;
// import io.springbatch.springbatchlecture.batch.domain.ApiResponseVO;
//
//
//
// @Service
// public class ApiService2 extends AbstractApiService {
//
// 	@Override
// 	protected ApiResponseVO doApiService(RestTemplate restTemplate, ApiInfo apiInfo) {
//
// 		ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8082/api/product/2",
// 			apiInfo, String.class);
//
// 		int statusCodeValue = responseEntity.getStatusCodeValue();
//
// 		return ApiResponseVO.builder()
// 			.status(statusCodeValue)
// 			.msg(responseEntity.getBody())
// 			.build();
// 	}
// }

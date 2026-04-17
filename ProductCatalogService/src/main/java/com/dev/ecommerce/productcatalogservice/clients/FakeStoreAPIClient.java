package com.dev.ecommerce.productcatalogservice.clients;

import com.dev.ecommerce.productcatalogservice.dtos.FakestoreProductDto;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class FakeStoreAPIClient {
    private RestTemplate restTemplate;
    public FakeStoreAPIClient(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public <T> ResponseEntity<T> requestForEntity(HttpMethod httpMethod, String url, @Nullable Object request, Class<T> responseType, @Nullable Object... uriVariables) throws RestClientException {
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables);
    }

    public boolean validateResponse(ResponseEntity<FakestoreProductDto> entity ){
        if(entity.hasBody() && entity.getStatusCode().equals(HttpStatusCode.valueOf(200))){
            return true;
        }
        return false;
    }
}

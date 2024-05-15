package com.application.services;

import com.application.dtos.UserDetailsDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;


@Service
public class UserWebClientService {
    @Value("${services.identity-service.authentication-key}")
    private String authenticationKey;

    private static final String getUser = "/get-user/";

    private final WebClient webClient;

    public UserWebClientService(WebClient.Builder webClientBuilder, @Value("${services.identity-service.url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public Mono<UserDetailsDto> fetchCustomerData(String data) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + authenticationKey);
        return webClient.get()
                .uri(getUser + data)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .exchange()
                .flatMap(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(UserDetailsDto.class);
                    } else {
                        return Mono.error(new WebClientResponseException(
                                "Aadhar Number is not present, please enter correct Aadhar Number Or add Aadhar data in Identity App",
                                response.statusCode().value(),
                                response.toString(),
                                response.headers().asHttpHeaders(),
                                null,
                                null
                        ));
                    }
                });
    }
}

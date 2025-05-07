package com.sankhya.portalpdv.Controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@RestController
public class ProxyController {

    private final RestTemplate restTemplate = new RestTemplate();

    @CrossOrigin
    @PostMapping("/api")
    public ResponseEntity<String> proxyLogin(HttpServletRequest request,
                                             @RequestBody String body,
                                             @RequestParam String host) throws IOException {

        String targetUrl = "http://" + host + "/mge/service.sbr?serviceName=MobileLoginSP.login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);
        headers.setContentLength(body.getBytes(StandardCharsets.UTF_8).length);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            return restTemplate.exchange(targetUrl, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Erro ao encaminhar para o ERP: " + e.getMessage());
        }
    }

    @CrossOrigin
    @PostMapping("/checkout")
    public ResponseEntity<String> proxyCheckout(HttpServletRequest request,
                                                @RequestBody String body,
                                                @RequestParam String host) throws IOException {

        String queryString = Objects.toString(request.getQueryString(), "");
        String targetUrl = "http://" + host + "/checkout/service.sbr?" + queryString;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentLength(body.getBytes(StandardCharsets.UTF_8).length);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            return restTemplate.exchange(targetUrl, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Erro ao encaminhar para o ERP: " + e.getMessage());
        }
    }
}
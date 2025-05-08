package com.sankhya.portalpdv.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@RestController
public class ProxyController {

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Proxy para login XML
     */
    @CrossOrigin
    @PostMapping("/api")
    public ResponseEntity<String> proxyLogin(HttpServletRequest request,
                                             @RequestBody String body,
                                             @RequestParam String host) {

        // ✅ Garantir que host comece com https:// ou http://
        if (!host.startsWith("https://") && !host.startsWith("http://")) {
            host = "https://" + host;
        }

        String targetUrl = host + "/mge/service.sbr?serviceName=MobileLoginSP.login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);
        headers.setContentLength(body.getBytes(StandardCharsets.UTF_8).length);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(targetUrl, HttpMethod.POST, entity, String.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Erro ao encaminhar para o ERP: " + e.getMessage());
        }
    }

    /**
     * Proxy para serviços JSON
     */
    @CrossOrigin
    @PostMapping("/checkout")
    public ResponseEntity<String> proxyCheckout(HttpServletRequest request,
                                                @RequestBody String body,
                                                @RequestParam String host) {

        if (!host.startsWith("https://") && !host.startsWith("http://")) {
            host = "https://" + host;
        }

        String queryString = Objects.toString(request.getQueryString(), "");
        String targetUrl = host + "/checkout/service.sbr?" + queryString;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentLength(body.getBytes(StandardCharsets.UTF_8).length);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(targetUrl, HttpMethod.POST, entity, String.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Erro ao encaminhar para o ERP: " + e.getMessage());
        }
    }
}

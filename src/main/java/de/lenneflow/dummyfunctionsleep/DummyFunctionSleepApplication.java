package de.lenneflow.dummyfunctionsleep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.concurrent.Executor;

@SpringBootApplication
public class DummyFunctionSleepApplication {

    public static void main(String[] args) {
        SpringApplication.run(DummyFunctionSleepApplication.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate() throws NoSuchAlgorithmException, KeyManagementException {

        // Create SSL context to trust all certificates
        SSLContext sslContext = SSLContext.getInstance("TLS");

        // Define trust managers to accept all certificates
        TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
            // Method to check client's trust - accepting all certificates
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
            }

            // Method to check server's trust - accepting all certificates
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
            }

            // Method to get accepted issuers - returning an empty array
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};

        // Initialize SSL context with the defined trust managers
        sslContext.init(null, trustManagers, null);

        // Disable SSL verification for RestTemplate

        // Set the default SSL socket factory to use the custom SSL context
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        // Set the default hostname verifier to allow all hostnames
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

        // Create a RestTemplate with a custom request factory

        // Build RestTemplate with SimpleClientHttpRequestFactory
        return new RestTemplateBuilder().requestFactory(SimpleClientHttpRequestFactory.class).setReadTimeout(Duration.ofMinutes(10))
                .build();
    }


    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("lenneflow-");
        executor.initialize();
        return executor;
    }

}

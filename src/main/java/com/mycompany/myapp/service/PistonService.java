package com.mycompany.myapp.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PistonService {

    private final WebClient webClient;
    private final Logger log = LoggerFactory.getLogger(PistonService.class);

    public PistonService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://emkc.org").build();
    }

    public Mono<ExecutionResult> executeCode(String language, String version, List<Map<String, String>> files) {
        Map<String, Object> payload = Map.of("language", language, "version", version, "files", files);
        log.debug("Payload is {}", payload);
        return this.webClient.post()
            .uri("/api/v2/piston/execute")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(payload)
            .retrieve()
            .bodyToMono(ExecutionResult.class);
    }

    public static class ExecutionResult {

        private String language;
        private String version;
        private Run run;

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public Run getRun() {
            return run;
        }

        public void setRun(Run run) {
            this.run = run;
        }

        // Getter to easily access the output
        public String getOutput() {
            return this.run != null ? this.run.getOutput() : null;
        }

        public static class Run {

            private String stdout;
            private String stderr;
            private String output;
            private int code;
            private String signal;

            public String getStdout() {
                return stdout;
            }

            public void setStdout(String stdout) {
                this.stdout = stdout;
            }

            public String getStderr() {
                return stderr;
            }

            public void setStderr(String stderr) {
                this.stderr = stderr;
            }

            public String getOutput() {
                return output;
            }

            public void setOutput(String output) {
                this.output = output;
            }

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }

            public String getSignal() {
                return signal;
            }

            public void setSignal(String signal) {
                this.signal = signal;
            }
        }
    }
}

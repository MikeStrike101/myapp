package com.mycompany.myapp.service.dto;

import java.util.List;

public class ReplicateResponseDTO {

    private String id;
    private String status;
    private List<String> output;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getOutput() {
        return output;
    }

    public void setOutput(List<String> output) {
        this.output = output;
    }

    public String getFirstImageUrl() {
        if (output != null && !output.isEmpty()) {
            return output.get(0);
        }
        return null;
    }
}

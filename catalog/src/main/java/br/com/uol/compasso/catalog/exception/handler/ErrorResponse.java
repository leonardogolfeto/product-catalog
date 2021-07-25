package br.com.uol.compasso.catalog.exception.handler;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonPropertyOrder({ "status_code", "message"})
public class ErrorResponse {

    @JsonProperty("status_code")
    private Integer statusCode;
    private String message;

}

package com.mntn.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateMomoRespone {

    private String partnerCode;
    private String requestId;
    private String orderId;
    private long amount;
    private long responeTime;
    private String message;
    private String resultCode;
    private String payUrl;
    private String qrCodeUrl;
    private String deeplink;
}

package com.mntn.pojo.momo;

import com.mntn.enums.RequestType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest extends Request {

    private String orderInfo;
    private long amount;
    private String partnerName;
    private String subPartnerCode;
    private RequestType requestType;
    private String redirectUrl;
    private String ipnUrl;
    private String storeId;
    private String extraData;
    private String partnerClientId;
    private Boolean autoCapture = true;
    private Long orderGroupId;
    private String signature;
}

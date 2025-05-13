package com.mntn.services.impl;

import com.mntn.dto.CreateMomoRequest;
import com.mntn.dto.CreateMomoRespone;
import com.mntn.services.PaymentService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.mntn.utils.HmacUtils;
import lombok.Builder;

@Builder
@Service("paymentService")
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Value("${momo.partner-code}")
    private String PARTNER_CODE;

    @Value("${momo.access-key}")
    private String ACCESS_KEY;

    @Value("${momo.secret-key}")
    private String SECRET_KEY;

    @Value("${momo.redirect-url}")
    private String REDIRECT_URL;

    @Value("${momo.ipn-url}")
    private String IPN_URL;

    @Value("${momo.request-type}")
    private String REQUEST_TYPE;

    @Override
    public CreateMomoRespone createQR() {

        String orderId = UUID.randomUUID().toString();
        String orderInfo = "Thanh toan don hang: " + orderId;
        String requestId = UUID.randomUUID().toString();
        String extraData = "Khong co khuyen mai gi het tron a";
        long amount = 2000;

        String rawSignature = String.format(
                "accessKey=%s&amount=%d&extraData=%s&ipnUrl=%s&orderId=%s&orderInfo=%s&partnerCode=%s&redirectUrl=%s&requestId=%s&requestType=%s",
                ACCESS_KEY, amount, extraData, IPN_URL, orderId, orderInfo, PARTNER_CODE, REDIRECT_URL, requestId, REQUEST_TYPE
        );

        String prettySignature = "";
        try {
            prettySignature = HmacUtils.hmacSHA256(rawSignature, SECRET_KEY);
        } catch (Exception ex) {
            return null;
        }

        if (prettySignature.isBlank()) {
            return null;
        }

//        CreateMomoRequest request = CreateMomoRequest.builder()
//                .partnerCode(PARTNER_CODE)
//                .requestType(REQUEST_TYPE)
//                .ipnUrl(IPN_URL)
//                .redirectUrl(REDIRECT_URL)
//                .orderId(orderId)
//                .requestId(requestId)
//                .extraData(extraData)
//                .amount(amount)
//                .signature(prettySignature)
//                .lang("vi")
//                .orderInfo(orderInfo)
//                .build();
        return null;
    }
}

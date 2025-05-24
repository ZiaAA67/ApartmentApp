package com.mntn.services.impl;

import com.mntn.configs.momo.Environment;
import com.mntn.constants.MomoParameter;
import com.mntn.pojo.momo.HttpResponse;
import com.mntn.pojo.momo.PaymentRequest;
import com.mntn.pojo.momo.PaymentResponse;
import com.mntn.services.AbstractProcess;
import com.mntn.utils.momo.LogUtils;
import org.springframework.stereotype.Service;
import com.mntn.enums.RequestType;
import com.mntn.enums.Language;
import com.mntn.utils.momo.Encoder;

@Service
public class CreateOrderMomo extends AbstractProcess<PaymentRequest, PaymentResponse> {

    public CreateOrderMomo(Environment environment) {
        super(environment);
    }

    public static PaymentResponse process(Environment env, String orderId, String requestId, String amount, String orderInfo, String returnURL, String notifyURL, String extraData, com.mntn.enums.RequestType requestType, Boolean autoCapture) throws Exception {
        try {
            CreateOrderMomo m2Processor = new CreateOrderMomo(env);

            PaymentRequest request = m2Processor.createPaymentCreationRequest(orderId, requestId, amount, orderInfo, returnURL, notifyURL, extraData, requestType, autoCapture);
            PaymentResponse captureMoMoResponse = m2Processor.execute(request);

            return captureMoMoResponse;
        } catch (Exception exception) {
            LogUtils.error("[CreateOrderMoMoProcess] " + exception);
        }
        return null;
    }

    @Override
    public PaymentResponse execute(PaymentRequest request) throws RuntimeException {
        try {

            String payload = getGson().toJson(request, PaymentRequest.class);

            HttpResponse response = execute.sendToMoMo(environment.getMomoEndpoint().getCreateUrl(), payload);

            if (response.getStatus() != 200) {
                throw new RuntimeException("[PaymentResponse] [" + request.getOrderId() + "] -> Error API");
            }

            System.out.println("uweryei7rye8wyreow8: " + response.getData());

            PaymentResponse captureMoMoResponse = getGson().fromJson(response.getData(), PaymentResponse.class);
            String responserawData = MomoParameter.REQUEST_ID + "=" + captureMoMoResponse.getRequestId()
                    + "&" + MomoParameter.ORDER_ID + "=" + captureMoMoResponse.getOrderId()
                    + "&" + MomoParameter.MESSAGE + "=" + captureMoMoResponse.getMessage()
                    + "&" + MomoParameter.PAY_URL + "=" + captureMoMoResponse.getPayUrl()
                    + "&" + MomoParameter.RESULT_CODE + "=" + captureMoMoResponse.getResultCode();

            LogUtils.info("[PaymentMoMoResponse] rawData: " + responserawData);

            return captureMoMoResponse;

        } catch (Exception exception) {
            LogUtils.error("[PaymentMoMoResponse] " + exception);
            throw new IllegalArgumentException("Invalid params capture MoMo Request");
        }
    }

    public PaymentRequest createPaymentCreationRequest(String orderId, String requestId, String amount, String orderInfo,
            String returnUrl, String notifyUrl, String extraData, com.mntn.enums.RequestType requestType, Boolean autoCapture) {

        try {
            String requestRawData = new StringBuilder()
                    .append(MomoParameter.ACCESS_KEY).append("=").append(partnerInfo.getAccessKey()).append("&")
                    .append(MomoParameter.AMOUNT).append("=").append(amount).append("&")
                    .append(MomoParameter.EXTRA_DATA).append("=").append(extraData).append("&")
                    .append(MomoParameter.IPN_URL).append("=").append(notifyUrl).append("&")
                    .append(MomoParameter.ORDER_ID).append("=").append(orderId).append("&")
                    .append(MomoParameter.ORDER_INFO).append("=").append(orderInfo).append("&")
                    .append(MomoParameter.PARTNER_CODE).append("=").append(partnerInfo.getPartnerCode()).append("&")
                    .append(MomoParameter.REDIRECT_URL).append("=").append(returnUrl).append("&")
                    .append(MomoParameter.REQUEST_ID).append("=").append(requestId).append("&")
                    .append(MomoParameter.REQUEST_TYPE).append("=").append(requestType.getRequestType())
                    .toString();

            String signRequest = Encoder.signHmacSHA256(requestRawData, partnerInfo.getSecretKey());
            LogUtils.debug("[PaymentRequest] rawData: " + requestRawData + ", [Signature] -> " + signRequest);

            return new PaymentRequest(partnerInfo.getPartnerCode(), orderId, requestId, Language.EN, orderInfo, Long.valueOf(amount), "test MoMo", null, requestType,
                    returnUrl, notifyUrl, "test store ID", extraData, null, autoCapture, null, signRequest);
        } catch (Exception e) {
            LogUtils.error("[PaymentRequest] " + e);
        }

        return null;
    }
}

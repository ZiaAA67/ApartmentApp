//package com.mntn.services.impl;
//
//import com.mntn.configs.momo.Environment;
//import com.mntn.constants.MomoParameter;
//import com.mntn.pojo.momo.HttpResponse;
//import com.mntn.pojo.momo.PaymentRequest;
//import com.mntn.pojo.momo.PaymentResponse;
//import com.mntn.services.AbstractProcess;
//import com.mntn.utils.momo.LogUtils;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CreateOrderMomo extends AbstractProcess<PaymentRequest, PaymentResponse> {
//
//    public CreateOrderMomo(Environment environment) {
//        super(environment);
//    }
//
//    public PaymentResponse process(Environment env, String OrderId, String requestId) {
//        try {
//            CreateOrderMomo m2Procesor = new CreateOrderMomo(env);
//            PaymentRequest request = m2Procesor.CreatePaymentCreationRequest();
//
//            return m2Procesor.execute(request);
//
//        } catch (Exception exception) {
//            LogUtils.error("[CreateOrderMomoProcess] " + exception);
//        }
//        return null;
//    }
//
//    @Override
//    public PaymentResponse execute(PaymentRequest request) throws RuntimeException {
//        try {
//            String payLoad = getGson().toJson(request, PaymentRequest.class);
//            HttpResponse response = execute.sendToMoMo(environment.getMomoEndpoint().getCreateUrl(), payLoad);
//            if (response.getStatus() != 200) {
//                LogUtils.error("[PaymentResponse execute] ");
//            }
//
//            PaymentResponse captureMoMoResponse = getGson().fromJson(response.getData(), PaymentResponse.class);
//            String responserawData = MomoParameter.REQUEST_ID + "=" + captureMoMoResponse.getRequestId()
//                    + "&" + MomoParameter.ORDER_ID + "=" + captureMoMoResponse.getOrderId()
//                    + "&" + MomoParameter.MESSAGE + "=" + captureMoMoResponse.getMessage()
//                    + "&" + MomoParameter.PAY_URL + "=" + captureMoMoResponse.getPayUrl()
//                    + "&" + MomoParameter.RESULT_CODE + "=" + captureMoMoResponse.getResultCode();
//
//            LogUtils.info("[PaymentMOMOResponse] rawData " + responserawData);
//
//            return captureMoMoResponse;
//        } catch (Exception exception) {
//            LogUtils.error("[PaymentMoMoResponse] " + exception);
//            throw new IllegalArgumentException("Invalid params capture Momo request");
//        }
//    }
//    
//    public static PaymentRequest createPaymentCreationRequest()
//}

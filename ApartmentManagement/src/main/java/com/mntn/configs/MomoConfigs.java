package com.mntn.configs;

import com.mntn.configs.momo.Environment;
import com.mntn.configs.momo.MoMoEndpoint;
import com.mntn.configs.momo.PartnerInfo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:environment.properties")
public class MomoConfigs {

    @Value("${momo.environment:DEV}")
    private String environment;

    @Value("${DEV_MOMO_ENDPOINT:}")
    private String devMomoEndpoint;

    @Value("${DEV_PARTNER_CODE:}")
    private String devPartnerCode;

    @Value("${DEV_ACCESS_KEY:}")
    private String devAccessKey;

    @Value("${DEV_SECRET_KEY:}")
    private String devSecretKey;

    @Value("${PROD_MOMO_ENDPOINT:}")
    private String prodMomoEndpoint;

    @Value("${PROD_PARTNER_CODE:}")
    private String prodPartnerCode;

    @Value("${PROD_ACCESS_KEY:}")
    private String prodAccessKey;

    @Value("${PROD_SECRET_KEY:}")
    private String prodSecretKey;

    @Value("${CREATE_URL:}")
    private String createUrl;

    @Value("${QUERY_URL:}")
    private String queryUrl;

    @Value("${CONFIRM_URL:}")
    private String confirmUrl;

    @Value("${REFUND_URL:}")
    private String refundUrl;

    @Value("${TOKEN_PAY_URL:}")
    private String tokenPayUrl;

    @Value("${TOKEN_BIND_URL:}")
    private String tokenBindUrl;

    @Value("${TOKEN_INQUIRY_URL:}")
    private String tokenInquiryUrl;

    @Value("${TOKEN_DELETE_URL:}")
    private String tokenDeleteUrl;

    @PostConstruct
    public void logConfig() {
        System.out.println("MomoConfigs - environment: " + environment);
        System.out.println("MomoConfigs - DEV_MOMO_ENDPOINT: " + devMomoEndpoint);
        System.out.println("MomoConfigs - DEV_PARTNER_CODE: " + devPartnerCode);
        System.out.println("MomoConfigs - DEV_ACCESS_KEY: " + devAccessKey);
        System.out.println("MomoConfigs - DEV_SECRET_KEY: " + (devSecretKey.isEmpty() ? "empty" : "set"));
        System.out.println("MomoConfigs - CREATE_URL: " + createUrl);
        System.out.println("MomoConfigs - QUERY_URL: " + queryUrl);
        System.out.println("MomoConfigs - CONFIRM_URL: " + confirmUrl);
        System.out.println("MomoConfigs - REFUND_URL: " + refundUrl);
        System.out.println("MomoConfigs - TOKEN_PAY_URL: " + tokenPayUrl);
        System.out.println("MomoConfigs - TOKEN_BIND_URL: " + tokenBindUrl);
        System.out.println("MomoConfigs - TOKEN_INQUIRY_URL: " + tokenInquiryUrl);
        System.out.println("MomoConfigs - TOKEN_DELETE_URL: " + tokenDeleteUrl);

        if (devMomoEndpoint.isEmpty()) {
            System.err.println("[Momo Configs] - Momo endpoint rỗng!!!");
        }
        if (createUrl.isEmpty()) {
            System.err.println("[Momo Configs] - Momo createUrl rỗng!!!");
        }
        if (devPartnerCode.isEmpty() || devAccessKey.isEmpty() || devSecretKey.isEmpty()) {
            System.err.println("[Momo Configs] -: partnerCode=" + devPartnerCode
                    + ", accessKey=" + devAccessKey + ", secretKey=" + (devSecretKey.isEmpty() ? "miss" : "set"));
        }
    }

    @Bean
    public Environment momoEnvironment() {
        try {
            PartnerInfo partnerInfo;
            String baseEndpoint;

            if ("PROD".equalsIgnoreCase(environment)) {
                partnerInfo = new PartnerInfo(prodPartnerCode, prodAccessKey, prodSecretKey);
                baseEndpoint = prodMomoEndpoint;
            } else {
                partnerInfo = new PartnerInfo(devPartnerCode, devAccessKey, devSecretKey);
                baseEndpoint = devMomoEndpoint;
            }

            if (baseEndpoint.isEmpty()) {
                throw new IllegalStateException("Missing DEV_MOMO_ENDPOINT");
            }
            if (createUrl.isEmpty()) {
                throw new IllegalStateException("Missing CREATE_URL");
            }
            if (partnerInfo.getPartnerCode().isEmpty() || partnerInfo.getAccessKey().isEmpty() || partnerInfo.getSecretKey().isEmpty()) {
                throw new IllegalStateException("Rỗng: partnerCode=" + partnerInfo.getPartnerCode()
                        + ", accessKey=" + partnerInfo.getAccessKey() + ", secretKey=" + (partnerInfo.getSecretKey().isEmpty() ? "empty" : "set"));
            }

            MoMoEndpoint endpoint = new MoMoEndpoint(
                    baseEndpoint,
                    createUrl,
                    refundUrl,
                    queryUrl,
                    confirmUrl,
                    tokenPayUrl,
                    tokenBindUrl,
                    tokenInquiryUrl,
                    tokenDeleteUrl
            );

            return new Environment(
                    endpoint,
                    partnerInfo,
                    Environment.EnvTarget.valueOf(environment.toUpperCase())
            );
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid MoMo environment: " + environment, e);
        }
    }
}

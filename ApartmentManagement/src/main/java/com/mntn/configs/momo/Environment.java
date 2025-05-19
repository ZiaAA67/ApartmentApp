package com.mntn.configs.momo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Environment {

    private PartnerInfo partnerInfo;
    private MoMoEndpoint endpoints;
    private String target;

    public Environment(MoMoEndpoint endpoints, PartnerInfo partnerInfo, EnvTarget target) {
        this(endpoints, partnerInfo, target.string());
    }

    public Environment(MoMoEndpoint momoEndpoint, PartnerInfo partnerInfo, String target) {
        this.endpoints = momoEndpoint;
        this.partnerInfo = partnerInfo;
        this.target = target;
    }

    /**
     *
     * @param target String target name ("dev" or "prod")
     * @return
     * @throws IllegalArgumentException
     */
    public static Environment selectEnv(String target) throws IllegalArgumentException {
        switch (target) {
            case "dev":
                return selectEnv(EnvTarget.DEV);
            case "prod":
                return selectEnv(EnvTarget.PROD);
            default:
                throw new IllegalArgumentException("MoMo doesnt provide other environment: dev and prod");
        }
    }

    /**
     * Select appropriate environment to run processes Create and modify your
     * environment.properties file appropriately
     *
     * @param target EnvTarget (choose DEV or PROD)
     * @return
     */
    public static Environment selectEnv(EnvTarget target) {
        try (InputStream input = Environment.class.getClassLoader().getResourceAsStream("environment.properties")) {
            Properties prop = new Properties();
            prop.load(input);

            switch (target) {
                case DEV:
                    MoMoEndpoint devEndpoint = new MoMoEndpoint("https://payment.momo.vn/v2/gateway/api", "/create");
                    PartnerInfo devInfo = new PartnerInfo("MOMOLRJZ20181206", "mTCKt9W3eU1m39TW", "SetA5RDnLHvt51AULf51DyauxUo3kDU6");
                    Environment dev = new Environment(devEndpoint, devInfo, target);
                    return dev;

                default:
                    throw new IllegalArgumentException("MoMo doesnt provide other environment: dev and prod");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public MoMoEndpoint getMomoEndpoint() {
        return endpoints;
    }

    public void setMomoEndpoint(MoMoEndpoint momoEndpoint) {
        this.endpoints = momoEndpoint;
    }

    public PartnerInfo getPartnerInfo() {
        return partnerInfo;
    }

    public void setPartnerInfo(PartnerInfo partnerInfo) {
        this.partnerInfo = partnerInfo;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public enum EnvTarget {
        DEV("development"), PROD("production");

        private String target;

        EnvTarget(String target) {
            this.target = target;
        }

        public String string() {
            return this.target;
        }
    }

    public enum ProcessType {
        PAY_GATE, APP_IN_APP, PAY_POS, PAY_QUERY_STATUS, PAY_REFUND, PAY_CONFIRM;

        public String getSubDir(Properties prop) {
            return prop.getProperty(this.toString());
        }
    }

}

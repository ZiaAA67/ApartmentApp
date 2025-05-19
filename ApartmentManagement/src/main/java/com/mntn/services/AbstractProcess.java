package com.mntn.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mntn.configs.momo.Environment;
import com.mntn.configs.momo.PartnerInfo;
import com.mntn.utils.momo.Execute;

public abstract class AbstractProcess<T, V> {

    protected PartnerInfo partnerInfo;
    protected Environment environment;
    protected Execute execute = new Execute();

    public AbstractProcess(Environment environment) {
        this.environment = environment;
        this.partnerInfo = environment.getPartnerInfo();
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .disableHtmlEscaping()
                .create();
    }

    public abstract V execute(T request) throws RuntimeException;
}

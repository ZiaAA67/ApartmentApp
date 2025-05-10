package com.mntn.services.impl;

import com.mntn.pojo.Method;
import com.mntn.repositories.MethodRepository;
import com.mntn.services.MethodService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("methodService")
public class MethodServiceImpl implements MethodService {

    @Autowired
    private MethodRepository methodRepository;

    @Override
    public List<Method> getMethod(Boolean isActive) {
        return methodRepository.getMethod(isActive);
    }

}

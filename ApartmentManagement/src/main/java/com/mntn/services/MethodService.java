package com.mntn.services;

import com.mntn.pojo.Method;
import java.util.List;

public interface MethodService {

    List<Method> getMethod(Boolean isActive);
}

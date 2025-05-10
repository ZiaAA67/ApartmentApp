package com.mntn.repositories;

import com.mntn.pojo.Method;
import java.util.List;

public interface MethodRepository {

    List<Method> getMethod(Boolean isActive);
}

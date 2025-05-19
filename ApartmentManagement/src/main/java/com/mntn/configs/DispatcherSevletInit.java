package com.mntn.configs;

import com.mntn.filters.JwtFilter;
import jakarta.servlet.Filter;
import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class DispatcherSevletInit extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{
            ThymeleafConfigs.class,
            HibernateConfigs.class,
            SpringSecurityConfigs.class,};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{
            WebAppContextConfigs.class
        };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    // cấu hình nơi lưu tạm file trước khi upload lên cloudinary
    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        String location = "/tmp";
        long maxFileSize = 5242880; // 5MB
        long maxRequestSize = 20971520; // 20MB
        int fileSizeThreshold = 0;

        registration.setMultipartConfig(new MultipartConfigElement(location, maxFileSize, maxRequestSize, fileSizeThreshold));
    }

    // chỉ định Jwt chạy tự động --> không cần do đã cấu hình trong spring secure
//    @Override
//    protected Filter[] getServletFilters() {
//        return new Filter[] {new JwtFilter()};
//    }
}

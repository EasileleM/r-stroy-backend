package com.example.rstroybackend.controller;

import com.example.rstroybackend.entity.views.SecurityViews;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
class SecurityJsonViewControllerAdvice extends AbstractMappingJacksonResponseBodyAdvice {

    @Override
    protected void beforeBodyWriteInternal(
            MappingJacksonValue bodyContainer,
            MediaType contentType,
            MethodParameter returnType,
            ServerHttpRequest request,
            ServerHttpResponse response) {
        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().getAuthorities() != null) {
            Collection<? extends GrantedAuthority> authorities = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getAuthorities();

            Set<Class> jsonViews = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(SecurityViews.MAPPING::get)
                    .collect(Collectors.toSet());

            if (jsonViews.contains(SecurityViews.Admin.class)) {
                bodyContainer.setSerializationView(SecurityViews.Admin.class);
            } else if (jsonViews.contains(SecurityViews.User.class)) {
                bodyContainer.setSerializationView(SecurityViews.User.class);
            } else {
                bodyContainer.setSerializationView(SecurityViews.Anonymous.class);
            }
        }
    }
}
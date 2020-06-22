package com.example.rstroybackend.dto;

import com.example.rstroybackend.entity.views.SecurityViews;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class JsonPage<T> extends org.springframework.data.domain.PageImpl<T> {

    public JsonPage(final List<T> content, final Pageable pageable, final long total) {
        super(content, pageable, total);
    }

    public JsonPage(final List<T> content) {
        super(content);
    }

    public JsonPage(final Page<T> page, final Pageable pageable) {
        super(page.getContent(), pageable, page.getTotalElements());
    }

    @JsonView(SecurityViews.Anonymous.class)
    public int getTotalPages() {
        return super.getTotalPages();
    }

    @JsonView(SecurityViews.Anonymous.class)
    public long getTotalElements() {
        return super.getTotalElements();
    }

    @JsonView(SecurityViews.Anonymous.class)
    public boolean hasNext() {
        return super.hasNext();
    }

    @JsonView(SecurityViews.Anonymous.class)
    public boolean isLast() {
        return super.isLast();
    }

    @JsonView(SecurityViews.Anonymous.class)
    public boolean hasContent() {
        return super.hasContent();
    }

    @JsonView(SecurityViews.Anonymous.class)
    public List<T> getContent() {
        return super.getContent();
    }
}
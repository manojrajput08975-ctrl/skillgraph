package com.skillgraph.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyDto {
    public String id;
    public String name;
    public String industry;
    public String size;
}

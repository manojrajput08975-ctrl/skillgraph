package com.skillgraph.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchResultDto {
    public String id;
    public String name;
    public String type;      // User | Skill | Project | Technology | JobRole | Company
    public String subtitle;  // category, domain, level, industry — depends on type
}

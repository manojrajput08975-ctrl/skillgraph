package com.skillgraph.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecommendationDto {
    public String roleId;
    public String roleTitle;
    public String level;
    public long matchedSkills;
    public long totalRequired;
    public double matchPct;
    public List<String> missingSkills;
    public List<CompanyDto> companies;
}

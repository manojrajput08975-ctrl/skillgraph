package com.skillgraph.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobRoleDto {
    public String id;
    public String title;
    public String level;
    public List<SkillDto> requiredSkills;
    public List<CompanyDto> companies;
}

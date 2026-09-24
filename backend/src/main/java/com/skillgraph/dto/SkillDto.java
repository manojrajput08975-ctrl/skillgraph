package com.skillgraph.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SkillDto {
    public String id;
    public String name;
    public String category;
    public String level;
    public Integer years;
    public List<SkillDto> relatedSkills;
    public List<JobRoleDto> roles;
}

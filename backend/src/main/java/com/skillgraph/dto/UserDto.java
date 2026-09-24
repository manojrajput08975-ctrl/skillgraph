package com.skillgraph.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    public String id;
    public String name;
    public String email;
    public Integer yearsExp;
    public String location;
    public List<SkillDto> skills;
    public List<ProjectDto> projects;
    public String contributorRole; // set when returned as a project contributor
}

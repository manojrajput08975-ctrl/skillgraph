package com.skillgraph.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TechnologyDto {
    public String id;
    public String name;
    public String type;
    public List<SkillDto> requiredSkills;
}

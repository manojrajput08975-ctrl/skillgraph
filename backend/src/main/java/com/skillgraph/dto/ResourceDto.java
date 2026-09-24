package com.skillgraph.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceDto {
    public String id;
    public String title;
    public String type;
    public String url;
    public List<SkillDto> teaches;
}

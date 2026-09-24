package com.skillgraph.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDto {
    public String id;
    public String name;
    public String status;
    public String domain;
    public String role;
    public List<TechnologyDto> technologies;
    public List<UserDto> contributors;
}

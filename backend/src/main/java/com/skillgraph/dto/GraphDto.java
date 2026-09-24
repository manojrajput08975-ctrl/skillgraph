package com.skillgraph.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GraphDto {

    public List<GraphNodeDto> nodes;
    public List<GraphEdgeDto> edges;

    public static class GraphNodeDto {
        public String id;
        public String label;
        public String type;
        public Map<String, Object> properties;
    }

    public static class GraphEdgeDto {
        public String source;
        public String target;
        public String type;
    }
}

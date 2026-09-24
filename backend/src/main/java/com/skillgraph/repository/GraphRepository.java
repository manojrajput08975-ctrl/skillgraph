package com.skillgraph.repository;

import com.skillgraph.dto.GraphDto;
import com.skillgraph.dto.GraphDto.GraphEdgeDto;
import com.skillgraph.dto.GraphDto.GraphNodeDto;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.StreamSupport;

@Repository
public class GraphRepository {

    private final Driver driver;

    public GraphRepository(Driver driver) { this.driver = driver; }

    /**
     * Returns nodes + directed edges up to 2 hops from the given node.
     * Uses startNode(r).id to determine direction without a nested query.
     */
    public GraphDto neighbourhood(String nodeId) {
        Map<String, GraphNodeDto> nodeMap = new LinkedHashMap<>();
        List<GraphEdgeDto> edges = new ArrayList<>();
        Set<String> seenEdges = new HashSet<>();

        try (Session s = driver.session()) {
            // 1-hop: all direct neighbours with direction
            String q1 = """
                MATCH (a {id: $nodeId})-[r]-(b)
                RETURN a, b, type(r) AS relType,
                       startNode(r).id AS srcId, endNode(r).id AS tgtId
                """;
            s.run(q1, Map.of("nodeId", nodeId)).list().forEach(rec -> {
                addNode(nodeMap, rec.get("a"));
                addNode(nodeMap, rec.get("b"));
                addEdge(edges, seenEdges,
                        rec.get("srcId").asString(),
                        rec.get("tgtId").asString(),
                        rec.get("relType").asString());
            });

            // 2-hop: neighbours of neighbours (direction via startNode)
            String q2 = """
                MATCH (a {id: $nodeId})-[]-(b)-[r2]-(c)
                WHERE c.id <> $nodeId
                RETURN b, c, type(r2) AS relType,
                       startNode(r2).id AS srcId, endNode(r2).id AS tgtId
                LIMIT 60
                """;
            s.run(q2, Map.of("nodeId", nodeId)).list().forEach(rec -> {
                addNode(nodeMap, rec.get("b"));
                addNode(nodeMap, rec.get("c"));
                addEdge(edges, seenEdges,
                        rec.get("srcId").asString(),
                        rec.get("tgtId").asString(),
                        rec.get("relType").asString());
            });
        }

        GraphDto graph = new GraphDto();
        graph.nodes = new ArrayList<>(nodeMap.values());
        graph.edges = edges;
        return graph;
    }

    /**
     * Full career-path graph: User→Project→Technology→Skill→JobRole
     * Returns all nodes and edges for the force-graph visualisation.
     */
    public GraphDto careerPathGraph() {
        Map<String, GraphNodeDto> nodeMap = new LinkedHashMap<>();
        List<GraphEdgeDto> edges = new ArrayList<>();
        Set<String> seenEdges = new HashSet<>();

        try (Session s = driver.session()) {
            String q = """
                MATCH (u:User)-[r1:USER_WORKED_ON]->(p:Project)
                      -[r2:PROJECT_USES]->(t:Technology)
                      -[r3:TECH_REQUIRES]->(sk:Skill)
                      -[r4:SKILL_REQUIRED_FOR]->(role:JobRole)
                RETURN u, p, t, sk, role,
                       u.id AS uId, p.id AS pId, t.id AS tId,
                       sk.id AS skId, role.id AS roleId
                LIMIT 80
                """;
            s.run(q).list().forEach(rec -> {
                addNode(nodeMap, rec.get("u"));
                addNode(nodeMap, rec.get("p"));
                addNode(nodeMap, rec.get("t"));
                addNode(nodeMap, rec.get("sk"));
                addNode(nodeMap, rec.get("role"));
                String uId   = rec.get("uId").asString();
                String pId   = rec.get("pId").asString();
                String tId   = rec.get("tId").asString();
                String skId  = rec.get("skId").asString();
                String roleId = rec.get("roleId").asString();
                addEdge(edges, seenEdges, uId,   pId,    "USER_WORKED_ON");
                addEdge(edges, seenEdges, pId,   tId,    "PROJECT_USES");
                addEdge(edges, seenEdges, tId,   skId,   "TECH_REQUIRES");
                addEdge(edges, seenEdges, skId,  roleId, "SKILL_REQUIRED_FOR");
            });
        }

        GraphDto graph = new GraphDto();
        graph.nodes = new ArrayList<>(nodeMap.values());
        graph.edges = edges;
        return graph;
    }

    /**
     * 4-hop traversal table: User→Project→Technology→Skill→JobRole
     */
    public List<Map<String, String>> multiHopTraversal() {
        try (Session s = driver.session()) {
            String q = """
                MATCH (u:User)-[:USER_WORKED_ON]->(p:Project)
                      -[:PROJECT_USES]->(t:Technology)
                      -[:TECH_REQUIRES]->(sk:Skill)
                      -[:SKILL_REQUIRED_FOR]->(r:JobRole)
                RETURN u.name AS user, p.name AS project,
                       t.name AS technology, sk.name AS skill, r.title AS role
                LIMIT 20
                """;
            return s.run(q).list(rec -> Map.of(
                "user",       rec.get("user").asString(),
                "project",    rec.get("project").asString(),
                "technology", rec.get("technology").asString(),
                "skill",      rec.get("skill").asString(),
                "role",       rec.get("role").asString()
            ));
        }
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private void addNode(Map<String, GraphNodeDto> map, Value v) {
        if (v == null || v.isNull()) return;
        String id = v.get("id").asString(null);
        if (id == null || map.containsKey(id)) return;

        List<String> keys = StreamSupport.stream(v.keys().spliterator(), false).toList();

        GraphNodeDto n = new GraphNodeDto();
        n.id    = id;
        n.label = !v.get("name").isNull()  ? v.get("name").asString()
                : !v.get("title").isNull() ? v.get("title").asString()
                : id;
        n.type  = keys.contains("category")                        ? "Skill"
                : keys.contains("domain")                          ? "Project"
                : keys.contains("yearsExp")                        ? "User"
                : keys.contains("industry")                        ? "Company"
                : keys.contains("level") && keys.contains("title") ? "JobRole"
                : keys.contains("type")                            ? "Technology"
                : keys.contains("url")                             ? "Resource"
                : "Node";
        n.properties = new HashMap<>();
        keys.forEach(k -> n.properties.put(k, v.get(k).asObject()));
        map.put(id, n);
    }

    private void addEdge(List<GraphEdgeDto> edges, Set<String> seen,
                         String src, String tgt, String type) {
        if (src == null || tgt == null) return;
        String key = src + "->" + tgt + ":" + type;
        if (seen.contains(key)) return;
        seen.add(key);
        GraphEdgeDto e = new GraphEdgeDto();
        e.source = src;
        e.target = tgt;
        e.type   = type;
        edges.add(e);
    }
}

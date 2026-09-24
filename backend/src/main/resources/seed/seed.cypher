// ── Constraints (idempotent) ────────────────────────────────────────────────
CREATE CONSTRAINT user_id IF NOT EXISTS FOR (u:User) REQUIRE u.id IS UNIQUE;
CREATE CONSTRAINT skill_id IF NOT EXISTS FOR (s:Skill) REQUIRE s.id IS UNIQUE;
CREATE CONSTRAINT project_id IF NOT EXISTS FOR (p:Project) REQUIRE p.id IS UNIQUE;
CREATE CONSTRAINT tech_id IF NOT EXISTS FOR (t:Technology) REQUIRE t.id IS UNIQUE;
CREATE CONSTRAINT role_id IF NOT EXISTS FOR (r:JobRole) REQUIRE r.id IS UNIQUE;
CREATE CONSTRAINT company_id IF NOT EXISTS FOR (c:Company) REQUIRE c.id IS UNIQUE;
CREATE CONSTRAINT resource_id IF NOT EXISTS FOR (res:Resource) REQUIRE res.id IS UNIQUE;

// ── Companies (5) ───────────────────────────────────────────────────────────
MERGE (c1:Company {id:'comp-1'}) SET c1.name='Nexus Labs',      c1.industry='FinTech',    c1.size='mid';
MERGE (c2:Company {id:'comp-2'}) SET c2.name='Orion Systems',   c2.industry='Cloud',      c2.size='large';
MERGE (c3:Company {id:'comp-3'}) SET c3.name='Vertex AI',       c3.industry='AI/ML',      c3.size='startup';
MERGE (c4:Company {id:'comp-4'}) SET c4.name='Bridgepoint',     c4.industry='Consulting', c4.size='large';
MERGE (c5:Company {id:'comp-5'}) SET c5.name='DataStream Inc',  c5.industry='Analytics',  c5.size='mid';

// ── Skills (15) ─────────────────────────────────────────────────────────────
MERGE (sk1:Skill  {id:'skill-1'})  SET sk1.name='Java',              sk1.category='Backend';
MERGE (sk2:Skill  {id:'skill-2'})  SET sk2.name='Python',            sk2.category='Backend';
MERGE (sk3:Skill  {id:'skill-3'})  SET sk3.name='React',             sk3.category='Frontend';
MERGE (sk4:Skill  {id:'skill-4'})  SET sk4.name='TypeScript',        sk4.category='Frontend';
MERGE (sk5:Skill  {id:'skill-5'})  SET sk5.name='Machine Learning',  sk5.category='AI/ML';
MERGE (sk6:Skill  {id:'skill-6'})  SET sk6.name='Cypher',            sk6.category='Database';
MERGE (sk7:Skill  {id:'skill-7'})  SET sk7.name='SQL',               sk7.category='Database';
MERGE (sk8:Skill  {id:'skill-8'})  SET sk8.name='Docker',            sk8.category='DevOps';
MERGE (sk9:Skill  {id:'skill-9'})  SET sk9.name='Kubernetes',        sk9.category='DevOps';
MERGE (sk10:Skill {id:'skill-10'}) SET sk10.name='Spring Boot',      sk10.category='Backend';
MERGE (sk11:Skill {id:'skill-11'}) SET sk11.name='GraphQL',          sk11.category='API';
MERGE (sk12:Skill {id:'skill-12'}) SET sk12.name='Data Engineering', sk12.category='Data';
MERGE (sk13:Skill {id:'skill-13'}) SET sk13.name='System Design',    sk13.category='Architecture';
MERGE (sk14:Skill {id:'skill-14'}) SET sk14.name='AWS',              sk14.category='Cloud';
MERGE (sk15:Skill {id:'skill-15'}) SET sk15.name='Neo4j',            sk15.category='Database';

// ── Technologies (10) ───────────────────────────────────────────────────────
MERGE (t1:Technology  {id:'tech-1'})  SET t1.name='Neo4j',        t1.type='Graph DB';
MERGE (t2:Technology  {id:'tech-2'})  SET t2.name='Apache Kafka', t2.type='Streaming';
MERGE (t3:Technology  {id:'tech-3'})  SET t3.name='React',        t3.type='UI Framework';
MERGE (t4:Technology  {id:'tech-4'})  SET t4.name='Spring Boot',  t4.type='Backend Framework';
MERGE (t5:Technology  {id:'tech-5'})  SET t5.name='TensorFlow',   t5.type='ML Framework';
MERGE (t6:Technology  {id:'tech-6'})  SET t6.name='Kubernetes',   t6.type='Orchestration';
MERGE (t7:Technology  {id:'tech-7'})  SET t7.name='PostgreSQL',   t7.type='Relational DB';
MERGE (t8:Technology  {id:'tech-8'})  SET t8.name='AWS Lambda',   t8.type='Serverless';
MERGE (t9:Technology  {id:'tech-9'})  SET t9.name='GraphQL',      t9.type='API Layer';
MERGE (t10:Technology {id:'tech-10'}) SET t10.name='dbt',         t10.type='Data Transform';

// ── JobRoles (6) ────────────────────────────────────────────────────────────
MERGE (r1:JobRole {id:'role-1'}) SET r1.title='Backend Engineer',       r1.level='mid';
MERGE (r2:JobRole {id:'role-2'}) SET r2.title='Frontend Engineer',      r2.level='mid';
MERGE (r3:JobRole {id:'role-3'}) SET r3.title='ML Engineer',            r3.level='senior';
MERGE (r4:JobRole {id:'role-4'}) SET r4.title='DevOps Engineer',        r4.level='mid';
MERGE (r5:JobRole {id:'role-5'}) SET r5.title='Data Engineer',          r5.level='mid';
MERGE (r6:JobRole {id:'role-6'}) SET r6.title='Graph Database Engineer',r6.level='senior';

// ── Projects (8) ────────────────────────────────────────────────────────────
MERGE (p1:Project {id:'proj-1'}) SET p1.name='SkillGraph Platform',    p1.status='active',    p1.domain='HR Tech';
MERGE (p2:Project {id:'proj-2'}) SET p2.name='FraudShield',            p2.status='active',    p2.domain='FinTech';
MERGE (p3:Project {id:'proj-3'}) SET p3.name='RecoEngine',             p3.status='completed', p3.domain='AI/ML';
MERGE (p4:Project {id:'proj-4'}) SET p4.name='CloudOps Dashboard',     p4.status='active',    p4.domain='DevOps';
MERGE (p5:Project {id:'proj-5'}) SET p5.name='DataLake Pipeline',      p5.status='active',    p5.domain='Data';
MERGE (p6:Project {id:'proj-6'}) SET p6.name='TalentMatch API',        p6.status='completed', p6.domain='HR Tech';
MERGE (p7:Project {id:'proj-7'}) SET p7.name='GraphQL Gateway',        p7.status='active',    p7.domain='API';
MERGE (p8:Project {id:'proj-8'}) SET p8.name='MLOps Infra',            p8.status='active',    p8.domain='AI/ML';

// ── Resources (10) ──────────────────────────────────────────────────────────
MERGE (res1:Resource  {id:'res-1'})  SET res1.title='Graph Databases Book',         res1.type='book',   res1.url='https://graphdatabases.com';
MERGE (res2:Resource  {id:'res-2'})  SET res2.title='Neo4j Cypher Manual',          res2.type='docs',   res2.url='https://neo4j.com/docs/cypher-manual';
MERGE (res3:Resource  {id:'res-3'})  SET res3.title='Spring Boot in Action',        res3.type='book',   res3.url='https://www.manning.com/books/spring-boot-in-action';
MERGE (res4:Resource  {id:'res-4'})  SET res4.title='Hands-On ML with Scikit-Learn',res4.type='book',   res4.url='https://oreilly.com/library/view/hands-on-machine-learning';
MERGE (res5:Resource  {id:'res-5'})  SET res5.title='React Docs',                   res5.type='docs',   res5.url='https://react.dev';
MERGE (res6:Resource  {id:'res-6'})  SET res6.title='Kubernetes in Action',         res6.type='book',   res6.url='https://www.manning.com/books/kubernetes-in-action';
MERGE (res7:Resource  {id:'res-7'})  SET res7.title='AWS Certified Solutions Arch', res7.type='course', res7.url='https://aws.amazon.com/certification';
MERGE (res8:Resource  {id:'res-8'})  SET res8.title='Kafka: The Definitive Guide',  res8.type='book',   res8.url='https://www.oreilly.com/library/view/kafka-the-definitive';
MERGE (res9:Resource  {id:'res-9'})  SET res9.title='dbt Learn',                    res9.type='course', res9.url='https://courses.getdbt.com';
MERGE (res10:Resource {id:'res-10'}) SET res10.title='GraphQL Spec',                res10.type='docs',  res10.url='https://spec.graphql.org';

// ── Users / Candidates (10) ─────────────────────────────────────────────────
MERGE (u1:User {id:'user-1'})  SET u1.name='Alice Chen',     u1.email='alice@example.com',   u1.yearsExp=5, u1.location='San Francisco';
MERGE (u2:User {id:'user-2'})  SET u2.name='Bob Patel',      u2.email='bob@example.com',     u2.yearsExp=3, u2.location='New York';
MERGE (u3:User {id:'user-3'})  SET u3.name='Clara Nguyen',   u3.email='clara@example.com',   u3.yearsExp=7, u3.location='Austin';
MERGE (u4:User {id:'user-4'})  SET u4.name='David Kim',      u4.email='david@example.com',   u4.yearsExp=4, u4.location='Seattle';
MERGE (u5:User {id:'user-5'})  SET u5.name='Eva Rossi',      u5.email='eva@example.com',     u5.yearsExp=6, u5.location='Chicago';
MERGE (u6:User {id:'user-6'})  SET u6.name='Frank Osei',     u6.email='frank@example.com',   u6.yearsExp=2, u6.location='Boston';
MERGE (u7:User {id:'user-7'})  SET u7.name='Grace Liu',      u7.email='grace@example.com',   u7.yearsExp=8, u7.location='San Francisco';
MERGE (u8:User {id:'user-8'})  SET u8.name='Hiro Tanaka',    u8.email='hiro@example.com',    u8.yearsExp=5, u8.location='Remote';
MERGE (u9:User {id:'user-9'})  SET u9.name='Isla Ferreira',  u9.email='isla@example.com',    u9.yearsExp=3, u9.location='Austin';
MERGE (u10:User {id:'user-10'}) SET u10.name='James Wright', u10.email='james@example.com',  u10.yearsExp=9, u10.location='New York';

// ── USER_HAS_SKILL ───────────────────────────────────────────────────────────
MATCH (u:User {id:'user-1'}),  (s:Skill {id:'skill-1'})  MERGE (u)-[:USER_HAS_SKILL {level:'expert',   years:5}]->(s);
MATCH (u:User {id:'user-1'}),  (s:Skill {id:'skill-10'}) MERGE (u)-[:USER_HAS_SKILL {level:'expert',   years:4}]->(s);
MATCH (u:User {id:'user-1'}),  (s:Skill {id:'skill-6'})  MERGE (u)-[:USER_HAS_SKILL {level:'advanced', years:2}]->(s);
MATCH (u:User {id:'user-1'}),  (s:Skill {id:'skill-15'}) MERGE (u)-[:USER_HAS_SKILL {level:'advanced', years:2}]->(s);
MATCH (u:User {id:'user-2'}),  (s:Skill {id:'skill-3'})  MERGE (u)-[:USER_HAS_SKILL {level:'advanced', years:3}]->(s);
MATCH (u:User {id:'user-2'}),  (s:Skill {id:'skill-4'})  MERGE (u)-[:USER_HAS_SKILL {level:'advanced', years:3}]->(s);
MATCH (u:User {id:'user-2'}),  (s:Skill {id:'skill-11'}) MERGE (u)-[:USER_HAS_SKILL {level:'beginner',  years:1}]->(s);
MATCH (u:User {id:'user-3'}),  (s:Skill {id:'skill-5'})  MERGE (u)-[:USER_HAS_SKILL {level:'expert',   years:6}]->(s);
MATCH (u:User {id:'user-3'}),  (s:Skill {id:'skill-2'})  MERGE (u)-[:USER_HAS_SKILL {level:'expert',   years:7}]->(s);
MATCH (u:User {id:'user-3'}),  (s:Skill {id:'skill-12'}) MERGE (u)-[:USER_HAS_SKILL {level:'advanced', years:4}]->(s);
MATCH (u:User {id:'user-4'}),  (s:Skill {id:'skill-8'})  MERGE (u)-[:USER_HAS_SKILL {level:'expert',   years:4}]->(s);
MATCH (u:User {id:'user-4'}),  (s:Skill {id:'skill-9'})  MERGE (u)-[:USER_HAS_SKILL {level:'advanced', years:3}]->(s);
MATCH (u:User {id:'user-4'}),  (s:Skill {id:'skill-14'}) MERGE (u)-[:USER_HAS_SKILL {level:'advanced', years:3}]->(s);
MATCH (u:User {id:'user-5'}),  (s:Skill {id:'skill-1'})  MERGE (u)-[:USER_HAS_SKILL {level:'advanced', years:4}]->(s);
MATCH (u:User {id:'user-5'}),  (s:Skill {id:'skill-7'})  MERGE (u)-[:USER_HAS_SKILL {level:'expert',   years:6}]->(s);
MATCH (u:User {id:'user-5'}),  (s:Skill {id:'skill-13'}) MERGE (u)-[:USER_HAS_SKILL {level:'expert',   years:5}]->(s);
MATCH (u:User {id:'user-6'}),  (s:Skill {id:'skill-2'})  MERGE (u)-[:USER_HAS_SKILL {level:'beginner',  years:1}]->(s);
MATCH (u:User {id:'user-6'}),  (s:Skill {id:'skill-5'})  MERGE (u)-[:USER_HAS_SKILL {level:'beginner',  years:1}]->(s);
MATCH (u:User {id:'user-7'}),  (s:Skill {id:'skill-6'})  MERGE (u)-[:USER_HAS_SKILL {level:'expert',   years:7}]->(s);
MATCH (u:User {id:'user-7'}),  (s:Skill {id:'skill-15'}) MERGE (u)-[:USER_HAS_SKILL {level:'expert',   years:8}]->(s);
MATCH (u:User {id:'user-7'}),  (s:Skill {id:'skill-1'})  MERGE (u)-[:USER_HAS_SKILL {level:'advanced', years:5}]->(s);
MATCH (u:User {id:'user-8'}),  (s:Skill {id:'skill-3'})  MERGE (u)-[:USER_HAS_SKILL {level:'expert',   years:5}]->(s);
MATCH (u:User {id:'user-8'}),  (s:Skill {id:'skill-4'})  MERGE (u)-[:USER_HAS_SKILL {level:'expert',   years:5}]->(s);
MATCH (u:User {id:'user-8'}),  (s:Skill {id:'skill-11'}) MERGE (u)-[:USER_HAS_SKILL {level:'advanced', years:3}]->(s);
MATCH (u:User {id:'user-9'}),  (s:Skill {id:'skill-12'}) MERGE (u)-[:USER_HAS_SKILL {level:'advanced', years:3}]->(s);
MATCH (u:User {id:'user-9'}),  (s:Skill {id:'skill-7'})  MERGE (u)-[:USER_HAS_SKILL {level:'advanced', years:3}]->(s);
MATCH (u:User {id:'user-10'}), (s:Skill {id:'skill-13'}) MERGE (u)-[:USER_HAS_SKILL {level:'expert',   years:9}]->(s);
MATCH (u:User {id:'user-10'}), (s:Skill {id:'skill-1'})  MERGE (u)-[:USER_HAS_SKILL {level:'expert',   years:9}]->(s);
MATCH (u:User {id:'user-10'}), (s:Skill {id:'skill-10'}) MERGE (u)-[:USER_HAS_SKILL {level:'expert',   years:8}]->(s);

// ── USER_WORKED_ON ───────────────────────────────────────────────────────────
MATCH (u:User {id:'user-1'}),  (p:Project {id:'proj-1'}) MERGE (u)-[:USER_WORKED_ON {role:'Tech Lead'}]->(p);
MATCH (u:User {id:'user-1'}),  (p:Project {id:'proj-6'}) MERGE (u)-[:USER_WORKED_ON {role:'Backend Dev'}]->(p);
MATCH (u:User {id:'user-2'}),  (p:Project {id:'proj-7'}) MERGE (u)-[:USER_WORKED_ON {role:'Frontend Dev'}]->(p);
MATCH (u:User {id:'user-3'}),  (p:Project {id:'proj-3'}) MERGE (u)-[:USER_WORKED_ON {role:'ML Lead'}]->(p);
MATCH (u:User {id:'user-3'}),  (p:Project {id:'proj-8'}) MERGE (u)-[:USER_WORKED_ON {role:'ML Engineer'}]->(p);
MATCH (u:User {id:'user-4'}),  (p:Project {id:'proj-4'}) MERGE (u)-[:USER_WORKED_ON {role:'DevOps Lead'}]->(p);
MATCH (u:User {id:'user-5'}),  (p:Project {id:'proj-2'}) MERGE (u)-[:USER_WORKED_ON {role:'Backend Dev'}]->(p);
MATCH (u:User {id:'user-5'}),  (p:Project {id:'proj-1'}) MERGE (u)-[:USER_WORKED_ON {role:'Architect'}]->(p);
MATCH (u:User {id:'user-7'}),  (p:Project {id:'proj-1'}) MERGE (u)-[:USER_WORKED_ON {role:'Graph Engineer'}]->(p);
MATCH (u:User {id:'user-8'}),  (p:Project {id:'proj-7'}) MERGE (u)-[:USER_WORKED_ON {role:'Frontend Lead'}]->(p);
MATCH (u:User {id:'user-9'}),  (p:Project {id:'proj-5'}) MERGE (u)-[:USER_WORKED_ON {role:'Data Engineer'}]->(p);
MATCH (u:User {id:'user-10'}), (p:Project {id:'proj-2'}) MERGE (u)-[:USER_WORKED_ON {role:'Architect'}]->(p);
MATCH (u:User {id:'user-10'}), (p:Project {id:'proj-4'}) MERGE (u)-[:USER_WORKED_ON {role:'Tech Lead'}]->(p);

// ── PROJECT_USES ─────────────────────────────────────────────────────────────
MATCH (p:Project {id:'proj-1'}), (t:Technology {id:'tech-1'}) MERGE (p)-[:PROJECT_USES]->(t);
MATCH (p:Project {id:'proj-1'}), (t:Technology {id:'tech-4'}) MERGE (p)-[:PROJECT_USES]->(t);
MATCH (p:Project {id:'proj-1'}), (t:Technology {id:'tech-9'}) MERGE (p)-[:PROJECT_USES]->(t);
MATCH (p:Project {id:'proj-2'}), (t:Technology {id:'tech-2'}) MERGE (p)-[:PROJECT_USES]->(t);
MATCH (p:Project {id:'proj-2'}), (t:Technology {id:'tech-4'}) MERGE (p)-[:PROJECT_USES]->(t);
MATCH (p:Project {id:'proj-2'}), (t:Technology {id:'tech-7'}) MERGE (p)-[:PROJECT_USES]->(t);
MATCH (p:Project {id:'proj-3'}), (t:Technology {id:'tech-5'}) MERGE (p)-[:PROJECT_USES]->(t);
MATCH (p:Project {id:'proj-3'}), (t:Technology {id:'tech-1'}) MERGE (p)-[:PROJECT_USES]->(t);
MATCH (p:Project {id:'proj-4'}), (t:Technology {id:'tech-6'}) MERGE (p)-[:PROJECT_USES]->(t);
MATCH (p:Project {id:'proj-4'}), (t:Technology {id:'tech-8'}) MERGE (p)-[:PROJECT_USES]->(t);
MATCH (p:Project {id:'proj-5'}), (t:Technology {id:'tech-2'}) MERGE (p)-[:PROJECT_USES]->(t);
MATCH (p:Project {id:'proj-5'}), (t:Technology {id:'tech-10'}) MERGE (p)-[:PROJECT_USES]->(t);
MATCH (p:Project {id:'proj-6'}), (t:Technology {id:'tech-1'}) MERGE (p)-[:PROJECT_USES]->(t);
MATCH (p:Project {id:'proj-6'}), (t:Technology {id:'tech-4'}) MERGE (p)-[:PROJECT_USES]->(t);
MATCH (p:Project {id:'proj-7'}), (t:Technology {id:'tech-9'}) MERGE (p)-[:PROJECT_USES]->(t);
MATCH (p:Project {id:'proj-7'}), (t:Technology {id:'tech-3'}) MERGE (p)-[:PROJECT_USES]->(t);
MATCH (p:Project {id:'proj-8'}), (t:Technology {id:'tech-5'}) MERGE (p)-[:PROJECT_USES]->(t);
MATCH (p:Project {id:'proj-8'}), (t:Technology {id:'tech-6'}) MERGE (p)-[:PROJECT_USES]->(t);

// ── TECH_REQUIRES (Technology → Skill) ──────────────────────────────────────
MATCH (t:Technology {id:'tech-1'}),  (s:Skill {id:'skill-6'})  MERGE (t)-[:TECH_REQUIRES]->(s);
MATCH (t:Technology {id:'tech-1'}),  (s:Skill {id:'skill-15'}) MERGE (t)-[:TECH_REQUIRES]->(s);
MATCH (t:Technology {id:'tech-2'}),  (s:Skill {id:'skill-12'}) MERGE (t)-[:TECH_REQUIRES]->(s);
MATCH (t:Technology {id:'tech-3'}),  (s:Skill {id:'skill-3'})  MERGE (t)-[:TECH_REQUIRES]->(s);
MATCH (t:Technology {id:'tech-3'}),  (s:Skill {id:'skill-4'})  MERGE (t)-[:TECH_REQUIRES]->(s);
MATCH (t:Technology {id:'tech-4'}),  (s:Skill {id:'skill-1'})  MERGE (t)-[:TECH_REQUIRES]->(s);
MATCH (t:Technology {id:'tech-4'}),  (s:Skill {id:'skill-10'}) MERGE (t)-[:TECH_REQUIRES]->(s);
MATCH (t:Technology {id:'tech-5'}),  (s:Skill {id:'skill-5'})  MERGE (t)-[:TECH_REQUIRES]->(s);
MATCH (t:Technology {id:'tech-5'}),  (s:Skill {id:'skill-2'})  MERGE (t)-[:TECH_REQUIRES]->(s);
MATCH (t:Technology {id:'tech-6'}),  (s:Skill {id:'skill-9'})  MERGE (t)-[:TECH_REQUIRES]->(s);
MATCH (t:Technology {id:'tech-6'}),  (s:Skill {id:'skill-8'})  MERGE (t)-[:TECH_REQUIRES]->(s);
MATCH (t:Technology {id:'tech-7'}),  (s:Skill {id:'skill-7'})  MERGE (t)-[:TECH_REQUIRES]->(s);
MATCH (t:Technology {id:'tech-8'}),  (s:Skill {id:'skill-14'}) MERGE (t)-[:TECH_REQUIRES]->(s);
MATCH (t:Technology {id:'tech-9'}),  (s:Skill {id:'skill-11'}) MERGE (t)-[:TECH_REQUIRES]->(s);
MATCH (t:Technology {id:'tech-10'}), (s:Skill {id:'skill-12'}) MERGE (t)-[:TECH_REQUIRES]->(s);
MATCH (t:Technology {id:'tech-10'}), (s:Skill {id:'skill-7'})  MERGE (t)-[:TECH_REQUIRES]->(s);

// ── SKILL_RELATED_TO ─────────────────────────────────────────────────────────
MATCH (a:Skill {id:'skill-1'}),  (b:Skill {id:'skill-10'}) MERGE (a)-[:SKILL_RELATED_TO]->(b);
MATCH (a:Skill {id:'skill-2'}),  (b:Skill {id:'skill-5'})  MERGE (a)-[:SKILL_RELATED_TO]->(b);
MATCH (a:Skill {id:'skill-3'}),  (b:Skill {id:'skill-4'})  MERGE (a)-[:SKILL_RELATED_TO]->(b);
MATCH (a:Skill {id:'skill-6'}),  (b:Skill {id:'skill-15'}) MERGE (a)-[:SKILL_RELATED_TO]->(b);
MATCH (a:Skill {id:'skill-8'}),  (b:Skill {id:'skill-9'})  MERGE (a)-[:SKILL_RELATED_TO]->(b);
MATCH (a:Skill {id:'skill-7'}),  (b:Skill {id:'skill-12'}) MERGE (a)-[:SKILL_RELATED_TO]->(b);
MATCH (a:Skill {id:'skill-14'}), (b:Skill {id:'skill-9'})  MERGE (a)-[:SKILL_RELATED_TO]->(b);
MATCH (a:Skill {id:'skill-11'}), (b:Skill {id:'skill-3'})  MERGE (a)-[:SKILL_RELATED_TO]->(b);

// ── SKILL_REQUIRED_FOR (Skill → JobRole) ─────────────────────────────────────
MATCH (s:Skill {id:'skill-1'}),  (r:JobRole {id:'role-1'}) MERGE (s)-[:SKILL_REQUIRED_FOR]->(r);
MATCH (s:Skill {id:'skill-10'}), (r:JobRole {id:'role-1'}) MERGE (s)-[:SKILL_REQUIRED_FOR]->(r);
MATCH (s:Skill {id:'skill-13'}), (r:JobRole {id:'role-1'}) MERGE (s)-[:SKILL_REQUIRED_FOR]->(r);
MATCH (s:Skill {id:'skill-3'}),  (r:JobRole {id:'role-2'}) MERGE (s)-[:SKILL_REQUIRED_FOR]->(r);
MATCH (s:Skill {id:'skill-4'}),  (r:JobRole {id:'role-2'}) MERGE (s)-[:SKILL_REQUIRED_FOR]->(r);
MATCH (s:Skill {id:'skill-11'}), (r:JobRole {id:'role-2'}) MERGE (s)-[:SKILL_REQUIRED_FOR]->(r);
MATCH (s:Skill {id:'skill-5'}),  (r:JobRole {id:'role-3'}) MERGE (s)-[:SKILL_REQUIRED_FOR]->(r);
MATCH (s:Skill {id:'skill-2'}),  (r:JobRole {id:'role-3'}) MERGE (s)-[:SKILL_REQUIRED_FOR]->(r);
MATCH (s:Skill {id:'skill-8'}),  (r:JobRole {id:'role-4'}) MERGE (s)-[:SKILL_REQUIRED_FOR]->(r);
MATCH (s:Skill {id:'skill-9'}),  (r:JobRole {id:'role-4'}) MERGE (s)-[:SKILL_REQUIRED_FOR]->(r);
MATCH (s:Skill {id:'skill-14'}), (r:JobRole {id:'role-4'}) MERGE (s)-[:SKILL_REQUIRED_FOR]->(r);
MATCH (s:Skill {id:'skill-12'}), (r:JobRole {id:'role-5'}) MERGE (s)-[:SKILL_REQUIRED_FOR]->(r);
MATCH (s:Skill {id:'skill-7'}),  (r:JobRole {id:'role-5'}) MERGE (s)-[:SKILL_REQUIRED_FOR]->(r);
MATCH (s:Skill {id:'skill-6'}),  (r:JobRole {id:'role-6'}) MERGE (s)-[:SKILL_REQUIRED_FOR]->(r);
MATCH (s:Skill {id:'skill-15'}), (r:JobRole {id:'role-6'}) MERGE (s)-[:SKILL_REQUIRED_FOR]->(r);
MATCH (s:Skill {id:'skill-13'}), (r:JobRole {id:'role-6'}) MERGE (s)-[:SKILL_REQUIRED_FOR]->(r);

// ── ROLE_AT (JobRole → Company) ──────────────────────────────────────────────
MATCH (r:JobRole {id:'role-1'}), (c:Company {id:'comp-1'}) MERGE (r)-[:ROLE_AT]->(c);
MATCH (r:JobRole {id:'role-1'}), (c:Company {id:'comp-2'}) MERGE (r)-[:ROLE_AT]->(c);
MATCH (r:JobRole {id:'role-2'}), (c:Company {id:'comp-3'}) MERGE (r)-[:ROLE_AT]->(c);
MATCH (r:JobRole {id:'role-3'}), (c:Company {id:'comp-3'}) MERGE (r)-[:ROLE_AT]->(c);
MATCH (r:JobRole {id:'role-3'}), (c:Company {id:'comp-5'}) MERGE (r)-[:ROLE_AT]->(c);
MATCH (r:JobRole {id:'role-4'}), (c:Company {id:'comp-2'}) MERGE (r)-[:ROLE_AT]->(c);
MATCH (r:JobRole {id:'role-5'}), (c:Company {id:'comp-5'}) MERGE (r)-[:ROLE_AT]->(c);
MATCH (r:JobRole {id:'role-6'}), (c:Company {id:'comp-1'}) MERGE (r)-[:ROLE_AT]->(c);
MATCH (r:JobRole {id:'role-6'}), (c:Company {id:'comp-3'}) MERGE (r)-[:ROLE_AT]->(c);

// ── RESOURCE_TEACHES (Resource → Skill) ──────────────────────────────────────
MATCH (res:Resource {id:'res-1'}),  (s:Skill {id:'skill-6'})  MERGE (res)-[:RESOURCE_TEACHES]->(s);
MATCH (res:Resource {id:'res-1'}),  (s:Skill {id:'skill-15'}) MERGE (res)-[:RESOURCE_TEACHES]->(s);
MATCH (res:Resource {id:'res-2'}),  (s:Skill {id:'skill-6'})  MERGE (res)-[:RESOURCE_TEACHES]->(s);
MATCH (res:Resource {id:'res-3'}),  (s:Skill {id:'skill-10'}) MERGE (res)-[:RESOURCE_TEACHES]->(s);
MATCH (res:Resource {id:'res-3'}),  (s:Skill {id:'skill-1'})  MERGE (res)-[:RESOURCE_TEACHES]->(s);
MATCH (res:Resource {id:'res-4'}),  (s:Skill {id:'skill-5'})  MERGE (res)-[:RESOURCE_TEACHES]->(s);
MATCH (res:Resource {id:'res-4'}),  (s:Skill {id:'skill-2'})  MERGE (res)-[:RESOURCE_TEACHES]->(s);
MATCH (res:Resource {id:'res-5'}),  (s:Skill {id:'skill-3'})  MERGE (res)-[:RESOURCE_TEACHES]->(s);
MATCH (res:Resource {id:'res-6'}),  (s:Skill {id:'skill-9'})  MERGE (res)-[:RESOURCE_TEACHES]->(s);
MATCH (res:Resource {id:'res-7'}),  (s:Skill {id:'skill-14'}) MERGE (res)-[:RESOURCE_TEACHES]->(s);
MATCH (res:Resource {id:'res-8'}),  (s:Skill {id:'skill-12'}) MERGE (res)-[:RESOURCE_TEACHES]->(s);
MATCH (res:Resource {id:'res-9'}),  (s:Skill {id:'skill-12'}) MERGE (res)-[:RESOURCE_TEACHES]->(s);
MATCH (res:Resource {id:'res-10'}), (s:Skill {id:'skill-11'}) MERGE (res)-[:RESOURCE_TEACHES]->(s);

// ── USER_RECOMMENDED_RESOURCE ────────────────────────────────────────────────
MATCH (u:User {id:'user-1'}),  (res:Resource {id:'res-1'})  MERGE (u)-[:USER_RECOMMENDED_RESOURCE]->(res);
MATCH (u:User {id:'user-3'}),  (res:Resource {id:'res-4'})  MERGE (u)-[:USER_RECOMMENDED_RESOURCE]->(res);
MATCH (u:User {id:'user-4'}),  (res:Resource {id:'res-6'})  MERGE (u)-[:USER_RECOMMENDED_RESOURCE]->(res);
MATCH (u:User {id:'user-7'}),  (res:Resource {id:'res-2'})  MERGE (u)-[:USER_RECOMMENDED_RESOURCE]->(res);
MATCH (u:User {id:'user-8'}),  (res:Resource {id:'res-5'})  MERGE (u)-[:USER_RECOMMENDED_RESOURCE]->(res);
MATCH (u:User {id:'user-10'}), (res:Resource {id:'res-3'})  MERGE (u)-[:USER_RECOMMENDED_RESOURCE]->(res);

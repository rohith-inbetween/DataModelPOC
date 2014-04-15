package com.cs.layer4.persistence.configuration;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.support.Neo4jTemplate;

@Configuration
public class Neo4jConfiguration {
	public static GraphDatabaseService graphDb;
	
	
	@Autowired
	public String dbpath;

	@Bean
	public GraphDatabaseService graphDatabaseService() {
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(dbpath);
		registerShutdownHook(graphDb);
		return graphDb;
	}

	@Bean
	public Neo4jTemplate neo4jTemplate() {
		return new Neo4jTemplate(graphDatabaseService());
	}

	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		});
	}
}
package com.cs.layer4.persistence.incoming;

import org.hibernate.search.FullTextSession;
import org.hibernate.search.jpa.FullTextEntityManager;

import com.mysema.query.jpa.impl.JPAQuery;

public interface SqlConnector extends IConnector{

	public JPAQuery getJPAQuery();
	
	public FullTextSession getFullTextSession();
	
}

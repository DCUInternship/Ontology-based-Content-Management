package com.google.gwt.sample.stockwatcher.server;

import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.http.HTTPRepository;

/**
 * 
 * @author markhender This class is used to check if a subject already exists in
 *         the sesame repository
 * 
 *         If it does exist -> Nothing happens in this class 
 *         If it does NOT exist: 
 *         		- It adds a triple => the subject | RDF.label | Index of subject in the content
 */
public class Sesame {
	private URI subject;

	public void subjectOnRepository(String webpage, String search, Integer[] indexes) {
		Repository myRepository = new HTTPRepository("http://localhost:8080/openrdf-sesame/", "University");
		String content = webpage + search.replace(' ', '_');
		try {
			myRepository.initialize();
			ValueFactory factory = myRepository.getValueFactory();
			subject = factory.createURI(content);
			RepositoryConnection con = myRepository.getConnection();
			RepositoryResult<Statement> statements = con.getStatements(subject, null, null, false);
			if (statements.asList().isEmpty()) {
				con.add(subject, org.openrdf.model.vocabulary.RDFS.LABEL, factory.createURI(webpage + indexes[0] + "_" + indexes[1]),
						factory.createURI(webpage));
			} else
			con.close();
			statements.close();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}

	}
}

package com.google.gwt.sample.stockwatcher.server;

import java.util.*;
import org.openrdf.model.Literal;

import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.http.HTTPRepository;
/**
 * This Class is used when the user has asked for triples to be suggested
 * based on the subject entered into the subject column
 * 
 *  It will return an arraylist of suggested Triples
 */
public class SuggestedTriple {

	public ArrayList<String[]> getSuggestedTriples(String in) {
		int index = in.lastIndexOf('/') + 1;
		String subject = in.substring(index);
		String predicate;
		ArrayList<String[]> st = new ArrayList<String[]>();
		Repository myRepository = new HTTPRepository("http://localhost:8080/openrdf-sesame/", "University");
		try {
			RepositoryConnection con = myRepository.getConnection();
			RepositoryResult<Statement> result = con.getStatements(null, null, null, false); // get all triples
			while (result.hasNext()) {
				Statement statement = result.next();
				Resource sub = statement.getSubject();

				String name = sub.stringValue();
				index = name.lastIndexOf('/') + 1; // remove website url infront of Subject 
				name = name.substring(index); // compare the subject the user has entered with the subject from sesame
				if (subject.equals(name)) {
					if(statement.getPredicate() == RDF.TYPE){ 
						predicate = "RDF.type";	// truncates to RDF.type
					}
					else{
						predicate = statement.getPredicate().stringValue();
					}
					Value object = statement.getObject();
					if (object instanceof Literal) { // Resource Resource Literal
						String[] triple = { name, predicate + "*", object.stringValue() };
						st.add(triple);
					} else {	// Resource Resource Resource
						String[] triple = { name, predicate, object.stringValue() };
						st.add(triple);
					}
					System.out.println( name +" "+predicate +" "+ object.stringValue() );
				}
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		return st; // return arraylist of suggested triples
	}
}

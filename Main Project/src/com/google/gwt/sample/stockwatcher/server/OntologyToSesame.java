package com.google.gwt.sample.stockwatcher.server;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.openrdf.OpenRDFException;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.rio.RDFFormat;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
/**
 * 
 * @author markhender
 *	Uploads ontology to sesame repository. 
 */
public class OntologyToSesame {
	private String baseURI = "";

	// Upload to Sesame server if the user uploaded ontology from file system
	public void upload(String file_path) {
		File file = new File(file_path);
		String sesame_server = "http://localhost:8080/openrdf-sesame";
		String database_name = "University";
		Repository myRepo = new HTTPRepository(sesame_server, database_name);
		try {
			myRepo.initialize();
			RepositoryConnection con = myRepo.getConnection();
			try {
				con.add(file, "file:///" + file.getAbsolutePath(), RDFFormat.forFileName(file.getPath()));
			} catch (RepositoryException re) {

			}
			finally {
				con.close();
			}
		} catch (OpenRDFException e) {

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IO NOT Connected\n\n");
		}

	}
	// Upload to Sesame if the user uploaded ontology from a web address
	public void uploadViaURL(String webpage) throws MalformedURLException{
		String sesame_server = "http://localhost:8080/openrdf-sesame";
		String database_name = "University";
		Repository myRepo = new HTTPRepository(sesame_server, database_name);
		try{
			myRepo.initialize();
			RepositoryConnection con = myRepo.getConnection();
			try{
				URL url = new URL(webpage);
				con.add(url, url.toString(), RDFFormat.RDFXML);
				
			}
			catch(RepositoryException re){
				re.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally{
				con.close();
			}
		}
		catch(OpenRDFException e){
			e.printStackTrace();
		}
	}

	// Store baseURI of uploaded ontology on sesame
	public String getBaseURI(String input) {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		File temp = new File(input);
		try {
			OWLOntology model = manager.loadOntologyFromOntologyDocument(temp);
			this.baseURI = model.getOntologyID().getOntologyIRI().toString();
			manager.removeOntology(model);
			temp.deleteOnExit();
			return this.baseURI;
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			return "Ontology could not be created";
		}
		
	}
}

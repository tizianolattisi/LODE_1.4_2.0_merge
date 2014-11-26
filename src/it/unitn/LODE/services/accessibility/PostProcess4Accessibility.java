package it.unitn.LODE.services.accessibility;

import it.unitn.LODE.Controllers.ControllersManager;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPath;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.Models.Lecture;
import it.unitn.LODE.utils.FileSystemManager;
import it.unitn.LODE.utils.Messanger;

/**
 * @author azanella
 * This class provide basic accessibility postprocessing functions.
 * It was called PostProcess in Alberto's thesis
 */

public class PostProcess4Accessibility {
	//private String log; //to be redirected to standard LODE Log
        private Messanger msgr;
	private Document dataxml;
	private Document slidexml;
	private XPath xpath;
	private ArrayList<String> match;
	private Lecture lecture;
	private String accessibilityFilesPath;
	private FileSystemManager fileSystemManager;
	private String endfile;
	/**
	 * @param currlecture - The current Lecture-object instance
	 */
	public PostProcess4Accessibility(Lecture currlecture)
	{
		fileSystemManager = ControllersManager.getinstance().getFileSystemManager();
		match = new ArrayList<String>();
		//log = new String("");
                msgr=Messanger.getInstance();
		lecture = currlecture;
		accessibilityFilesPath = lecture.getDistributionPath()+LODEConstants.FS + "content"+LODEConstants.FS+"accessibility"+LODEConstants.FS+"files";
	}
	
	
	/**
	 * Questo metodo crea il file lecture.js che contiene tutti i dati relativi alla lezione e alle tempistiche.
	 * Questo metodo deve essere eseguito per poter poi creare la versione offline e online.
	 * Questo metodo deve essere richiamato solo dopo aver creato il file data.xml all'interno della
	 * directory Distribution e avendo il file SLIDES.XML in Acquisition.
	 */
	public void createLectureJS()
	{
		//Costruzione nomi dei file e path
		String basepath = lecture.getDistributionPath()+LODEConstants.FS + "content";
		String dataxmlfile = basepath + LODEConstants.FS+ LODEConstants.DATA_XML;
		String slidexmlfile = lecture.getAcquisitionPath() + LODEConstants.FS + LODEConstants.SLIDES_XML;
		String lecturejsfile = accessibilityFilesPath + LODEConstants.FS + "script" + LODEConstants.FS + "lecture.js"; 
		
		//Stringa contenente il file finale prodotto nei successivi tre step poi da scrivere sul file lecture.js
		endfile = new String("");
		
		
		//Predisposizione per XPath
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		
		DocumentBuilder db;
		try {
			db = domFactory.newDocumentBuilder();
	                //if there are no slides do not attempt to process slidexmlfile
		if (fileSystemManager.fileExists(slidexmlfile))
                      slidexml = db.parse(slidexmlfile);
                else {
                    msgr.w("Warning: lecture has no slides.", LODEConstants.MSG_WARNING);
                }

		dataxml = db.parse(dataxmlfile);

		} catch (ParserConfigurationException e) {
                    msgr.w("[Accessibility] - Parsing ERROR - aborted", LODEConstants.MSG_ERROR);
			//log = log + "\n[Accessibility] - Parsing ERROR - aborted";
		} catch (SAXException e) {
                    msgr.w("[Accessibility] - Parsing ERROR - aborted", LODEConstants.MSG_ERROR);
			//log = log + "\n[Accessibility] - Parsing ERROR - aborted";
		} catch (IOException e) {
                    msgr.w("[Accessibility] - ERROR - problem opening XML files - aborted", LODEConstants.MSG_ERROR);			//log = log + "\n[Accessibility] - Parsing ERROR - aborted";
			//log = log + "\n[Accessibility] - ERROR - problem opening XML files - aborted";
		}
		xpath = XPathFactory.newInstance().newXPath();
		//-----------------------
		

		//Step 1: Creo la lista per le associazioni e creo l'array imgs
			
		//TODO: Aggiungere eccezioni o qualcosa per gestire il caso in cui il metodo privato mi ritorni false
		processImg();

		//Step 2: Estrarre informazioni su inizio/fine slide
		//TODO: Aggiungere eccezioni o qualcosa per gestire il caso in cui il metodo privato mi ritorni false
                processChapter();
				
		//Step 3: Estrapolare informazioni generali sulla lezione
		//TODO: Aggiungere eccezioni o qualcosa per gestire il caso in cui il metodo privato mi ritorni false
                processInfo();
		//Write file
                writeUTF8(endfile,lecturejsfile);
                //System.out.println(log);
	}
	
	
	/**
	 * Funzione per creare l'array imgs e riempire l'ArrayList match 
	 * @param endfile - stringa che rappresenta il file finale da scrivere 
	 * @return true se le operazioni vengono completate correttamente
	 */
	private boolean processImg()
	{
		try {
			XPathExpression exprimg = xpath.compile("//SLIDES/SLIDE/FILENAME/text()"); //Cerco il nome del file <SLIDES><SLIDE><FILENAME><testo da me cercato/></FILENAME></SLIDE>...</SLIDES>
			XPathExpression exprtxt = xpath.compile("//SLIDES/SLIDE/TEXT/text()"); //Cerco il testo
			NodeList imgs = (NodeList) exprimg.evaluate(slidexml,XPathConstants.NODESET);
			NodeList text = (NodeList) exprtxt.evaluate(slidexml,XPathConstants.NODESET);
			
			endfile = "\nvar imgs = new Array(new Array(), new Array(), new Array());"; //Creo array tridimensionale JS 
			for(int i = 0; i<imgs.getLength(); i++)
			{
				String imgpath = createBasePath(imgs.item(i).getNodeValue());
				String out = text.item(i).getNodeValue();
                                System.out.println(i+":"+out);
				out = out.replace("\\","\\\\").replace("\"","\\\"");
				String plain = out.replace("\n","\\n").replace("/n","\\n"); //Pulisco dai caratteri di escape per il "solo testo"
				String httext = out.replace("\n","<BR>").replace("/n","<BR>"); //Pulisco dai caratteri di escape e converto i \n in HTML
				endfile = endfile + ("imgs[0]["+i+"] = \"../../img/"+""+imgpath+"\";\n");
				endfile = endfile + ("imgs[1]["+i+"] = \""+httext+"\";\n");
				endfile = endfile + ("imgs[2]["+i+"] = \""+plain+"\";\n");
				match.add(imgpath); //Aggiungo all'ArrayList imgpath il path dell'immagine.
			}

		} catch (XPathExpressionException e) {return false;	}
		return true;
	}
	
	/**
	 * Crea l'array Chapters andando a leggere la mappa per le associazioni
	 * @param endfile - Stringa del file finale
	 * @return true se la procedura conclude correttamente
	 */
	private boolean processChapter()
	{
		try {
			XPathExpression exprtitles = xpath.compile("//slide/titolo/text()");
			XPathExpression exprtempo = xpath.compile("//slide/tempo/text()");
			XPathExpression exprimgs = xpath.compile("//slide/immagine/text()");
			NodeList titles = (NodeList) exprtitles.evaluate(dataxml,XPathConstants.NODESET);
			NodeList tempo = (NodeList) exprtempo.evaluate(dataxml,XPathConstants.NODESET);
			NodeList imgs = (NodeList) exprimgs.evaluate(dataxml,XPathConstants.NODESET);
			XPathExpression exprduration = xpath.compile("//video/totaltime/text()");
			String duration = (String) exprduration.evaluate(dataxml,XPathConstants.STRING);
			
			
			endfile = endfile + "\nvar chapters = new Array(new Array(), new Array(), new Array(), new Array());"; //Creo array chapters a quattro dimensioni in JS 
			for(int i = 0; i<(imgs.getLength()); i++)
			{
				int imgref = match.indexOf(createBasePath(imgs.item(i).getNodeValue())) ; //Estraggo il numero nell'arraylist (e quindi anche il numero nell'array JS) in cui si trova il path dell'immagine
				
				if(imgref == -1)
				{
					return false;  //Problem matching - I due array contengono path diversi (data.xml contiene un nome di file non presente in SLIDES.XML)
				}
				
				String titolo = titles.item(i).getNodeValue().replace("\\","\\\\").replace("\"","\\\"");;
				titolo = titolo.replace("\n","  ").replace("/n","  ");
				String start = tempo.item(i).getNodeValue();
				String end;
				if(i<imgs.getLength()-1)
				{
					end = tempo.item(i+1).getNodeValue(); //End of the current slide is start of the next
				}
				else
				{
					end = duration; //End of the last slide is the video end.
				}
				
				endfile = endfile + ("chapters[0]["+i+"] = \""+titolo+"\"; ");
				endfile = endfile + ("chapters[1]["+i+"] = \""+start+"\"; ");
				endfile = endfile + ("chapters[2]["+i+"] = \""+end+"\"; ");
				endfile = endfile + ("chapters[3]["+i+"] = "+imgref+"; ");
				
			}
		} catch (XPathExpressionException e) {return false;	}
		return true;
	}
	
	/**
	 * Processa le informazioni relative alla lezione
	 * @param endfile - Stringa rappresentante il file finale
	 * @return se la procedura é completata correttamente.
	 */
	private boolean processInfo()
	{
		try {
			XPathExpression exprtitle = xpath.compile("//info/titolo/text()");
			XPathExpression exprcourse = xpath.compile("//info/corso/text()");
			XPathExpression exprteacher = xpath.compile("//info/professore/text()");
			XPathExpression exprfilename = xpath.compile("//video/nome/text()");
			String title = (String) exprtitle.evaluate(dataxml,XPathConstants.STRING);
			String  course = (String) exprcourse.evaluate(dataxml,XPathConstants.STRING);
			String teacher = (String) exprteacher.evaluate(dataxml,XPathConstants.STRING);
			String filename = (String) exprfilename.evaluate(dataxml,XPathConstants.STRING);
			
			endfile = endfile + ("\nfunction gettitle() {return \""+title+"\";}");
			endfile = endfile + ("\nfunction getcourse() {return \""+course+"\";}");
			endfile = endfile + ("\nfunction getprofessor() {return \""+teacher+"\";}");
			endfile = endfile + ("\nfunction getfile() {return \""+filename+"\";}");

		} catch (XPathExpressionException e) {return false;	}
		return true;
	}
	
	
	
	/**
	 * Questo metodo preso un path restituisce il solo nome del file cercando l'ultimo carattere "/" e prendendo solo ci� che c'� dopo di questo
	 * @param filename - Path completo
	 * @return il solo nome del file con l'estensione. 
	 */
	private String createBasePath(String filepath)
	{
		
		int index = filepath.lastIndexOf('/');
		
		if(index != -1)
		{
			return (filepath.substring(index+1, filepath.length()));
		}
		return filepath;
	}
	
	
	/**
	 * Organizza i file nella Distribution Directory per la produzione dell'online mode
	 */
	public boolean switchOnLine()
	{
		File index = new File(accessibilityFilesPath+LODEConstants.FS+"index.html");
		File indexon = new File(accessibilityFilesPath+LODEConstants.FS+"indexon.html");
		
		if(index.exists())
		{
			fileSystemManager.recursiveDelete(index);
		}
		try {
			fileSystemManager.copyFiles(index, indexon);
		} catch (IOException e) {return false;}
		return true;
		
	}
	
	
	/**
	 * Organizza i file nella Distribution Directory per la produzione dell'offline mode.
	 */
	public boolean switchOffLine() {
		File indexoff = new File(accessibilityFilesPath+LODEConstants.FS+"indexoff.html");
		File index = new File(accessibilityFilesPath+LODEConstants.FS+"index.html");
		
		if(index.exists())
		{
			fileSystemManager.recursiveDelete(index);
		}
		try {
			fileSystemManager.copyFiles(index, indexoff);
		} catch (IOException e) {return false;}
		return true;

	}
	
	//Write UTF-8 FIle
	private void writeUTF8(String stringa, String path)
	{
		try { 
			Writer out = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(path), "UTF8")); 
			out.write(stringa);
			out.close(); 
			} catch (UnsupportedEncodingException e) { } 
			catch (IOException e) { } 
	}
	
	
	//Main temporaneo --- da eliminare.
	public static void main(String args[])
	{
		PostProcess4Accessibility pp = new PostProcess4Accessibility(new Lecture());
		pp.createLectureJS();
		pp.switchOffLine();
		
	}
	
}

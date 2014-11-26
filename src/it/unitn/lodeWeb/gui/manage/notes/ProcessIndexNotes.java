package it.unitn.lodeWeb.gui.manage.notes;
//--------------------------------------------------------------------------
import it.unitn.lodeWeb.util.Util;
import it.unitn.lodeWeb.util.WriteOnHTML;
import java.io.*;
import java.util.regex.*;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class ProcessIndexNotes {

    //--------------------------------------------------------------------------
    /**
     * Class Constructor
     */
    //--------------------------------------------------------------------------
    public ProcessIndexNotes() {
    }

    //--------------------------------------------------------------------------
    /**
     *  Edit index page
     *
     * @param title
     * @param pageIndex
     * @param homeWeb
     * @param newNote
     * 
     * @throws java.io.IOException
     */
    //--------------------------------------------------------------------------
    public void processIndex(String title, String pageIndex, String homeWeb, String newNote) throws IOException {

        if (title != null && !title.equals("") && new File(homeWeb).exists() && new File(homeWeb).isDirectory()) {
            // leggi il contenuto della pagina index
            String testoPagina = new Util().leggiFile(homeWeb + File.separator + pageIndex);

            // ricerca il tag nota e restituisci la nota inserita
            Pattern pattern = Pattern.compile("<div name=\"note_" + title + "\">(.*?)</div>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(testoPagina);

            String newTestoPagina = testoPagina;
            if (matcher.find()) {
                String nota = matcher.group(1);
                if (newNote != null) {
                    if (!newNote.equals("")) {
                        if (newNote.length() > 10) {
                            newNote = newNote.substring(0, 9) + "...";
                        // sostituisci la vecchia nota con la nuova
                        }
                        newTestoPagina = testoPagina.replaceAll("<div name=\"note_" + title + "\">" + nota + "</div>", "<div name=\"note_" + title + "\">" + newNote + "</div>");
                    } else {
                        // sostituisci la vecchia nota con la nuova
                        newTestoPagina = testoPagina.replaceAll("<div name=\"note_" + title + "\">" + nota + "</div>", "<div name=\"note_" + title + "\"></div>");
                    }
                } else {
                    newTestoPagina = testoPagina.replaceAll("<div name=\"note_" + title + "\">" + nota + "</div>", "<div name=\"note_" + title + "\"></div>");
                }
            }
            // riscrivi la index
            (new WriteOnHTML()).writeHTML(homeWeb, pageIndex, newTestoPagina);
        }
    }
}
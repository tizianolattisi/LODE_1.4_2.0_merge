package it.unitn.lodeWeb.serializationxml.data;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
@Root
public class Slide {

    //--------------------------------------------------------------------------
    /**
     * Class Constructor
     */
    //--------------------------------------------------------------------------
    public Slide() {
        super();
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Class Constructor
     *
     * @param tempo
     * @param titolo
     * @param immagine
     */
    //--------------------------------------------------------------------------
    public Slide(int tempo, String titolo, String immagine) {
        this.tempo = tempo;
        this.immagine = immagine;
        this.titolo = titolo;
    }
    @Element(name = "tempo")
    private int tempo;
    @Element(name = "titolo")
    private String titolo;
    @Element(name = "immagine")
    private String immagine;

    /**
     *
     * @return int
     */
    public int getTempo() {
        return tempo;
    }

    /**
     *
     * @return String
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     *
     * @return String
     */
    public String getImmagine() {
        return immagine;
    }
}


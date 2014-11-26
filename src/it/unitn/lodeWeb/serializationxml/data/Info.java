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
public class Info {

    //--------------------------------------------------------------------------
    /**
     *
     * Class costructor
     *
     */
    //--------------------------------------------------------------------------
    public Info() {
        super();
    }

    /**
     *
     * Class Constructor
     *
     * @param corso
     * @param titolo
     * @param professore
     * @param dinamic_url
     */
    public Info(String corso, String titolo, String professore, String dinamic_url) {
        this.corso = corso;
        this.professore = professore;
        this.titolo = titolo;
        this.dinamic_url = dinamic_url;
    }
    @Element(name = "corso")
    private String corso;
    @Element(name = "titolo")
    private String titolo;
    @Element(name = "professore")
    private String professore;
    @Element(name = "dinamic_url")
    private String dinamic_url;

    /**
     *
     * @return String
     */
    public String getCorso() {
        return corso;
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
    public String getProfessore() {
        return professore;
    }

    /**
     *
     * @return String
     */
    public String getURL() {
        return dinamic_url;
    }
}

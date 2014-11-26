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
public class Video {

    //--------------------------------------------------------------------------
    /**
     *
     */
    //--------------------------------------------------------------------------
    public Video() {
        super();
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Class Constructor
     *
     * @param nome
     * @param totaltime
     * @param starttime
     */
    //--------------------------------------------------------------------------
    public Video(String nome, int totaltime, int starttime) {
        this.nome = nome;
        this.totaltime = totaltime;
        this.starttime = starttime;
    }
    @Element(name = "nome")
    private String nome;
    @Element(name = "totaltime")
    private int totaltime;
    @Element(name = "starttime")
    private int starttime;

    //--------------------------------------------------------------------------
    /**
     *
     * @return int
     */
    //--------------------------------------------------------------------------
    public int getstarttime() {
        return starttime;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * @return int
     */
    //--------------------------------------------------------------------------
    public int gettotaltime() {
        return totaltime;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * @return String
     */
    //--------------------------------------------------------------------------
    public String getnome() {
        return nome;
    }
}

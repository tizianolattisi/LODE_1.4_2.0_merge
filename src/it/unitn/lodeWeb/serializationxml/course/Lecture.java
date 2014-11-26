package it.unitn.lodeWeb.serializationxml.course;

import org.simpleframework.xml.*;

/**
 *
 * @author Colombari Mattia
 */
@Root(name = "LECTURE")
public class Lecture implements Comparable {

    /**
     *
     * Class Constructor
     *
     */
    public Lecture() {
        super();
    }

    /**
     *
     *
     *
     * @param o
     * @return int
     */
    public int compareTo(Object o) {
        return 1;
    }
}


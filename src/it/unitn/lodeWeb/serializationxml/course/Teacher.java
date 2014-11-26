package it.unitn.lodeWeb.serializationxml.course;

import org.simpleframework.xml.*;

/**
 *
 * @author Colombari Mattia
 */
@Root(name = "TEACHER_NAME")
public class Teacher implements Comparable {

    /**
     *
     * Class Constructor
     *
     */
    public Teacher() {
        super();
    }

    /**
     *
     * @param o - Object
     * @return int
     */
    public int compareTo(Object o) {
        return 1;
    }
}

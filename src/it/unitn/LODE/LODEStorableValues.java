package it.unitn.LODE;

import it.unitn.LODE.Models.StorableAsXML;
import org.simpleframework.xml.*;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author ronchet
 */
public abstract class LODEStorableValues extends StorableAsXML {

    File prefsFile;
    @ElementMap(entry = "SECTION", key = "NAME", attribute = true, inline = true)
    HashMap<String, Properties> propertiesMap = new HashMap<String, Properties>();
    HashMap<String, String> prefsMap = new HashMap<String, String>();

    abstract void readDefaults();

    public final void savePreferences() {
        for (Properties p : propertiesMap.values()) {
            p.saveProperties();
        }
        this.persist(prefsFile);
    }

    public String get(String name) {
        return prefsMap.get(name);
    }

    public final void cleanup() {
        for (Properties p : propertiesMap.values()) {
            p.cleanup();
        }
    }

    protected void loadValuesIntoMap() {
        for (Properties p1 : propertiesMap.values()) {
            for (Property p : p1.properties) {
                prefsMap.put(p.name, p.value);
            }
        }
    }

    protected void addProperty(String name, String value, String comment, String sectionName) {
        if (!propertiesMap.containsKey(sectionName)) {
            propertiesMap.put(sectionName, new Properties());
        }
        Properties section = propertiesMap.get(sectionName);
        section.name = sectionName;
        propertiesMap.put(sectionName, section);
        section.addProperty(name, value, comment);
        prefsMap.put(name, value);
    }

    public void changeProperty(String name, String value) {
        // Cycle over the properties until you find the right one, work on it, save and exit cycle
        cycle:
        for (Properties section : propertiesMap.values()) {
            for (Property p : section.properties) {
                if (p.name.equals(name)) {
                    p.value = value;
                    prefsMap.put(name, value);
                    savePreferences();
                    //System.out.println("props changed! "+name+"<->"+value);
                    break cycle;
                }
            }
        }
    }
}

@Root(name = "GROUP_OF_PROPERTIES")
final class Properties extends StorableAsXML {

    static String UNDEFINED = "none";
    //@Attribute(name="name")
    String name = null;
    @ElementList(inline = true) //, name="GROUPS")
    Set<Property> properties = new HashSet<Property>();

    void addProperty(String name, String value, String comment) {
        Property p = new Property(name, value, comment);
        properties.add(p);
    }

    void saveProperties() {
        for (Property p : properties) {
            if (p.comment == null | p.value == null) {
                if (p.comment == null) {
                    p.comment = UNDEFINED;
                }
                if (p.value == null) {
                    p.value = UNDEFINED;
                }
                properties.add(p);
            }
        }
    }

    public final void cleanup() {
        for (Property p : properties) {
            if (p.comment.equals(UNDEFINED) | p.value.equals(UNDEFINED)) {
                if (p.comment.equals(UNDEFINED)) {
                    p.comment = null;
                }
                if (p.value.equals(UNDEFINED)) {
                    p.value = null;
                }
                properties.add(p);
            }
        }
    }
}

@Root(name = "PROPERTY")
final class Property extends StorableAsXML {
    //@Element(name="COMMENT")

    @Text
    String comment = null;
    @Attribute(name = "NAME")
    String name = null;
    @Attribute(name = "VALUE")
    String value = null;

    Property() {
    }

    Property(String name, String value, String comment) {
        this.name = name;
        this.value = value;
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) { // needed for the TreeSet!
        if (o instanceof Property) {
            return name.equals(((Property) o).name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }
}

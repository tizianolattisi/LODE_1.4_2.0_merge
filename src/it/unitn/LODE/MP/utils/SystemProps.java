
package it.unitn.LODE.MP.utils;


public class SystemProps {
    // extracted from
    // http://svn.apache.org/viewvc/commons/proper/lang/trunk/src/main/java/org/apache/commons/lang3/SystemUtils.java?revision=1199816&view=markup
    
    private static final String USER_HOME_KEY = "user.home";
    private static final String OS_NAME_WINDOWS_PREFIX = "Windows";
    public static final String JAVA_RUNTIME_VERSION = getSystemProperty("java.runtime.version");
    public static final String JAVA_RUNTIME_NAME = getSystemProperty("java.runtime.name");
    public static final String JAVA_VERSION = getSystemProperty("java.version");
    public static final String OS_NAME = getSystemProperty("os.name");
    public static final String OS_VERSION = getSystemProperty("os.version");
    public static final String PATH_SEPARATOR = getSystemProperty("path.separator");
    public static final String USER_COUNTRY = getSystemProperty("user.country") == null ? getSystemProperty("user.region") : getSystemProperty("user.country");
    public static final String USER_HOME = getSystemProperty(USER_HOME_KEY);
    public static final String USER_LANGUAGE = getSystemProperty("user.language");
    public static final String USER_NAME = getSystemProperty("user.name");
    public static final String JAVA_SPECIFICATION_VERSION = getSystemProperty("java.specification.version");
    public static final boolean IS_JAVA_1_6 = getJavaVersionMatches("1.6");
    public static final boolean IS_JAVA_1_7 = getJavaVersionMatches("1.7");
    public static final boolean IS_OS_LINUX = getOSMatchesName("Linux") || getOSMatchesName("LINUX");
    public static final boolean IS_OS_MAC = getOSMatchesName("Mac");
    public static final boolean IS_OS_MAC_OSX = getOSMatchesName("Mac OS X");
    public static final boolean IS_OS_WINDOWS = getOSMatchesName(OS_NAME_WINDOWS_PREFIX);

    private static boolean getOSMatches(String osNamePrefix, String osVersionPrefix) {
        return isOSMatch(OS_NAME, OS_VERSION, osNamePrefix, osVersionPrefix);
    }

    static boolean isOSMatch(String osName, String osVersion, String osNamePrefix, String osVersionPrefix) {
        if (osName == null || osVersion == null) {
            return false;
        }
        return osName.startsWith(osNamePrefix) && osVersion.startsWith(osVersionPrefix);
    }

    static boolean isOSNameMatch(String osName, String osNamePrefix) {
        if (osName == null) {
            return false;
        }
        return osName.startsWith(osNamePrefix);
    }

    private static String getSystemProperty(String property) {
        try {
            return System.getProperty(property);
        } catch (SecurityException ex) {
            // we are not allowed to look at this property
            System.err.println("Caught a SecurityException reading the system property '" + property
                    + "'; the SystemUtils property value will default to null.");
            return null;
        }
    }

    private static boolean getOSMatchesName(String osNamePrefix) {
        return isOSNameMatch(OS_NAME, osNamePrefix);
    }

    static boolean isJavaVersionMatch(String version, String versionPrefix) {
        if (version == null) {
            return false;
        }
        return version.startsWith(versionPrefix);
    }

    private static boolean getJavaVersionMatches(String versionPrefix) {
        return isJavaVersionMatch(JAVA_SPECIFICATION_VERSION, versionPrefix);
    }
}

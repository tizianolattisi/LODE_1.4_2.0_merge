/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unitn.QT;

/**
 *
 * @author ronchet
 */
public class QT_LODEConstants {
    
    //====================================================================
    //
    // our own constants definitions
    // values discovered scavenging some code
    //
    //====================================================================        
    // CODED CODES
    public static final int 	CODEC_ANIMATION = 1919706400;
    public static final int 	CODEC_CINEPAK 	= 1668704612;
    public static final int 	CODEC_H_263 	= 1748121139;
    public static final int 	CODEC_MOTION_JPEG_B = 1835692130;
    public static final int 	CODEC_MPEG_4 	= 1836070006;
    public static final int 	CODEC_RAW 	= 0;
    public static final int 	CODEC_SORENSON 	= 1398165809;
    public static final int 	CODEC_SORENSON_3 = 1398165811;
    // QUALITY VALUES
    public static final int 	QUALITY_HIGH 	= 768;
    public static final int 	QUALITY_LOW 	= 256;
    public static final int 	QUALITY_MAXIMUM = 1023;
    public static final int 	QUALITY_NORMAL 	= 512;
    // DEVICE NAMES
    public static final String INTERNAL_ISIGHT="USB Video Class Video"; 
    public static final String EXTERNAL_ISIGHT="IIDC FireWire Video"; 
    public static final String EXTERNAL_DV="DV Video"; 
    public static final String INTERNAL_MICROPHONE="Built-in Microphone";
    public static final String EXTERNAL_MICROPHONE="Built-in Input";
    public static final String DV_MICROPHONE="Audio DV";


    //====================================================================
    //
    // our own Java binding of the Quicktime constants - see
    // http://developer.apple.com/documentation/QuickTime/Reference/QTRef_Constants/Reference/reference.html
    //
    //====================================================================    
    /* Component Identifiers
     * Identify the types of components.
     * All components of the same type or subtype provide the same kinds of 
     * services and support a common application programming interface. 
     * Codecs have their own set of types.
     */
    public static final String clockComponentType                 ="clok";
    public static final String compressorComponentType            ="imco";
    public static final String CreateFilePreviewComponentType     ="pmak";
    public static final String DataHandlerType                    ="dhlr";
    public static final String decompressorComponentType          ="imdc";
    public static final String MediaHandlerType                   ="mhlr";
    public static final String MovieControllerComponentType       ="play";
    public static final String MovieExportType                    ="spit";
    public static final String MovieImportType                    ="eat ";
    public static final String SeqGrabChannelType                 ="sgch";
    public static final String SeqGrabComponentType               ="barg";
    public static final String SeqGrabCompressionPanelType        ="cmpr";
    public static final String SeqGrabPanelType                   ="sgpn";
    public static final String SeqGrabSourcePanelType             ="sour";
    public static final String ShowFilePreviewComponentType       ="pnot";
    public static final String StandardCompressionSubType         ="imag";
    public static final String StandardCompressionSubTypeSound    ="soun";
    public static final String StandardCompressionType            ="scdi";
    public static final String systemMicrosecondClock             ="micr";
    public static final String systemMillisecondClock             ="mill";
    public static final String systemSecondClock                  ="seco";
    public static final String systemTickClock                    ="tick";
    public static final String videoDigitizerComponentType        ="vdig";

    /* Component Property IDs and Flags
     * Constants that contain the flags and IDs of component properties.
     */

    public static int uppCallComponentGetComponentPropertyInfoProcInfo = 0x0003FFF0;
    public static int uppCallComponentGetComponentPropertyProcInfo = 0x0003FFF0;
    public static int uppCallComponentSetComponentPropertyProcInfo = 0x0000FFF0;
    public static int uppCallComponentAddComponentPropertyListenerProcInfo = 0x0000FFF0;
    public static int uppCallComponentRemoveComponentPropertyListenerProcInfo = 0x0000FFF0;
    public static int kCallComponentExecuteWiredActionSelect     = -9;
    public static long kComponentPropertyFlagCanSetLater = (1L << 0);
    public static long kComponentPropertyFlagCanSetNow = (1L << 1);
    public static long kComponentPropertyFlagCanGetNow = (1L << 3);
    public static long kComponentPropertyFlagHasExtendedInfo = (1L << 4);
    public static long kComponentPropertyFlagValueMustBeReleased = (1L << 5);
    public static long kComponentPropertyFlagValueIsCFTypeRef = (1L << 6);
    public static long kComponentPropertyFlagGetBufferMustBeInitialized = (1L << 7);
    public static int kQTComponentPropertyListenerCollectionContextVersion = 1;
    public static int kQTGetComponentPropertyInfoSelect          = -11;
    public static int kQTGetComponentPropertySelect              = -12;
    public static int kQTSetComponentPropertySelect              = -13;
    public static int kQTAddComponentPropertyListenerSelect      = -14;
    public static int kQTRemoveComponentPropertyListenerSelect   = -15;

}

package it.unitn.LODE.services;

import com.xuggle.xuggler.Converter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

/**
 *
 * @author ronchet
 */
public class MultiPlatformVideoConverter {

    public void convert(final String videoPath,
            final boolean selectResolution,
            final boolean createDistribution,
            final boolean showFinalDialog,
            final boolean synchronous) {
        Converter converter = new Converter();
        String[] args = {"-vbitrate", "2000", videoPath, videoPath + ".flv"};
        //String [] args={videoPath,videoPath+".flv"};
        System.out.println("starting to convert " + videoPath);
        try {
            // first define options
            Options options = converter.defineOptions();
            // And then parse them.
            CommandLine cmdLine = converter.parseOptions(options, args);
            // Finally, run the converter.
            System.out.println("executing " + cmdLine);

            converter.run(cmdLine);
            //_addFlvMetadata(videoPath + ".flv", createDistribution, showFinalDialog);
        } catch (Exception exception) {
            System.err.printf("Error: %s\n", exception.getMessage());
        }
        System.out.println("end conversion " + videoPath);
    }
}

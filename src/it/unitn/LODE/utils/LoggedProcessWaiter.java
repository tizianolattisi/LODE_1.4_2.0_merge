package it.unitn.LODE.utils;

import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.gui.ProgressBar;
import it.unitn.LODE.services.ProcessLogger;
/**
 *
 * @author ronchet
 */
public class LoggedProcessWaiter extends ProcessWaiter {
    public LoggedProcessWaiter(Process p){
        super(p);
    }
    public LoggedProcessWaiter(Process p, String msg){
        super(p,msg);
    }
    /**
     * the method that waits for the process, and prints the message
     */
    @Override
    public void run() {
        Messanger m=Messanger.getInstance();
        ProcessLogger logger=ProcessLogger.getProcessLogger(p);
        ProgressBar pb=ProgressBar.showIndeterminateProgressBar("Please wait - executing command");
        try {
            p.exitValue();
            m.w("Process not active "+p.toString(),LODEConstants.MSG_WARNING);
        } catch (IllegalThreadStateException ex) {
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace(m.getLogger());
                m.w("Interrupted exception while waiting for Process "+p.toString(),LODEConstants.MSG_WARNING);
            }
        }
        m.log(message);
        pb.closeWindow();
    }
}

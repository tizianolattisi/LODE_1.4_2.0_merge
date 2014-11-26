package it.unitn.LODE.utils;

import it.unitn.LODE.MP.constants.LODEConstants;

/**
 *
 * @author ronchet
 */
public class ProcessWaiter extends Thread {
    Process p =null;
    String message="Process completed";
    /**
     * A Thread that waits for a process to end
     * @param p the process to be waited for
     */
    public ProcessWaiter(Process p){
        this.p=p;
    }
    /**
     * A Thread that waits for a process to end
     * @param p the process to be waited for
     * @param msg the message to be printed after finishing
     */
    public ProcessWaiter(Process p, String msg){
        this.p=p;
        this.message=msg;
    }
    /**
     * the method that waits for the process, and prints the message
     */
    @Override
    public void run() {
        try {
            p.waitFor();
        } catch (InterruptedException ex) {
            Messanger m=Messanger.getInstance();
            ex.printStackTrace(m.getLogger());
            m.w("Interrupted exception while waiting for Process "+p.toString(),LODEConstants.MSG_WARNING);
        }
        Messanger m=Messanger.getInstance();
        m.log(message);
    }
}


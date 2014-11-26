package it.unitn.lodeWeb.util.Zip;

//--------------------------------------------------------------------------
import it.unitn.lodeWeb.util.ProcessRuntime;
import it.unitn.lodeWeb.util.Row;
import it.unitn.lodeWeb.util.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class ZipDir {

    //--------------------------------------------------------------------------
    /**
     *
     * 	Create zip file from a directory
     *
     * @param dirzipIn - directory to zip
     * @param zos - ZipOutputStream
     *
     * @return long - compress size
     *
     */
    //--------------------------------------------------------------------------
    public long zip(String dirzipIn, ZipOutputStream zos, long dim, String nameDir) {
        try {
            //creo un oggetto File con la directory da zippare
            File zipDir = new File(dirzipIn);
            //ottengo la lista di file contenuti nella directory
            //String[] dirList = zipDir.list();
            File[] dirList = zipDir.listFiles();
            // buffer di lettura
            byte[] readBuffer = new byte[2156];
            int bytesIn = 0;
            //per ogni file nella dirList
            for (int i = 0; i < dirList.length; i++) {
                //File f = new File(zipDir, dirList[i]);
                File f = new File(dirList[i].getPath());


                if (f.isDirectory()) {
                    //se è una directory richiamo la funzione ricorsivamente sulla nuova directory
                    String filePath = f.getPath();
                    dim = dim + zip(filePath, zos, dim, nameDir);
                } else {
                    //create a FileInputStream on top of f
                    FileInputStream fis = new FileInputStream(f);
                    //creo una nuova zipEntry

                    // creo il percorso per il file partendo da nameDir
                    String a = f.getName();
                    File f1 = f;
                    while (!f1.getName().equals(nameDir)) {
                        f1 = f1.getParentFile();
                        a = f1.getName() + File.separator + a;
                    }
                    if (f1.getParentFile().getName().equals(nameDir)) {
                        a = nameDir + File.separator + a;
                    }

                    ZipEntry anEntry = new ZipEntry(a);// getpath

                    dim = dim + anEntry.getCompressedSize();
                    //inserisco la zipEntry nel ZipOutputStream
                    zos.putNextEntry(anEntry);
                    //scrivo il contenuto del file nel ZipOutputStream
                    while ((bytesIn = fis.read(readBuffer)) != -1) {
                        zos.write(readBuffer, 0, bytesIn);
                    }
                    //chiudo lo stream
                    fis.close();
                }

            }
        } catch (Exception e) {
        }
        return dim;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * 	create zip from a directory
     *
     * @param inD - directory to zip
     * @param outD - output directory
     */
    //--------------------------------------------------------------------------
    public void zipDir(String inD, String outD) throws java.io.IOException {

        String filename = outD + File.separator + new File(inD).getName() + ".zip";

        //Creazione dello stream di scrittura
        File file = new File(filename);
        FileOutputStream fileZip = new FileOutputStream(file);
        //Decoro lo stream di output con il filtro di Zip
        ZipOutputStream zip = new ZipOutputStream(fileZip);

        System.out.println("zip: " + file.getName());
        zip(inD, zip, 0, new File(inD).getName());
        zip.close();
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Zip a dir
     *
     * @param dirToZip - directory to zip
     * @param outDir - output directory
     *
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     *
     * @return Process
     */
    //--------------------------------------------------------------------------
    public Process doZip(String dirToZip, String outDir) throws IOException, InterruptedException {
        String comand = "";
        Process p = null;
        ProcessRuntime pR = new ProcessRuntime();

        if (pR.getSo().equals(ProcessRuntime.Sop.LINUX)) {


            comand = "zip -r " + outDir + File.separator + new File(dirToZip).getName() + ".zip .";// + dirToZip;
            // zip -r
            p = pR.execute(comand, null, new File(dirToZip));

        // attendo ceh termini ogni processo di zip per avviarne uno nuovo
        //p.waitFor();

        } else {
            zipDir(dirToZip, outDir);
        }
        return p;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Zip lectures
     *
     * @param dist_Dir - directory distribution
     * @param lecturesList - list of lectures
     * @param outDir - output directory
     *
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     *
     */
    //--------------------------------------------------------------------------
    public void zipLectures(String dist_Dir, List<Row> lecturesList, String outDir) throws Exception {
        if (new File(dist_Dir).exists()) {

            new Util().createDir(outDir);

            Iterator it = lecturesList.iterator();

            // contatore dei processi attivi
            int i_Process = 0;
            // numero di processi attivi per volta
            int Nprocess = 3;

            // array dei processi attivi
            Process[] arrayProc = new Process[Nprocess];

            while (it.hasNext()) {

                Row row = (Row) it.next();

                if (new File(dist_Dir + File.separator + row.getNameDir()).exists()) {

                    if (!new File(outDir + File.separator + row.getNameDir() + ".zip").exists()) {
                        // processo di zip
                        Process p = doZip(new File(dist_Dir + File.separator + row.getNameDir()).getPath(), outDir);
                        // inserisco il processo nell'array
                        arrayProc[i_Process] = p;

                        // se il contatore è uguale
                        if (i_Process == arrayProc.length - 1) {
                            i_Process = 0;
                            // attendo la fine dei n processi avviati
                            for (int i = 0; i < arrayProc.length; i++) {
                                if (arrayProc[i] != null) {
                                    arrayProc[i].waitFor();
                                }
                            }
                        } else {
                            // incremento il contatore processi
                            i_Process++;
                        }
                    }

                }
            }
            // attendo la fine dei processi rimanenti
            for (int i = 0; i < arrayProc.length; i++) {
                if (arrayProc[i] != null) {
                    arrayProc[i].waitFor();
                }
            }

        } else {
            throw new Exception("directory Distribution empty");
        }
    }
}

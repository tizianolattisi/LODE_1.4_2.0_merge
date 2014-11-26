package it.unitn.lodeWeb.gui.manage.notes;

//--------------------------------------------------------------------------
import it.unitn.lodeWeb.gui.ChooserPanel;
import it.unitn.lodeWeb.serializationxml.ManageInfoXML;
import it.unitn.lodeWeb.serializationxml.course.Course;
import it.unitn.lodeWeb.util.Constants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLEditorKit;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class ViewNotesPanel extends JPanel
        implements ActionListener {

    private JTable table;
    private JTextArea output;
    private JButton buttonEditNota;
    private JFrame jFrameEdit;
    private int selectedRow;
    private Object[][] data = {
        {new Integer(1), "", "", ""}};
    private static int nCol = 4;
    private Document[] arrayDoc = null;
    JButton jButtonShow;
    private JTextPane outputNote;
    private JTextPane jedp;
    private JButton viewPageButton;
    ChooserPanel cp;

    //--------------------------------------------------------------------------
    /**
     *
     * @param cp
     */
    //--------------------------------------------------------------------------
    public void setChooser(ChooserPanel cp) {
        this.cp = cp;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Class Constructor
     *
     */
    //--------------------------------------------------------------------------
    public ViewNotesPanel() {

        super();

        setLayout(new BorderLayout(5, 5));

        setOpaque(false);

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));



        jButtonShow = new JButton("Show/Refresh");
        jButtonShow.setIcon(new ImageIcon("Images"+ File.separator + "refresh.png"));
        jButtonShow.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    setDataTable(cp.getOutDir(), cp.getCourseDir());
                    table.repaint();

                } catch (Exception ex) {
                }
            }
        });

        buttonEditNota = new JButton("Edit note");
         buttonEditNota.setIcon(new ImageIcon("Images"+ File.separator + "editNote.png"));
        buttonEditNota.addActionListener(this);
        buttonEditNota.setEnabled(false);

        viewPageButton = new JButton("View index page");
        viewPageButton.setIcon(new ImageIcon("Images"+ File.separator + "Globe-16x16.png"));
        viewPageButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                viewIndex();
            }
        });
        viewPageButton.setEnabled(false);


        JPanel jPanelButtons = new JPanel();

        jPanelButtons.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)));


        jPanelButtons.add(jButtonShow);
        jPanelButtons.add(viewPageButton);
        jPanelButtons.add(buttonEditNota);

        add(jPanelButtons, BorderLayout.NORTH);

        //----------------------------------


        table = new JTable(new MyTableModel());

        table.setPreferredScrollableViewportSize(new Dimension(500, 70));

        table.setFillsViewportHeight(true);

        table.getSelectionModel().addListSelectionListener(new RowListener());

        table.setRowSelectionAllowed(true);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane jScrollPaneTable = new JScrollPane(table);

        output = new JTextArea(5, 40);

        output.setEditable(false);
        JScrollPane jScrollPaneOutput = new JScrollPane(output);

        jScrollPaneOutput.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("INFO: "),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)));

        outputNote = new JTextPane();
        outputNote.setContentType("text/html");
        outputNote.setEditable(false);
        JScrollPane jScrollPaneOutputNote = new JScrollPane(outputNote);

        jScrollPaneOutputNote.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("NOTE: "),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)));


        JSplitPane splitPaneOut = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                jScrollPaneOutput,
                jScrollPaneOutputNote);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                jScrollPaneTable,
                splitPaneOut);

        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.5);

        JPanel centerPane = new JPanel(new GridLayout(1, 0));

        centerPane.add(splitPane);

        centerPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        add(centerPane, BorderLayout.CENTER);

    }

    //--------------------------------------------------------------------------
    /**
     *
     * write log output
     *
     * @param log - row to write
     * @param colore - color of the new row
     */
    //--------------------------------------------------------------------------
    private void writeOutput(String log, Color colore) {
        output.setForeground(colore);
        output.setText(log);
        //output.setForeground(Color.BLACK);
        output.repaint();
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Set log error
     *
     * @param err - row error to write
     */
    //--------------------------------------------------------------------------
    private void writeError(String err) {
        writeOutput(err, Color.RED);
        viewPageButton.setEnabled(false);
        buttonEditNota.setEnabled(false);
        data = new Object[0][0];
        table.repaint();
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Show index in browser
     *
     */
    //--------------------------------------------------------------------------
    private void viewIndex() {
        if (cp != null) {
            String dirSite = cp.getOutDir();

            Course c = null;
            try {
                c = (new ManageInfoXML()).getInfoCourse(Constants.courseXmlName, new File(cp.getCourseDir()).getPath());

            } catch (Exception ex) {
                writeError("Error index page");
            }
            String home_Corso = c.getCourseHome();

            //File nameIndex = new File(home_Corso+"index.html");
            File nameIndex = new File("index.html");

            File index = new File(dirSite + File.separator + nameIndex);

            if (index.isFile() && index.exists()) {
                try {
                    Desktop.getDesktop().browse(index.toURI());
                } catch (Exception ex) {
                }
            }
        }
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Repaint data table
     *
     * @param outDir - output directory
     * @param courseDir - course directory
     * 
     * @throws java.lang.Exception
     *
     */
    //--------------------------------------------------------------------------
    private void setDataTable(String outDir, String courseDir) throws Exception {

        boolean flag = true;

        if (outDir != null && !outDir.equals("") && courseDir != null && !courseDir.equals("")) {

            File dirO = new File(outDir);
            File dirC = new File(courseDir);

            if (dirO.isDirectory() && dirO.exists() && dirC.isDirectory() && dirC.exists()) {

                Course c = (new ManageInfoXML()).getInfoCourse(Constants.courseXmlName, dirC.getPath());

                String nome_Corso = c.getCourseHome();

                File dirNotes = new File(outDir + File.separator + Constants.dirCoursesWEB +
                        File.separator + nome_Corso + File.separator + Constants.dirNoteWEB);

                if (dirNotes.exists() && dirNotes.isDirectory()) {

                    File lf[] = dirNotes.listFiles();

                    arrayDoc = new Document[lf.length];
                    data = new Object[lf.length][nCol];

                    for (int i = 0; i < lf.length; i++) {

                        jedp = new JTextPane();

                        jedp.setContentType("text/html");
                        // ottengo il documentstyle
                        StyledDocument doc = (StyledDocument) jedp.getDocument();
                        File f = new File(lf[i].getPath());
                        FileReader reader = new FileReader(f);
                        // leggo il file html solo il contenuto
                        javax.swing.text.html.HTMLEditorKit s = new HTMLEditorKit();
                        s.read(reader, doc, 0);

                        jedp.setEditable(true);

                        arrayDoc[i] = jedp.getDocument();
                        Object[] a = {new Integer(i + 1), lf[i].getName().split(".html")[0], nome_Corso, lf[i].getPath()};
                        data[i] = a;
                    }
                    table.repaint();
                    viewPageButton.setEnabled(true);
                    buttonEditNota.setEnabled(true);
                } else {
                    flag = false;
                }
            } else {
                flag = false;
            }
        } else {
            flag = false;
        }
        if (flag) {
            writeError("Please select a valid directory");
        }
    }

    //--------------------------------------------------------------------------
    /**
     *
     */
    //--------------------------------------------------------------------------
    private void outputSelection() {
        selectedRow = table.getSelectedRow();
        writeOutput(
                "TITLE : " + table.getValueAt(table.getSelectedRow(), 1).toString() + "\n" +
                "COURSE NAME : " + table.getValueAt(table.getSelectedRow(), 2).toString() + "\n" +
                "PATH : " + new File(table.getValueAt(table.getSelectedRow(), nCol - 1).toString()).getParentFile().getPath() + "\n" +
                "NOTE : " + File.separator + new File(table.getValueAt(table.getSelectedRow(), 3).toString()).getName(), Color.BLUE);
        if (arrayDoc != null) {
            outputNote.setDocument(arrayDoc[table.getSelectedRow()]);
        }
    }

    //--------------------------------------------------------------------------
    /**
     *
     * actionPerformed
     *
     * @param e - ActionEvent
     */
    //--------------------------------------------------------------------------
    public void actionPerformed(ActionEvent e) {

        if (table.getSize() != new Dimension(0, 0) && table.getSelectedRow() > -1) {

            String titleLecture = table.getValueAt(table.getSelectedRow(), 1).toString();
            String pathLectureNote = table.getValueAt(table.getSelectedRow(), nCol - 1).toString();
            String nomeCorso = table.getValueAt(table.getSelectedRow(), 2).toString();


            if (pathLectureNote != null && !pathLectureNote.equals("")) {
                if (new File(pathLectureNote).isFile() && new File(pathLectureNote).exists()) {
                    try {
                        //jFrameEdit = new Editor(pathLectureNote, titleLecture, nomeCorso + "_index.html");
                        jFrameEdit = new Editor(pathLectureNote, titleLecture,"index.html");
                    } catch (Exception ex) {
                        writeOutput("Error", Color.RED);
                    }
                } else {
                    writeOutput("Bad note dir", Color.RED);
                }
            } else {
                writeOutput("Bad note dir", Color.RED);
            }

        } else {
            writeOutput("You must select one row", Color.RED);
        }

    }

    //--------------------------------------------------------------------------
    /**
     *
     * RowListener
     *
     */
    //--------------------------------------------------------------------------
    private class RowListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            outputSelection();
        }
    }

    //--------------------------------------------------------------------------
    /**
     *
     */
    //--------------------------------------------------------------------------
    class MyTableModel extends AbstractTableModel {

        public String[] columnNames = {
            "ID",
            "Title",
            "Nome Corso",
            "Path"
        };

        //--------------------------------------------------------------------------
        /**
         *
         * @return int - coloumn count
         */
        //--------------------------------------------------------------------------
        public int getColumnCount() {
            return columnNames.length;
        }
        //--------------------------------------------------------------------------

        /**
         *
         * @return int - row count
         */
        //--------------------------------------------------------------------------
        public int getRowCount() {
            return data.length;
        }

        //--------------------------------------------------------------------------
        @Override
        /**
         *
         */
        //--------------------------------------------------------------------------
        public String getColumnName(int col) {
            return columnNames[col];
        }

        //--------------------------------------------------------------------------
        /**
         *
         * @param row
         * @param col
         * @return
         */
        //--------------------------------------------------------------------------
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        //--------------------------------------------------------------------------
        /**
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        //--------------------------------------------------------------------------
        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        //--------------------------------------------------------------------------
        /**
         * Don't need to implement this method unless your table's
         * editable.
         */
        //--------------------------------------------------------------------------
        @Override
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col < 2) {
                return false;
            } else {
                return true;
            }
        }

        //--------------------------------------------------------------------------
        /**
         * Don't need to implement this method unless your table's
         * data can change.
         */
        //--------------------------------------------------------------------------
        @Override
        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }
    }
}
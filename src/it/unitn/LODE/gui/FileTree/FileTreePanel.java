package it.unitn.LODE.gui.FileTree;

import it.unitn.LODE.gui.*;
import it.unitn.LODE.MP.constants.LODEConstants;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.text.Position;


public class FileTreePanel extends JPanel 
{
  public static ImageIcon ICON_HOME = null;
  //public static  ImageIcon ICON_LODE = null;
  public static  ImageIcon ICON_FOLDER = null;
  public static  ImageIcon ICON_COURSE = null;
  public static  ImageIcon ICON_LECTURE =null;
  public static  ImageIcon ICON_EXPANDEDFOLDER = null;
  
  private static final int UNDEFINED=0;
  private static final int SELECT_GENERIC_LECTURE=1;
  private static final int SELECT_LECTURE_IN_A_COURSE=2;
  private static final int SELECT_COURSE=3;
  int mode=UNDEFINED;
  
  int selectedType=0;

  protected JTree  m_tree;
  protected DefaultTreeModel m_model;
  protected JTextField m_display;
  
  public FileTreePanel(String path,int type){
    FileTreePanelBuilder(path);
  }
  private void loadImages(){
      java.net.URL imgURL = FileTreePanel.class.getClassLoader().getResource(LODEConstants.IMGS_PREFIX + "home.gif");
    if (imgURL != null) {
        ICON_HOME=new ImageIcon(imgURL, "Home");
    }
    /*imgURL = FileTreePanel.class.getResource(LODEConstants.IMGS_PREFIX + "LODE.gif");
    if (imgURL != null) {
        ICON_LODE=new ImageIcon(imgURL, "LODE");
    }*/
    /*
    imgURL = FileTreePanel.class.getResource(LODEConstants.IMGS_PREFIX + "button_pause.gif");
    if (imgURL != null) {
        ICON_FOLDER=new ImageIcon(imgURL, "TEST");
    }    
    imgURL = FileTreePanel.class.getResource(LODEConstants.IMGS_PREFIX + "button_rec.gif");
    if (imgURL != null) {
        ICON_EXPANDEDFOLDER=new ImageIcon(imgURL, "TEST");
    } */
    imgURL = FileTreePanel.class.getClassLoader().getResource(LODEConstants.IMGS_PREFIX + "lecture.gif");
    if (imgURL != null) {
        ICON_LECTURE=new ImageIcon(imgURL, "TEST");
    }
    imgURL = FileTreePanel.class.getClassLoader().getResource(LODEConstants.IMGS_PREFIX + "course.gif");
    if (imgURL != null) {
        ICON_COURSE=new ImageIcon(imgURL, "TEST");
    }
  }
  private void FileTreePanelBuilder(String path) {
    loadImages();

    DefaultMutableTreeNode top = new DefaultMutableTreeNode(
      new IconData(LODEConstants.ICON_LODE, null, "LODE"));

    DefaultMutableTreeNode node;
      File[] roots = {new File(path)};
    for (int k=0; k<roots.length; k++) {
        ImageIcon ii=ICON_HOME;
        // se la root è un corso, setta l'icona corridpondentemente 
        File xmlFile=new File(path+File.separator+LODEConstants.SERIALIZED_COURSE);
        if (xmlFile.exists()) ii=ICON_COURSE;
        
        node = new DefaultMutableTreeNode(
            new IconData(ii, 
            null, new LodeFileNode(roots[k])));
        top.add(node);  
        node.add( new DefaultMutableTreeNode(
        new Boolean(true)));
    }

    m_model = new DefaultTreeModel(top);
    m_tree = new JTree(m_model);
    m_tree.setRootVisible(false);
    m_tree.getSelectionModel().setSelectionMode(
      TreeSelectionModel.SINGLE_TREE_SELECTION);
    m_tree.putClientProperty("JTree.lineStyle", "Angled");
    TreeCellRenderer renderer = new IconCellRenderer();
    m_tree.setCellRenderer(renderer);
    m_tree.addTreeExpansionListener(new DirExpansionListener());
    m_tree.addTreeSelectionListener(new DirSelectionListener());
    m_tree.setShowsRootHandles(true); 
    m_tree.setEditable(false);

    this.setLayout(new BorderLayout());
    JScrollPane s = new JScrollPane();
    s.getViewport().add(m_tree);
    add(s, BorderLayout.CENTER);

    m_display = new JTextField();
    m_display.setEditable(false);
    add(m_display, BorderLayout.SOUTH);

  }

  DefaultMutableTreeNode getTreeNode(TreePath path) {
    return (DefaultMutableTreeNode) (path.getLastPathComponent());
  }

  LodeFileNode getLodeFileNode(DefaultMutableTreeNode node) {
    if (node == null)
      return null;
    Object obj = node.getUserObject();
    if (obj instanceof IconData)
      obj = ((IconData)obj).getObject();
    if (obj instanceof LodeFileNode)
      return (LodeFileNode)obj;
    else
      return null;
  }

  // Make sure expansion is threaded and updating the tree model
  // only occurs within the event dispatching thread.
  class DirExpansionListener implements TreeExpansionListener
  {
    public void treeExpanded(TreeExpansionEvent event) {
      final DefaultMutableTreeNode node = getTreeNode(
        event.getPath());
      final LodeFileNode fnode = getLodeFileNode(node);

      Thread runner = new Thread() {
        public void run() {
          if (fnode != null && fnode.expand(node)) {
            Runnable runnable = new Runnable() {
              public void run() {
                m_model.reload(node);
              }
            };
            SwingUtilities.invokeLater(runnable);
          }
        }
      };
      runner.start();
    }

    public void treeCollapsed(TreeExpansionEvent event) {}
  }

  class DirSelectionListener implements TreeSelectionListener
  {
    public void valueChanged(TreeSelectionEvent event) {
      DefaultMutableTreeNode node = getTreeNode(event.getPath());
        LodeFileNode fnode = getLodeFileNode(node);
        selectedType=UNDEFINED;
        if (fnode.isLecture()) selectedType=SELECT_GENERIC_LECTURE;
        if (fnode.isCourse()) selectedType=SELECT_COURSE;
        if (fnode!=null) {
            //System.out.println(mode+" "+fnode);
            switch (mode){
                case SELECT_GENERIC_LECTURE:
                case SELECT_LECTURE_IN_A_COURSE:
                    if (fnode.isLecture())
                      m_display.setText(fnode.getFile().getAbsolutePath());
                    else
                      m_display.setText("SELECTION IS NOT A LECTURE!");
                    break;
                case SELECT_COURSE:
                    if (fnode.isCourse())
                      m_display.setText(fnode.getFile().getAbsolutePath());
                    else
                      m_display.setText("SELECTION IS NOT A COURSE!");
                    break;
                case UNDEFINED:
                      m_display.setText(fnode.getFile().getAbsolutePath());
                      break;
                default:
                      m_display.setText("???");                    
            }
        } else  m_display.setText("");
    }
  }
  
  public static void main(String argv[]) {
      FileTreePanel f=new FileTreePanel("/Users/ronchet/_LODE/COURSES/Hh_2008",0);//LODEConstants.COURSES_HOME,0);
      String s=f.selectLecture();
      f=new FileTreePanel(LODEConstants.COURSES_HOME,0);//LODEConstants.COURSES_HOME,0);
      System.out.println(s);
      s=f.selectCourse();
      System.out.println(s);      
      s=f.selectCourseOrLecture();
      System.out.println(s);
  }
  public String selectLecture(){
      //FileTreePanel f=new FileTreePanel(null,0);
      mode=SELECT_GENERIC_LECTURE;
      m_tree.expandRow(0);
      String s=showWindow("Select Lecture");
      return s;
  }  
  public String selectCourseOrLecture(){
      //FileTreePanel f=new FileTreePanel(null,0);
      expandToLast(m_tree);
      mode=UNDEFINED;
      String s=showWindow("Select Course or Lecture");
      return s;
  }
  public String selectCourse(){
      //FileTreePanel f=new FileTreePanel(null,0);
      m_tree.expandRow(0);
      mode=SELECT_COURSE;
      String s=showWindow("Select Course");
      return s;
  }
  private String showWindow(String title) {
      JPanel pane=new JPanel();
      pane.setSize(400,300);
      pane.setPreferredSize(new Dimension(400,300));
      pane.setLayout(new BorderLayout());
      pane.add(this,BorderLayout.CENTER);
      m_display.setText("");
      int dd=JOptionPane.showOptionDialog(null,pane,title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,LODEConstants.ICON_LODE,null,null);
      if (dd!=JOptionPane.OK_OPTION) return null;
      return(m_display.getText());
  }
  public void collapseAll(JTree tree) {
    int row = tree.getRowCount() - 1;
    while (row >= 0) {
      tree.collapseRow(row);
      row--;
      }
 }
 public void expandAll(JTree tree) {
    int row = 0;
    while (row < tree.getRowCount()) {
      tree.expandRow(row);
      row++;
      }
    }
 public void expandToLast(JTree tree) {
    TreeModel data = tree.getModel();
    Object node = data.getRoot();

    if (node == null) return;

    TreePath p = new TreePath(node);
    while (true) {
         int count = data.getChildCount(node);
         if (count == 0) break;
         node = data.getChild(node, count - 1);
         p = p.pathByAddingChild(node);
    }
    tree.scrollPathToVisible(p);
  }

    // expand to the last leaf from the root
    //DefaultMutableTreeNode  root;
//    root = (DefaultMutableTreeNode) tree.getModel().getRoot();
  //  tree.scrollPathToVisible(new TreePath(root.getLastLeaf().getPath()));
 //}


}

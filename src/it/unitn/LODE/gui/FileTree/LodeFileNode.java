/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unitn.LODE.gui.FileTree;

// =============================================================================

import it.unitn.LODE.MP.constants.LODEConstants;
import java.io.File;
import java.io.FileFilter;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

public class LodeFileNode
{
  protected File m_file;

  public LodeFileNode(File file) { m_file = file; }

  public File getFile() { return m_file; }

  /**
   * this method is essential !
   * @return
   */
    @Override
  public String toString() {
    return m_file.getName().length() > 0 ? m_file.getName() :
      m_file.getPath();
  }
  

  public boolean expand(DefaultMutableTreeNode parent) {
    DefaultMutableTreeNode flag =
      (DefaultMutableTreeNode)parent.getFirstChild();
    if (flag==null)      // No flag
      return false;
    Object obj = flag.getUserObject();
    if (!(obj instanceof Boolean))
      return false;      // Already expanded

    parent.removeAllChildren();  // Remove Flag

    File[] files = listFiles();
    if (files == null)
      return true;

    Vector<LodeFileNode> v = new Vector<LodeFileNode>();

    for (int k=0; k<files.length; k++) {
      File f = files[k];
      if (!(f.isDirectory()))
        continue;

      LodeFileNode newNode = new LodeFileNode(f);
           
      boolean isAdded = false;
      for (int i=0; i<v.size(); i++) {
        LodeFileNode nd = (LodeFileNode)v.elementAt(i);
        if (newNode.compareTo(nd) < 0) {
          v.insertElementAt(newNode, i);
          isAdded = true;
          break;
        }
      }
      if (!isAdded)
      v.addElement(newNode);
    }

    for (int i=0; i<v.size(); i++) {
      LodeFileNode nd = (LodeFileNode)v.elementAt(i);
      IconData idata =null;
      if (nd.isCourse())
        idata = new IconData(FileTreePanel.ICON_COURSE,FileTreePanel.ICON_COURSE, nd);
      else if (nd.isLecture()) 
        idata = new IconData(FileTreePanel.ICON_LECTURE,FileTreePanel.ICON_LECTURE, nd);
      else
        idata = new IconData(FileTreePanel.ICON_FOLDER,FileTreePanel.ICON_EXPANDEDFOLDER, nd);
      DefaultMutableTreeNode node = 
        new DefaultMutableTreeNode(idata);
      parent.add(node);
              
      if (nd.hasSubDirs())
        node.add(new DefaultMutableTreeNode( 
          Boolean.valueOf(true) ));
    }
    return true;
  }

  public boolean hasSubDirs() {
    File[] files = listFiles();
    if (files == null)
      return false;
    for (int k=0; k<files.length; k++) {
      if (files[k].isDirectory())
        return true;
    }
    return false;
  }
    
  public int compareTo(LodeFileNode toCompare) { 
    return  m_file.getName().compareToIgnoreCase(
      toCompare.m_file.getName() ); 
  }


    protected File[] listFiles() {
    if (!m_file.isDirectory())
      return null;
    try {
        // CHECK IF THIS IS THE COURSE HOME - IF SO ONLY LIST COURSES
        if (m_file.getAbsolutePath().equalsIgnoreCase(LODEConstants.COURSES_HOME)) return m_file.listFiles(
                new FileFilter(){
                    public boolean accept(File arg0) {
                        File xmlFile=new File(arg0.getAbsolutePath()+File.separator+LODEConstants.SERIALIZED_COURSE);
                        return (xmlFile.exists()); // true only for course directories
                    }
            });
        // CHECK IF THIS IS A COURSE - IF SO ONLY LIST LECTURES iN THE ACQUISITION BRANCH
        if (isCourse()) { // this is a course directory 
            File acquisitionDir=new File(m_file.getAbsolutePath()+LODEConstants.ACQUISITION_SUBDIR);
            if (acquisitionDir.exists()) return acquisitionDir.listFiles(
                new FileFilter(){
                    public boolean accept(File arg0) {
                        File xmlFile=new File(arg0.getAbsolutePath()+File.separator+LODEConstants.SERIALIZED_LECTURE);
                        return (xmlFile.exists()); // true only for course directories
                    }
            });
            return null; //
        }
        // IN ALL OTHER CASES, DO NOT LIST ANY CONTENT OF THE DIRECTORY
        return null;
    }
    catch (Exception ex) {
      JOptionPane.showMessageDialog(null, 
        "Error reading directory "+m_file.getAbsolutePath(),
        "Warning", JOptionPane.WARNING_MESSAGE);
          return null;
    }
  }

    boolean isCourse(){
        File xmlFile=new File(m_file.getAbsolutePath()+File.separator+LODEConstants.SERIALIZED_COURSE);
        return xmlFile.exists();
    }
    boolean isLecture(){
        File xmlFile=new File(m_file.getAbsolutePath()+File.separator+LODEConstants.SERIALIZED_LECTURE);
        return xmlFile.exists();
    }
  }

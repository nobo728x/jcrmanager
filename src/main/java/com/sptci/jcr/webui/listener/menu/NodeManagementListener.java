package com.sptci.jcr.webui.listener.menu;

import static com.sptci.jcr.webui.MainController.getController;
import com.sptci.jcr.webui.tree.JCRNode;
import com.sptci.jcr.webui.tree.JCRTree;

import nextapp.echo.extras.app.tree.TreePath;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * A base listener for events related to node management (CRUD).
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-13
 * @version $Id: NodeManagementListener.java 18 2009-08-22 11:46:13Z spt $
 */
public abstract class NodeManagementListener extends AbstractSessionListener
{
  /** @return The path of the currently selected node in the content area. */
  protected String getPath()
  {
    final JCRTree tree = getController().getContentArea().getTree();
    String path = "";

    final TreePath treePath = tree.getSelectionModel().getSelectionPath();
    if ( treePath == null ) return path;

    final Object object = treePath.getLastPathComponent();

    if ( ( object instanceof JCRNode ) )
    {
      final JCRNode treeNode = (JCRNode) object;

      try
      {
        path = treeNode.getUserObject().getPath();
      }
      catch ( RepositoryException e )
      {
        getController().displayException( e );
      }
    }

    return path;
  }

  /** @return The Node that is currently selected in the repository tree. */
  protected Node getNode()
  {
    final JCRTree tree = getController().getContentArea().getTree();
    Node node = tree.getRoot().getUserObject();

    final TreePath treePath = tree.getSelectionModel().getSelectionPath();
    if ( treePath == null ) return node;

    final Object object = treePath.getLastPathComponent();

    if ( ( object instanceof JCRNode ) )
    {
      node = ( (JCRNode) object ).getUserObject();
    }

    return node;
  }
}

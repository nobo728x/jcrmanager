package com.sptci.jcr.webui.tree;

import com.sptci.echo.tree.Tree;
import com.sptci.jcr.webui.listener.JCRNodeListener;

/**
 * A tree used to display all the nodes in the JCR.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_top'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-06-01
 * @version $Id: JCRTree.java 44 2010-01-12 21:01:38Z spt $
 */
public class JCRTree extends Tree
{
  private static final long serialVersionUID = 1L;

  public JCRTree()
  {
    super( new RepositoryTreeModel() );
    addActionListener( new JCRNodeListener() );
    setSelectionEnabled( true );
    setCellRenderer( new NodeCellRenderer() );
  }

  @Override
  public RepositoryTreeModel getModel()
  {
    return (RepositoryTreeModel) super.getModel();
  }

  public JCRNode getRoot()
  {
    return getModel().getRoot();
  }
}

package com.sptci.jcr.webui.tree;

import com.sptci.echo.tree.Tree;
import com.sptci.jcr.webui.listener.ScriptSelectionListener;

/**
 * A tree that displays the groovy scripts stored in the system.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-01-12
 * @version $Id: ScriptTree.java 44 2010-01-12 21:01:38Z spt $
 */
public class ScriptTree extends Tree
{
  private static final long serialVersionUID = 1L;

  public ScriptTree()
  {
    super( new ScriptTreeModel() );
    addActionListener( new ScriptSelectionListener() );
    setSelectionEnabled( true );
    setCellRenderer( new ScriptCellRenderer() );
  }

  @Override
  public ScriptTreeModel getModel()
  {
    return (ScriptTreeModel) super.getModel();
  }

  public ScriptNode getRoot()
  {
    return getModel().getRoot();
  }
}

package com.sptci.jcr.webui.table;

import com.sptci.echo.table.Table;
import com.sptci.jcr.webui.listener.NodeSelectionListener;

import javax.jcr.Node;

/**
 * A table used to display all the nodes in a node iterator.  The nodes
 * displayed are double-clickable which displays a {@link com.sptci.jcr.webui.NodeWindow}
 * with the details of the selected node.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-13
 * @version $Id: NodesTable.java 24 2009-08-31 14:32:29Z spt $
 */
public class NodesTable extends Table<Node>
{
  private static final long serialVersionUID = 1L;

  public NodesTable( final NodesTableModel model )
  {
    super( model );
    addSelectionListener( new NodeSelectionListener() );
  }

  @Override
  public NodesTableModel getModel()
  {
    return (NodesTableModel) super.getModel();
  }
}

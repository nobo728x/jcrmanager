package com.sptci.jcr.webui;

import com.sptci.jcr.webui.table.NodesTable;

/**
 * An interface that is implemented by view components that display a
 * table of nodes.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-09-20
 * @version $Id: TableView.java 33 2009-09-21 14:28:28Z spt $
 */
public interface TableView
{
  NodesTable getTable();
}

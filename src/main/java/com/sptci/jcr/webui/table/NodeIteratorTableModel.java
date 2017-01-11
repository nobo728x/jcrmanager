package com.sptci.jcr.webui.table;

import static java.lang.Integer.MAX_VALUE;

import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

/**
 * A table model that displays the nodes contained in a pre-populated
 * node iterator.
 *
 * <p>&copy; Copyright 2010 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-02-18
 * @version $Id: NodeIteratorTableModel.java 66 2010-02-22 15:24:18Z spt $
 */
public class NodeIteratorTableModel extends NodesTableModel
{
  private static final long serialVersionUID = 1L;

  @SuppressWarnings( { "TransientFieldNotInitialized" } )
  private final transient NodeIterator iterator;

  public NodeIteratorTableModel( final NodeIterator iterator )
  {
    pageSize = MAX_VALUE;
    this.iterator = iterator;
  }

  @Override
  protected NodeIterator getNodes() throws RepositoryException
  {
    return iterator;
  }
}

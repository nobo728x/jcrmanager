package com.sptci.jcr.webui.table;

import javax.jcr.Node;
import javax.jcr.NodeIterator;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A simple list backed node iterator implementation.
 *
 * <p>&copy; Copyright 2010 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-02-19
 * @version $Id: NodeIteratorImpl.java 66 2010-02-22 15:24:18Z spt $
 */
public class NodeIteratorImpl implements NodeIterator
{
  private final ArrayList<Node> nodes = new ArrayList<Node>();

  private int index;

  public NodeIteratorImpl( final Collection<Node> nodes )
  {
    this.nodes.addAll( nodes );
  }

  public Node nextNode()
  {
    return nodes.get( index++ );
  }

  public void skip( final long position )
  {
    index = (int) position;
  }

  public long getSize()
  {
    return nodes.size();
  }

  public long getPosition()
  {
    return index;
  }

  public boolean hasNext()
  {
    System.out.format( "Checking availability at index: %d status: %s size: %d%n", index, nodes.size() > index, nodes.size() );
    return nodes.size() > index;
  }

  public Object next()
  {
    return nextNode();
  }

  public void remove()
  {
    nodes.remove( index );
  }
}
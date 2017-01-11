package com.sptci.jcr.webui.tree;

import static com.sptci.jcr.webui.MainController.getController;

import javax.jcr.Node;
import javax.jcr.NodeIterator;

/**
 * A tree node that represents a range of nodes within the specified iterator.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_top'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh 2009-06-03
 * @version $Id: RangeNode.java 81 2013-04-22 18:52:51Z spt $
 */
public class RangeNode extends AbstractNode<Node>
{
  private static final long serialVersionUID = 1L;

  /** The start index of the position in the iterator. */
  private final long start;

  /** The end index of the position in the iterator. */
  private final long end;

  /**
   * Create a new node that displays child nodes that represent the nodes
   * within the specified range in the iterator.
   *
   * @param node The node for which paginated ranges are being displayed.
   * @param start The ending position within the iterator.
   * @param end The beginning position within the iterator.
   */
  public RangeNode( final Node node, final long start,
      final long end )
  {
    super( node );
    this.start = start;
    this.end = end;
  }

  /** {@inheritDoc} */
  @Override
  void createChildren()
  {
    initialised = true;
    final long size = end - start;

    try
    {
      if ( size > LIMIT )
      {
        paginate( getUserObject(), start, end );
      }
      else
      {
        final NodeIterator iter = getUserObject().getNodes();
        if ( start > 0 ) iter.skip( start );

        for ( long i = start; i < end; ++i )
        {
          add( new JCRNode( iter.nextNode() ) );
        }
      }
    }
    catch ( Throwable t )
    {
      getController().displayException( t );
    }
  }

  @Override
  public String toString()
  {
    return start + " - " + end;
  }
}

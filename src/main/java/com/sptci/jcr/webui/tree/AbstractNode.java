package com.sptci.jcr.webui.tree;

import com.sptci.echo.tree.TreeNode;

import static com.sptci.echo.Dimensions.getInt;
import static com.sptci.jcr.webui.MainController.getController;
import static java.lang.Integer.parseInt;
import static java.lang.Math.min;
import static java.lang.String.format;
import static java.lang.System.getProperty;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * An abstract node that represents nodes displayed in the tree.  Supports
 * both node based nodes and node iterator based nodes.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_top'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-06-03
 * @version $Id: AbstractNode.java 81 2013-04-22 18:52:51Z spt $
 */
public abstract class AbstractNode<M> extends TreeNode<M>
{
  private static final long serialVersionUID = 1L;

  /** The maximum number of nodes to display without pagination. */
  static final int LIMIT;

  static
  {
    final String value = getProperty( format( "%s.pageSize", AbstractNode.class.getName() ) );
    LIMIT = ( value == null ) ? getInt( AbstractNode.class, "limit" ) : parseInt( value );
  }

  /** {@inheritDoc} */
  public AbstractNode( final M model )
  {
    super( model );
  }

  /** {@inheritDoc} */
  public AbstractNode( final M model, final boolean allowsChildren )
  {
    super( model, allowsChildren );
  }

  /** Over-ridden to lazily load children and return count. */
  @Override
  public int getChildCount()
  {
    if ( ! initialised ) createChildren();
    return super.getChildCount();
  }

  public void add( final JCRNode node )
  {
    super.add( node );
    getController().getContentArea().getTree().getModel().addNode( node );
  }

  /** Create the child nodes of this node. */
  abstract void createChildren();

  /**
   * Create the child range nodes.
   *
   * @param node The node whose child nodes are to be paginated.
   * @param positions The optional start and end positions in the iterator
   *   in that order.
   * @throws RepositoryException If errors are encountered while using nodes.
   */
  @SuppressWarnings( { "ConstantConditions" } )
  void paginate( final Node node, final long... positions )
      throws RepositoryException
  {
    final long resultSize = node.getNodes().getSize();
    long size = resultSize;
    long start = 0;
    long end = size;

    if ( positions.length > 0 )
    {
      start = positions[0];
      if ( positions.length > 1 ) end = positions[1];
      size = end - start;
    }

    final int levels = numberOfLevels( size );
    final long cut = size / levels;

    for ( int i = 1; i <= levels; ++i )
    {
      if ( i > 1 ) start += cut;
      end = min( start + cut, resultSize );

      if ( i == levels )
      {
        end = min( end + ( size % levels ), resultSize );
      }

      if ( start >= end )
      {
        break;
      }

      add( new RangeNode( node, start, end ) );
    }
  }

  /**
   * Compute the number of levels into which the specified size should be
   * sub-divided.
   *
   * @param size The size that is to be sub-divided.
   * @return The number of sub-divisions.
   */
  int numberOfLevels( final double size )
  {
    int count = 1;
    double value = size;

    while ( value > LIMIT )
    {
      value /= LIMIT;
      ++count;
    }

    return count;
  }

  @Override
  @SuppressWarnings( {"unchecked"} )
  public AbstractNode<M> getParent()
  {
    return (AbstractNode<M>) super.getParent();
  }
}

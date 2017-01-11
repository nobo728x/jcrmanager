package com.sptci.jcr.webui.tree;

import static com.sptci.jcr.webui.MainController.getController;
import static java.lang.String.format;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

/**
 * A tree node that represents a node in a JCR.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_top'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh 2009-06-02
 * @version $Id: JCRNode.java 63 2010-02-16 15:23:00Z spt $
 */
public class JCRNode extends AbstractNode<Node>
{
  private static final long serialVersionUID = 1L;

  /** {@inheritDoc} */
  public JCRNode( final Node node )
  {
    super( node );
  }

  /** {@inheritDoc} */
  public JCRNode( final Node node, final boolean allowsChildren )
  {
    super( node, allowsChildren );
  }

  /** @return The path of the node. */
  @Override
  public String toString()
  {
    try
    {
      if ( getUserObject().getPath().length() == 1 )
      {
        return format( "%s (Workspace: {%s})", getUserObject().getPath(),
            getUserObject().getSession().getWorkspace().getName() );
      }
      
      return getUserObject().getName();
    }
    catch ( RepositoryException e )
    {
      getController().displayException( e );
    }

    return super.toString();
  }

  /** Lazily load all the child nodes of this node. */
  @Override
  void createChildren()
  {
    initialised = true;

    try
    {
      final NodeIterator iter = getUserObject().getNodes();

      if ( iter.getSize() > LIMIT )
      {
        paginate( getUserObject() );
      }
      else
      {
        while ( iter.hasNext() )
        {
          add( new JCRNode( iter.nextNode() ) );
        }
      }
    }
    catch ( RepositoryException e )
    {
      getController().displayException( e );
    }
  }

  @Override
  public JCRNode getRoot()
  {
    return (JCRNode) super.getRoot();
  }
}

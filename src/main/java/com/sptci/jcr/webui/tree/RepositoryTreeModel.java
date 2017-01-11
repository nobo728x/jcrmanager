package com.sptci.jcr.webui.tree;

import static com.sptci.jcr.webui.MainController.getController;

import echopoint.tree.DefaultTreeModel;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.HashMap;
import java.util.Map;

/**
 * A custom tree model for representing the repository.  Maintains a
 * node to tree node mapping to help find the tree node corresponding to
 * a node to help find tree nodes after they have been paginated for UI
 * efficiency.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh 2009-08-15
 * @version $Id: RepositoryTreeModel.java 10 2009-08-15 23:08:37Z spt $
 */
public class RepositoryTreeModel extends DefaultTreeModel
{
  private static final long serialVersionUID = 1l;

  @SuppressWarnings( { "TransientFieldNotInitialized" } )
  private transient Map<String,JCRNode> map = new HashMap<String,JCRNode>();

  public RepositoryTreeModel()
  {
    super( new JCRNode( getController().getRoot() ) );
    addToMap( getRoot() );
  }

  public void addNode( final JCRNode node )
  {
    addToMap( node );
  }

  public void addChild( final AbstractNode<Node> parent,
      final AbstractNode<Node> child )
  {
    super.addChild( parent, child );

    if ( child instanceof JCRNode )
    {
      JCRNode node = (JCRNode) child;
      addToMap( node );
    }
  }

  public void removeChild( final AbstractNode<Node> parent,
      final AbstractNode<Node> child, final String childPath )
  {
    super.removeChild( parent, child );
    map.remove( childPath );
  }

  public AbstractNode<Node> getParent( final Node node )
  {
    AbstractNode<Node> parent = null;

    try
    {
      final String path = node.getPath();

      if ( map.containsKey( path ) )
      {
        parent = map.get( path ).getParent();
      }
    }
    catch ( RepositoryException e )
    {
      getController().displayException( e );
    }

    return parent;
  }

  private void addToMap( final JCRNode node )
  {
    try
    {
      map.put( node.getUserObject().getPath(), node );
    }
    catch ( RepositoryException e )
    {
      getController().displayException( e );
    }
  }

  public JCRNode getNode( final Node node )
  {
    JCRNode jcrnode = null;

    try
    {
      jcrnode = map.get( node.getPath() );
    }
    catch ( RepositoryException e )
    {
      getController().displayException( e );
    }

    return jcrnode;
  }

  @Override
  public JCRNode getRoot()
  {
    return (JCRNode) super.getRoot();
  }
}

package com.sptci.jcr.webui.listener;

import static com.sptci.echo.Configuration.getString;
import com.sptci.echo.ErrorPane;
import com.sptci.echo.Listener;
import com.sptci.echo.View;
import static com.sptci.jcr.webui.MainController.getController;
import com.sptci.jcr.webui.tree.AbstractNode;
import com.sptci.jcr.webui.tree.JCRNode;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-13
 * @version $Id: NodeManagementListener.java 33 2009-09-21 14:28:28Z spt $
 */
public abstract class NodeManagementListener<D extends View> extends Listener<D>
{
  protected Node getNode( String path ) throws RepositoryException
  {
    return getController().getNode( path );
  }

  protected JCRNode getNode( final Node node )
  {
    return getController().getContentArea().getTree().getModel().getNode( node );
  }

  protected AbstractNode<Node> getParent( final Node node )
  {
    AbstractNode<Node> parent =
        getController().getContentArea().getTree().getModel().getParent( node );

    return ( parent != null ) ? parent :
        getController().getContentArea().getTree().getRoot();
  }

  protected void addToTree( final Node node ) throws RepositoryException
  {
    final Node parentNode = node.getParent();
    final JCRNode parent = getNode( parentNode );

    if ( parent != null )
    {
      getController().getContentArea().getTree().getModel().addChild(
          parent, new JCRNode( node, true ) );
    }
    else
    {
      final ErrorPane pane = new ErrorPane(
          getString( this, "error.title" ),
          getString( this, "error.message" ) );
      getApplication().addPane( pane );
    }
  }

  protected void removeFromTree( final JCRNode child, final String childPath,
      final AbstractNode<Node> parent, final Node parentNode )
      throws RepositoryException
  {
    if ( parent.getUserObject().getPath().contains( parentNode.getPath() ) )
    {
      getController().getContentArea().getTree().getModel().removeChild(
          parent, child, childPath );
    }
    else
    {
      final ErrorPane pane = new ErrorPane(
          getString( this, "error.title" ),
          getString( this, "error.message" ) );
      getApplication().addPane( pane );
    }
  }
}

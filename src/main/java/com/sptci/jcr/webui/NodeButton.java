package com.sptci.jcr.webui;

import static com.sptci.jcr.webui.MainController.getController;

import nextapp.echo.app.Button;
import nextapp.echo.app.event.ActionListener;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * A button used to represent a node.  Typically used to display a node that
 * will open a {@link NodeView} with the full node details.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-21
 * @version $Id: NodeButton.java 24 2009-08-31 14:32:29Z spt $
 */
public class NodeButton extends Button
{
  private static final long serialVersionUID = 1L;

  @SuppressWarnings( { "TransientFieldNotInitialized" } )
  private transient final Node node;

  public NodeButton( final Node node )
  {
    this.node = node;
    setStyleName( "Link.Button" );

    try
    {
      setText( node.getPath() );
    }
    catch ( RepositoryException e )
    {
      getController().displayException( e );
    }
  }

  public NodeButton( final Node node, final ActionListener listener )
  {
    this( node );
    addActionListener( listener );
  }

  public Node getNode() { return node; }
}

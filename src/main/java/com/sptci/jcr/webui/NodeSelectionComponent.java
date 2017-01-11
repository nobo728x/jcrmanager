package com.sptci.jcr.webui;

import com.sptci.echo.View;
import com.sptci.echo.annotation.ActionListener;
import com.sptci.echo.annotation.Constraints;
import com.sptci.echo.binding.ViewInitialiser;
import static com.sptci.jcr.webui.MainController.getController;

import nextapp.echo.app.Button;
import nextapp.echo.app.Row;
import nextapp.echo.app.TextField;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * A view that displays a text field to hold the path to a node and a
 * control to select a node using a {@link NodeSearchWindow}.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-21
 * @version $Id: NodeSelectionComponent.java 18 2009-08-22 11:46:13Z spt $
 */
public class NodeSelectionComponent extends Row implements SetNodeView, View
{
  private static final long serialVersionUID = 1L;

  protected transient Node node;

  @Constraints( value = Constraints.Value.NOT_NULL )
  private TextField path;

  @ActionListener( value = "com.sptci.jcr.webui.listener.NodeSearchWindowListener" )
  private Button select;

  public NodeSelectionComponent()
  {
    new ViewInitialiser<NodeSelectionComponent>( this ).init();
  }

  public NodeSelectionComponent( final Node node )
  {
    this();
    setNode( node );
  }

  @Override
  public void init()
  {
    removeAll();
    super.init();

    add( path );
    add( select );
  }

  public void setNode( final Node node )
  {
    this.node = node;

    try
    {
      path.setText( node.getPath() );
    }
    catch ( RepositoryException e )
    {
      getController().displayException( e );
    }
  }

  public Node getNode() { return node; }

  public String getPath() { return path.getText(); }
}

package com.sptci.jcr.webui;

import com.sptci.echo.WindowPane;
import com.sptci.echo.annotation.ActionListener;
import com.sptci.echo.annotation.Constraints;
import com.sptci.echo.binding.ViewInitialiser;

import nextapp.echo.app.Button;
import nextapp.echo.app.Label;
import nextapp.echo.app.Row;
import nextapp.echo.app.SplitPane;

import static com.sptci.jcr.webui.MainController.getController;
import static nextapp.echo.app.SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP;

import javax.jcr.Node;

/**
 * A dialogue used to select the destination node under which a specified
 * node is to be moved.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-25
 * @version $Id: NodeMoveDialog.java 47 2010-01-21 20:38:45Z spt $
 */
public class NodeMoveDialog<V extends TableView> extends WindowPane
{
  private static final long serialVersionUID = 1L;

  @SuppressWarnings( { "TransientFieldNotInitialized" } )
  private transient final Node node;

  private Label pathLabel;
  @Constraints( value = Constraints.Value.NOT_NULL )
  private NodeSelectionComponent path;

  @ActionListener( value = "com.sptci.jcr.webui.listener.NodeMoveListener" )
  private Button move;

  /**
   * An optional view for when the dialog is launched from a
   * query/search results view.
   */
  @SuppressWarnings( { "NonSerializableFieldInSerializableClass" } )
  private V view;

  public NodeMoveDialog( final Node node )
  {
    new ViewInitialiser<NodeMoveDialog>( this ).init();
    this.node = node;
    path = new NodeSelectionComponent( getController().getRoot() );
  }

  /** Layout the view when the pane is added to the hierarchy. */
  @Override
  public void init()
  {
    removeAll();
    super.init();

    final SplitPane pane = new SplitPane( ORIENTATION_VERTICAL_BOTTOM_TOP );
    pane.setAutoPositioned( true );

    createButtons( pane );
    createRow( pane );

    add( pane );
  }

  private void createRow( final SplitPane pane )
  {
    final Row grid = new Row();

    grid.add( pathLabel );
    grid.add( path );

    pane.add( grid );
  }

  private void createButtons( final SplitPane pane )
  {
    final Row row = new Row();
    row.add( move );
    pane.add( row );
  }

  public String getPath() { return path.getPath(); }

  public Node getNode() { return node; }

  public V getView()
  {
    return view;
  }

  public void setView( final V view )
  {
    this.view = view;
  }
}

package com.sptci.jcr.webui;

import com.sptci.echo.WindowPane;
import com.sptci.echo.annotation.ActionListener;
import com.sptci.echo.annotation.Constraints;
import com.sptci.echo.binding.ViewInitialiser;

import nextapp.echo.app.Button;
import nextapp.echo.app.Label;
import nextapp.echo.app.Row;
import nextapp.echo.app.SplitPane;

import static com.sptci.echo.Application.getApplication;
import static nextapp.echo.app.SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP;

import javax.jcr.Node;

/**
 * A dialogue used to specify a single node that is to be deleted. <p/>
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-13
 * @version $Id: NodeDeleteDialog.java 59 2010-02-09 18:07:17Z spt $
 */
public class NodeDeleteDialog<V extends TableView> extends WindowPane
{
  private static final long serialVersionUID = 1L;

  private Label pathLabel;
  @Constraints( value = Constraints.Value.NOT_NULL )
  private final NodeSelectionComponent path;

  @ActionListener( value = "com.sptci.jcr.webui.listener.NodeDeleteListener" )
  private Button delete;

  /**
   * An optional view for when the dialog is launched from a
   * query/search results view.
   */
  @SuppressWarnings( { "NonSerializableFieldInSerializableClass" } )
  private V view;

  public NodeDeleteDialog( final Node node )
  {
    new ViewInitialiser<NodeDeleteDialog>( this ).init();
    path = new NodeSelectionComponent( node );
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

    getApplication().setFocusedComponent( path );
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
    row.add( delete );
    pane.add( row );
  }

  public String getPath() { return path.getPath(); }

  public V getView()
  {
    return view;
  }

  public void setView( final V view )
  {
    this.view = view;
  }
}

package com.sptci.jcr.webui.listener;

import static com.sptci.echo.Application.getApplication;
import com.sptci.echo.table.EventObject;
import com.sptci.echo.table.SelectionListener;
import com.sptci.echo.table.Table;
import com.sptci.jcr.webui.NodeSearchWindow;
import com.sptci.jcr.webui.NodeViewController;
import com.sptci.jcr.webui.NodeWindow;

import nextapp.echo.app.Component;
import nextapp.echo.app.WindowPane;

import javax.jcr.Node;
import static java.lang.System.currentTimeMillis;

/**
 * A selection listener for the tables that display nodes.  Operates as a double-click
 * listener, in that only double-clicking (simulated) causes the action event to trigger.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh 2009-08-12
 * @version $Id: NodeSelectionListener.java 18 2009-08-22 11:46:13Z spt $
 */
public class NodeSelectionListener implements SelectionListener<Node, Table<Node>>
{
  private static final long serialVersionUID = 1l;

  /** The node that was last selected. */
  @SuppressWarnings( { "NonSerializableFieldInSerializableClass" } )
  private Node selected;

  /** The time at which a node was last selected. */
  private long time = currentTimeMillis();

  /** {@inheritDoc} */
  public void rowSelected( final EventObject<Node, Table<Node>> event )
  {
    final long current = currentTimeMillis();

    if ( event.getData().equals( selected ) && ( current - time ) < 500 )
    {
      handleDoubleClick( event );
    }

    selected = event.getData();
    time = currentTimeMillis();
  }

  private void handleDoubleClick( final EventObject<Node, Table<Node>> event )
  {
    final WindowPane pane = getWindowPane( event.getSource() );

    if ( pane instanceof NodeSearchWindow )
    {
      final NodeSearchWindow window = (NodeSearchWindow) pane;
      window.getView().setNode( event.getData() );
      window.userClose();
    }
    else
    {
      final NodeViewController controller = new NodeViewController( event.getData() );
      getApplication().addPane( new NodeWindow( controller.getView() ) );
    }
  }

  private WindowPane getWindowPane( final Component component )
  {
    Component parent = component.getParent();

    while ( parent != null )
    {
      if ( parent instanceof WindowPane ) break;
      parent = parent.getParent();
    }

    return (WindowPane) parent;
  }
}

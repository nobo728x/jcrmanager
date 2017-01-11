package com.sptci.jcr.webui.listener.menu;

import com.sptci.echo.Listener;
import com.sptci.jcr.webui.ContentArea;
import com.sptci.jcr.webui.NodeCreateDialog;
import com.sptci.jcr.webui.NodeDeleteDialog;
import com.sptci.jcr.webui.NodeImportDialog;
import com.sptci.jcr.webui.NodeMixinTypesDialog;
import com.sptci.jcr.webui.NodeMoveDialog;
import com.sptci.jcr.webui.NodeRenameDialog;
import com.sptci.jcr.webui.tree.NodeContextMenuModel;

import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.extras.app.ContextMenu;

import static com.sptci.jcr.webui.tree.NodeCellRenderer.ADD;
import static com.sptci.jcr.webui.tree.NodeCellRenderer.DELETE;
import static com.sptci.jcr.webui.tree.NodeCellRenderer.EDIT;
import static com.sptci.jcr.webui.tree.NodeCellRenderer.EXPORT;
import static com.sptci.jcr.webui.tree.NodeCellRenderer.IMPORT;
import static com.sptci.jcr.webui.tree.NodeCellRenderer.MOVE;
import static com.sptci.jcr.webui.tree.NodeCellRenderer.RENAME;
import static java.lang.Integer.parseInt;

import javax.jcr.Node;

/**
 * The event listener for events fired from the context menu associated
 * with nodes displayed in the {@link com.sptci.jcr.webui.tree.JCRTree}.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-23
 * @version $Id: NodeContextMenuListener.java 47 2010-01-21 20:38:45Z spt $
 */
public class NodeContextMenuListener extends Listener<ContentArea>
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    ContextMenu menu = (ContextMenu) event.getSource();

    final int index = parseInt( event.getActionCommand() );
    final NodeContextMenuModel model =
        (NodeContextMenuModel) menu.getModel().getItem( index );

    display( event.getActionCommand(), model.getNode() );
  }

  private void display( final String command, final Node node )
  {
    try
    {
      if ( ADD.equals( command ) )
      {
        getApplication().addPane( new NodeCreateDialog( node ) );
      }
      if ( EDIT.equals( command ) )
      {
        getApplication().addPane( new NodeMixinTypesDialog( node ) );
      }
      else if ( DELETE.equals( command ) )
      {
        getApplication().addPane( new NodeDeleteDialog( node ) );
      }
      else if ( EXPORT.equals( command ) )
      {
        new NodeExportListener().export( node );
      }
      else if ( IMPORT.equals( command ) )
      {
        getApplication().addPane( new NodeImportDialog( node ) );
      }
      else if ( MOVE.equals( command ) )
      {
        getApplication().addPane( new NodeMoveDialog( node ) );
      }
      else if ( RENAME.equals( command ) )
      {
        getApplication().addPane( new NodeRenameDialog( node ) );
      }
    }
    catch ( Throwable t )
    {
      getApplication().processFatalException( t );
    }
  }
}

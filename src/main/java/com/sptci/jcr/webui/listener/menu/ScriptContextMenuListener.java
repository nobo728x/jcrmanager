package com.sptci.jcr.webui.listener.menu;

import com.sptci.echo.Confirmation;
import com.sptci.echo.ErrorPane;
import com.sptci.echo.Executor;
import com.sptci.echo.Listener;
import com.sptci.io.FileUtilities;
import com.sptci.jcr.webui.ScriptAddDialog;
import com.sptci.jcr.webui.ScriptDirectoryDialog;
import com.sptci.jcr.webui.ScriptManagementWindow;
import com.sptci.jcr.webui.ScriptRenameDialog;
import com.sptci.jcr.webui.tree.ScriptContextMenuModel;
import com.sptci.jcr.webui.tree.ScriptNode;
import com.sptci.jcr.webui.tree.ScriptTreeModel;

import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.extras.app.ContextMenu;

import static com.sptci.echo.Configuration.getString;
import static com.sptci.jcr.webui.MainController.getController;
import static com.sptci.jcr.webui.tree.ScriptCellRenderer.ADD_DIRECTORY;
import static com.sptci.jcr.webui.tree.ScriptCellRenderer.ADD_SCRIPT;
import static com.sptci.jcr.webui.tree.ScriptCellRenderer.DELETE;
import static com.sptci.jcr.webui.tree.ScriptCellRenderer.RENAME;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;

import java.io.File;

/**
 * The context menu listener for {@link com.sptci.jcr.webui.tree.ScriptNode}
 * instances.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-01-11
 * @version $Id: ScriptContextMenuListener.java 44 2010-01-12 21:01:38Z spt $
 */
public class ScriptContextMenuListener extends Listener<ScriptManagementWindow>
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    ContextMenu menu = (ContextMenu) event.getSource();
    final ScriptManagementWindow window = getView( event );

    final int index = parseInt( event.getActionCommand() );
    final ScriptContextMenuModel model =
        (ScriptContextMenuModel) menu.getModel().getItem( index );

    display( window, event.getActionCommand(), model.getFile() );
  }

  private void display( final ScriptManagementWindow window,
      final String command, final File file )
  {
    if ( ADD_SCRIPT.equals( command ) )
    {
      getApplication().addPane( new ScriptAddDialog( window, file ) );
    }
    else if ( ADD_DIRECTORY.equals( command ) )
    {
      getApplication().addPane( new ScriptDirectoryDialog( window, file ) );
    }
    else if ( RENAME.equals( command ) )
    {
      getApplication().addPane( new ScriptRenameDialog( window, file ) );
    }
    else if ( DELETE.equals( command ) )
    {
      final Executor<ScriptContextMenuListener> executor =
          new Executor<ScriptContextMenuListener>( this, "delete" );
      executor.addParameter( ScriptManagementWindow.class, window );
      executor.addParameter( File.class, file );

      final Confirmation confirmation = new Confirmation(
          getString( window, "delete.title" ),
          format( getString( window, "delete.message" ), file.getName() ),
          executor );
      getApplication().addPane( confirmation );
    }
  }

  public void delete( final ScriptManagementWindow window, final File file )
  {
    final ScriptTreeModel model = window.getTree().getModel();
    if ( FileUtilities.delete( file, true ) > 0 )
    {
      final ScriptNode parent = model.getNode( file.getParentFile() );
      final ScriptNode child = model.getNode( file );

      model.removeChild( parent, child, file.getAbsolutePath() );
      getController().getContentArea().resetMenu();
    }
    else
    {
      final ErrorPane pane = new ErrorPane(
          getString( window, "error.title" ),
          format( getString( window, "error.message" ), file.getAbsolutePath() ) );
      getApplication().addPane( pane );
    }
  }
}

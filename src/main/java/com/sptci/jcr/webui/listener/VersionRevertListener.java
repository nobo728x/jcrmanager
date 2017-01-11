package com.sptci.jcr.webui.listener;

import com.sptci.jcr.webui.VersionsView;
import com.sptci.jcr.webui.table.VersionsTable;
import com.sptci.jcr.webui.table.VersionsTableModel;

import nextapp.echo.app.event.ActionEvent;

import static com.sptci.jcr.webui.MainController.getController;

import javax.jcr.RepositoryException;
import javax.jcr.version.Version;

/**
 * The event listener for reverting a node to its a selected previous
 * version.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-30
 * @version $Id$
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class VersionRevertListener extends VersionManagementListener
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    final VersionsView view = getView( event );
    if ( ! checkView( view ) ) return;

    try
    {
      revert( view );
      updateView( view );
    }
    catch ( RepositoryException e )
    {
      processFatalException( e );
    }
  }

  private void revert( final VersionsView view ) throws RepositoryException
  {
    final VersionsTable table = (VersionsTable) view.getVersions().getTable();
    final VersionsTableModel model = table.getModel();
    
    if ( table.getSelectionModel().isSelectionEmpty() ) return;

    final int index = table.getSelectionModel().getMinSelectedIndex();
    final Version version = model.getObjectAt( index );
    getController().getVersionManager().restore( version, false );
  }
}

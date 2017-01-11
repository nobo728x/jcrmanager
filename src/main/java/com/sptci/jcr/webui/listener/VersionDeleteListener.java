package com.sptci.jcr.webui.listener;

import com.sptci.jcr.webui.VersionsView;
import com.sptci.jcr.webui.table.VersionsTable;
import com.sptci.jcr.webui.table.VersionsTableModel;

import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.list.ListSelectionModel;

import static com.sptci.jcr.webui.MainController.getController;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The event listener for deleting old versions selected by the user.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-30
 * @version $Id$
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class VersionDeleteListener extends VersionManagementListener
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    final VersionsView view = getView( event );
    if ( ! checkView( view ) ) return;
    
    final Collection<Version> versions = getVersions( view );
    final Node node = getNode( view );
    
    try
    {
      delete( node, versions );
      updateView( view );
    }
    catch ( RepositoryException e )
    {
      processFatalException( e );
    }
  }

  private Collection<Version> getVersions( final VersionsView view )
  {
    final ArrayList<Version> versions = new ArrayList<Version>();

    final VersionsTable table = (VersionsTable) view.getVersions().getTable();
    final VersionsTableModel model = table.getModel();
    final ListSelectionModel selectionModel = table.getSelectionModel();

    if ( selectionModel.isSelectionEmpty() ) return versions;

    final int min = selectionModel.getMinSelectedIndex();
    final int max = selectionModel.getMaxSelectedIndex();

    for ( int i = min; i <= max; ++i )
    {
      if ( selectionModel.isSelectedIndex( i ) )
      {
        versions.add( model.getObjectAt( i ) );
      }
    }

    return versions;
  }

  private void delete( final Node node, final Collection<Version> versions )
      throws RepositoryException
  {
    final VersionHistory history =
        getController().getVersionManager().getVersionHistory( node.getPath() );

    for ( final Version version : versions )
    {
      history.removeVersion( version.getName() );
    }

    getController().getSession().save();
  }

}

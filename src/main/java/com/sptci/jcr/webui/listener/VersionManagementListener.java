package com.sptci.jcr.webui.listener;

import static com.sptci.echo.Configuration.getString;
import com.sptci.echo.ErrorPane;
import com.sptci.echo.Listener;
import static com.sptci.jcr.webui.MainController.getController;
import com.sptci.jcr.webui.VersionsView;
import com.sptci.jcr.webui.table.VersionsTable;
import com.sptci.jcr.webui.table.VersionsTableModel;

import javax.jcr.Node;

/**
 * A base listener class for events related to managing version history
 * of a node.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-31
 * @version $Id: VersionManagementListener.java 24 2009-08-31 14:32:29Z spt $
 */
public abstract class VersionManagementListener extends Listener<VersionsView>
{
  @Override
  protected boolean checkView( final VersionsView view )
  {
    boolean result = true;
    final VersionsTable table = (VersionsTable) view.getVersions().getTable();

    if ( table.getSelectionModel().isSelectionEmpty() )
    {
      result = false;

      final ErrorPane pane = new ErrorPane(
          getString( view, "error.title" ),
          getString( view, "error.message" ) );
      getApplication().addPane( pane );
    }

    return result;
  }
  protected Node getNode( final VersionsView view )
  {
    final VersionsTable table = (VersionsTable) view.getVersions().getTable();
    final VersionsTableModel model = table.getModel();
    return model.getNode();
  }

  protected void updateView( final VersionsView view )
  {
    getController().displayNode( getNode( view ) );
  }
}

package com.sptci.jcr.webui.table;

import com.sptci.echo.table.Table;

import static nextapp.echo.app.list.ListSelectionModel.MULTIPLE_SELECTION;

import javax.jcr.version.Version;

/**
 * A table used to display the history of versions available for a specified
 * node.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-30
 * @version $Id: VersionsTable.java 24 2009-08-31 14:32:29Z spt $
 */
public class VersionsTable extends Table<Version>
{
  private static final long serialVersionUID = 1l;

  public VersionsTable( final VersionsTableModel model )
  {
    super( model );
    setDefaultRenderer( Version.class, new VersionCellRenderer() );
  }

  @Override
  public void init()
  {
    super.init();
    getSelectionModel().setSelectionMode( MULTIPLE_SELECTION );
  }

  @Override
  public VersionsTableModel getModel()
  {
    return (VersionsTableModel) super.getModel();
  }
}

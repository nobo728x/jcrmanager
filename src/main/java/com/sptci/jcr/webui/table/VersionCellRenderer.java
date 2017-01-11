package com.sptci.jcr.webui.table;

import com.sptci.jcr.webui.NodeButton;
import com.sptci.jcr.webui.listener.NodeListener;

import nextapp.echo.app.Component;
import nextapp.echo.app.Table;
import nextapp.echo.app.table.TableCellRenderer;

import static com.sptci.echo.Application.getApplication;

import javax.jcr.RepositoryException;
import javax.jcr.version.Version;

/**
 * A cell renderer used to display a {@link com.sptci.jcr.webui.NodeButton}
 * for nodes displayed in tables.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-30
 * @version $Id: VersionCellRenderer.java 59 2010-02-09 18:07:17Z spt $
 */
public class VersionCellRenderer implements TableCellRenderer
{
  private static final long serialVersionUID = 1L;

  private final NodeListener listener = new NodeListener();

  public Component getTableCellRendererComponent( final Table table,
      final Object value, final int column, final int row )
  {
    final Version node = (Version) value;
    final NodeButton button = new NodeButton( node, listener );

    try
    {
      button.setText( node.getIdentifier() );
    }
    catch ( RepositoryException e )
    {
      getApplication().processFatalException( e );
    }

    return button;
  }
}

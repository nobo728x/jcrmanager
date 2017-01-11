package com.sptci.jcr.webui;

import static com.sptci.echo.Application.getApplication;
import static com.sptci.echo.Utilities.createLabel;
import com.sptci.echo.WindowPane;
import static com.sptci.echo.style.Extent.PERCENT;
import static com.sptci.echo.style.Extent.getInstance;
import com.sptci.jcr.SessionManager;

import nextapp.echo.app.Border;
import static nextapp.echo.app.Border.STYLE_SOLID;
import static nextapp.echo.app.Color.BLACK;
import nextapp.echo.app.Grid;
import nextapp.echo.app.Label;

import javax.jcr.Repository;
import javax.naming.NamingException;

/**
 * A floating pane used to display the repository description keys and
 * values as specified by the vendor.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh 2009-08-25
 * @version $Id: RepositoryDetailsView.java 22 2009-08-26 14:42:03Z spt $
 */
public class RepositoryDetailsView extends WindowPane
{
  private static final long serialVersionUID = 1L;

  @Override
  public void init()
  {
    removeAll();
    super.init();

    final Grid grid = createGrid();
    addTitle( grid );
    addDescriptions( grid );
    add( grid );
  }

  private Grid createGrid()
  {
    final Grid grid = new Grid();
    grid.setWidth( getInstance( 100, PERCENT ) );
    grid.setBorder( new Border( 1, BLACK, STYLE_SOLID ) );
    return grid;
  }

  private void addTitle( final Grid grid )
  {
    grid.add( createLabel( getClass().getName(), "key", "Bold.Label" ) );
    grid.add( createLabel( getClass().getName(), "value", "Bold.Label" ) );
  }

  private void addDescriptions( final Grid grid )
  {
    try
    {
      final Repository repository = new SessionManager().getRepository();

      for ( final String key : repository.getDescriptorKeys() )
      {
        final Label name = new Label( key );
        name.setStyleName( "Bold.Label" );
        grid.add( name );

        final Label value = new Label( repository.getDescriptor( key ) );
        value.setStyleName( "Italic.Label" );
        grid.add( value );
      }
    }
    catch ( NamingException e )
    {
      getApplication().processFatalException( e );
    }
  }
}

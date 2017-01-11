package com.sptci.jcr.webui;

import com.sptci.echo.Column;
import static com.sptci.echo.Utilities.createLabel;
import com.sptci.echo.annotation.ActionListener;
import com.sptci.echo.binding.ViewInitialiser;
import com.sptci.echo.table.TableContainer;

import echopoint.Strut;
import nextapp.echo.app.Border;
import nextapp.echo.app.Button;
import nextapp.echo.app.Color;
import nextapp.echo.app.Row;

/**
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-30
 * @version $Id: VersionsView.java 24 2009-08-31 14:32:29Z spt $
 */
public class VersionsView extends Column
{
  private static final long serialVersionUID = 1L;

  private TableContainer versions;

  @ActionListener( value = "com.sptci.jcr.webui.listener.VersionRevertListener" )
  private Button revert;

  @ActionListener( value = "com.sptci.jcr.webui.listener.VersionDeleteListener" )
  private Button delete;

  public VersionsView( final TableContainer versions )
  {
    this.versions = versions;
    new ViewInitialiser<VersionsView>( this ).init();
  }

  @Override
  public void init()
  {
    setBorder( new Border( 2, Color.BLACK, Border.STYLE_GROOVE ) );
    removeAll();
    super.init();

    add( createLabel( getClass().getName(), "title", "Bold.Label" ) );
    add( versions );

    add( new Strut() );
    createControls();
  }

  private void createControls()
  {
    final Row controls = new Row();
    controls.add( revert );
    controls.add( new Strut() );
    controls.add( delete );

    add( controls );
  }

  public TableContainer getVersions()
  {
    return versions;
  }
}

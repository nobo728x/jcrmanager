package com.sptci.jcr.webui;

import com.sptci.jcr.webui.listener.ScriptAddListener;

import nextapp.echo.app.Column;
import nextapp.echo.app.Row;

import echopoint.DirectHtml;

import static com.sptci.echo.Configuration.getString;
import static com.sptci.echo.Utilities.createLabel;

import java.io.File;

/**
 * A dialogue used to add a new groovy script to the system.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-01-11
 * @version $Id: ScriptAddDialog.java 44 2010-01-12 21:01:38Z spt $
 */
public class ScriptAddDialog extends ScriptDialog
{
  private static final long serialVersionUID = 1L;

  public ScriptAddDialog( final ScriptManagementWindow window, final File file )
  {
    super( window, file );
    setModal( true );
  }

  public void init()
  {
    removeAll();
    super.init();

    final Row row = new Row();
    row.add( createLabel( getClass().getName(), "name" ) );

    createName();
    name.addActionListener( new ScriptAddListener() );
    row.add( name );

    final Column column = new Column();
    column.add( row );
    column.add( new DirectHtml( getString( this, "message.text" ) ) );
    
    add( column );
    getApplicationInstance().setFocusedComponent( name );
  }
}

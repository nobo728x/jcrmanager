package com.sptci.jcr.webui;

import com.sptci.jcr.webui.listener.ScriptDirectoryListener;

import nextapp.echo.app.Row;

import static com.sptci.echo.Utilities.createLabel;

import java.io.File;

/**
 * The dialog used to add a new directory under which groovy scripts are stored
 * in the system.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-01-12
 * @version $Id: ScriptDirectoryDialog.java 44 2010-01-12 21:01:38Z spt $
 */
public class ScriptDirectoryDialog extends ScriptDialog
{
  private static final long serialVersionUID = 1L;

  public ScriptDirectoryDialog( final ScriptManagementWindow window, final File file )
  {
    super( window, file );
  }

  public void init()
  {
    removeAll();
    super.init();

    final Row row = new Row();
    row.add( createLabel( getClass().getName(), "name" ) );

    createName();
    name.addActionListener( new ScriptDirectoryListener() );
    row.add( name );

    add( row );
    getApplicationInstance().setFocusedComponent( name );
  }
}

package com.sptci.jcr.webui;

import com.sptci.echo.WindowPane;

import echopoint.RegexTextField;

import static com.sptci.echo.Dimensions.getExtent;

import java.io.File;

/**
 * A base dialog used to add script files or directories to the system.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-01-12
 * @version $Id: ScriptDialog.java 44 2010-01-12 21:01:38Z spt $
 */
public abstract class ScriptDialog extends WindowPane
{
  protected final ScriptManagementWindow window;
  protected final File file;
  protected RegexTextField name;

  public ScriptDialog( final ScriptManagementWindow window, final File file ) {
    this.window = window;
    this.file = file;
  }

  protected void createName()
  {
    name = new RegexTextField( "^[\\w]+$" );
    name.setWidth( getExtent( this, "name.width" ) );
  }

  public String getName() { return name.getText(); }

  public ScriptManagementWindow getWindow() { return window; }

  public File getFile() { return file; }
}

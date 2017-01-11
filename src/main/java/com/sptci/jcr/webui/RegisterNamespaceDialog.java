package com.sptci.jcr.webui;

import com.sptci.echo.WindowPane;
import com.sptci.echo.annotation.ActionListener;
import com.sptci.echo.binding.ViewInitialiser;

import nextapp.echo.app.Button;
import nextapp.echo.app.Grid;
import nextapp.echo.app.Label;
import nextapp.echo.app.TextField;

/**
 * A dialogue for registering a custom namespace to a specified URI.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-11-16
 * @version $Id: RegisterNamespaceDialog.java 41 2009-11-16 20:16:22Z spt $
 */
public class RegisterNamespaceDialog extends WindowPane
{
  private static final long serialVersionUID = 1L;

  Label prefixLabel;
  TextField prefix;
  Label uriLabel;
  TextField uri;
  @ActionListener( value = "com.sptci.jcr.webui.listener.RegisterNamespaceListener" )
  private Button register;

  public RegisterNamespaceDialog()
  {
    new ViewInitialiser<RegisterNamespaceDialog>( this ).init();
  }

  public void init()
  {
    removeAll();
    super.init();

    final Grid grid = new Grid();
    grid.add( prefixLabel );
    grid.add( prefix );
    grid.add( uriLabel );
    grid.add( uri );
    grid.add( register );

    add( grid );
  }

  public String getPrefix() { return prefix.getText(); }
  public String getURI() { return uri.getText(); }
}

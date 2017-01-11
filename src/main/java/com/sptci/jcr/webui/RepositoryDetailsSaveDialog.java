package com.sptci.jcr.webui;

import com.sptci.echo.WindowPane;
import com.sptci.echo.annotation.ActionListener;
import com.sptci.echo.annotation.Constraints;
import com.sptci.echo.binding.ViewInitialiser;

import nextapp.echo.app.Label;
import nextapp.echo.app.Row;
import nextapp.echo.app.TextField;

/**
 * A dialogue used to prompt the user for the
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-09-06
 * @version $Id: RepositoryDetailsSaveDialog.java 63 2010-02-16 15:23:00Z spt $
 */
public class RepositoryDetailsSaveDialog extends WindowPane
{
  private static final long serialVersionUID = 1L;

  private final LoginDialog dialog;

  private Label nameLabel;
  @ActionListener( value = "com.sptci.jcr.webui.listener.RepositoryDetailsSaveListener" )
  @Constraints( value = Constraints.Value.NOT_NULL )
  private TextField name;

  public RepositoryDetailsSaveDialog( final LoginDialog dialog )
  {
    this.dialog = dialog;
    new ViewInitialiser<RepositoryDetailsSaveDialog>( this ).init();
    style();
  }

  @Override
  public void init()
  {
    removeAll();
    super.init();

    final Row row = new Row();
    row.add( nameLabel );
    row.add( name );
    add( row );

    getApplicationInstance().setFocusedComponent( name );
  }

  private void style()
  {
    setMaximizeEnabled( false );
  }

  public LoginDialog getDialog() { return dialog; }

  public String getName() { return name.getText(); }
}

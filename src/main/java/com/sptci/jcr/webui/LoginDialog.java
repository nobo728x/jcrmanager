package com.sptci.jcr.webui;

import com.sptci.echo.WindowPane;
import com.sptci.echo.annotation.ActionListener;
import com.sptci.echo.annotation.Constraints;
import com.sptci.echo.binding.ViewInitialiser;

import nextapp.echo.app.Button;
import nextapp.echo.app.Component;
import nextapp.echo.app.Label;
import nextapp.echo.app.Row;
import nextapp.echo.app.SplitPane;
import nextapp.echo.app.TextField;

import static com.sptci.echo.Configuration.getString;
import static com.sptci.echo.Dimensions.getExtent;

/**
 * A base dialogue used to display a repository connection dialog.  Sub-classes
 * implement direct repository connection or resource connection dialogues.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh 2009-08-19
 * @version $Id: LoginDialog.java 63 2010-02-16 15:23:00Z spt $
 */
public abstract class LoginDialog extends WindowPane
{
  protected Label userLabel;
  @Constraints( value = Constraints.Value.NOT_NULL )
  protected TextField user;
  protected Label passwordLabel;
  @Constraints( value = Constraints.Value.NOT_NULL )
  @ActionListener( value = "com.sptci.jcr.webui.listener.ConnectListener" )
  protected TextField password;
  @ActionListener( value = "com.sptci.jcr.webui.listener.ConnectListener" )
  protected Button open;

  protected LoginDialog()
  {
    new ViewInitialiser<LoginDialog>( this ).init();
  }

  /** Layout the view when the pane is added to the hierarchy. */
  @Override
  public void init()
  {
    removeAll();
    super.init();

    style();

    final SplitPane pane = new SplitPane( SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP );
    pane.setAutoPositioned( true );

    pane.add( createButtons() );
    createGrid( pane );

    add( pane );
  }

  private void style()
  {
    setTitle( getString( this, "title" ) );
    setHeight( getExtent( this, "height" ) );
    setWidth( getExtent( this, "width" ) );
  }

  protected Component createButtons()
  {
    final Row row = new Row();
    row.add( open );
    return row;
  }

  protected abstract void createGrid( SplitPane pane );

  /** @return Returns the value in the user name field. */
  public String getUser() { return user.getText(); }

  /** @param user The value to set for the user text field. */
  public void setUser( final String user ) { this.user.setText( user ); }

  /** @return Returns the value in the password field. */
  public String getPassword() { return password.getText(); }

  /** @param password The value to set for the password text field. */
  public void setPassword( final String password )
  {
    this.password.setText( password );
  }
}

package com.sptci.jcr.webui;

import com.sptci.echo.annotation.ActionListener;
import com.sptci.echo.annotation.Constraints;

import nextapp.echo.app.Button;
import nextapp.echo.app.Component;
import nextapp.echo.app.Grid;
import nextapp.echo.app.Label;
import nextapp.echo.app.SplitPane;
import nextapp.echo.app.TextField;

import static com.sptci.echo.Application.getApplication;

/**
 * A dialogue used to select the repository configuration file and repository
 * location for opening a session.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_top'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-06-25
 * @version $Id: LoginLocalDialog.java 63 2010-02-16 15:23:00Z spt $
 */
public class LoginLocalDialog extends LoginDialog
{
  private static final long serialVersionUID = 1L;

  private Label fileLabel;
  @Constraints( value = Constraints.Value.NOT_NULL )
  private TextField file;
  @ActionListener( value = "com.sptci.jcr.webui.listener.FileSystemWindowPaneListener" )
  private Button fileButton;

  private Label directoryLabel;
  @Constraints( value = Constraints.Value.NOT_NULL )
  private TextField directory;

  @ActionListener( value = "com.sptci.jcr.webui.listener.FileSystemWindowPaneListener" )
  private Button directoryButton;

  @ActionListener( value = "com.sptci.jcr.webui.listener.RepositoryDialogSaveListener" )
  private Button save;

  @Override
  public void init()
  {
    super.init();
    getApplication().setFocusedComponent( file );
  }

  @Override
  protected Component createButtons()
  {
    final Component buttons =  super.createButtons();
    buttons.add( save );

    return buttons;
  }

  @Override
  protected void createGrid( final SplitPane pane )
  {
    final Grid grid = new Grid( 3 );
    grid.add( fileLabel );
    grid.add( file );
    grid.add( fileButton );
    grid.add( directoryLabel );
    grid.add( directory );
    grid.add( directoryButton );
    grid.add( userLabel );
    grid.add( user );
    grid.add( new Label() );
    grid.add( passwordLabel );
    grid.add( password );
    grid.add( new Label() );
    pane.add( grid );
  }

  /** @return The value in the configuration file field. */
  public String getFile() { return file.getText(); }

  /** @param file The value to set for the file text field. */
  public void setFile( final String file ) { this.file.setText( file ); }

  /** @return The value in the repository directory field */
  public String getDirectory() { return directory.getText(); }

  /** @param directory The value to set for the directory text field. */
  public void setDirectory( final String directory )
  {
    this.directory.setText( directory );
  }

  public TextField getFileTextField() { return file; }
  public TextField getDirectoryTextField() { return directory; }
}

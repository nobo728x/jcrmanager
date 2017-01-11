package com.sptci.jcr.webui.listener;

import com.sptci.echo.Listener;
import com.sptci.echo.tree.filesystem.FileSystemWindowPane;
import com.sptci.jcr.webui.LoginLocalDialog;

import nextapp.echo.app.Button;
import nextapp.echo.app.Component;
import nextapp.echo.app.TextField;
import nextapp.echo.app.event.ActionEvent;

/**
 * A listener used to launch the {@link FileSystemWindowPane}
 * from action buttons in the UI.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author rakesh 2009-08-10
 * @version $Id: FileSystemWindowPaneListener.java 63 2010-02-16 15:23:00Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class FileSystemWindowPaneListener extends Listener<LoginLocalDialog>
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent actionEvent )
  {
    final FileSystemWindowPane<FileSelectionListener> pane =
        new FileSystemWindowPane<FileSelectionListener>();
    pane.setOpenListener( new FileSelectionListener( getTextField( actionEvent ) ) );
    getApplication().addPane( pane );
  }

  private TextField getTextField( final ActionEvent event )
  {
    final Component button = (Button) event.getSource();
    final Component parent = button.getParent();

    final int index = parent.indexOf( button );
    return (TextField) parent.getComponent( index - 1 );
  }
}

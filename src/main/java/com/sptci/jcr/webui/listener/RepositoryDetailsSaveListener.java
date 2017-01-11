package com.sptci.jcr.webui.listener;

import com.sptci.echo.Confirmation;
import com.sptci.echo.ErrorPane;
import com.sptci.echo.Executor;
import com.sptci.echo.Listener;
import com.sptci.jcr.webui.LoginJNDIDialog;
import com.sptci.jcr.webui.LoginLocalDialog;
import com.sptci.jcr.webui.LoginRMIDialog;
import com.sptci.jcr.webui.RepositoryDetailsSaveDialog;
import com.sptci.jcr.webui.model.RepositoryConnectionData;
import com.sptci.jcr.webui.model.RepositoryData;

import nextapp.echo.app.event.ActionEvent;

import static com.sptci.echo.Configuration.getString;
import static com.sptci.jcr.webui.MainController.getController;

import java.io.IOException;

/**
 * The event listener for saving the details entered in {@link
 * com.sptci.jcr.webui.LoginLocalDialog} using the name specified in
 * {@link com.sptci.jcr.webui.RepositoryDetailsSaveDialog}.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-09-06
 * @version $Id: RepositoryDetailsSaveListener.java 73 2011-11-04 17:23:50Z orair $
 */
public class RepositoryDetailsSaveListener extends
    Listener<RepositoryDetailsSaveDialog>
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    final RepositoryDetailsSaveDialog dialog = getView( event );

    if ( ! checkView( dialog ) )
    {
      getApplication().addPane( new ErrorPane(
          getString( dialog, "error.title" ),
          getString( dialog, "error.message" ) ) );
      return;
    }

    try
    {
      save( dialog );
    }
    catch ( IOException e )
    {
      processFatalException( e );
    }
  }

  private void save( final RepositoryDetailsSaveDialog dialog )
      throws IOException
  {
    final RepositoryConnectionData connections =
        RepositoryConnectionData.getConnectionData(
            getController().getRepositoryDataFile() );

    if ( connections.hasName( dialog.getName() ) )
    {
      final Executor<RepositoryDetailsSaveListener> executor =
          new Executor<RepositoryDetailsSaveListener>( this, "saveConnections" );
      executor.addParameter( RepositoryDetailsSaveDialog.class, dialog );
      executor.addParameter( RepositoryConnectionData.class, connections );
      getApplication().addPane( new Confirmation(
          getString( dialog, "nameexists.title" ),
          getString( dialog, "nameexists.message" ), executor ) );
    }
    else
    {
      saveConnections( dialog, connections );
    }
  }

  private void saveConnections( final RepositoryDetailsSaveDialog dialog,
      final RepositoryConnectionData connections )
      throws IOException
  {
    final RepositoryData data = getData( dialog );
    connections.addData( dialog.getName(), data );
    connections.save( getController().getRepositoryDataFile() );
    
    dialog.userClose();
    getController().getContentArea().resetMenu();
    new ConnectListener().process( dialog.getDialog() );
  }

  private RepositoryData getData( final RepositoryDetailsSaveDialog dialog )
  {
    if ( dialog.getDialog() instanceof LoginLocalDialog )
    {
      final LoginLocalDialog rd = (LoginLocalDialog) dialog.getDialog();
      return new RepositoryData( rd.getFile(), rd.getDirectory(),
          rd.getUser(), rd.getPassword() );
    }
    else if (dialog.getDialog() instanceof LoginRMIDialog)
    {
      final LoginRMIDialog rd = (LoginRMIDialog) dialog.getDialog();
      return new RepositoryData( rd.getUrl(), rd.getUser(), rd.getPassword() );
    }
    else if (dialog.getDialog() instanceof LoginJNDIDialog)
    {
      final LoginJNDIDialog rd = (LoginJNDIDialog) dialog.getDialog();
      return new RepositoryData( true, rd.getJndi(), rd.getUser(), rd.getPassword() );
    }
    return null;
  }
}

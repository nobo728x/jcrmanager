package com.sptci.jcr.webui.listener;

import static com.sptci.echo.Configuration.getString;
import com.sptci.echo.Confirmation;
import com.sptci.echo.ErrorPane;
import com.sptci.echo.Executor;
import com.sptci.echo.Listener;
import static com.sptci.jcr.webui.MainController.getController;
import com.sptci.jcr.webui.QuerySaveDialog;

import nextapp.echo.app.event.ActionEvent;

import java.io.FileNotFoundException;

/**
 * The event listener for saving the query from {@link
 * com.sptci.jcr.webui.QueryWindow} with the name specified in {@link
 * QuerySaveDialog}.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-10-01
 * @version $Id: QuerySaveListener.java 38 2009-10-12 14:35:15Z spt $
 */
public class QuerySaveListener extends Listener<QuerySaveDialog>
{
  private static final long serialVersionUID = 1l;

  public void actionPerformed( final ActionEvent event )
  {
    final QuerySaveDialog dialog = getView( event );
    
    if ( checkView( dialog ) )
    {
      saveQuery( dialog );
    }
    else
    {
      getApplication().addPane( new ErrorPane(
          getString( dialog, "error.title" ),
          getString( dialog, "error.message" ) ) );
    }
  }

  private void saveQuery( final QuerySaveDialog dialog )
  {
    if ( getController().getQueries().hasQuery( dialog.getName() ) )
    {
      final Executor<QuerySaveListener> executor =
          new Executor<QuerySaveListener>( this, "save" );
      executor.addParameter( QuerySaveDialog.class, dialog );

      getApplication().addPane( new Confirmation(
          getString( dialog, "nameexists.title" ),
          getString( dialog, "nameexists.message" ), executor ) );
    }
    else
    {
      save( dialog );
    }
  }

  private void save( final QuerySaveDialog dialog )
  {
    try
    {
      getController().getQueries().save( dialog.getName(), dialog.getQuery() );
      getController().getContentArea().resetMenu();
      dialog.userClose();
    }
    catch ( FileNotFoundException e )
    {
      processFatalException( e );
    }
  }
}

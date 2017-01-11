package com.sptci.jcr.webui.listener;

import static com.sptci.echo.Configuration.getString;
import com.sptci.echo.ErrorPane;
import com.sptci.echo.Listener;
import com.sptci.jcr.webui.QueryResultsView;

import nextapp.echo.app.event.ActionEvent;

import javax.jcr.Node;

/**
 * An abstract base listener for the node management operations exposed
 * by the {@link com.sptci.jcr.webui.QueryResultsView} component.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-09-11
 * @version $Id: QueryResultsViewListener.java 33 2009-09-21 14:28:28Z spt $
 */
public abstract class QueryResultsViewListener extends Listener<QueryResultsView>
{
  public void actionPerformed( final ActionEvent event )
  {
    final QueryResultsView view = getView( event );

    if ( ! checkView( view ) )
    {
      getApplication().addPane( new ErrorPane(
              getString( view, "error.title" ),
              getString( view, "error.message" ) ) );
      return;
    }

    process( view );
  }

  protected Node getNode( final QueryResultsView view )
  {
    final int index = view.getTable().getSelectionModel().getMinSelectedIndex();
    return view.getTable().getModel().getObjectAt( index );
  }

  @Override
  protected boolean checkView( final QueryResultsView view )
  {
    return ! view.getTable().getSelectionModel().isSelectionEmpty();
  }

  protected abstract void process( final QueryResultsView view );
}

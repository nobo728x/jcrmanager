package com.sptci.jcr.webui.listener;

import static com.sptci.echo.Configuration.getString;
import com.sptci.echo.ErrorPane;
import com.sptci.echo.Listener;
import com.sptci.jcr.webui.SubNodesView;

import nextapp.echo.app.event.ActionEvent;

import javax.jcr.Node;

/**
 * A base listener class for all node management events triggered from the
 * {@link com.sptci.jcr.webui.SubNodesView}.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-09-20
 * @version $Id: SubNodesViewListener.java 33 2009-09-21 14:28:28Z spt $
 */
public abstract class SubNodesViewListener extends Listener<SubNodesView>
{
  public void actionPerformed( final ActionEvent event )
  {
    final SubNodesView view = getView( event );

    if ( ! checkView( view ) )
    {
      getApplication().addPane( new ErrorPane(
          getString( view, "error.title" ),
          getString( view, "error.message" ) ) );
      return;
    }

    process( view );
  }

  protected Node getNode( final SubNodesView view )
  {
    final int index = view.getTable().getSelectionModel().getMinSelectedIndex();
    return view.getTable().getModel().getObjectAt( index );
  }

  @Override
  protected boolean checkView( final SubNodesView view )
  {
    return ! view.getTable().getSelectionModel().isSelectionEmpty();
  }

  protected abstract void process( final SubNodesView view );
}

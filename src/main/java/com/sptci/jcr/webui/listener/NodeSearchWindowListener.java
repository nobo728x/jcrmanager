package com.sptci.jcr.webui.listener;

import static com.sptci.echo.Application.getApplication;
import static com.sptci.echo.Application.getParentView;
import com.sptci.jcr.webui.NodeSearchWindow;
import com.sptci.jcr.webui.SetNodeView;

import nextapp.echo.app.Component;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-21
 * @version $Id: NodeSearchWindowListener.java 18 2009-08-22 11:46:13Z spt $
 */
public class NodeSearchWindowListener implements ActionListener
{
  private static final long serialVersionUID = 1l;

  public void actionPerformed( final ActionEvent event )
  {
    final SetNodeView view = (SetNodeView)
        getParentView( (Component) event.getSource() );
    getApplication().addPane( new NodeSearchWindow( view ) );
  }
}

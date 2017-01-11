package com.sptci.jcr.webui.listener;

import com.sptci.echo.Listener;
import com.sptci.jcr.webui.NodeButton;
import com.sptci.jcr.webui.NodeView;
import com.sptci.jcr.webui.NodeViewController;
import com.sptci.jcr.webui.NodeWindow;

import nextapp.echo.app.event.ActionEvent;

import javax.jcr.Node;

/**
 * An event listener for displaying the details of a node embedded in
 * a {@link com.sptci.jcr.webui.NodeButton} instance.
 * Usually accessed from the {@link com.sptci.jcr.webui.NodeViewController}.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-12
 * @version $Id: NodeListener.java 18 2009-08-22 11:46:13Z spt $
 */
public class NodeListener extends Listener<NodeView>
{
  private static final long serialVersionUID = 1l;

  public void actionPerformed( final ActionEvent event )
  {
    final Node node = ( (NodeButton) event.getSource() ).getNode();
    getApplication().addPane(
        new NodeWindow( new NodeViewController( node ).getView() ) );
  }
}

package com.sptci.jcr.webui;

import com.sptci.echo.WindowPane;

/**
 * A window pane used to display the details about a node.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-12
 * @version $Id: NodeWindow.java 10 2009-08-15 23:08:37Z spt $
 */
public class NodeWindow extends WindowPane
{
  private static final long serialVersionUID = 1l;

  /** The node view with the details of a node. */
  private final NodeView node;

  public NodeWindow( final NodeView node ) { this.node = node; }

  @Override
  public void init()
  {
    super.init();
    removeAll();
    add( node );
  }
}

package com.sptci.jcr.webui.listener;

import com.sptci.jcr.webui.SubNodesView;
import com.sptci.jcr.webui.listener.menu.NodeExportListener;

/**
 * The event listener for exporting the selected node in {@link
 * com.sptci.jcr.webui.SubNodesView} in XML.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-09-20
 * @version $Id: SubNodesExportListener.java 47 2010-01-21 20:38:45Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class SubNodesExportListener extends SubNodesViewListener
{
  private static final long serialVersionUID = 1L;

  @Override
  protected void process( final SubNodesView view )
  {
    new NodeExportListener().export( getNode( view ) );
  }
}

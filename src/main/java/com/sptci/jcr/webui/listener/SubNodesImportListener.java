package com.sptci.jcr.webui.listener;

import com.sptci.jcr.webui.NodeImportDialog;
import com.sptci.jcr.webui.SubNodesView;

/**
 * The event listener for importing a user specified XML export file into
 * a selected sub-node in {@link com.sptci.jcr.webui.SubNodesView}.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-09-20
 * @version $Id: SubNodesImportListener.java 47 2010-01-21 20:38:45Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class SubNodesImportListener extends SubNodesViewListener
{
  private static final long serialVersionUID = 1L;

  protected void process( final SubNodesView view )
  {
    getApplication().addPane( new NodeImportDialog( getNode( view ) ) );
  }
}

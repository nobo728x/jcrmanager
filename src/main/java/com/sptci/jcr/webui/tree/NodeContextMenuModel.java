package com.sptci.jcr.webui.tree;

import nextapp.echo.app.ImageReference;
import nextapp.echo.extras.app.menu.DefaultOptionModel;

import javax.jcr.Node;

/**
 * The menu option model for context menu attached to {@link JCRNode} instances.
 * Stores the associated {@link javax.jcr.Node} on which menu operations
 * are performed.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-09-20
 * @version $Id: NodeContextMenuModel.java 44 2010-01-12 21:01:38Z spt $
 */
public class NodeContextMenuModel extends DefaultOptionModel
{
  private static final long serialVersionUID = 1L;

  /** The node upon which operations are to be performed. */
  @SuppressWarnings( { "TransientFieldNotInitialized" } )
  private transient final Node node;

  public NodeContextMenuModel( final String id, final String text,
      final ImageReference icon, final Node node )
  {
    super( id, text, icon );
    this.node = node;
  }

  public Node getNode()
  {
    return node;
  }
}

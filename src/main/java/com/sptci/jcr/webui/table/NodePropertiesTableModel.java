package com.sptci.jcr.webui.table;

import javax.jcr.Node;

/**
 * A base table model for representing the properties (system and user
 * defined) of a specified node.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-24
 * @version $Id: NodePropertiesTableModel.java 66 2010-02-22 15:24:18Z spt $
 */
public abstract class NodePropertiesTableModel extends AbstractPropertiesTableModel
{
  /** The node whose properties are being displayed by the model. */
  @SuppressWarnings( { "TransientFieldNotInitialized" } )
  protected final transient Node node;

  public NodePropertiesTableModel( final Node node ) { this.node = node; }

  public Node getNode() { return node; }
}

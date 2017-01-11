package com.sptci.jcr.webui;

import javax.jcr.Node;
import java.io.Serializable;

/**
 * An interface that mandates the existence of {@link #setNode} method.
 * This is used from {@link NodeSearchWindow} to ensure that the user
 * selected node is sent back to the view from which it was spawned.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-21
 * @version $Id: SetNodeView.java 18 2009-08-22 11:46:13Z spt $
 */
public interface SetNodeView extends Serializable
{
  void setNode( final Node node );
}

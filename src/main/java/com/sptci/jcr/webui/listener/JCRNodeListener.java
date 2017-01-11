package com.sptci.jcr.webui.listener;

import com.sptci.echo.Listener;
import com.sptci.jcr.webui.ContentArea;
import com.sptci.jcr.webui.NodeViewController;
import com.sptci.jcr.webui.tree.JCRNode;
import com.sptci.jcr.webui.tree.JCRTree;

import nextapp.echo.app.Component;
import nextapp.echo.app.event.ActionEvent;

import echopoint.DirectHtml;

import javax.jcr.Node;

/**
 * The listener for the nodes in {@link JCRTree}.  Displays properties and
 * other meta information about the selected node.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_top'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-06-02
 * @version $Id: JCRNodeListener.java 44 2010-01-12 21:01:38Z spt $
 */
public class JCRNodeListener extends Listener<ContentArea>
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent actionEvent )
  {
    final JCRTree tree = (JCRTree) actionEvent.getSource();
    final ContentArea area = getView( actionEvent );
    area.setChild( getProperties( tree ) );
  }

  private Component getProperties( final JCRTree tree )
  {
    if ( tree.getSelectionModel().isSelectionEmpty() )
    {
      return new DirectHtml();
    }

    final Object object =
        tree.getSelectionModel().getSelectionPath().getLastPathComponent();
    if ( ! ( object instanceof JCRNode ) )
    {
      return new DirectHtml();
    }

    final JCRNode treeNode = (JCRNode) object;
    final Node node = treeNode.getUserObject();
    return new NodeViewController( node ).getView();
  }
}

package com.sptci.jcr.webui.listener.menu;

import com.sptci.jcr.webui.ContentArea;
import static com.sptci.jcr.webui.MainController.getController;
import com.sptci.jcr.webui.tree.JCRTree;

import nextapp.echo.app.Label;
import nextapp.echo.app.event.ActionEvent;

/**
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-13
 * @version $Id: RefreshListener.java 10 2009-08-15 23:08:37Z spt $
 */
public class RefreshListener extends AbstractSessionListener
{
  private static final long serialVersionUID = 1l;

  public void actionPerformed( final ActionEvent event )
  {
    if ( checkView() )
    {
      final ContentArea area = getController().getContentArea();
      area.setTree( new JCRTree() );
      area.setChild( new Label() );
    }
    else
    {
      displayError();
    }
  }
}

package com.sptci.jcr.webui.listener.menu;

import static com.sptci.echo.Configuration.getString;
import com.sptci.echo.ErrorPane;
import com.sptci.echo.Listener;
import com.sptci.jcr.webui.ContentArea;

import javax.jcr.Session;

/**
 * A base listener class that ensures that provides methods that ensure that
 * a valid JCR session is in effect.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-12
 * @version $Id: AbstractSessionListener.java 10 2009-08-15 23:08:37Z spt $
 */
public abstract class AbstractSessionListener extends Listener<ContentArea>
{
  protected boolean checkView()
  {
    return ( getApplication().getProperty( Session.class.getName() ) != null );
  }

  protected void displayError()
  {
    getApplication().addPane( new ErrorPane(
        getString( AbstractSessionListener.class, "error.title" ),
        getString( AbstractSessionListener.class, "error.message" ) ) );
  }
}

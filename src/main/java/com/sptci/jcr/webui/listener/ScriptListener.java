package com.sptci.jcr.webui.listener;

import com.sptci.echo.Listener;
import com.sptci.jcr.webui.ScriptDialog;

import nextapp.echo.app.event.ActionEvent;

import static com.sptci.jcr.webui.MainController.getController;

/**
 * A base listener class for script file/directory management actions.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-01-12
 * @version $Id: ScriptListener.java 44 2010-01-12 21:01:38Z spt $
 */
public abstract class ScriptListener<V extends ScriptDialog> extends Listener<V>
{
  public void actionPerformed( final ActionEvent event )
  {
    final V dialog = getView( event );
    if ( dialog.getName().isEmpty() ) return;
    dialog.setModal( false );

    process( dialog );

    dialog.userClose();
    getController().getContentArea().resetMenu();
  }

  protected abstract void process( V dialog );
}

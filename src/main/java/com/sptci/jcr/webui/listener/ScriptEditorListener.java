package com.sptci.jcr.webui.listener;

import com.sptci.echo.Application;
import com.sptci.echo.Listener;
import com.sptci.jcr.webui.ScriptEditorComponent;
import com.sptci.jcr.webui.ScriptManagementWindow;

import nextapp.echo.app.event.ActionEvent;

import java.text.SimpleDateFormat;

/**
 * A base listener for events triggered from the {@link com.sptci.jcr.webui.ScriptEditorComponent}.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-01-11
 * @version $Id: ScriptEditorListener.java 44 2010-01-12 21:01:38Z spt $
 */
public abstract class ScriptEditorListener extends Listener<ScriptEditorComponent>
{
  protected static final SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss Z" );
  
  public void actionPerformed( final ActionEvent event )
  {
    final ScriptEditorComponent editor = getView( event );
    final ScriptManagementWindow window =
        (ScriptManagementWindow) Application.getParentView( editor );

    process( editor, window );
  }

  protected abstract void process( ScriptEditorComponent editor, ScriptManagementWindow window );
}

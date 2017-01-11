package com.sptci.jcr.webui.listener;

import com.sptci.echo.Listener;
import com.sptci.echo.tree.Tree;
import com.sptci.jcr.webui.ScriptEditorComponent;
import com.sptci.jcr.webui.ScriptManagementWindow;
import com.sptci.jcr.webui.tree.ScriptNode;

import nextapp.echo.app.Column;
import nextapp.echo.app.event.ActionEvent;

/**
 * The event listener for displaying the contents of the script selected in
 * {@link com.sptci.jcr.webui.ScriptManagementWindow}.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-01-11
 * @version $Id: ScriptSelectionListener.java 44 2010-01-12 21:01:38Z spt $
 */
public class ScriptSelectionListener extends Listener<ScriptManagementWindow>
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    final ScriptManagementWindow window = getView( event );
    final Tree tree = (Tree) event.getSource();
    
    if ( tree.getSelectionModel().isSelectionEmpty() )
    {
      window.setEditor( new Column() );
      return;
    }

    final ScriptNode node = (ScriptNode)
        tree.getSelectionModel().getSelectionPath().getLastPathComponent();

    if ( node.getUserObject().isDirectory() )
    {
      window.setEditor( new Column() );
      return;
    }

    final ScriptEditorComponent editor = new ScriptEditorComponent();
    editor.setFile( node.getUserObject() );
    window.setEditor( editor );
  }
}

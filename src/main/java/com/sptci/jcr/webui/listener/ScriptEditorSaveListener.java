package com.sptci.jcr.webui.listener;

import com.sptci.jcr.webui.ScriptEditorComponent;
import com.sptci.jcr.webui.ScriptManagementWindow;
import com.sptci.jcr.webui.tree.ScriptNode;
import com.sptci.jcr.webui.tree.ScriptTreeModel;

import static com.sptci.jcr.webui.MainController.getController;
import static com.sptci.util.StringUtilities.toFile;
import static java.lang.String.format;

import java.util.Date;

/**
 * The event listener for saving the contents of the editor component in
 * {@link com.sptci.jcr.webui.ScriptEditorComponent} to the underlying
 * file.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-01-11
 * @version $Id: ScriptEditorSaveListener.java 44 2010-01-12 21:01:38Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class ScriptEditorSaveListener extends ScriptEditorListener
{
  private static final long serialVersionUID = 1L;

  @Override
  protected void process( final ScriptEditorComponent editor,
      final ScriptManagementWindow window )
  {
    try
    {
      boolean updateMenu = false;

      if ( ! editor.getFile().exists() )
      {
        final ScriptTreeModel model = window.getTree().getModel();
        final ScriptNode parent = model.getNode( editor.getFile().getParentFile() );
        final ScriptNode child = new ScriptNode( editor.getFile() );
        child.setModel( parent.getModel() );
        model.addChild( parent, child );
        updateMenu = true;
      }
      
      toFile( editor.getText(), editor.getFile().getAbsolutePath() );
      window.setOutput( format( "%s: Saved script to file: %s",
          sdf.format( new Date() ), editor.getFile() ) );

      if ( updateMenu ) getController().getContentArea().resetMenu();
    }
    catch ( Throwable t )
    {
      processFatalException( t );
    }
  }
}

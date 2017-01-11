package com.sptci.jcr.webui.listener;

import com.sptci.echo.ErrorPane;
import com.sptci.jcr.webui.ScriptEditorComponent;
import com.sptci.jcr.webui.ScriptRenameDialog;
import com.sptci.jcr.webui.tree.ScriptNode;
import com.sptci.jcr.webui.tree.ScriptTreeModel;

import static com.sptci.echo.Configuration.getString;
import static com.sptci.io.FileUtilities.FILE_SEPARATOR;
import static java.lang.String.format;

import java.io.File;

/**
 * The event listener for renaming an existing script or directory
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-01-12
 * @version $Id: ScriptRenameListener.java 44 2010-01-12 21:01:38Z spt $
 */
public class ScriptRenameListener extends ScriptListener<ScriptRenameDialog>
{
  private static final long serialVersionUID = 1L;

  @Override
  protected void process( final ScriptRenameDialog dialog )
  {
    final File file = dialog.getFile();
    String pattern = ( file.isDirectory() ) ? "%s%s%s" : "%s%s%s.groovy";

    final File dest = new File( format( pattern, file.getParent(),
        FILE_SEPARATOR, dialog.getName() ) );
    if ( file.renameTo( dest ) )
    {
      final ScriptTreeModel model = dialog.getWindow().getTree().getModel();
      final ScriptNode parent = model.getNode( file.getParentFile() );
      ScriptNode child = model.getNode( file );
      model.removeChild( parent, child );
      
      child = new ScriptNode( dest );
      child.setModel( model );
      model.addChild( parent, child );

      if ( dest.isFile() )
      {
        final ScriptEditorComponent editor = new ScriptEditorComponent();
        editor.setFile( dest );
        dialog.getWindow().setEditor( editor );
      }
    }
    else
    {
      final ErrorPane pane = new ErrorPane(
          getString( dialog, "error.title" ),
          format( getString( dialog, "error.message" ),
              file.getAbsolutePath(), dest.getAbsolutePath() ) );
      getApplication().addPane( pane );
    }
  }
}

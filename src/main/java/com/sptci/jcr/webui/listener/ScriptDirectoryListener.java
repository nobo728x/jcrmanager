package com.sptci.jcr.webui.listener;

import com.sptci.echo.ErrorPane;
import com.sptci.jcr.webui.ScriptDirectoryDialog;
import com.sptci.jcr.webui.tree.ScriptNode;
import com.sptci.jcr.webui.tree.ScriptTreeModel;

import static com.sptci.echo.Configuration.getString;
import static com.sptci.io.FileUtilities.FILE_SEPARATOR;
import static java.lang.String.format;

import java.io.File;

/**
 * The event listener for adding a new script directory to the system.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-01-12
 * @version $Id: ScriptDirectoryListener.java 44 2010-01-12 21:01:38Z spt $
 */
public class ScriptDirectoryListener extends ScriptListener<ScriptDirectoryDialog>
{
  private static final long serialVersionUID = 1L;

  @Override
  protected void process( final ScriptDirectoryDialog dialog )
  {
    File file = dialog.getFile();
    if ( ! file.isDirectory() ) file = file.getParentFile();

    final File dir =  new File( format( "%s%s%s",
        file.getAbsolutePath(), FILE_SEPARATOR, dialog.getName() ) );
    if ( ! dir.exists() )
    {
      if ( dir.mkdir() )
      {
        final ScriptTreeModel model = dialog.getWindow().getTree().getModel();
        final ScriptNode parent = model.getNode( dir.getParentFile() );
        
        final ScriptNode child = new ScriptNode( dir );
        child.setModel( parent.getModel() );
        model.addChild( parent, child );
      }
      else
      {
        final ErrorPane pane = new ErrorPane(
            getString( dialog, "error.title" ),
            format( getString( dialog, "error.message" ), dir.getAbsolutePath() ) );
        getApplication().addPane( pane );
      }
    }
  }
}

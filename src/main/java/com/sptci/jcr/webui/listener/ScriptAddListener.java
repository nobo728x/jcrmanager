package com.sptci.jcr.webui.listener;

import com.sptci.jcr.webui.ScriptAddDialog;
import com.sptci.jcr.webui.ScriptEditorComponent;

import static com.sptci.io.FileUtilities.FILE_SEPARATOR;
import static java.lang.String.format;

import java.io.File;

/**
 * The event listener for adding a new script to the system.  Note that this
 * listener only creates the script editor view.  The save button on the
 * resulting view needs to be clicked to save the script to the system.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-01-12
 * @version $Id: ScriptAddListener.java 44 2010-01-12 21:01:38Z spt $
 */
public class ScriptAddListener extends ScriptListener<ScriptAddDialog>
{
  private static final long serialVersionUID = 1L;

  @Override
  protected void process( final ScriptAddDialog dialog )
  {
    File file = dialog.getFile();
    if ( ! file.isDirectory() ) file = file.getParentFile();

    final ScriptEditorComponent editor = new ScriptEditorComponent();
    editor.setFile( new File( format( "%s%s%s.groovy",
        file.getAbsolutePath(), FILE_SEPARATOR, dialog.getName() ) ) );
    dialog.getWindow().setEditor( editor );
  }
}

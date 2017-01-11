package com.sptci.jcr.webui.listener;

import com.sptci.jcr.webui.ScriptEditorComponent;
import com.sptci.jcr.webui.ScriptManagementWindow;

import static java.lang.String.format;

import java.util.Date;

/**
 * The event listener for reverting the contents of the editor area in {@link
 * com.sptci.jcr.webui.ScriptEditorComponent} to its saves state.  Note that
 * no history of saves is maintained.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-01-11
 * @version $Id: ScriptEditorRevertListener.java 44 2010-01-12 21:01:38Z spt $
 */
public class ScriptEditorRevertListener extends ScriptEditorListener
{
  private static final long serialVersionUID = 1L;

  @Override
  protected void process( final ScriptEditorComponent editor, final ScriptManagementWindow window )
  {
    try
    {
      editor.setFile( editor.getFile() );
      window.setOutput( format( "%s: Reverted script from file: %s",
          sdf.format( new Date() ), editor.getFile() ) );
    }
    catch ( Throwable t )
    {
      processFatalException( t );
    }
  }
}

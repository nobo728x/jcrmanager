package com.sptci.jcr.webui;

import com.sptci.echo.View;
import com.sptci.echo.annotation.ActionListener;
import com.sptci.echo.binding.ViewInitialiser;

import nextapp.echo.app.Button;
import nextapp.echo.app.Component;
import nextapp.echo.app.Row;
import nextapp.echo.app.SplitPane;
import nextapp.echo.app.TextArea;

import echopoint.Strut;

import static com.sptci.echo.Application.getApplication;
import static com.sptci.util.StringUtilities.fromFile;
import static nextapp.echo.app.Color.WHITE;

import java.io.File;

/**
 * A component that displays a script editor area and controls to save or
 * execute the script.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-01-11
 * @version $Id: ScriptEditorComponent.java 44 2010-01-12 21:01:38Z spt $
 */
public class ScriptEditorComponent extends SplitPane implements View
{
  private static final long serialVersionUID = 1L;

  private TextArea editor;

  @ActionListener( value = "com.sptci.jcr.webui.listener.ScriptEditorSaveListener" )
  private Button save;
  
  @ActionListener( value = "com.sptci.jcr.webui.listener.ScriptEditorRevertListener" )
  private Button revert;

  @ActionListener( value = "com.sptci.jcr.webui.listener.ScriptEditorExecuteListener" )
  private Button execute;
  
  private File file;

  public ScriptEditorComponent()
  {
    super( SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP, true );
    setBackground( WHITE );
    new ViewInitialiser<ScriptEditorComponent>( this ).init();
  }

  @Override
  public void init()
  {
    removeAll();
    super.init();
    add( createControls() );
    add( editor );
  }

  private Component createControls()
  {
    final Row row = new Row();
    row.add( new Strut() );
    row.add( save );
    row.add( new Strut() );
    row.add( revert );
    row.add( new Strut() );
    row.add( execute );

    return row;
  }

  public String getText() { return editor.getText(); }

  public void setText( final String text )
  {
    editor.setText( text );
  }

  public File getFile() { return file; }

  public void setFile( final File file )
  {
    this.file = file;
    if ( ! file.exists() || file.isDirectory() ) return;

    try
    {
      setText( fromFile( file.getAbsolutePath() ) );
    }
    catch ( Throwable t )
    {
      getApplication().processFatalException( t );
    }
  }
}

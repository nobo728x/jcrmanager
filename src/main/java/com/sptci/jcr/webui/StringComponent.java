package com.sptci.jcr.webui;

import static com.sptci.ReflectionUtility.execute;
import static com.sptci.echo.Application.getApplication;
import static com.sptci.echo.Application.getParentView;
import com.sptci.echo.Column;
import static com.sptci.echo.Configuration.getString;
import static com.sptci.echo.Utilities.createLabel;
import static com.sptci.echo.Utilities.createTextArea;
import com.sptci.echo.View;

import jfix.echo.RichTextArea;
import nextapp.echo.app.Component;
import nextapp.echo.app.RadioButton;
import nextapp.echo.app.Row;
import nextapp.echo.app.TextArea;
import nextapp.echo.app.WindowPane;
import nextapp.echo.app.button.ButtonGroup;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 * A component that is used to display a text/string edit component to the
 * user.  Also displays a control to switch between a regular {@link
 * nextapp.echo.app.TextArea} or a {@link nextapp.echo.extras.app.RichTextArea}.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-10-10
 * @version $Id: StringComponent.java 38 2009-10-12 14:35:15Z spt $
 */
public class StringComponent extends Column
{
  private static final long serialVersionUID = 1L;

  /** The component that is used to display/edit the string value. */
  private Component editor;

  /** The control used to indicate plain text editor. */
  private RadioButton text;

  /** The control used to indicate html text editor. */
  private RadioButton html;

  /** A flag indicating the button to set as enabled. */
  private boolean textContent = true;

  /**
   * A string used to store the content to be displayed in the editor
   * components if the text is set before the components have been
   * initialised.
   */
  private String content;

  public void init()
  {
    removeAll();
    super.init();

    createText();
    createHtml();
    final ButtonGroup type = new ButtonGroup();
    text.setGroup( type );
    html.setGroup( type );

    final Row row = new Row();
    row.add( createLabel( getClass().getName(), "type") );
    row.add( text );
    row.add( html );
    add( row );

    add( createEditor() );
  }

  private RadioButton createText()
  {
    text = new RadioButton();
    text.setSelected( textContent );
    text.setText( getString( this, "text.label" ) );
    text.setToolTipText( getString( this, "text.tooltip" ) );
    text.addActionListener( new TextListener() );
    return text;
  }

  private RadioButton createHtml()
  {
    html = new RadioButton();
    html.setSelected( ! textContent );
    html.setText( getString( this, "html.label" ) );
    html.setToolTipText( getString( this, "html.tooltip" ) );
    html.addActionListener( new HtmlListener() );
    return html;
  }

  private Component createEditor()
  {
    editor = ( textContent ) ? createTextEditor() : createHtmlEditor();
    return editor;
  }

  private TextArea createTextEditor()
  {
    final TextArea ta = createTextArea( getClass().getName(), "editor" );
    
    if ( content != null )
    {
      ta.setText( content );
      content = null;
    }

    return ta;
  }

  private RichTextArea createHtmlEditor()
  {
    final RichTextArea ta = new RichTextArea();

    if ( content != null )
    {
      ta.setText( content );
      content = null;
    }

    return ta;
  }

  public String getText()
  {
    try
    {
      return (String) execute( editor, "getText" );
    }
    catch ( Throwable e )
    {
      getApplication().processFatalException( e );
    }

    return null;
  }

  public void setText( final String text )
  {
    if ( editor == null )
    {
      content = text;
      return;
    }
    
    try
    {
      execute( editor, "setText", text );
    }
    catch ( Throwable e )
    {
      getApplication().processFatalException( e );
    }
  }

  public boolean isTextContent() { return textContent; }

  public void setTextContent( final boolean textContent )
  {
    this.textContent = textContent;
  }

  /** The event listener for converting editor to a text area. */
  private class TextListener implements ActionListener
  {
    private static final long serialVersionUID = 1l;

    public void actionPerformed( final ActionEvent event )
    {
      if ( editor instanceof TextArea ) return;
      textContent = true;
      final String text = getText();

      int index = indexOf( editor );
      remove( editor );

      editor = createTextEditor();
      add( editor, index );
      setText( text );
    }
  }

  /** The event listener for converting editor to a rich text area. */
  private class HtmlListener implements ActionListener
  {
    private static final long serialVersionUID = 1l;

    public void actionPerformed( final ActionEvent event )
    {
      if ( editor instanceof RichTextArea ) return;
      textContent = false;
      final String text = getText();

      int index = indexOf( editor );
      remove( editor );

      editor = createHtmlEditor();
      add( editor, index );
      setText( text );
      getPane().userMaximize();
    }

    private WindowPane getPane()
    {
      View view = getParentView( StringComponent.this );

      while ( ! ( view instanceof WindowPane ) )
      {
        view = getParentView( (Component) view );
      }

      return (WindowPane) view;
    }
  }
}

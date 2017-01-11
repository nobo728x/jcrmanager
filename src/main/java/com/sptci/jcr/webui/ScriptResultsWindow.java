package com.sptci.jcr.webui;

import com.sptci.echo.WindowPane;

import nextapp.echo.app.Component;

/**
 * A window used to display JCR result objects returned after executing a
 * Groovy script.
 *
 * @author Rakesh Vidyadharan 2010-02-18
 * @version $Id: ScriptResultsWindow.java 66 2010-02-22 15:24:18Z spt $
 */
public class ScriptResultsWindow extends WindowPane
{
  private static final long serialVersionUID = 1L;

  private Component view;

  public ScriptResultsWindow( final Component view )
  {
    this.view = view;
  }

  @Override
  public void init()
  {
    removeAll();
    super.init();

    if ( view != null ) add( view );
  }

  public void setView( final Component view )
  {
    if ( getComponentCount() > 0 ) remove( 0 );
    this.view = view;
    add( view );
  }
}

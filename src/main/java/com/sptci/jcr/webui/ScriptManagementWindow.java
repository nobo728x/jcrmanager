package com.sptci.jcr.webui;

import com.sptci.echo.WindowPane;
import com.sptci.echo.style.Extent;
import com.sptci.jcr.webui.tree.ScriptTree;

import nextapp.echo.app.Column;
import nextapp.echo.app.Component;
import nextapp.echo.app.SplitPane;

import echopoint.DirectHtml;

import static com.sptci.echo.Dimensions.getInt;
import static java.lang.String.format;
import static nextapp.echo.app.Color.WHITE;

/**
 * A window that displays the available Groovy scripts and enables adding,
 * editing or deleting the scripts.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-01-11
 * @version $Id: ScriptManagementWindow.java 44 2010-01-12 21:01:38Z spt $
 */
public class ScriptManagementWindow extends WindowPane
{
  private static final long serialVersionUID = 1L;
  private ScriptTree tree;

  public void init()
  {
    setBackground( WHITE );
    removeAll();
    super.init();

    final SplitPane outer = new SplitPane();
    outer.setSeparatorPosition( Extent.getInstance( getInt( this, "outer.position" ) ) );
    outer.setResizable( true );
    createTree( outer );

    final SplitPane inner = new SplitPane( SplitPane.ORIENTATION_VERTICAL );
    inner.setSeparatorPosition( Extent.getInstance( getInt( this, "inner.position" ) ) );
    inner.setResizable( true );

    createEditor( inner );
    createOutput( inner );
    outer.add( inner );

    add( outer );
  }

  private void createTree( final Component parent )
  {
    tree = new ScriptTree();
    parent.add( tree );
  }

  private void createEditor( final Component parent )
  {
    parent.add( new Column() );
  }

  private void createOutput( final Component parent )
  {
    parent.add( new DirectHtml() );
  }

  public void setEditor( final Component component )
  {
    final Component parent = getComponent( 0 ).getComponent( 1 );
    parent.remove( 0 );
    parent.add( component, 0 );
  }

  public void setOutput( final String text )
  {
    final DirectHtml html = (DirectHtml) getComponent( 0 ).getComponent( 1 ).getComponent( 1 );
    html.setText( format( "%s<br/>%s", html.getText(), text ) );
  }

  public ScriptTree getTree() { return tree; }
}

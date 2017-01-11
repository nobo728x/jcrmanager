package com.sptci.jcr.webui;

import com.sptci.echo.Dimensions;
import com.sptci.echo.MenuFrame;
import com.sptci.jcr.webui.tree.JCRTree;

import nextapp.echo.app.Component;
import nextapp.echo.app.SplitPane;
import nextapp.echo.extras.app.MenuBarPane;

/**
 * The main content frame for the application.  Displays the standard header and
 * application menu and the split pane that displays the node tree and the node
 * details area.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_top'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-06-01
 * @version $Id: ContentArea.java 63 2010-02-16 15:23:00Z spt $
 */
public class ContentArea extends MenuFrame<MenuBarPane>
{
  private static final long serialVersionUID = 1L;

  private final SplitPane pane;

  public ContentArea()
  {
    MainController.init();
    pane = new SplitPane( SplitPane.ORIENTATION_VERTICAL,
        Dimensions.getExtent( this, "separatorPosition" ) );
    pane.setResizable( true );
  }

  @Override
  public void init()
  {
    super.init();
    pane.removeAll();

    setContent( pane );
    setMenuBar( createMenu() );
  }

  public JCRTree getTree()
  {
    if ( pane.getComponentCount() == 0 ) return null;
    return (JCRTree) pane.getComponent( 0 );
  }

  public void setTree( final JCRTree tree )
  {
    if ( pane.getComponentCount() > 0 ) pane.remove( 0 );
    if ( tree != null ) pane.add( tree, 0 );
  }

  public void setChild( final Component content )
  {
    if ( pane.getComponentCount() > 1 ) pane.remove( 1 );
    pane.add( content );
  }

  public void resetMenu()
  {
    setMenuBar( createMenu() );
  }

  private MenuBarPane createMenu()
  {
    return new MenuBar();
  }
}
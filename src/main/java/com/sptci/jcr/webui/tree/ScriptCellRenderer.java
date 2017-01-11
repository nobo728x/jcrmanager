package com.sptci.jcr.webui.tree;

import com.sptci.echo.tree.TreeCellRenderer;
import com.sptci.jcr.webui.listener.menu.ScriptContextMenuListener;

import nextapp.echo.app.Component;
import nextapp.echo.extras.app.ContextMenu;
import nextapp.echo.extras.app.Tree;
import nextapp.echo.extras.app.menu.DefaultMenuModel;
import nextapp.echo.extras.app.menu.MenuModel;
import nextapp.echo.extras.app.tree.TreePath;

import static com.sptci.echo.Configuration.getString;

import java.io.File;

/**
 * A cell renderer for attaching a context menu to the default cell component.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-01-11
 * @version $Id: ScriptCellRenderer.java 44 2010-01-12 21:01:38Z spt $
 */
public class ScriptCellRenderer extends TreeCellRenderer
{
  private static final long serialVersionUID = 1L;

  public static final String ADD_SCRIPT = "0";
  public static final String ADD_DIRECTORY = "1";
  public static final String RENAME = "2";
  public static final String DELETE = "3";

  private final ScriptContextMenuListener listener = new ScriptContextMenuListener();

  @Override
  public Component getTreeCellRendererComponent( final Tree tree,
      final TreePath treePath, final Object value, final int column,
      final int row, final boolean leaf )
  {
    final Component component = super.getTreeCellRendererComponent(
        tree, treePath, value, column, row, leaf );

    final ScriptNode node = (ScriptNode) treePath.getLastPathComponent();
    final ContextMenu menu =
        new ContextMenu( component, createMenu( node.getUserObject() ) );
    menu.setStyleName( "Default" );
    menu.addActionListener( listener );

    return menu;
  }

  private MenuModel createMenu( final File file )
  {
    final DefaultMenuModel model = new DefaultMenuModel();

    model.addItem( new ScriptContextMenuModel(
        ADD_SCRIPT, getString( this, "addScript.title" ), null, file ) );
    model.addItem( new ScriptContextMenuModel(
        ADD_DIRECTORY, getString( this, "addDirectory.title" ), null, file ) );
    model.addItem( new ScriptContextMenuModel(
        RENAME, getString( this, "rename.title" ), null, file ) );
    model.addItem( new ScriptContextMenuModel(
        DELETE, getString( this, "delete.title" ), null, file ) );

    return model;
  }
}

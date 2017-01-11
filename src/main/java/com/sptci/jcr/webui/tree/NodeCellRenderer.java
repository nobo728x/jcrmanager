package com.sptci.jcr.webui.tree;

import com.sptci.echo.tree.TreeCellRenderer;
import com.sptci.jcr.webui.listener.menu.NodeContextMenuListener;

import nextapp.echo.app.Component;
import nextapp.echo.extras.app.ContextMenu;
import nextapp.echo.extras.app.Tree;
import nextapp.echo.extras.app.menu.DefaultMenuModel;
import nextapp.echo.extras.app.menu.MenuModel;
import nextapp.echo.extras.app.tree.TreePath;

import static com.sptci.echo.Configuration.getString;

import javax.jcr.Node;

/**
 * A renderer used to display the tree nodes with a context menu for easy
 * access to management features.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-23
 * @version $Id: NodeCellRenderer.java 47 2010-01-21 20:38:45Z spt $
 */
public class NodeCellRenderer extends TreeCellRenderer
{
  private static final long serialVersionUID = 1L;

  public static final String ADD = "0";
  public static final String EDIT = "1";
  public static final String EXPORT = "2";
  public static final String IMPORT = "3";
  public static final String MOVE = "4";
  public static final String RENAME = "5";
  public static final String DELETE = "6";

  private final NodeContextMenuListener listener = new NodeContextMenuListener();

  @Override
  public Component getTreeCellRendererComponent( final Tree tree,
      final TreePath treePath, final Object value, final int column,
      final int row, final boolean leaf )
  {
    final Component component = super.getTreeCellRendererComponent(
        tree, treePath, value, column, row, leaf );

    if ( ! ( treePath.getLastPathComponent() instanceof JCRNode ) )
    {
      return component;
    }

    final JCRNode node = (JCRNode) treePath.getLastPathComponent();
    final ContextMenu menu =
        new ContextMenu( component, createMenu( node.getUserObject() ) );
    menu.setStyleName( "Default" );
    menu.addActionListener( listener );

    return menu;
  }

  private MenuModel createMenu( final Node node )
  {
    final DefaultMenuModel model = new DefaultMenuModel();

    model.addItem( new NodeContextMenuModel(
        ADD, getString( this, "add.title" ), null, node ) );
    model.addItem( new NodeContextMenuModel(
        EDIT, getString( this, "edit.title" ), null, node ) );
    model.addItem( new NodeContextMenuModel(
        EXPORT, getString( this, "export.title" ), null, node ) );
    model.addItem( new NodeContextMenuModel(
        IMPORT, getString( this, "import.title" ), null, node ) );
    model.addItem( new NodeContextMenuModel(
        MOVE, getString( this, "move.title" ), null, node ) );
    model.addItem( new NodeContextMenuModel(
        RENAME, getString( this, "rename.title" ), null, node ) );
    model.addItem( new NodeContextMenuModel(
        DELETE, getString( this, "delete.title" ), null, node ) );

    return model;
  }
}

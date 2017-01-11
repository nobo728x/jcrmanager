package com.sptci.jcr.webui.listener;

import com.sptci.echo.ErrorPane;
import com.sptci.jcr.webui.NodeRenameDialog;
import com.sptci.jcr.webui.TableView;
import com.sptci.jcr.webui.tree.AbstractNode;
import com.sptci.jcr.webui.tree.JCRNode;

import nextapp.echo.app.event.ActionEvent;

import static com.sptci.echo.Configuration.getString;
import static com.sptci.jcr.webui.MainController.getController;
import static java.lang.String.format;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * The event listener for renaming a selected node.  Renaming involves
 * moving a node at the same level in the tree.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-25
 * @version $Id: NodeRenameListener.java 47 2010-01-21 20:38:45Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class NodeRenameListener extends NodeManagementListener<NodeRenameDialog>
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    final NodeRenameDialog dialog = getView( event );

    if ( ! checkView( dialog ) )
    {
      final ErrorPane pane = new ErrorPane(
          getString( dialog, "error.title" ),
          getString( dialog, "error.message" ) );
      getApplication().addPane( pane );
      return;
    }

    rename( dialog );
  }

  private void rename( final NodeRenameDialog dialog )
  {
    try
    {
      final Node node = dialog.getNode();
      final Node parent = node.getParent();

      final JCRNode child = getNode( node );
      final String childPath = node.getPath();
      final AbstractNode<Node> parentNode = getParent( node );

      final String newName = rename( node, dialog.getName() );

      updateTable( dialog );
      removeFromTree( child, childPath, parentNode, parent );
      addToTree( getController().getNode( newName ) );

      dialog.userClose();
    }
    catch ( Throwable t )
    {
      processFatalException( t );
    }
  }

  private String rename( final Node node, final String name )
      throws RepositoryException
  {
    final Node parent = node.getParent();
    final String destination = format( "%s/%s", parent.getPath(), name );
    
    final Session session = getController().getSession();
    session.move( node.getPath(), destination );
    session.save();

    return destination;
  }

  private void updateTable( final NodeRenameDialog dialog )
  {
    final TableView view = dialog.getView();
    if ( view == null ) return;

    final int row = view.getTable().getSelectionModel().getMinSelectedIndex();
    view.getTable().getModel().updateRow( row, dialog.getNode() );
  }
}

package com.sptci.jcr.webui.listener;

import com.sptci.echo.ErrorPane;
import com.sptci.jcr.webui.NodeMoveDialog;
import com.sptci.jcr.webui.SubNodesView;
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
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-25
 * @version $Id: NodeMoveListener.java 47 2010-01-21 20:38:45Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class NodeMoveListener extends NodeManagementListener<NodeMoveDialog>
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    final NodeMoveDialog dialog = getView( event );

    if ( ! checkView( dialog ) )
    {
      final ErrorPane pane = new ErrorPane(
          getString( dialog, "error.title" ),
          getString( dialog, "error.message" ) );
      getApplication().addPane( pane );
      return;
    }

    move( dialog );
  }

  @Override
  protected boolean checkView( final NodeMoveDialog dialog )
  {
    boolean result = super.checkView( dialog );
    result = result && ( dialog.getPath().length() > 0 );
    return result;
  }

  private void move( final NodeMoveDialog dialog )
  {
    try
    {
      final Node node = dialog.getNode();
      final JCRNode child = getNode( node );
      final String childPath = node.getPath();
      final Node parent = node.getParent();
      final AbstractNode<Node> parentNode = getParent( node );

      final Node destination = getNode( dialog.getPath() );
      final String newPath = move( dialog.getNode(), destination );

      updateTable( dialog );
      removeFromTree( child, childPath, parentNode, parent );
      addToTree( getController().getNode( newPath ) );

      dialog.userClose();
    }
    catch ( Throwable t )
    {
      processFatalException( t );
    }
  }

  private String move( final Node source, final Node destination )
      throws RepositoryException
  {
    final String name =
        format( "%s/%s", destination.getPath(), source.getName() );

    final Session session = getController().getSession();
    session.move( source.getPath(), name );
    session.save();

    return name;
  }

  private void updateTable( final NodeMoveDialog dialog )
  {
    final TableView view = dialog.getView();
    if ( view == null ) return;

    final int row = view.getTable().getSelectionModel().getMinSelectedIndex();

    if ( view instanceof SubNodesView )
    {
      view.getTable().getModel().deleteRow( row );
    }
    else
    {
      view.getTable().getModel().updateRow( row, dialog.getNode() );
    }
  }
}

package com.sptci.jcr.webui.listener;

import com.sptci.echo.ErrorPane;
import com.sptci.echo.Executor;
import com.sptci.jcr.webui.NodeDeleteConfirmation;
import com.sptci.jcr.webui.NodeDeleteDialog;
import com.sptci.jcr.webui.TableView;
import com.sptci.jcr.webui.tree.AbstractNode;
import com.sptci.jcr.webui.tree.JCRNode;

import nextapp.echo.app.event.ActionEvent;

import static com.sptci.echo.Configuration.getString;
import static com.sptci.jcr.webui.MainController.getController;

import javax.jcr.Node;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;

/**
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-13
 * @version $Id: NodeDeleteListener.java 59 2010-02-09 18:07:17Z spt $
 */
public class NodeDeleteListener extends NodeManagementListener<NodeDeleteDialog>
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    final NodeDeleteDialog dialog = getView( event );

    if ( ! checkView( dialog ) )
    {
      final ErrorPane pane = new ErrorPane(
          getString( dialog, "error.title" ),
          getString( dialog, "error.message" ) );
      getApplication().addPane( pane );
      return;
    }

    delete( dialog );
  }

  @Override
  protected boolean checkView( final NodeDeleteDialog nodeDeleteDialog )
  {
    boolean result = super.checkView( nodeDeleteDialog );
    result = result && ( nodeDeleteDialog.getPath().length() > 1 );
    return result;
  }

  private void delete( final NodeDeleteDialog dialog )
  {
    try
    {
      final Node node = getNode( dialog.getPath() );
      final boolean hasWeakReferences = node.getWeakReferences().getSize() > 0;

      if ( node.getReferences().getSize() > 0 )
      {
        final Executor<NodeDeleteListener> executor =
            new Executor<NodeDeleteListener>( this, "deleteNode" );
        executor.addParameter( NodeDeleteDialog.class, dialog );
        executor.addParameter( Node.class, node );
        
        getApplication().addPane( new NodeDeleteConfirmation(
            getString( this, "references.title" ),
            getString( this, "references.message" ),
            executor, hasWeakReferences ) );
      }
      else if ( hasWeakReferences )
      {
        final Executor<NodeDeleteListener> executor =
            new Executor<NodeDeleteListener>( this, "deleteNode" );
        executor.addParameter( NodeDeleteDialog.class, dialog );
        executor.addParameter( Node.class, node );

        getApplication().addPane( new NodeDeleteConfirmation(
            getString( this, "weakReferences.title" ),
            getString( this, "weakReferences.message" ),
            executor, hasWeakReferences ) );
      }
      else
      {
        delete( dialog, node );
      }
    }
    catch ( Throwable t )
    {
      processFatalException( t );
    }
  }

  /**
   *  Call-back method used by the Executor in Confirmation.
   *
   * @param dialog The dialog from which the delete action was triggered.
   * @param node The node to be deleted.
   * @param deleteWeakReferences A flag indicating whether weak references
   *   to the node are to be deleted.
   */
  @SuppressWarnings( { "UnusedDeclaration" } )
  private void deleteNode( final NodeDeleteDialog dialog, final Node node,
      final boolean deleteWeakReferences )
  {
    try
    {
      PropertyIterator iter = node.getReferences();
      while ( iter.hasNext() )
      {
        iter.nextProperty().remove();
      }

      if ( deleteWeakReferences )
      {
        iter = node.getWeakReferences();
        while ( iter.hasNext() )
        {
          iter.nextProperty().remove();
        }
      }

      delete( dialog, node );
    }
    catch ( Throwable t )
    {
      processFatalException( t );
    }
  }

  private void delete( final NodeDeleteDialog dialog, final Node node )
      throws RepositoryException
  {
    final JCRNode child = getNode( node );
    final String childPath = node.getPath();
    final Node parent = node.getParent();
    final AbstractNode<Node> parentNode = getParent( node );

    delete( node );
    updateTable( dialog );
    removeFromTree( child, childPath, parentNode, parent );

    dialog.userClose();
  }

  private void delete( final Node node ) throws RepositoryException
  {
    node.remove();
    getController().getSession().save();
  }

  private void updateTable( final NodeDeleteDialog dialog )
  {
    final TableView view = dialog.getView();
    if ( view == null ) return;

    final int index = view.getTable().getSelectionModel().getMinSelectedIndex();
    view.getTable().getModel().deleteRow( index );
  }
}

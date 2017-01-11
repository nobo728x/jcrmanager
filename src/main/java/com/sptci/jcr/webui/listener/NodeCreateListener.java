package com.sptci.jcr.webui.listener;

import com.sptci.echo.ErrorPane;
import com.sptci.jcr.webui.FileUploadComponent;
import com.sptci.jcr.webui.NodeCreateDialog;
import com.sptci.jcr.webui.TableView;

import nextapp.echo.app.event.ActionEvent;

import static com.sptci.echo.Configuration.getString;
import static com.sptci.jcr.webui.MainController.getController;
import static org.apache.jackrabbit.JcrConstants.JCR_CONTENT;
import static org.apache.jackrabbit.JcrConstants.JCR_DATA;
import static org.apache.jackrabbit.JcrConstants.JCR_ENCODING;
import static org.apache.jackrabbit.JcrConstants.JCR_LASTMODIFIED;
import static org.apache.jackrabbit.JcrConstants.JCR_MIMETYPE;
import static org.apache.jackrabbit.JcrConstants.MIX_VERSIONABLE;
import static org.apache.jackrabbit.JcrConstants.NT_RESOURCE;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import java.util.Calendar;

/**
 * The event listener triggered from {@link com.sptci.jcr.webui.NodeCreateDialog}
 * to create a new node at a specified location in the repository.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-13
 * @version $Id: NodeCreateListener.java 59 2010-02-09 18:07:17Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class NodeCreateListener extends NodeManagementListener<NodeCreateDialog>
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    final NodeCreateDialog dialog = getView( event );

    if ( ! checkView( dialog ) )
    {
      final ErrorPane pane = new ErrorPane(
          getString( dialog, "error.title" ),
          getString( dialog, "error.message" ) );
      getApplication().addPane( pane );
      return;
    }

    save( dialog );
  }

  @Override
  protected boolean checkView( final NodeCreateDialog nodeCreateDialog )
  {
    boolean result = super.checkView( nodeCreateDialog );
    result = result && ( nodeCreateDialog.getPath().length() > 0 );
    return result;
  }

  private void save( final NodeCreateDialog dialog )
  {
    try
    {
      final Node node = create( dialog );
      addToTree( node );
      updateTable( dialog, node );
      dialog.userClose();
    }
    catch ( Throwable t )
    {
      getController().displayException( t );
    }
  }

  private Node create( final NodeCreateDialog dialog ) throws RepositoryException
  {
    final Node parent = getNode( dialog.getPath() );
    if ( getController().isVersionable( parent ) )
    {
      getController().getVersionManager().checkout( parent.getPath() );
    }

    final Node node = parent.addNode(
        dialog.getName(), dialog.getNodeType() );

    if ( dialog.getUpload() != null ) createFile( node, dialog );

    boolean versionable = false;
    for ( final String type : dialog.getMixinTypes() )
    {
      if ( MIX_VERSIONABLE.equals( type ) ) versionable = true;
      node.addMixin( type );
    }

    getController().getSession().save();
    if ( versionable )
    {
      getController().getVersionManager().checkin( node.getPath() );
    }

    return node;
  }

  private void createFile( final Node node, final NodeCreateDialog dialog )
      throws RepositoryException
  {
    final FileUploadComponent upload = dialog.getUpload();
    final Node resNode = node.addNode( JCR_CONTENT, NT_RESOURCE );
    resNode.setProperty( JCR_MIMETYPE, upload.getContentType() );
    resNode.setProperty( JCR_ENCODING, "" );
    resNode.setProperty( JCR_DATA,
        node.getSession().getValueFactory().createBinary( upload.getInputStream() ) );
    resNode.setProperty( JCR_LASTMODIFIED,
        Calendar.getInstance( getApplication().getTimeZone() ) );
    upload.getFile().delete();
  }

  private void updateTable( final NodeCreateDialog dialog, final Node node )
  {
    final TableView view = dialog.getView();
    if ( view == null ) return;

    view.getTable().getModel().addRow( node );
  }
}

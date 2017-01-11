package com.sptci.jcr.webui.listener;

import com.sptci.echo.Confirmation;
import com.sptci.echo.Executor;
import com.sptci.jcr.webui.PropertiesView;
import com.sptci.jcr.webui.table.PropertiesTable;
import com.sptci.jcr.webui.table.PropertiesTableModel;

import nextapp.echo.app.event.ActionEvent;

import static com.sptci.echo.Configuration.getString;
import static com.sptci.jcr.webui.MainController.getController;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import java.util.Collection;

/**
 * The event listener for deleting properties that have been selected in
 * the properties view component table.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-17
 * @version $Id: PropertyDeleteListener.java 59 2010-02-09 18:07:17Z spt $
 */
public class PropertyDeleteListener extends PropertyManagementListener
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    final PropertiesView view = getView( event );
    final Collection<Property> properties = getProperties( view.getProperties() );

    if ( ! checkSelected( view, properties ) ) return;

    try
    {
      deleteProperties( view, properties );
    }
    catch ( Throwable t )
    {
      processFatalException( t );
    }
  }

  private void deleteProperties( final PropertiesView view,
      final Collection<Property> properties )
    throws RepositoryException
  {
    final Session session = getController().getSession();
    final PropertiesTable<PropertiesTableModel> table = view.getProperties();
    final Node node = table.getModel().getNode();
    final boolean versionable = getController().isVersionable( node );

    if ( versionable && ! node.isCheckedOut() )
    {
      getController().getVersionManager().checkout( node.getPath() );
    }

    for ( final Property property : properties )
    {
      table.deleteRow( property );
      property.remove();
    }

    session.save();
    
    if ( versionable ) displayCheckin( node );
  }

  private void displayCheckin( final Node node )
  {
    final Executor<PropertyDeleteListener> executor =
        new Executor<PropertyDeleteListener>( this, "checkin" );
    executor.addParameter( Node.class, node );
    final Confirmation confirmation = new Confirmation(
        getString( this, "checkin.title" ),
        getString( this, "checkin.message" ), executor );
    getApplication().addPane( confirmation );
  }

  @SuppressWarnings( { "UnusedDeclaration" } )
  protected void checkin( final Node node )
  {
    try
    {
      getController().getVersionManager().checkin( node.getPath() );
      getController().displayNode( node );
    }
    catch ( RepositoryException e )
    {
      processFatalException( e );
    }
  }
}

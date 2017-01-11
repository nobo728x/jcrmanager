package com.sptci.jcr.webui;

import com.sptci.echo.table.TableContainer;
import com.sptci.jcr.webui.table.NodesTable;
import com.sptci.jcr.webui.table.PropertiesTable;
import com.sptci.jcr.webui.table.PropertiesTableModel;
import com.sptci.jcr.webui.table.ReferencesTableModel;
import com.sptci.jcr.webui.table.SubNodesTableModel;
import com.sptci.jcr.webui.table.SystemPropertiesTableModel;
import com.sptci.jcr.webui.table.VersionsTable;
import com.sptci.jcr.webui.table.VersionsTableModel;
import com.sptci.jcr.webui.table.WeakReferencesTableModel;

import nextapp.echo.app.Label;

import static com.sptci.jcr.webui.MainController.getController;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.version.Version;

/**
 * A controller used to configure a {@link NodeView} from a {@link
 * javax.jcr.Node} object. <p/> <p>&copy; Copyright 2009 <a
 * href='http://sptci.com/' target='_new'>Sans Pareil Technologies,
 * Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-12
 * @version $Id: NodeViewController.java 66 2010-02-22 15:24:18Z spt $
 */
public class NodeViewController
{
  /** The node that is to be visually depicted. */
  private Node node;

  /** The view that is controlled by this controller. */
  private NodeView view;

  public NodeViewController( final Node node ) { setNode( node ); }

  private void createView()
  {
    view = new NodeView();

    try
    {
      addMetadata();
      addSystemProperties();
      addProperties();
      addNodes();
      addReferences();
      addWeakReferences();
      addVersions();
    }
    catch ( RepositoryException e )
    {
      getController().displayException( e );
    }
  }

  private void addMetadata() throws RepositoryException
  {
    Label label = new Label( "Name" );
    label.setStyleName( "Bold.Label" );
    view.addMetadata( label );
    
    view.addMetadata( new Label( node.getName() ) );

    label = new Label( "Path" );
    label.setStyleName( "Bold.Label" );
    view.addMetadata( label );

    view.addMetadata( new Label( node.getPath() ) );

    label = new Label( "Identifier" );
    label.setStyleName( "Bold.Label" );
    view.addMetadata( label );

    view.addMetadata( new Label( node.getIdentifier() ) );
  }

  private void addSystemProperties() throws RepositoryException
  {
    if ( ! node.hasProperties() ) return;

    final SystemPropertiesTableModel model = new SystemPropertiesTableModel( node );
    final PropertiesTable<SystemPropertiesTableModel> table =
        new PropertiesTable<SystemPropertiesTableModel>( model );
    view.addSystemProperty( table );
  }

  private void addProperties() throws RepositoryException
  {
    if ( ! node.hasProperties() ) return;

    final PropertiesTableModel model = new PropertiesTableModel( node );
    final PropertiesTable<PropertiesTableModel> table =
        new PropertiesTable<PropertiesTableModel>( model );
    view.addProperty( new PropertiesView( table ) );
  }

  private void addNodes() throws RepositoryException
  {
    if ( ! node.hasNodes() ) return;

    final SubNodesTableModel model = new SubNodesTableModel( node );
    final SubNodesView nodes = new SubNodesView(
        new TableContainer<Node>( new NodesTable( model ) ) );
    view.setNodes( nodes );
  }

  private void addReferences() throws RepositoryException
  {
    if ( node.getReferences().getSize() == 0 ) return;
    
    final ReferencesTableModel model = new ReferencesTableModel( node );
    view.setReferences( new TableContainer<Node>( new NodesTable( model ) ) );
  }

  private void addWeakReferences() throws RepositoryException
  {
    if ( node.getWeakReferences().getSize() == 0 ) return;

    final WeakReferencesTableModel model = new WeakReferencesTableModel( node );
    view.setWeakReferences( new TableContainer<Node>( new NodesTable( model ) ) );
  }

  private void addVersions() throws RepositoryException
  {
    if ( ! getController().isVersionable( node ) ) return;

    final VersionsTableModel model = new VersionsTableModel( node );
    final VersionsTable table = new VersionsTable( model );
    view.setVersions( new VersionsView( new TableContainer<Version>( table ) ) );
  }

  public NodeView getView()
  {
    if ( view == null ) createView();
    return view;
  }

  public Node getNode()
  {
    return node;
  }

  public void setNode( final Node node )
  {
    this.node = node;
  }
}

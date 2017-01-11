package com.sptci.jcr.webui;

import com.sptci.echo.Column;
import com.sptci.echo.View;
import com.sptci.echo.table.TableContainer;
import com.sptci.jcr.webui.table.PropertiesTable;
import com.sptci.jcr.webui.table.SystemPropertiesTableModel;

import nextapp.echo.app.Border;
import nextapp.echo.app.Component;
import nextapp.echo.app.Grid;
import nextapp.echo.app.Label;
import nextapp.echo.extras.app.TabPane;
import nextapp.echo.extras.app.layout.TabPaneLayoutData;

import static com.sptci.echo.Configuration.getString;
import static com.sptci.echo.style.Extent.PERCENT;
import static com.sptci.echo.style.Extent.getInstance;
import static nextapp.echo.app.Border.STYLE_SOLID;
import static nextapp.echo.app.Color.BLACK;

import javax.jcr.Node;

/**
 * A view component used to represent a JCR node.  This component should
 * be accessed through the associated {@link NodeViewController}.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh 2009-08-12
 * @version $Id: NodeView.java 59 2010-02-09 18:07:17Z spt $
 */
public class NodeView extends TabPane implements View
{
  private static final long serialVersionUID = 1L;

  private Grid metadata;
  private Column systemProperties;
  private PropertiesView properties;
  private SubNodesView nodes;
  private TableContainer<Node> references;
  private TableContainer<Node> weakReferences;
  private VersionsView versions;

  /** @see com.sptci.jcr.webui.NodeViewController#getView() */
  protected NodeView() {}

  public void addMetadata( final Component component )
  {
    if ( metadata == null ) createMetadata();
    metadata.add( component );
  }

  private void createMetadata()
  {
    metadata = new Grid( 2 );
    metadata.setWidth( getInstance( 100, PERCENT ) );
    metadata.setBorder( new Border( 1, BLACK, STYLE_SOLID ) );
    addName( metadata );
    addValue( metadata );

    final Column column = new Column();
    column.add( createLabel( "title" ) );
    column.add( metadata );

    final TabPaneLayoutData layout = new TabPaneLayoutData();
    layout.setTitle( getString( this, "tab.metadata" ) );
    column.setLayoutData( layout );

    add( column );
  }

  public void addSystemProperty(
      final PropertiesTable<SystemPropertiesTableModel> table )
  {
    if ( systemProperties == null ) createSystemProperties();

    if ( systemProperties.getComponentCount() > 1 )
    {
      systemProperties.remove( 1 );
    }

    systemProperties.add( table );
  }

  private void createSystemProperties()
  {
    systemProperties = new Column();
    systemProperties.add( createLabel( "system.property.title" ) );

    final Column column = new Column();
    column.add( systemProperties );
    
    final TabPaneLayoutData layout = new TabPaneLayoutData();
    layout.setTitle( getString( this, "tab.system" ) );
    column.setLayoutData( layout );

    add( column );
  }

  public void addProperty( final PropertiesView component )
  {
    if ( properties != null )
    {
      remove( properties );
    }

    properties = component;

    final TabPaneLayoutData layout = new TabPaneLayoutData();
    layout.setTitle( getString( this, "tab.properties" ) );
    properties.setLayoutData( layout );

    add( properties );
  }

  public void setNodes( final SubNodesView component )
  {
    if ( nodes != null )
    {
      remove( nodes );
    }

    this.nodes = component;

    final TabPaneLayoutData layout = new TabPaneLayoutData();
    layout.setTitle( getString( this, "tab.nodes" ) );
    component.setLayoutData( layout );

    add( component );
  }

  public void setReferences( final TableContainer<Node> component )
  {
    if ( references != null )
    {
      remove( references.getParent() );
    }

    references = component;
    final Column column = new Column();
    column.add( createLabel( "references.title" ) );
    column.add( component );

    final TabPaneLayoutData layout = new TabPaneLayoutData();
    layout.setTitle( getString( this, "tab.references" ) );
    column.setLayoutData( layout );

    add( column );
  }

  public void setWeakReferences( final TableContainer<Node> component )
  {
    if ( weakReferences != null )
    {
      remove( weakReferences.getParent() );
    }

    weakReferences = component;
    final Column column = new Column();
    column.add( createLabel( "weakReferences.title" ) );
    column.add( component );

    final TabPaneLayoutData layout = new TabPaneLayoutData();
    layout.setTitle( getString( this, "tab.weakReferences" ) );
    column.setLayoutData( layout );

    add( column );
  }

  public void setVersions( final VersionsView component )
  {
    if ( versions != null )
    {
      remove( versions );
    }

    versions = component;

    final TabPaneLayoutData layout = new TabPaneLayoutData();
    layout.setTitle( getString( this, "tab.versions" ) );
    versions.setLayoutData( layout );

    add( component );
  }

  private void addName( final Grid grid )
  {
    grid.add( createLabel( "name.label" ) );
  }

  private void addValue( final Grid grid )
  {
    grid.add( createLabel( "value.label" ) );
  }

  private Label createLabel( final String key )
  {
    final Label label = new Label( getString( this, key ) );
    label.setStyleName( "Bold.Label" );

    return label;
  }
}

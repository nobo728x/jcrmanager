package com.sptci.jcr.webui;

import com.sptci.echo.Column;
import static com.sptci.echo.Utilities.createLabel;
import com.sptci.echo.annotation.ActionListener;
import com.sptci.echo.binding.ViewInitialiser;
import com.sptci.echo.table.TableContainer;
import com.sptci.jcr.webui.table.NodesTable;
import com.sptci.jcr.webui.table.SubNodesTableModel;

import echopoint.Strut;
import nextapp.echo.app.Button;
import nextapp.echo.app.Row;

import javax.jcr.Node;

/**
 * A view used to display all the sub-nodes for a parent node as well as
 * controls for managing the sub-nodes.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-09-19
 * @version $Id: SubNodesView.java 33 2009-09-21 14:28:28Z spt $
 */
public class SubNodesView extends Column implements TableView
{
  private static final long serialVersionUID = 1l;

  private TableContainer<Node> nodes;

  @ActionListener( value = "com.sptci.jcr.webui.listener.SubNodesExportListener" )
  private Button export;

  @ActionListener( value = "com.sptci.jcr.webui.listener.SubNodesImportListener" )
  private Button imp;

  @ActionListener( value = "com.sptci.jcr.webui.listener.SubNodesAddListener" )
  private Button add;

  @ActionListener( value = "com.sptci.jcr.webui.listener.SubNodesRenameListener" )
  private Button rename;

  @ActionListener( value = "com.sptci.jcr.webui.listener.SubNodesMoveListener" )
  private Button move;

  @ActionListener( value = "com.sptci.jcr.webui.listener.SubNodesDeleteListener" )
  private Button delete;

  public SubNodesView( final TableContainer<Node> nodes )
  {
    this.nodes = nodes;
    new ViewInitialiser<SubNodesView>( this ).init();
  }

  @Override
  public void init()
  {
    removeAll();
    super.init();

    add( createLabel( getClass().getName(), "title", "Bold.Label" ) );
    add( nodes );

    add( new Strut() );
    createControls();
  }

  private void createControls()
  {
    final Row row = new Row();
    row.add( export );
    row.add( imp );

    row.add( new Strut( 20, 10 ) );
    row.add( rename );
    row.add( move );

    row.add( new Strut( 20, 10 ) );
    row.add( add );
    row.add( delete );

    add( row );
  }

  public NodesTable getTable() { return (NodesTable) nodes.getTable(); }

  public SubNodesTableModel getModel() { return (SubNodesTableModel) nodes.getTable().getModel(); }
}

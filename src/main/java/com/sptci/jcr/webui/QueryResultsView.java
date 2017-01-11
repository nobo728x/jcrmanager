package com.sptci.jcr.webui;

import com.sptci.echo.Column;
import com.sptci.echo.annotation.ActionListener;
import com.sptci.echo.binding.ViewInitialiser;
import com.sptci.echo.table.TableContainer;
import com.sptci.jcr.webui.table.NodesTable;
import com.sptci.jcr.webui.table.QueryResultTableModel;

import nextapp.echo.app.Button;
import nextapp.echo.app.Row;

import echopoint.HtmlLabel;
import echopoint.Strut;

import static java.lang.String.format;

import javax.jcr.Node;
import javax.jcr.query.QueryResult;

/**
 * A component that displays the results of a query (or search).  Displays
 * the resulting nodes, as well as controls to operate on the resulting
 * nodes.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh 2009-09-10
 * @version $Id: QueryResultsView.java 66 2010-02-22 15:24:18Z spt $
 */
public class QueryResultsView extends Column implements TableView
{
  private static final long serialVersionUID = 1L;

  private final boolean toolBar;

  private final HtmlLabel time = new HtmlLabel();
  private final QueryResultTableModel model;
  private final TableContainer<Node> container;

  @ActionListener( value = "com.sptci.jcr.webui.listener.QueryResultsExportListener" )
  private Button export;

  @ActionListener( value = "com.sptci.jcr.webui.listener.QueryResultsImportListener" )
  private Button imp;

  @ActionListener( value = "com.sptci.jcr.webui.listener.QueryResultsAddListener" )
  private Button add;

  @ActionListener( value = "com.sptci.jcr.webui.listener.QueryResultsRenameListener" )
  private Button rename;

  @ActionListener( value = "com.sptci.jcr.webui.listener.QueryResultsMoveListener" )
  private Button move;

  @ActionListener( value = "com.sptci.jcr.webui.listener.QueryResultsDeleteListener" )
  private Button delete;

  public QueryResultsView( final QueryResult results, final double time,
      final boolean toolBar )
  {
    this.toolBar = toolBar;
    new ViewInitialiser<QueryResultsView>( this ).init();
    setTime( time );
    
    model = new QueryResultTableModel( results );
    container = new TableContainer<Node>( new NodesTable( model ) );
  }

  @Override
  public void init()
  {
    removeAll();
    super.init();

    add( time );
    add( container );

    if ( toolBar )
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
  }

  public void setTime( final double time )
  {
    this.time.setText(
        format( "<b>Query execution time:</b> <i>%g s</i>", time / 1000.0 ) );
  }

  public QueryResultTableModel getModel() { return model; }

  public NodesTable getTable() { return (NodesTable) container.getTable(); }
}

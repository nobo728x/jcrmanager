package com.sptci.jcr.webui;

import com.sptci.echo.annotation.ActionListener;
import com.sptci.echo.annotation.Constraints;
import com.sptci.echo.binding.ViewInitialiser;
import com.sptci.echo.list.EnumListModel;
import com.sptci.echo.list.SelectField;

import nextapp.echo.app.Button;
import nextapp.echo.app.Label;
import nextapp.echo.app.Row;
import nextapp.echo.app.SplitPane;
import nextapp.echo.app.TextArea;

import static com.sptci.echo.Application.getApplication;
import static nextapp.echo.app.SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP;

/**
 * A floating pane used to display a JCR query interface for executing queries and viewing
 * the resulting nodes.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh 2009-08-11
 * @version $Id: QueryWindow.java 63 2010-02-16 15:23:00Z spt $
 */
public class QueryWindow extends AbstractQueryWindow
{
  private static final long serialVersionUID = 1L;

  public enum QueryTypes { XPath, SQL, SQL2 }
  
  @Constraints( value = Constraints.Value.NOT_NULL )
  protected TextArea query;

  private Label queryTypeLabel;
  private SelectField<EnumListModel<QueryTypes>> queryType;

  @ActionListener( value = "com.sptci.jcr.webui.listener.QueryHistoryListener" )
  private Button history;

  @ActionListener( value = "com.sptci.jcr.webui.listener.QuerySaveDialogListener" )
  private Button save;

  public QueryWindow()
  {
    new ViewInitialiser<QueryWindow>( this ).init();
  }

  @Override
  public void init()
  {
    super.init();
    getApplication().setFocusedComponent( query );
  }

  @Override
  protected SplitPane createTop()
  {
    final SplitPane top = new SplitPane( ORIENTATION_VERTICAL_BOTTOM_TOP );
    top.setAutoPositioned( true );

    final Row row = new Row();
    row.add( queryTypeLabel );
    row.add( createQueryType() );
    row.add( execute );
    row.add( history );
    row.add( save );

    top.add( row );
    top.add( query );
    
    return top;
  }

  private SelectField<EnumListModel<QueryTypes>> createQueryType()
  {
    queryType.setModel( new EnumListModel<QueryTypes>( QueryTypes.class ) );
    return queryType;
  }

  /** @return The query type as selected in the select field. */
  public QueryTypes getQueryType()
  {
    return (QueryTypes) queryType.getSelectedItem();
  }

  @Override
  public String getQuery() { return query.getText(); }

  @Override
  public void setQuery( final String query ) { this.query.setText( query ); }
}

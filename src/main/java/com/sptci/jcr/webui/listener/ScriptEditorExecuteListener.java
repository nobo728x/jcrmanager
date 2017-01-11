package com.sptci.jcr.webui.listener;

import com.sptci.echo.table.TableContainer;
import com.sptci.jcr.webui.NodeViewController;
import com.sptci.jcr.webui.QueryResultsView;
import com.sptci.jcr.webui.ScriptEditorComponent;
import com.sptci.jcr.webui.ScriptManagementWindow;
import com.sptci.jcr.webui.ScriptResultsWindow;
import com.sptci.jcr.webui.table.NodeIteratorImpl;
import com.sptci.jcr.webui.table.NodeIteratorTableModel;
import com.sptci.jcr.webui.table.NodesTable;
import com.sptci.jcr.webui.table.PropertiesTable;
import com.sptci.jcr.webui.table.PropertyIteratorImpl;
import com.sptci.jcr.webui.table.PropertyIteratorTableModel;

import nextapp.echo.app.Component;

import static com.sptci.jcr.webui.MainController.getController;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import org.apache.jackrabbit.core.LazyItemIterator;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.query.QueryResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * The event listener for executing the contents of the editor area in the
 * {@link com.sptci.jcr.webui.ScriptEditorComponent}.  Note that it is not
 * necessary to save the contents of the area (nor is it saved automatically)
 * to execute the script.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-01-11
 * @version $Id: ScriptEditorExecuteListener.java 66 2010-02-22 15:24:18Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class ScriptEditorExecuteListener extends ScriptEditorListener
{
  private static final long serialVersionUID = 1L;
  private long time;

  @Override
  protected void process( final ScriptEditorComponent editor,
      final ScriptManagementWindow window )
  {
    try
    {
      final Object result = run( editor.getText() );
      window.setOutput( format( "%s: Result of executing script from file: %s is: %s",
          sdf.format( new Date() ), editor.getFile(), result ) );
      processResult( result );
    }
    catch ( Throwable t )
    {
      processFatalException( format( "Error executing script: %s", editor.getText() ), t );
    }
  }

  public Object run( final String text ) throws Throwable
  {
    final long start = currentTimeMillis();
    final Binding binding = new Binding();
    binding.setVariable( "session", getController().getSession() );

    final GroovyShell shell = new GroovyShell( binding );
    final Script script = shell.parse( text );
    final Object result = script.run();

    time = currentTimeMillis() - start;
    return result;
  }

  private void processResult( final Object result )
  {
    Component view = null;

    if ( result instanceof QueryResult )
    {
      view = new QueryResultsView( (QueryResult) result, time, false );
    }
    else if ( result instanceof LazyItemIterator )
    {
      view = processResult( (LazyItemIterator) result );
    }
    else if ( result instanceof PropertyIterator )
    {
      view = processResult( (PropertyIterator) result );
    }
    else if ( result instanceof NodeIterator )
    {
      view = processResult( (NodeIterator) result );
    }
    else if ( result instanceof Node )
    {
      final NodeViewController controller = new NodeViewController( (Node) result );
      view = controller.getView();
    }

    if ( view != null )
    {
      getApplication().addPane( new ScriptResultsWindow( view ) );
    }
  }

  @SuppressWarnings( { "unchecked" } )
  private Component processResult( final LazyItemIterator iterator )
  {
    final ArrayList list = new ArrayList();

    while ( iterator.hasNext() )
    {
      list.add( iterator.next() );
    }

    return ( list.get( 0 ) instanceof Node ) ?
        processResult( new NodeIteratorImpl( (Collection<Node>) list ) ) :
        processResult( new PropertyIteratorImpl( (Collection<Property>) list ) );
  }

  private Component processResult( final PropertyIterator result )
  {
    return new PropertiesTable<PropertyIteratorTableModel>(
        new PropertyIteratorTableModel( result ) );
  }

  private Component processResult( final NodeIterator result )
  {
    return new TableContainer<Node>( new NodesTable(
        new NodeIteratorTableModel( result ) ) );
  }
}

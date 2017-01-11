package com.sptci.jcr.webui.listener;

import com.sptci.jcr.SessionManager;
import com.sptci.jcr.webui.NodeExportDialog;

import nextapp.echo.app.event.ActionEvent;

import echopoint.tucana.AbstractDownloadProvider;
import echopoint.tucana.DownloadCommand;
import echopoint.tucana.InputStreamDownloadProvider;
import echopoint.tucana.Status;
import echopoint.tucana.event.DownloadCallbackAdapter;

import static com.sptci.io.FileUtilities.END_OF_LINE;
import static com.sptci.jcr.webui.MainController.getController;
import static com.sptci.util.StringUtilities.toXMLAttribute;
import static com.sptci.util.StringUtilities.toXMLCharacters;
import static java.lang.String.format;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getAnonymousLogger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.jcr.Credentials;
import javax.jcr.Node;
import javax.jcr.Session;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * The event listener to use to export a node hierarchy from the specified
 * node.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-24
 * @version $Id: NodeExportListener.java 83 2013-07-24 19:33:50Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class NodeExportListener extends NodeManagementListener<NodeExportDialog>
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    final NodeExportDialog dialog = getView( event );
    export( dialog );
  }

  private void export( final NodeExportDialog dialog )
  {
    final Node node = dialog.getNode();

    try
    {
      final PipedOutputStream out = new PipedOutputStream();
      final PipedInputStream in = new PipedInputStream( out );
      new Output( out, getController().getCredentials(),
          node.getSession().getWorkspace().getName(), node.getPath(),
          dialog.isBinary(), dialog.isRecursive(),
          dialog.isSystemView() ).start();

      final AbstractDownloadProvider provider = ( dialog.isFormat() ) ?
          new FormatXMLDownloadProvider( in ) :
          new InputStreamDownloadProvider( in );
      final String name = ( !node.getName().isEmpty() ) ? node.getName() : "repository";
      provider.setFileName( format( "%s.xml", name ) );
      provider.setContentType( "application/xml" );

      final DownloadCommand command = new DownloadCommand( provider );
      command.setCallback( new DownloadCallbackAdapter() );
      getApplication().enqueueCommand( command );
      dialog.userClose();
    }
    catch ( Throwable t )
    {
      processFatalException( t );
    }
  }

  /** A thread used to write to a piped output stream. */
  private static class Output extends Thread
  {
    private final PipedOutputStream out;
    private final Credentials credentials;
    private final String workspace;
    private final String path;
    private final boolean binary;
    private final boolean recursive;
    private final boolean systemView;

    private Output( final PipedOutputStream out, final Credentials credentials,
        final String workspace, final String path, final boolean binary,
        final boolean recursive, final boolean systemView )
    {
      this.out = out;
      this.credentials = credentials;
      this.workspace = workspace;
      this.path = path;
      this.binary = binary;
      this.recursive = recursive;
      this.systemView = systemView;
    }

    @Override
    public void run()
    {
      Session session = null;

      try
      {
        session = new SessionManager().getSession( credentials, workspace );
        if ( systemView ) session.exportSystemView( path, out, ! binary, ! recursive );
        else session.exportDocumentView( path, out, ! binary, ! recursive );
      }
      catch ( Throwable t )
      {
        getAnonymousLogger().log( SEVERE,
            format( "Error exporting node: %s in workspace: %s", path, workspace ), t );
      }
      finally
      {
        if ( session != null ) session.logout();

        try
        {
          out.close();
        }
        catch ( Throwable t ) { /* ignore */ }
      }
    }
  }

  /** A download provider that writes formatted XML. */
  private class FormatXMLDownloadProvider extends AbstractDownloadProvider
  {
    private static final long serialVersionUID = 1L;

    private transient final InputStream stream;

    public FormatXMLDownloadProvider( final InputStream stream )
    {
      this.stream = stream;
    }

    public String getContentType()
    {
      return ( contentType == null ) ? "application/xml" : contentType;
    }

    public void writeFile( final OutputStream out ) throws IOException
    {
      status = Status.inprogress;
      final Handler handler = new Handler( out );

      try
      {
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        parser.parse( stream, handler );
        stream.close();
      }
      catch ( Throwable t )
      {
        status = Status.failed;
        processFatalException( t );
      }

      status = Status.completed;
    }
  }

  /** A simple SAX handler to add new lines to end of elements. */
  private class Handler extends DefaultHandler
  {
    private final OutputStreamWriter out;
    private int indent = 0;
    private String previous = "";

    public Handler( final OutputStream out )
    {
      this.out = new OutputStreamWriter( out );
    }

    private void write( final String text ) throws SAXException
    {
      try
      {
        out.write( text );
      }
      catch ( Exception e )
      {
        throw new SAXException( e );
      }
    }

    private void eol() throws SAXException
    {
      write( END_OF_LINE );
    }

    private void writeIndent( final int value ) throws SAXException
    {
      eol();
      for ( int i = 0; i < value; ++i )
      {
        write( "  " );
      }
    }

    private void flush() throws SAXException
    {
      try
      {
        out.flush();
      }
      catch ( Exception e )
      {
        throw new SAXException( e );
      }
    }

    @Override
    public void startDocument() throws SAXException
    {
      previous = "document";
      write( "<?xml version='1.0' encoding='UTF-8'?>" );
    }

    @Override
    public void endDocument() throws SAXException
    {
      try
      {
        eol();
        out.flush();
        out.close();
      }
      catch ( Exception e )
      {
        throw new SAXException( e );
      }
    }

    @Override
    public void startElement( final String uri, final String localName,
        final String qName, final Attributes attributes ) throws SAXException
    {
      previous = "start";
      writeIndent( indent++ );

      write( "<" );
      write( qName );

      for ( int i = 0; i < attributes.getLength(); ++i )
      {
        write( " " );
        write( attributes.getQName( i ) );
        write( "='" );
        write( toXMLAttribute( attributes.getValue( i ) ) );
        write( "'" );
      }

      write( ">" );
    }

    @Override
    public void endElement( final String uri, final String localName, final String qName ) throws SAXException
    {
      indent--;
      if ( "end".equals( previous ) ) writeIndent( indent );

      write( "</" );
      write( qName );
      write( ">" );
      flush();

      previous = "end";
    }

    @Override
    public void characters( final char[] ch, final int start,
        final int length ) throws SAXException
    {
      try
      {
        out.write( toXMLCharacters( ch, start, length ) );
      }
      catch ( IOException e )
      {
        throw new SAXException( e );
      }
    }
  }
}
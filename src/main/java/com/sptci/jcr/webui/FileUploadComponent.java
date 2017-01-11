package com.sptci.jcr.webui;

import static com.sptci.echo.Application.getApplication;
import com.sptci.echo.Column;
import static com.sptci.echo.Utilities.createLabel;
import com.sptci.echo.style.Color;
import com.sptci.echo.style.Extent;
import static com.sptci.jcr.webui.MainController.getController;

import echopoint.DirectHtml;
import echopoint.tucana.ButtonDisplay;
import echopoint.tucana.ButtonMode;
import echopoint.tucana.FileUploadSelector;
import echopoint.tucana.ProgressBar;
import static echopoint.tucana.UploadSPI.NO_SIZE_LIMIT;
import echopoint.tucana.event.DefaultUploadCallback;
import echopoint.tucana.event.InvalidContentTypeEvent;
import echopoint.tucana.event.UploadCancelEvent;
import echopoint.tucana.event.UploadFailEvent;
import echopoint.tucana.event.UploadFinishEvent;
import nextapp.echo.app.Border;
import nextapp.echo.app.Button;
import nextapp.echo.app.Label;
import nextapp.echo.app.TextField;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.System.getProperty;
import static java.util.logging.Level.FINE;

/**
 * A component used to display a file upload selector for uploading files
 * to use a node properties or as node resource.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-21
 * @version $Id: FileUploadComponent.java 41 2009-11-16 20:16:22Z spt $
 */
public class FileUploadComponent extends Column
{
  private static final long serialVersionUID = 1L;

  private Label path;
  @SuppressWarnings( { "TransientFieldNotInitialized" } )
  private transient File directory;
  private transient File file;
  private String contentType;
  private transient InputStream stream;

  private TextField textField;
  private Button externalButton;

  public FileUploadComponent()
  {
    directory = getController().getUploadDirectory();
    path = createLabel( getClass().getName(), "file" );
  }

  public FileUploadComponent( final TextField textField )
  {
    this();
    this.textField = textField;
  }

  @Override
  public void init()
  {
    removeAll();
    super.init();
    add( createUpload() );

    add( path );
  }

  public String getPath() { return path.getText(); }

  public void setPath( final String path ) { this.path.setText( path ); }

  public InputStream getInputStream() { return stream; }

  public File getFile() { return file; }

  public String getContentType() { return contentType; }

  public void setExternalButton( final Button externalButton )
  {
    this.externalButton = externalButton;
  }

  private FileUploadSelector createUpload()
  {
    final FileUploadSelector selector = new FileUploadSelector();
    selector.setButtonMode( ButtonMode.image );
    selector.setButtonDisplayMode( ButtonDisplay.none );
    selector.setInputSize( 20 );
    selector.setUploadSizeLimit( NO_SIZE_LIMIT );
    selector.setBackground( Color.getInstance( 0xa1a1a1 ) );
    selector.setBorder( new Border( 1, Color.BLUE, Border.STYLE_GROOVE ) );
    selector.setHeight( Extent.getInstance( 47 ) );

    final ProgressBar bar = new ProgressBar();
    bar.setWidth( Extent.getInstance( 298 ) );
    bar.setPattern( "#bytes# of #length# Kb @ #rate# Kb/s" );
    selector.setProgressBar( bar );

    final Callback callback = new Callback( selector, directory );
    callback.setLevel( FINE );
    selector.setUploadCallback( callback );

    return selector;
  }

  private class Callback extends DefaultUploadCallback
  {
    private static final long serialVersionUID = 1l;

    private final FileUploadSelector upload;

    private Callback( final FileUploadSelector upload, final File file )
    {
      super( file );
      this.upload = upload;
    }

    @Override
    public void uploadSucceeded( final UploadFinishEvent event )
    {
      logger.fine( "Running succeeded task" );
      final ProgressBar bar = (ProgressBar) upload.getProgressBar();
      bar.setText( "Finished upload!" );
      displayDownload( event );

      if ( externalButton != null ) externalButton.setEnabled( true );
      super.uploadSucceeded( event );
    }

    @Override
    public void uploadFailed( final UploadFailEvent event )
    {
      final ProgressBar bar = (ProgressBar) upload.getProgressBar();
      bar.setText( "Upload failed!" );
      displayError();
      super.uploadFailed( event );
    }

    @Override
    public void uploadDisallowed( final InvalidContentTypeEvent event )
    {
      final ProgressBar bar = (ProgressBar) upload.getProgressBar();
      bar.setText( "Upload disallowed!" );
      super.uploadDisallowed( event );
    }

    @Override
    public void uploadCancelled( final UploadCancelEvent event )
    {
      final ProgressBar bar = (ProgressBar) upload.getProgressBar();
      bar.setText( "Upload cancelled!" );
      super.uploadCancelled( event );
    }

    private void displayDownload( final UploadFinishEvent event )
    {
      path.setText( getDirectory() + getProperty( "file.separator" ) +
          event.getFileName() );

      try
      {
        stream = event.getFileItem().getInputStream();
        file = new File( path.getText() );
        contentType = event.getContentType();

        if ( ( textField != null ) && textField.getText().length() == 0 )
        {
          textField.setText( file.getName() );
        }
      }
      catch ( IOException e )
      {
        getApplication().processFatalException( e );
      }
    }

    private void displayError()
    {
      final StringBuilder builder = new StringBuilder( 128 );
      builder.append( "Upload " );
      if ( getEvent() != null )
      {
        builder.append( " of file: <b>" );
        builder.append( getEvent().getFileName() );
        builder.append( "</b>" );
      }

      builder.append( " failed/cancelled." );
      upload.getParent().add( new DirectHtml( builder.toString() ) );
    }
  }
}

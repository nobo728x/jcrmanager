package com.sptci.jcr.webui.listener;

import com.sptci.echo.Listener;
import com.sptci.jcr.webui.RegisterNodeDialog;

import nextapp.echo.app.event.ActionEvent;

import static com.sptci.echo.Configuration.getString;
import static com.sptci.jcr.webui.MainController.getController;
import static java.lang.String.format;
import org.apache.jackrabbit.core.nodetype.NodeTypeManagerImpl;
import org.apache.jackrabbit.core.nodetype.NodeTypeRegistry;
import org.apache.jackrabbit.core.nodetype.xml.NodeTypeReader;
import org.apache.jackrabbit.spi.QNodeTypeDefinition;

import javax.jcr.nodetype.NodeTypeManager;

/**
 * The event listener for registering the node type definitions in the file
 * uploaded using the {@link com.sptci.jcr.webui.RegisterNodeDialog}.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-11-16
 * @version $Id: RegisterNodeListener.java 83 2013-07-24 19:33:50Z spt $
 */
@SuppressWarnings( "UnusedDeclaration" )
public class RegisterNodeListener extends Listener<RegisterNodeDialog>
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    final RegisterNodeDialog dialog = getView( event );

    try
    {
      final NodeTypeManager manager = getController().getWorkspace().getNodeTypeManager();
      final NodeTypeRegistry registry = ( (NodeTypeManagerImpl) manager ).getNodeTypeRegistry();

      switch ( dialog.getLanguage() )
      {
        case XML:
          final QNodeTypeDefinition[] definitions = NodeTypeReader.read( dialog.getInputStream() );

          for ( final QNodeTypeDefinition def : definitions )
          {
            if ( registry.isRegistered( def.getName() ) ) continue;

            logger.info( format( "Registering node type %s", def.getName() ) );
            registry.registerNodeType( def );
          }

          break;
        case CND:
          throw new RuntimeException( "CND not supported!" );
      }

      dialog.userClose();
    }
    catch ( Throwable e )
    {
      processFatalException( getString( dialog, "error.message" ), e );
    }
  }
}

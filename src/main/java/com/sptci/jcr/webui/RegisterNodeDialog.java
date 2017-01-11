package com.sptci.jcr.webui;

import com.sptci.echo.WindowPane;
import com.sptci.echo.annotation.ActionListener;
import com.sptci.echo.binding.ViewInitialiser;
import com.sptci.echo.list.EnumListModel;
import com.sptci.echo.list.SelectField;
import com.sptci.jcr.webui.model.NodeDefinitionLanguages;

import nextapp.echo.app.Button;
import nextapp.echo.app.Grid;
import nextapp.echo.app.Label;

import java.io.InputStream;

/**
 * The dialog used to allow user to upload the node type(s) definition file
 * to the repository.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-11-16
 * @version $Id: RegisterNodeDialog.java 41 2009-11-16 20:16:22Z spt $
 */
public class RegisterNodeDialog extends WindowPane
{
  private static final long serialVersionUID = 1L;

  Label languageLabel;
  SelectField<EnumListModel<NodeDefinitionLanguages>> language;
  Label fileLabel;
  @ActionListener( value = "com.sptci.jcr.webui.listener.RegisterNodeListener" )
  Button register;
  FileUploadComponent upload;

  public RegisterNodeDialog()
  {
    new ViewInitialiser<RegisterNodeDialog>( this ).init();
  }

  @Override
  public void init()
  {
    removeAll();
    super.init();

    final Grid grid = new Grid();

    grid.add( languageLabel );
    language.setModel( new EnumListModel<NodeDefinitionLanguages>(
        NodeDefinitionLanguages.class ) );
    grid.add( language );
    
    grid.add( fileLabel );

    upload = new FileUploadComponent();
    upload.setExternalButton( register );
    grid.add( upload );

    register.setEnabled( false );
    grid.add( register );

    add( grid );
  }

  public NodeDefinitionLanguages getLanguage()
  {
    return (NodeDefinitionLanguages) language.getSelectedItem();
  }

  public InputStream getInputStream() { return upload.getInputStream(); }
}

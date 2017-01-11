package com.sptci.jcr.webui.listener;

import com.sptci.echo.Listener;
import com.sptci.echo.tree.filesystem.FileList;
import com.sptci.echo.tree.filesystem.FileSystemTree;
import com.sptci.echo.tree.filesystem.FileSystemTreeNode;
import com.sptci.echo.tree.filesystem.FileSystemWindowPane;

import nextapp.echo.app.TextField;
import nextapp.echo.app.event.ActionEvent;

import java.io.File;

/**
 * An action listener for handling the file selected in a file system window
 * pane.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_top'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-06-24
 * @version $Id: FileSelectionListener.java 10 2009-08-15 23:08:37Z spt $
 */
public class FileSelectionListener extends Listener<FileSystemWindowPane>
{
  private static final long serialVersionUID = 1L;

  /** The text component whose value is to be updated with the file. */
  private final TextField textField;

  /**
   * Create a new instance of the listener that will populate the specified
   * text field with the path of the selected file.
   *
   * @param textField The text field that is to be updated.
   */
  public FileSelectionListener( final TextField textField )
  {
    this.textField = textField;
  }

  public void actionPerformed( final ActionEvent event )
  {
    final FileSystemWindowPane pane = getView( event );

    File file = getFile( pane );
    if ( file == null ) file = getDirectory( pane );
    textField.setText( file.getAbsolutePath() );

    pane.userClose();
    pane.dispose();
  }

  private File getFile( final FileSystemWindowPane pane )
  {
    final FileList list = pane.getPane().getList();
    return list.getSelected();
  }

  private File getDirectory( final FileSystemWindowPane pane )
  {
    final FileSystemTree tree = pane.getPane().getTree();
    final Object object =
        tree.getSelectionModel().getSelectionPath().getLastPathComponent();

    if ( object instanceof FileSystemTreeNode )
    {
      return ( (FileSystemTreeNode) object ).getUserObject();
    }
    else
    {
      return File.listRoots()[0];
    }
  }
}

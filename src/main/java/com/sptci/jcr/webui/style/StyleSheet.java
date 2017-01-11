package com.sptci.jcr.webui.style;

import jfix.echo.RichTextArea;

/**
 * The style sheet for the application.  Sets/modifies default styles.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-10-10
 * @version $Id: StyleSheet.java 38 2009-10-12 14:35:15Z spt $
 */
public class StyleSheet extends com.sptci.echo.style.StyleSheet
{
  private static final long serialVersionUID = 1l;

  @Override
  protected void init()
  {
    super.init();
    addRichTextAreaStyles();
  }

  protected void addRichTextAreaStyles()
  {
    addStyle( RichTextArea.class, "", new RichTextAreaStyle() );
  }

}

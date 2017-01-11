package com.sptci.jcr.webui.listener.menu;


import nextapp.echo.app.event.ActionEvent;

import com.sptci.echo.Listener;
import com.sptci.jcr.webui.ContentArea;
import com.sptci.jcr.webui.LoginJNDIDialog;


/**
 * The action listener for displaying the JNDI dialog that will be used to
 * select the repository JNDI.
 * 
 * <p>
 * &copy; Copyright 2009 <a href='http://sptci.com/' target='_top'>Sans Pareil Technologies, Inc.</a>
 * </p>
 * 
 * @author Rakesh Vidyadharan 2009-06-22
 * @version $Id: LoginLocalListener.java 63 2010-02-16 15:23:00Z spt $
 */
@SuppressWarnings({ "UnusedDeclaration" })
public class LoginResourceListener extends Listener<ContentArea> {

	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(final ActionEvent event) {
		final LoginJNDIDialog pane = new LoginJNDIDialog();
		getApplication().addPane(pane);
	}
}

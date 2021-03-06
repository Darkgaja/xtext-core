/*******************************************************************************
 * Copyright (c) 2010 itemis AG (http://www.itemis.eu) and others.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.xtext.linking;

import org.junit.Test;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class IgnoreCaseLinkingWithNamespaceImportsTest extends AbstractIgnoreCaseLinkingTest {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		with(IgnoreCaseNamespacesTestLanguageStandaloneSetup.class);
	}
	
	@Override
	@Test public void testWithImports() throws Exception {
		String input = "'a.b.*' a C { b a { c B {} d a {} } }";
		getModel(input);
	}
	
	@Test public void testWithImportsIgnoreCase_01() throws Exception {
		String input = "'A.B.*' a C { b a { c B {} d a {} } }";
		getModel(input);
	}
	
}

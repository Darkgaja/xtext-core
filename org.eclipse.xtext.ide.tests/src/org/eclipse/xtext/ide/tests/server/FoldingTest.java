/*******************************************************************************
 * Copyright (c) 2021 TypeFox GmbH (http://www.typefox.io) and others.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.xtext.ide.tests.server;

import org.junit.Test;

/**
 * @author Mark Sujew - Initial contribution and API
 */
public class FoldingTest extends AbstractTestLangLanguageServerTest {

	@Test
	public void testFoldingService() {
		testFolding(it -> {
			it.setModel("/*\nMultiline Comment\n*/\ntype Foo {\nint bar\n}");
			String expectedText = 
					"FoldingRange [\n"
					+ "  startLine = 3\n"
					+ "  endLine = 5\n"
					+ "  startCharacter = 24\n"
					+ "  endCharacter = 44\n"
					+ "  kind = null\n"
					+ "]FoldingRange [\n"
					+ "  startLine = 0\n"
					+ "  endLine = 2\n"
					+ "  startCharacter = 0\n"
					+ "  endCharacter = 23\n"
					+ "  kind = \"comment\"\n"
					+ "]";
			it.setExpectedFoldings(expectedText);
		});
	}
	
}

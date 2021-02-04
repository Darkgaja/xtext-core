/*******************************************************************************
 * Copyright (c) 2010 Michael Clay and others.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *******************************************************************************/
package org.eclipse.xtext.ide.editor.folding;

import java.util.Collection;

/**
 * @author Michael Clay - Initial contribution and API
 * @author Sebastian Zarnekow - Introduced FoldedPosition
 */
public class DefaultFoldingRangeAcceptor implements IFoldingRangeAcceptor {
	private Collection<FoldingRange> result;

	public DefaultFoldingRangeAcceptor(Collection<FoldingRange> result) {
		this.result = result;
	}

	/**
	 * @since 2.8
	 */
	@Override
	public void accept(FoldingRange foldingRange) {
		result.add(foldingRange);
	}
	
	public Collection<FoldingRange> getFoldingRanges() {
		return result;
	}
}

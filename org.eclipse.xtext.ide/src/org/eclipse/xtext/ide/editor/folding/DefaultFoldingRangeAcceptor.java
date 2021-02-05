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
public class DefaultFoldingRangeAcceptor implements IFoldingRangeAcceptor<Void> {
	private Collection<FoldingRange> result;

	public DefaultFoldingRangeAcceptor(Collection<FoldingRange> result) {
		this.result = result;
	}

	public Collection<FoldingRange> getFoldingRanges() {
		return result;
	}

	@Override
	public void accept(int offset, int length) {
		accept(offset, length, false);
	}

	@Override
	public void accept(int offset, int length, String kind) {
		accept(offset, length, kind, null);
	}

	@Override
	public void accept(int offset, int length, boolean initiallyFolded) {
		accept(offset, length, null, initiallyFolded, null);
	}

	@Override
	public void accept(int offset, int length, Void addedParams) {
		accept(offset, length, null, false, addedParams);
	}

	@Override
	public void accept(int offset, int length, String kind, Void addedParams) {
		accept(offset, length, kind, false, addedParams);
	}

	@Override
	public void accept(int offset, int length, String kind, boolean initiallyFolded, Void addedParams) {
		result.add(new FoldingRange(offset, length, kind));
	}
}

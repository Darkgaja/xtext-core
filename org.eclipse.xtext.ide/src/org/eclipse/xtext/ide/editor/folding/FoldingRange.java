/*******************************************************************************
 * Copyright (c) 2021 itemis AG (http://www.itemis.eu) and others.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.xtext.ide.editor.folding;

/**
 * @author Mark - Initial contribution and API
 */
public class FoldingRange {
	
	private int offset;
	private int length;
	private String kind;
	private boolean initiallyFolded;
	
	public boolean isInitiallyFolded() {
		return initiallyFolded;
	}

	public void setInitiallyFolded(boolean initiallyFolded) {
		this.initiallyFolded = initiallyFolded;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public int getOffset() {
		return offset;
	}
	
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public FoldingRange(int offset, int length) {
		this(offset, length, null, false);
	}
	
	public FoldingRange(int offset, int length, String kind) {
		this(offset, length, kind, false);
	}
	
	public FoldingRange(int offset, int length, String kind, boolean initiallyFolded) {
		this.offset = offset;
		this.length = length;
		this.kind = kind;
		this.initiallyFolded = initiallyFolded;
	}
}

/*******************************************************************************
 * Copyright (c) 2021 TypeFox GmbH (http://www.typefox.io) and others.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.xtext.ide.editor.folding;

/**
 * @author Mark Sujew - Initial contribution and API
 */
public class FoldingRange {

	private int offset;
	private int length;
	private String kind;

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
		this(offset, length, null);
	}

	public FoldingRange(int offset, int length, String kind) {
		this.offset = offset;
		this.length = length;
		this.kind = kind;
	}
}

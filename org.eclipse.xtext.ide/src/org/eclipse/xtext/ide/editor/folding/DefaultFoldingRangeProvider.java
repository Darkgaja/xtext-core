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
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.TerminalRule;
import org.eclipse.xtext.documentation.impl.AbstractMultiLineCommentProvider;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.parser.IParseResult;
import org.eclipse.xtext.resource.ILocationInFileProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.util.ITextRegion;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * @author Michael Clay - Initial contribution and API
 * @author Sebastian Zarnekow - Introduced FoldedRegion, use ILocationInFileProvider
 */
public class DefaultFoldingRangeProvider implements IFoldingRangeProvider {

	@Inject
	private ILocationInFileProvider locationInFileProvider;

	@Inject(optional = true)
	@Named(AbstractMultiLineCommentProvider.RULE)
	protected String ruleName = "ML_COMMENT";

	@Override
	public Collection<FoldingRange> getFoldingRanges(final XtextResource xtextDocument,
			CancelIndicator cancelIndicator) {
		Collection<FoldingRange> result = Sets.newLinkedHashSet();
		IFoldingRangeAcceptor foldingRegionAcceptor = createAcceptor(xtextDocument, result);
		computeObjectFolding(xtextDocument, foldingRegionAcceptor, cancelIndicator);
		computeCommentFolding(xtextDocument, foldingRegionAcceptor);
		return result;
	}

	protected IFoldingRangeAcceptor createAcceptor(XtextResource xtextDocument,
			Collection<FoldingRange> foldedPositions) {
		return new DefaultFoldingRangeAcceptor(foldedPositions);
	}

	protected void computeObjectFolding(XtextResource xtextResource, IFoldingRangeAcceptor foldingRegionAcceptor,
			CancelIndicator cancelIndicator) {
		IParseResult parseResult = xtextResource.getParseResult();
		if (parseResult != null) {
			EObject rootASTElement = parseResult.getRootASTElement();
			if (rootASTElement != null) {
				if (cancelIndicator.isCanceled())
					throw new OperationCanceledException();
				if (isHandled(rootASTElement)) {
					computeObjectFolding(rootASTElement, foldingRegionAcceptor);
				}
				if (shouldProcessContent(rootASTElement)) {
					TreeIterator<EObject> allContents = rootASTElement.eAllContents();
					while (allContents.hasNext()) {
						if (cancelIndicator.isCanceled())
							throw new OperationCanceledException();
						EObject eObject = allContents.next();
						if (isHandled(eObject)) {
							computeObjectFolding(eObject, foldingRegionAcceptor);
						}
						if (!shouldProcessContent(eObject)) {
							allContents.prune();
						}
					}
				}
			}
		}
	}

	protected ILocationInFileProvider getLocationInFileProvider() {
		return locationInFileProvider;
	}

	/**
	 * @since 2.8
	 */
	protected void computeObjectFolding(EObject eObject, IFoldingRangeAcceptor foldingRegionAcceptor) {
		ITextRegion region = locationInFileProvider.getFullTextRegion(eObject);
		if (region != null) {
			foldingRegionAcceptor.accept(new FoldingRange(region.getOffset(), region.getLength()));
		}
	}

	protected void computeCommentFolding(XtextResource xtextDocument, IFoldingRangeAcceptor foldingRegionAcceptor) {
		IParseResult parseResult = xtextDocument.getParseResult();
		if (parseResult != null) {
			EObject rootASTElement = parseResult.getRootASTElement();
			List<INode> nodes = getCommentNodes(rootASTElement);
			for (INode node : nodes) {
				foldingRegionAcceptor
						.accept(new FoldingRange(node.getOffset(), node.getLength(), FoldingRangeKind.COMMENT));
			}
		}
	}

	protected List<INode> getCommentNodes(EObject eObject) {
		ICompositeNode node = NodeModelUtils.getNode(eObject);
		List<INode> result = Collections.emptyList();
		if (node != null) {
			for (INode leafNode : node.getAsTreeIterable()) {
				if (leafNode.getGrammarElement() instanceof TerminalRule
						&& ruleName.equalsIgnoreCase(((TerminalRule) leafNode.getGrammarElement()).getName())) {
					result.add(leafNode);
				}
			}
		}
		return result;
	}

	/**
	 * @return <code>true</code> if the object should be folded if it spans more than one line. Default is
	 *         <code>false</code> if and only if the object is the root object of the resource.
	 */
	protected boolean isHandled(EObject eObject) {
		return eObject.eContainer() != null;
	}

	/**
	 * @return clients should <code>false</code> to abort the traversal of the model.
	 */
	protected boolean shouldProcessContent(EObject object) {
		return true;
	}

}

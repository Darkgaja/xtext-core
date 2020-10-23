/*******************************************************************************
 * Copyright (c) 2020 itemis AG (http://www.itemis.eu) and others.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.xtext.formatting2;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.formatting2.regionaccess.IEObjectRegion;
import org.eclipse.xtext.formatting2.regionaccess.IHiddenRegion;
import org.eclipse.xtext.formatting2.regionaccess.ISemanticRegion;
import org.eclipse.xtext.formatting2.regionaccess.ISemanticRegionFinder;
import org.eclipse.xtext.formatting2.regionaccess.ISemanticRegionsFinder;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.PolymorphicDispatcher;

import com.google.common.annotations.Beta;

/**
 * <p>
 * This is an abstract base class for language-specific formatters, intended to be extended with a java class.
 * </p>
 * <p>
 * It is working very much like {@link AbstractFormatter2} but does support additional methods to simplify java
 * formatter code.
 * </p>
 * <p>
 * the "_format" methods are called by reflection. The name is important as well as there are exactly two arguments
 * whereas the first one is of the element type you like to format and the second one of type
 * {@link IFormattableDocument}. The methods should be protected.
 * </p>
 * <p>
 * Example code for a java formatter:
 * 
 * <pre>
 * import static org.xtext.example.mydsl.myDsl.MyDslPackage.Literals.*;
 * 
 * public class MyDslFormatter2 extends AbstractJavaFormatter {
 * 	protected void _format(Model model, IFormattableDocument doc) {
 * 		for (Parent parent : model.getParents())
 * 			doc.format(parent);
 * 	}
 * 
 * 	protected void _format(Parent parent, IFormattableDocument doc) {
 * 		doc.prepend(regionFor(parent).keyword("parent"), it -> it.noSpace());
 * 		doc.append(regionFor(parent).keyword("parent"), it -> it.oneSpace());
 * 		doc.append(regionFor(parent).feature(PARENT__NAME), it -> it.oneSpace());
 * 		doc.prepend(regionFor(parent).keyword("{"), it -> it.oneSpace());
 * 		doc.append(regionFor(parent).keyword("{"), it -> it.newLine());
 * 		doc.interior(regionFor(parent).keyword("{"), regionFor(parent).keyword("}"), it -> it.indent());
 * 		doc.append(regionFor(parent).keyword("}"), it -> it.setNewLines(1, 1, 2));
 * 		for (Child child : parent.getChildren())
 * 			doc.format(child);
 * 	}
 * 
 * 	protected void _format(Child child, IFormattableDocument doc) {
 * 		doc.append(regionFor(child).keyword("child"), it -> it.oneSpace());
 * 		doc.append(regionFor(child).feature(CHILD__NAME), it -> it.newLine());
 * 	}
 * }
 * </pre>
 * </p>
 * <p>
 * This is suitable for the following grammar:
 * 
 * <pre>
 * grammar org.xtext.example.mydsl.MyDsl with org.eclipse.xtext.common.Terminals
 * 
 * generate myDsl "http://www.xtext.org/example/mydsl/MyDsl"
 * 
 * Model: parents+=Parent*;
 * Parent: 'parent' name=ID '!' ('{' children+=Child* '}')?;
 * Child: 'child' name=ID;
 * </pre>
 * </p>
 */
@Beta
public abstract class AbstractJavaFormatter extends AbstractFormatter2 {

	private PolymorphicDispatcher<Void> dispatcher = createPolymorhicDispatcher();

	// reflective method that calls "_format" methods found in the implementing class.
	@Override
	public void format(Object child, IFormattableDocument document) {
		if (child instanceof XtextResource) {
			_format((XtextResource) child, document);
			return;
		} else if (child == null) {
			_format((Void) null, document);
			return;
		}
		try {
			dispatcher.invoke(child, document);
		} catch (SecurityException | IllegalArgumentException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Override if you like to specify formatting methods in different way then default (using annotations or similar).
	 */
	protected PolymorphicDispatcher<Void> createPolymorhicDispatcher() {
		return PolymorphicDispatcher.createForSingleTarget(m -> "format".equals(m.getName())
				&& m.getParameterCount() == 2 && m.getParameterTypes()[1] == IFormattableDocument.class, this);
	}

	// implementations that forward the methods of ITextRegionExtensions to simplify formatter code.

	protected ISemanticRegionsFinder allRegionsFor(EObject semanticElement) {
		return textRegionExtensions.allRegionsFor(semanticElement);
	}

	protected Iterable<ISemanticRegion> allSemanticRegions(EObject semanticElement) {
		return textRegionExtensions.allSemanticRegions(semanticElement);
	}

	protected EObject grammarElement(EObject semanticElement) {
		return textRegionExtensions.grammarElement(semanticElement);
	}

	protected ISemanticRegionFinder immediatelyFollowing(EObject semanticElement) {
		return textRegionExtensions.immediatelyFollowing(semanticElement);
	}

	protected ISemanticRegionFinder immediatelyPreceding(EObject semanticElement) {
		return textRegionExtensions.immediatelyPreceding(semanticElement);
	}

	protected boolean isMultiline(EObject semanticElement) {
		return textRegionExtensions.isMultiline(semanticElement);
	}

	protected IHiddenRegion nextHiddenRegion(EObject semanticElement) {
		return textRegionExtensions.nextHiddenRegion(semanticElement);
	}

	protected IHiddenRegion previousHiddenRegion(EObject semanticElement) {
		return textRegionExtensions.previousHiddenRegion(semanticElement);
	}

	protected ISemanticRegionsFinder regionFor(EObject semanticElement) {
		return textRegionExtensions.regionFor(semanticElement);
	}

	protected IEObjectRegion regionForEObject(EObject semanticElement) {
		return textRegionExtensions.regionForEObject(semanticElement);
	}

	protected Iterable<ISemanticRegion> semanticRegions(EObject semanticElement) {
		return textRegionExtensions.semanticRegions(semanticElement);
	}
	
}

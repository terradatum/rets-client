/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.metadata;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.realtors.rets.metadata.types.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Parses apart a complete Standard-XML response, returns a Metadata object
 */
public class JDomStandardBuilder extends MetadataBuilder {
	public static final String ELEMENT_SYSTEM = "System";
	public static final String ELEMENT_RESOURCE = "Resource";
	public static final String ELEMENT_FOREIGNKEY = "ForeignKey";
	public static final String ELEMENT_CLASS = "Class";
	public static final String ELEMENT_TABLE = "Field";
	public static final String ELEMENT_UPDATE = "UpdateType";
	public static final String ELEMENT_UPDATETYPE = "UpdateField";
	public static final String ELEMENT_OBJECT = "Object";
	public static final String ELEMENT_SEARCHHELP = "SearchHelp";
	public static final String ELEMENT_EDITMASK = "EditMask";
	public static final String ELEMENT_UPDATEHELP = "UpdateHelp";
	public static final String ELEMENT_LOOKUP = "Lookup";
	public static final String ELEMENT_LOOKUPTYPE = "LookupType";
	public static final String ELEMENT_VALIDATIONLOOKUP = "ValidationLookup";
	public static final String ELEMENT_VALIDATIONLOOKUPTYPE = "ValidationLookupType";
	public static final String ELEMENT_VALIDATIONEXPRESSION = "ValidationExpression";
	public static final String ELEMENT_VALIDATIONEXTERNAL = "ValidationExternalType";
	public static final String ELEMENT_VALIDATIONEXTERNALTYPE = "ValidationExternal";
	public static final String ATTRIBUTE_RESOURCEID = ELEMENT_RESOURCE;
	public static final String ATTRIBUTE_CLASSNAME = ELEMENT_CLASS;
	public static final String ATTRIBUTE_UPDATE = ELEMENT_UPDATE;
	public static final String ATTRIBUTE_LOOKUP = ELEMENT_LOOKUP;
	public static final String ATTRIBUTE_VALIDATIONLOOKUP = ELEMENT_VALIDATIONLOOKUP;
	public static final String ATTRIBUTE_VALIDATIONEXTERNAL = ELEMENT_VALIDATIONEXTERNAL;
	public static final Map<MetadataType, String> sType2Element = new HashMap<>();
	public static final String CONTAINER_PREFIX = "METADATA-";
	public static final String CONTAINER_ROOT = "RETS";
	public static final String CONTAINER_METADATA = "METADATA";
	public static final String CONTAINER_SYSTEM = "METADATA-SYSTEM";
	public static final String CONTAINER_RESOURCE = "METADATA-RESOURCE";
	public static final String CONTAINER_FOREIGNKEY = "METADATA-FOREIGN_KEYS";
	public static final String CONTAINER_CLASS = "METADATA-CLASS";
	public static final String CONTAINER_TABLE = "METADATA-TABLE";
	public static final String CONTAINER_UPDATE = "METADATA-UPDATE";
	public static final String CONTAINER_UPDATE_TYPE = "METADATA-UPDATE_TYPE";
	public static final String CONTAINER_OBJECT = "METADATA-OBJECT";
	public static final String CONTAINER_SEARCH_HELP = "METADATA-SEARCH_HELP";
	public static final String CONTAINER_EDITMASK = "METADATA-EDITMASK";
	public static final String CONTAINER_UPDATEHELP = "METADATA-UPDATE_HELP";
	public static final String CONTAINER_LOOKUP = "METADATA-LOOKUP";
	public static final String CONTAINER_LOOKUPTYPE = "METADATA-LOOKUP_TYPE";
	public static final String CONTAINER_VALIDATIONLOOKUP = "METADATA-VALIDATION_LOOKUP";
	public static final String CONTAINER_VALIDATIONLOOKUPTYPE = "METADATA-VALIDATION_LOOKUP_TYPE";
	public static final String CONTAINER_VALIDATIONEXPRESSION = "METADATA-VALIDATION_EXPRESSION";
	public static final String CONTAINER_VALIDATIONEXTERNAL = "METADATA-VALIDATION_EXTERNAL";
	public static final String CONTAINER_VALIDATIONEXTERNALTYPE = "METADATA-VALIDATION_EXTERNAL_TYPE";
	public static final Map sContainer2Type = new HashMap();

	static {
		sType2Element.put(MetadataType.SYSTEM, ELEMENT_SYSTEM);
		sType2Element.put(MetadataType.RESOURCE, ELEMENT_RESOURCE);
		sType2Element.put(MetadataType.FOREIGNKEYS, ELEMENT_FOREIGNKEY);
		sType2Element.put(MetadataType.CLASS, ELEMENT_CLASS);
		sType2Element.put(MetadataType.TABLE, ELEMENT_TABLE);
		sType2Element.put(MetadataType.UPDATE, ELEMENT_UPDATE);
		sType2Element.put(MetadataType.UPDATE_TYPE, ELEMENT_UPDATETYPE);
		sType2Element.put(MetadataType.SEARCH_HELP, ELEMENT_SEARCHHELP);
		sType2Element.put(MetadataType.EDITMASK, ELEMENT_EDITMASK);
		sType2Element.put(MetadataType.UPDATE_HELP, ELEMENT_UPDATEHELP);
		sType2Element.put(MetadataType.LOOKUP, ELEMENT_LOOKUP);
		sType2Element.put(MetadataType.LOOKUP_TYPE, ELEMENT_LOOKUPTYPE);
		sType2Element.put(MetadataType.VALIDATION_LOOKUP, ELEMENT_VALIDATIONLOOKUP);
		sType2Element.put(MetadataType.VALIDATION_LOOKUP_TYPE, ELEMENT_VALIDATIONLOOKUPTYPE);
		sType2Element.put(MetadataType.VALIDATION_EXTERNAL, ELEMENT_VALIDATIONEXTERNAL);
		sType2Element.put(MetadataType.VALIDATION_EXTERNAL_TYPE, ELEMENT_VALIDATIONEXTERNALTYPE);
		sType2Element.put(MetadataType.VALIDATION_EXPRESSION, ELEMENT_VALIDATIONEXPRESSION);
	}

	static {
		for (int i = 0; i < MetadataType.values().length; i++) {
			MetadataType type = MetadataType.values()[i];
			sContainer2Type.put(CONTAINER_PREFIX + type.name(), type);
		}
		/* you have got to be kidding me.  The spec (compact) says
		 METADATA-FOREIGNKEYS and that's the request type but the DTD says
		 METADATA-FOREIGN_KEY.
		 I think I'm going to be sick.
		 */
		sContainer2Type.remove(CONTAINER_PREFIX + MetadataType.FOREIGNKEYS.name());
		sContainer2Type.put(CONTAINER_FOREIGNKEY, MetadataType.FOREIGNKEYS);
	}

	@Override
	public Metadata doBuild(Object src) throws MetadataException {
		return build((Document) src);
	}

	public Metadata build(Document src) throws MetadataException {
		Element element = src.getRootElement();
		expectElement(element, CONTAINER_ROOT);
		element = getElement(element, CONTAINER_METADATA);
		return build(element);
	}

	@Override
	public MetadataObject[] parse(Object src) throws MetadataException {
		return parse((Document) src);
	}

	public MetadataObject[] parse(Document src) throws MetadataException {
		Element element = src.getRootElement();
		expectElement(element, CONTAINER_ROOT);
		Element container = getElement(element, CONTAINER_METADATA);
		boolean recurse = checkForRecursion(container);
		List<?> list = container.getChildren();
		if (list.size() == 0) {
			return null;
		}
		return processContainer(null, (Element) list.get(0), recurse);
	}

	/**
	 * Function to determine if a request contains recursive data or not.
	 * This is done here instead of inside processContainer because, well,
	 * it's easier and more reliable (processContainer might not figure out
	 * that a request is recursive until the third or 4th child if there are
	 * no children for the first couple of elements.
	 *
	 * @param top The outside METADATA container.
	 * @return true if the request is recursive
	 */
	private boolean checkForRecursion(Element top) {
		/*
		 * this seems like a really nasty loop.  However, if there are a
		 * lot of recursive elements, we'll find out pretty quickly, and if
		 * we fall all the way to the end then there probably wasn't that
		 * much to look through.
		 */
		for (Object item : top.getChildren()) {
			/* each of these is a container (METADATA-*) type */
			Element element = (Element) item;
			for (Object value : element.getChildren()) {
				/* each of these is an item element */
				Element child = (Element) value;
				for (Object o : child.getChildren()) {
					Element subtype = (Element) o;
					if (subtype.getName().startsWith(CONTAINER_PREFIX)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private MetadataObject[] processContainer(MetadataObject parent, Element container, boolean recursion) {
		MetadataType type = (MetadataType) sContainer2Type.get(container.getName());
		if (type == null) {
			throw new RuntimeException("no matching type for container " + container.getName());
		}
		List<?> elements = container.getChildren(sType2Element.get(type));
		String path = getPath(container);
		List<MetadataObject> output = null;
		if (parent == null) {
			output = new LinkedList();
		}
		/**
		 * Weirdness abounds.  There IS an ID attribute of System,
		 * and the SystemID is included in the Metadata container
		 * attributes, but the system id is not part of the metadata
		 * request path for a getMetadata request, so we ignore it.
		 */
		for (Object o : elements) {
			Element element = (Element) o;
			MetadataObject obj = newType(type);
			setAttributes(obj, element);
			if (output != null) {
				output.add(obj);
			}
			if (parent != null) {
				parent.addChild(type, obj);
			} else {
				/*
				  Weirdness abounds.  There IS an ID attribute of System,
				  and the SystemID is included in the Metadata container
				  attributes, but the system id is not part of the metadata
				  request path for a getMetadata request, so we ignore it.
				 */
				if (!type.equals(MetadataType.SYSTEM)) {
					obj.setPath(path);
				}
			}
			if (recursion) {
				MetadataType[] childTypes = obj.getChildTypes();
				for (MetadataType childType : childTypes) {
					Element childContainer = element.getChild(CONTAINER_PREFIX + childType.name());
					if (childContainer == null) {
						obj.addChild(childType, null);
					} else {
						processContainer(obj, childContainer, true);
					}
				}
			}
		}
		if (output == null) {
			return null;
		}
		return output.toArray(new MetadataObject[0]);
	}

	String getPath(Element container) {
		String resource = container.getAttributeValue(ATTRIBUTE_RESOURCEID);
		if (resource == null) {
			return null;
		}
		String classname = container.getAttributeValue(ATTRIBUTE_CLASSNAME);
		if (classname != null) {
			String update = container.getAttributeValue(ATTRIBUTE_UPDATE);
			if (update != null) {
				return resource + ":" + classname + ":" + update;
			}
			return resource + ":" + classname;
		}
		String lookup = container.getAttributeValue(ATTRIBUTE_LOOKUP);
		if (lookup != null) {
			return resource + ":" + lookup;
		}
		String vallkp = container.getAttributeValue(ATTRIBUTE_VALIDATIONLOOKUP);
		if (vallkp != null) {
			return resource + ":" + vallkp;
		}
		String vale = container.getAttributeValue(ATTRIBUTE_VALIDATIONEXTERNAL);
		if (vale != null) {
			return resource + ":" + vale;
		}
		return resource;
	}

	public Metadata build(Element element) throws MetadataException {
		expectElement(element, CONTAINER_METADATA);
		element = getElement(element, CONTAINER_SYSTEM);
		//maybe i get the attribute here
		RetsSystem sys = processSystem(element);
		return new Metadata(sys);
	}

	private Element getElement(Element parent, String type) throws MetadataException {
		Element element = parent.getChild(type);
		if (element == null) {
			throw new MetadataException("Missing element " + type);
		}
		return element;
	}

	private void expectElement(Element element, String type) throws MetadataException {
		if (!element.getName().equalsIgnoreCase(type)) {// changed to ignore case
			throw new MetadataException("Expecting element " + type + ", got " + element.getName());
		}
	}

	private void setAttributes(MetadataObject obj, Element el) {

		List<?> children = el.getChildren();
		for (Object o : children) {
			Element child = (Element) o;
			String name = child.getName();
			if (!name.startsWith(CONTAINER_PREFIX)) {
				String value = child.getTextTrim();
				setAttribute(obj, name, value);
			} else {
				// do nothing
			}
		}
	}

	//when atrributes from the xml element are needed
	public void setAttributesFromXMLAttr(MetadataObject obj, Element el) {
		for (Object o : el.getParentElement().getAttributes()) {
			Attribute attr = (Attribute) o;
			String name = attr.getName();
			String value = attr.getValue().trim();
			setAttribute(obj, name, value);
		}
	}

	/**
	 * If we're a recursive request, initialize all possible child types so
	 * we don't have to try to pull them later, dynamically
	 */
	private void init(MetadataObject item) {
		MetadataType[] childTypes = item.getChildTypes();
		for (MetadataType type : childTypes) {
			item.addChild(type, null);
		}
	}

	private RetsSystem processSystem(Element container) {
		Element element = container.getChild(ELEMENT_SYSTEM);
		if (element == null) {
			element = container.getChild(ELEMENT_SYSTEM.toUpperCase());
		}
		RetsSystem system = buildSystem();
		init(system);
		setAttributesFromXMLAttr(system, element);
		setAttributes(system, element);
		Element child;
		child = element.getChild(CONTAINER_RESOURCE);
		if (child != null) {
			processResource(system, child);
		}
		child = element.getChild(CONTAINER_FOREIGNKEY);
		if (child != null) {
			processForeignKey(system, child);
		}
		return system;
	}

	private void processResource(RetsSystem system, Element container) {
		List<?> resources = container.getChildren(ELEMENT_RESOURCE);
		for (Object o : resources) {
			Element element = (Element) o;
			RetsResource resource = buildResource();
			init(resource);
			setAttributes(resource, element);
			system.addChild(MetadataType.RESOURCE, resource);
			Element child;
			child = element.getChild(CONTAINER_CLASS);
			if (child != null) {
				processClass(resource, child);
			}
			child = element.getChild(CONTAINER_OBJECT);
			if (child != null) {
				processObject(resource, child);
			}
			child = element.getChild(CONTAINER_SEARCH_HELP);
			if (child != null) {
				processSearchHelp(resource, child);
			}
			child = element.getChild(CONTAINER_EDITMASK);
			if (child != null) {
				processEditMask(resource, child);
			}
			child = element.getChild(CONTAINER_LOOKUP);
			if (child != null) {
				processLookup(resource, child);
			}
			child = element.getChild(CONTAINER_UPDATEHELP);
			if (child != null) {
				processUpdateHelp(resource, child);
			}
			child = element.getChild(CONTAINER_VALIDATIONLOOKUP);
			if (child != null) {
				processValidationLookup(resource, child);
			}
			child = element.getChild(CONTAINER_VALIDATIONEXPRESSION);
			if (child != null) {
				processValidationExpression(resource, child);
			}
			child = element.getChild(CONTAINER_VALIDATIONEXTERNAL);
			if (child != null) {
				processValidationExternal(resource, child);
			}
		}
	}

	private void processEditMask(RetsResource parent, Element container) {
		List<?> elements = container.getChildren(ELEMENT_EDITMASK);
		for (Object o : elements) {
			Element element = (Element) o;
			RetsEditMask mask = buildEditMask();
			setAttributes(mask, element);
			parent.addChild(MetadataType.EDITMASK, mask);
		}
	}

	private void processLookup(RetsResource parent, Element container) {
		List<?> elements15 = container.getChildren(ELEMENT_LOOKUP);
		List<?> elements17 = container.getChildren(ELEMENT_LOOKUPTYPE);
		List<?> elements;
		//some Rets Servers have lookuptype and lookup elements interchanged
		if (elements15.isEmpty()) {
			elements = elements17;
		} else {
			elements = elements15;
		}
		for (Object o : elements) {
			Element element = (Element) o;
			RetsLookup lookup = buildLookup();
			init(lookup);
			setAttributes(lookup, element);
			parent.addChild(MetadataType.LOOKUP, lookup);
			Element child = element.getChild(CONTAINER_LOOKUPTYPE);
			if (child != null) {
				processLookupType(lookup, child);
			}
		}
	}

	private void processLookupType(RetsLookup parent, Element container) {

		List<?> elements15 = container.getChildren(ELEMENT_LOOKUPTYPE);// check spec
		List<?> elements17 = container.getChildren(ELEMENT_LOOKUP);
		List<?> elements;
		//some Rets Servers have lookuptype and lookup elements interchanged
		if (elements15.isEmpty()) {
			elements = elements17;
		} else {
			elements = elements15;
		}
		for (Object o : elements) {
			Element element = (Element) o;
			RetsLookupType type = buildLookupType();
			setAttributes(type, element);
			parent.addChild(MetadataType.LOOKUP_TYPE, type);
		}
	}

	private void processUpdateHelp(RetsResource parent, Element container) {
		List<?> elements = container.getChildren(ELEMENT_UPDATEHELP);
		for (Object o : elements) {
			Element element = (Element) o;
			RetsUpdateHelp help = buildUpdateHelp();
			setAttributes(help, element);
			parent.addChild(MetadataType.UPDATE_HELP, help);
		}
	}

	private void processValidationLookup(RetsResource parent, Element container) {
		List<?> elements = container.getChildren(ELEMENT_VALIDATIONLOOKUP);
		for (Object o : elements) {
			Element element = (Element) o;
			RetsValidationLookup lookup = buildValidationLookup();
			init(lookup);
			setAttributes(lookup, element);
			parent.addChild(MetadataType.VALIDATION_LOOKUP, lookup);
			Element child = element.getChild(CONTAINER_VALIDATIONLOOKUPTYPE);
			if (child != null) {
				processValidationLookupType(lookup, child);
			}
		}
	}

	private void processValidationLookupType(RetsValidationLookup parent, Element container) {
		List<?> elements = container.getChildren(ELEMENT_VALIDATIONLOOKUPTYPE);
		for (Object o : elements) {
			Element element = (Element) o;
			RetsValidationLookupType lookupType = buildValidationLookupType();
			setAttributes(lookupType, element);
			parent.addChild(MetadataType.VALIDATION_LOOKUP_TYPE, lookupType);
		}
	}

	private void processValidationExpression(RetsResource parent, Element container) {
		List<?> elements = container.getChildren(ELEMENT_VALIDATIONEXPRESSION);
		for (Object o : elements) {
			Element element = (Element) o;
			RetsValidationExpression expression = buildValidationExpression();
			setAttributes(expression, element);
			parent.addChild(MetadataType.VALIDATION_EXPRESSION, expression);
		}
	}

	private void processValidationExternal(RetsResource parent, Element container) {
		List<?> elements = container.getChildren(ELEMENT_VALIDATIONEXTERNAL);
		for (Object o : elements) {
			Element element = (Element) o;
			RetsValidationExternal external = buildValidationExternal();
			init(external);
			setAttributes(external, element);
			parent.addChild(MetadataType.VALIDATION_EXTERNAL, external);
			Element child = element.getChild(CONTAINER_VALIDATIONEXTERNALTYPE);
			if (child != null) {
				processValidationExternalType(external, child);
			}
		}
	}

	private void processValidationExternalType(RetsValidationExternal parent, Element container) {
		List<?> elements = container.getChildren(ELEMENT_VALIDATIONEXTERNALTYPE);
		for (Object o : elements) {
			Element element = (Element) o;
			RetsValidationExternalType type = buildValidationExternalType();
			setAttributes(type, element);
			parent.addChild(MetadataType.VALIDATION_EXTERNAL_TYPE, type);
		}
	}

	private void processSearchHelp(RetsResource parent, Element container) {
		List<?> searchhelps = container.getChildren(ELEMENT_SEARCHHELP);
		for (Object o : searchhelps) {
			Element element = (Element) o;
			RetsSearchHelp searchhelp = buildSearchHelp();
			setAttributes(searchhelp, element);
			parent.addChild(MetadataType.SEARCH_HELP, searchhelp);
		}
	}

	private void processObject(RetsResource parent, Element container) {
		List<?> objects = container.getChildren(ELEMENT_OBJECT);
		for (Object object : objects) {
			Element element = (Element) object;
			RetsObject obj = buildObject();
			setAttributes(obj, element);
			parent.addChild(MetadataType.OBJECT, obj);
		}
	}

	private void processClass(RetsResource parent, Element container) {
		List<?> classes = container.getChildren(ELEMENT_CLASS);
		for (Object aClass : classes) {
			Element element = (Element) aClass;
			RetsClass clazz = buildClass();
			init(clazz);
			setAttributes(clazz, element);
			parent.addChild(MetadataType.CLASS, clazz);
			Element child;
			child = element.getChild(CONTAINER_TABLE);
			if (child != null) {
				processTable(clazz, child);
			}
			child = element.getChild(CONTAINER_UPDATE);
			if (child != null) {
				processUpdate(clazz, child);
			}
		}
	}

	private void processTable(RetsClass parent, Element container) {
		List<?> tables = container.getChildren(ELEMENT_TABLE);
		for (Object o : tables) {
			Element element = (Element) o;
			RetsTable table = buildTable();
			setAttributes(table, element);
			parent.addChild(MetadataType.TABLE, table);
		}
	}

	private void processUpdate(RetsClass parent, Element container) {
		List<?> updates = container.getChildren(ELEMENT_UPDATE);
		for (Object o : updates) {
			Element element = (Element) o;
			RetsUpdate update = buildUpdate();
			init(update);
			setAttributes(update, element);
			parent.addChild(MetadataType.UPDATE, update);
			Element child = element.getChild(CONTAINER_UPDATE_TYPE);
			if (child != null) {
				processUpdateType(update, child);
			}
		}
	}

	private void processUpdateType(RetsUpdate parent, Element container) {
		List<?> updateFields = container.getChildren(ELEMENT_UPDATETYPE);
		for (Object updateField : updateFields) {
			Element element = (Element) updateField;
			RetsUpdateType updateType = buildUpdateType();
			parent.addChild(MetadataType.UPDATE_TYPE, updateType);
			setAttributes(updateType, element);
		}
	}

	private void processForeignKey(RetsSystem system, Element container) {
		List<?> fkeys = container.getChildren("ForeignKey");
		for (Object fkey : fkeys) {
			Element element = (Element) fkey;
			RetsForeignKey foreignKey = buildForeignKey();
			setAttributes(foreignKey, element);
			system.addChild(MetadataType.FOREIGNKEYS, foreignKey);
		}
	}

}

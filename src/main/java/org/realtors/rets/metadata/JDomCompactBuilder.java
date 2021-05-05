/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.metadata;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.realtors.rets.metadata.types.*;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class JDomCompactBuilder extends MetadataBuilder {
	public static final String CONTAINER_PREFIX = "METADATA-";
	public static final String CONTAINER_ROOT = "RETS";
	public static final String CONTAINER_METADATA = "METADATA";
	public static final String CONTAINER_SYSTEM = "METADATA-SYSTEM";
	public static final String CONTAINER_RESOURCE = "METADATA-RESOURCE";
	public static final String CONTAINER_FOREIGNKEY = "METADATA-FOREIGN_KEY";
	public static final String CONTAINER_CLASS = "METADATA-CLASS";
	public static final String CONTAINER_TABLE = "METADATA-TABLE";
	public static final String CONTAINER_UPDATE = "METADATA-UPDATE";
	public static final String CONTAINER_UPDATETYPE = "METADATA-UPDATE_TYPE";
	public static final String CONTAINER_OBJECT = "METADATA-OBJECT";
	public static final String CONTAINER_SEARCHHELP = "METADATA-SEARCH_HELP";
	public static final String CONTAINER_EDITMASK = "METADATA-EDITMASK";
	public static final String CONTAINER_UPDATEHELP = "METADATA-UPDATE_HELP";
	public static final String CONTAINER_LOOKUP = "METADATA-LOOKUP";
	public static final String CONTAINER_LOOKUPTYPE = "METADATA-LOOKUP_TYPE";
	public static final String CONTAINER_VALIDATIONLOOKUP = "METADATA-VALIDATION_LOOKUP";
	public static final String CONTAINER_VALIDATIONLOOKUPTYPE = "METADATA-VALIDATION_LOOKUP_TYPE";
	public static final String CONTAINER_VALIDATIONEXPRESSION = "METADATA-VALIDATION_EXPRESSION";
	public static final String CONTAINER_VALIDATIONEXTERNAL = "METADATA-VALIDATION_EXTERNAL";
	public static final String CONTAINER_VALIDATIONEXTERNALTYPE = "METADATA-VALIDATION_EXTERNAL_TYPE";
	public static final String ELEMENT_SYSTEM = "SYSTEM";
	public static final String COLUMNS = "COLUMNS";
	public static final String DATA = "DATA";
	public static final String ATTRIBUTE_RESOURCE = "Resource";
	public static final String ATTRIBUTE_CLASS = "Class";
	public static final String ATTRIBUTE_UPDATE = "Update";
	public static final String ATTRIBUTE_LOOKUP = "Lookup";
	public static final String ATTRIBUTE_VALIDATIONEXTERNAL = "ValidationExternal";
	public static final String ATTRIBUTE_VALIDATIONLOOKUP = "ValidationLookup";
	private static final Log LOG = LogFactory.getLog(JDomCompactBuilder.class);

	@Override
	public Metadata doBuild(Object src) throws MetadataException {
		return build((Document) src);
	}

	public Metadata build(InputSource source) throws MetadataException {
		SAXBuilder builder = new SAXBuilder();
		Document document;
		try {
			document = builder.build(source);
		} catch (JDOMException e) {
			throw new MetadataException("Couldn't build document", e);
		} catch (IOException e) {
			throw new MetadataException("Couldn't build document", e);
		}
		return build(document);
	}

	@Override
	public MetadataObject[] parse(Object src) throws MetadataException {
		return parse((Document) src);
	}

	public MetadataObject[] parse(Document src) throws MetadataException {
		Element root = src.getRootElement();
		if (!root.getName().equals(CONTAINER_ROOT)) {
			throw new MetadataException("Invalid root element");
		}
		Element container = root.getChild(CONTAINER_SYSTEM);
		if (container != null) {
			RetsSystem sys = processSystem(container);
			if (root.getChild(CONTAINER_RESOURCE) != null) {
				Metadata m = new Metadata(sys);
				recurseAll(m, root);
			}
			return new MetadataObject[]{sys};
		}
		container = root.getChild(CONTAINER_RESOURCE);
		if (container != null) {
			return processResource(container);
		}
		container = root.getChild(CONTAINER_CLASS);
		if (container != null) {
			return processClass(container);
		}
		container = root.getChild(CONTAINER_TABLE);
		if (container != null) {
			return processTable(container);
		}
		container = root.getChild(CONTAINER_UPDATE);
		if (container != null) {
			return processUpdate(container);
		}
		container = root.getChild(CONTAINER_UPDATETYPE);
		if (container != null) {
			return processUpdateType(container);
		}
		container = root.getChild(CONTAINER_OBJECT);
		if (container != null) {
			return processObject(container);
		}
		container = root.getChild(CONTAINER_SEARCHHELP);
		if (container != null) {
			return processSearchHelp(container);
		}
		container = root.getChild(CONTAINER_EDITMASK);
		if (container != null) {
			return processEditMask(container);
		}
		container = root.getChild(CONTAINER_LOOKUP);
		if (container != null) {
			return processLookup(container);
		}
		container = root.getChild(CONTAINER_LOOKUPTYPE);
		if (container != null) {
			return processLookupType(container);
		}
		container = root.getChild(CONTAINER_VALIDATIONLOOKUP);
		if (container != null) {
			return processValidationLookup(container);
		}
		container = root.getChild(CONTAINER_VALIDATIONLOOKUPTYPE);
		if (container != null) {
			return processValidationLookupType(container);
		}
		container = root.getChild(CONTAINER_VALIDATIONEXTERNAL);
		if (container != null) {
			return processValidationExternal(container);
		}
		container = root.getChild(CONTAINER_VALIDATIONEXTERNALTYPE);
		if (container != null) {
			return processValidationExternalType(container);
		}
		container = root.getChild(CONTAINER_VALIDATIONEXPRESSION);
		if (container != null) {
			return processValidationExpression(container);
		}
		return null;
	}

	public Metadata build(Document src) throws MetadataException {
		Element root = src.getRootElement();
		if (!root.getName().equals(CONTAINER_ROOT)) {
			throw new MetadataException("Invalid root element");
		}
		Element element = root.getChild(CONTAINER_SYSTEM);
		if (element == null) {
			throw new MetadataException("Missing element " + CONTAINER_SYSTEM);
		}
		RetsSystem sys = processSystem(element);
		Metadata metadata;
		metadata = new Metadata(sys);
		recurseAll(metadata, root);
		return metadata;
	}

	private void recurseAll(Metadata metadata, Element root) throws MetadataParseException {
		attachResource(metadata, root);
		attachClass(metadata, root);
		attachTable(metadata, root);
		attachUpdate(metadata, root);
		attachUpdateType(metadata, root);
		attachObject(metadata, root);
		attachSearchHelp(metadata, root);
		attachEditMask(metadata, root);
		attachLookup(metadata, root);
		attachLookupType(metadata, root);
		attachValidationLookup(metadata, root);
		attachValidationLookupType(metadata, root);
		attachValidationExternal(metadata, root);
		attachValidationExternalType(metadata, root);
		attachValidationExpression(metadata, root);
	}

	private void setAttributes(MetadataObject obj, String[] columns, String[] data) {
		int count = columns.length;
		if (count > data.length) {
			count = data.length;
		}
		for (int i = 0; i < count; i++) {
			String column = columns[i];
			String datum = data[i];
			if (!datum.equals("")) {
				setAttribute(obj, column, datum);
			}
		}
	}

	private String[] getColumns(Element el) {
		Element cols = el.getChild(COLUMNS);
		return split(cols);
	}

	/**
	 * do NOT use string.split() unless your prepared to deal with loss due to token boundary conditions
	 */
	private String[] split(Element el) {
		if (el == null) return null;
		final String delimiter = "\t";
		StringTokenizer tkn = new StringTokenizer(el.getText(), delimiter, true);
		List<String> list = new LinkedList<>();
		tkn.nextToken(); // junk the first element
		String last = null;
		while (tkn.hasMoreTokens()) {
			String next = tkn.nextToken();
			if (next.equals(delimiter)) {
				if (last == null) {
					list.add("");
				} else {
					last = null;
				}
			} else {
				list.add(next);
				last = next;
			}
		}
		return list.toArray(new String[0]);
	}

	/**
	 * Gets an attribute that is not expected to be null (i.e. an attribute that
	 * MUST exist).
	 *
	 * @param element Element
	 * @param name    Attribute name
	 * @return value of attribute
	 * @throws MetadataParseException if the value is null.
	 */
	private String getNonNullAttribute(Element element, String name) throws MetadataParseException {
		String value = element.getAttributeValue(name);
		if (value == null) {
			throw new MetadataParseException("Attribute '" + name + "' not found on tag " + toString(element));
		}
		return value;
	}

	private String toString(Element element) {
		StringBuilder buffer = new StringBuilder();
		List<?> attributes = element.getAttributes();
		buffer.append("'").append(element.getName()).append("'");
		buffer.append(", attributes: ").append(attributes);
		return buffer.toString();
	}

	private RetsSystem processSystem(Element container) {
		Element element = container.getChild(ELEMENT_SYSTEM);
		RetsSystem system = buildSystem();
		// system metadata is such a hack.  the first one here is by far my favorite
		String comment = container.getChildText(RetsSystem.COMMENTS);
		String systemId = element.getAttributeValue(RetsSystem.SYSTEMID);
		String systemDescription = element.getAttributeValue(RetsSystem.SYSTEMDESCRIPTION);
		String version = container.getAttributeValue(RetsSystem.VERSION);
		String date = container.getAttributeValue(RetsSystem.DATE);
		setAttribute(system, RetsSystem.COMMENTS, comment);
		setAttribute(system, RetsSystem.SYSTEMID, systemId);
		setAttribute(system, RetsSystem.SYSTEMDESCRIPTION, systemDescription);
		setAttribute(system, RetsSystem.VERSION, version);
		setAttribute(system, RetsSystem.DATE, date);
		return system;
	}

	private void attachResource(Metadata metadata, Element root) {
		RetsSystem system = metadata.getSystem();
		List<?> containers = root.getChildren(CONTAINER_RESOURCE);
		for (Object o : containers) {
			Element container = (Element) o;
			RetsResource[] resources = this.processResource(container);
			for (RetsResource resource : resources) {
				system.addChild(MetadataType.RESOURCE, resource);
			}
		}
	}

	private RetsResource[] processResource(Element resourceContainer) {
		String[] columns = getColumns(resourceContainer);
		List<?> rows = resourceContainer.getChildren(DATA);
		RetsResource[] resources = new RetsResource[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			RetsResource resource = buildResource();
			setAttributes(resource, columns, data);
			resources[i] = resource;
		}
		return resources;
	}

	private void attachClass(Metadata metadata, Element root) throws MetadataParseException {
		List<?> containers = root.getChildren(CONTAINER_CLASS);
		for (Object o : containers) {
			Element container = (Element) o;
			String resourceId = getNonNullAttribute(container, ATTRIBUTE_RESOURCE);
			RetsResource resource = metadata.getResource(resourceId);
			RetsClass[] classes = processClass(container);
			for (RetsClass aClass : classes) {
				resource.addChild(MetadataType.CLASS, aClass);
			}
		}
	}

	private RetsClass[] processClass(Element classContainer) throws MetadataParseException {
		String name = classContainer.getName();
		String resourceId = getNonNullAttribute(classContainer, ATTRIBUTE_RESOURCE);
		LOG.debug("resource name: " + resourceId + " for container " + name);
		String[] columns = getColumns(classContainer);
		List<?> rows = classContainer.getChildren(DATA);
		RetsClass[] classes = new RetsClass[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			RetsClass clazz = buildClass();
			setAttributes(clazz, columns, data);
			classes[i] = clazz;
		}
		return classes;
	}

	private void attachTable(Metadata metadata, Element root) throws MetadataParseException {
		List<?> containers = root.getChildren(CONTAINER_TABLE);
		for (Object o : containers) {
			Element container = (Element) o;
			String resourceId = getNonNullAttribute(container, ATTRIBUTE_RESOURCE);
			String className = getNonNullAttribute(container, ATTRIBUTE_CLASS);
			RetsClass clazz = metadata.getRetsClass(resourceId, className);

			if (clazz == null) {
				//MarketLinx Strikes!!!
				LOG.warn("Found table metadata for resource class: " + resourceId + ":" + className
						+ " but there is no class metadata for " + resourceId + ":" + className);
				continue;
			}

			RetsTable[] fieldMetadata = processTable(container);
			for (RetsTable fieldMetadatum : fieldMetadata) {
				clazz.addChild(MetadataType.TABLE, fieldMetadatum);
			}
		}
	}

	private RetsTable[] processTable(Element tableContainer) {
		String[] columns = getColumns(tableContainer);
		List<?> rows = tableContainer.getChildren(DATA);
		RetsTable[] fieldMetadata = new RetsTable[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			RetsTable retsTable = buildTable();
			setAttributes(retsTable, columns, data);
			fieldMetadata[i] = retsTable;
		}
		return fieldMetadata;
	}

	private void attachUpdate(Metadata metadata, Element root) throws MetadataParseException {
		List<?> containers = root.getChildren(CONTAINER_UPDATE);
		for (Object o : containers) {
			Element container = (Element) o;
			RetsClass parent = metadata.getRetsClass(getNonNullAttribute(container, ATTRIBUTE_RESOURCE), getNonNullAttribute(
					container, ATTRIBUTE_CLASS));
			RetsUpdate[] updates = processUpdate(container);
			for (RetsUpdate update : updates) {
				parent.addChild(MetadataType.UPDATE, update);
			}
		}
	}

	private RetsUpdate[] processUpdate(Element container) {
		String[] columns = getColumns(container);
		List<?> rows = container.getChildren(DATA);
		RetsUpdate[] updates = new RetsUpdate[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			RetsUpdate update = buildUpdate();
			setAttributes(update, columns, data);
			updates[i] = update;
		}
		return updates;
	}

	private void attachUpdateType(Metadata metadata, Element root) throws MetadataParseException {
		List<?> containers = root.getChildren(CONTAINER_UPDATETYPE);
		for (Object o : containers) {
			Element container = (Element) o;
			RetsUpdate parent = metadata.getUpdate(getNonNullAttribute(container, ATTRIBUTE_RESOURCE),
					getNonNullAttribute(container, ATTRIBUTE_CLASS), getNonNullAttribute(container, ATTRIBUTE_UPDATE));
			RetsUpdateType[] updateTypes = processUpdateType(container);
			for (RetsUpdateType updateType : updateTypes) {
				parent.addChild(MetadataType.UPDATE_TYPE, updateType);
			}
		}
	}

	private RetsUpdateType[] processUpdateType(Element container) {
		String[] columns = getColumns(container);
		List<?> rows = container.getChildren(DATA);
		RetsUpdateType[] updateTypes = new RetsUpdateType[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			RetsUpdateType updateType = buildUpdateType();
			setAttributes(updateType, columns, data);
			updateTypes[i] = updateType;
		}
		return updateTypes;
	}

	private void attachObject(Metadata metadata, Element root) throws MetadataParseException {
		List<?> containers = root.getChildren(CONTAINER_OBJECT);
		for (Object o : containers) {
			Element container = (Element) o;
			RetsResource parent = metadata.getResource(getNonNullAttribute(container, ATTRIBUTE_RESOURCE));
			RetsObject[] objects = processObject(container);
			for (RetsObject object : objects) {
				parent.addChild(MetadataType.OBJECT, object);
			}
		}
	}

	private RetsObject[] processObject(Element objectContainer) {
		String[] columns = getColumns(objectContainer);
		List<?> rows = objectContainer.getChildren(DATA);
		RetsObject[] objects = new RetsObject[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			RetsObject object = buildObject();
			setAttributes(object, columns, data);
			objects[i] = object;
		}
		return objects;
	}

	private void attachSearchHelp(Metadata metadata, Element root) throws MetadataParseException {
		List<?> containers = root.getChildren(CONTAINER_SEARCHHELP);
		for (Object o : containers) {
			Element container = (Element) o;
			RetsResource parent = metadata.getResource(getNonNullAttribute(container, ATTRIBUTE_RESOURCE));
			RetsSearchHelp[] searchHelps = processSearchHelp(container);
			for (RetsSearchHelp searchHelp : searchHelps) {
				parent.addChild(MetadataType.SEARCH_HELP, searchHelp);
			}
		}
	}

	private RetsSearchHelp[] processSearchHelp(Element container) {
		String[] columns = getColumns(container);
		List<?> rows = container.getChildren(DATA);
		RetsSearchHelp[] searchHelps = new RetsSearchHelp[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			RetsSearchHelp searchHelp = buildSearchHelp();
			setAttributes(searchHelp, columns, data);
			searchHelps[i] = searchHelp;
		}
		return searchHelps;
	}

	private void attachEditMask(Metadata metadata, Element root) throws MetadataParseException {
		List<?> containers = root.getChildren(CONTAINER_EDITMASK);
		for (Object o : containers) {
			Element container = (Element) o;
			RetsResource parent = metadata.getResource(getNonNullAttribute(container, ATTRIBUTE_RESOURCE));
			RetsEditMask[] editMasks = processEditMask(container);
			for (RetsEditMask editMask : editMasks) {
				parent.addChild(MetadataType.EDITMASK, editMask);
			}
		}
	}

	private RetsEditMask[] processEditMask(Element container) {
		String[] columns = getColumns(container);
		List<?> rows = container.getChildren(DATA);
		RetsEditMask[] editMasks = new RetsEditMask[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			RetsEditMask editMask = buildEditMask();
			setAttributes(editMask, columns, data);
			editMasks[i] = editMask;
		}
		return editMasks;
	}

	private void attachLookup(Metadata metadata, Element root) throws MetadataParseException {
		List<?> containers = root.getChildren(CONTAINER_LOOKUP);
		for (Object o : containers) {
			Element container = (Element) o;
			RetsResource parent = metadata.getResource(getNonNullAttribute(container, ATTRIBUTE_RESOURCE));
			RetsLookup[] lookups = processLookup(container);
			for (RetsLookup lookup : lookups) {
				parent.addChild(MetadataType.LOOKUP, lookup);
			}
		}
	}

	private RetsLookup[] processLookup(Element container) {
		String[] columns = getColumns(container);
		List<?> rows = container.getChildren(DATA);
		RetsLookup[] lookups = new RetsLookup[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			RetsLookup lookup = buildLookup();
			setAttributes(lookup, columns, data);
			lookups[i] = lookup;
		}
		return lookups;
	}

	private void attachLookupType(Metadata metadata, Element root) throws MetadataParseException {
		List<?> containers = root.getChildren(CONTAINER_LOOKUPTYPE);
		for (Object o : containers) {
			Element container = (Element) o;
			RetsLookup parent = metadata.getLookup(getNonNullAttribute(container, ATTRIBUTE_RESOURCE),
					getNonNullAttribute(container, ATTRIBUTE_LOOKUP));

			if (parent == null) {
				LOG.warn("Skipping lookup type: could not find lookup for tag " + toString(container));
				continue;
			}

			RetsLookupType[] lookupTypes = processLookupType(container);
			for (RetsLookupType lookupType : lookupTypes) {
				parent.addChild(MetadataType.LOOKUP_TYPE, lookupType);
			}
		}
	}

	private RetsLookupType[] processLookupType(Element container) {
		String[] columns = getColumns(container);
		List<?> rows = container.getChildren(DATA);
		RetsLookupType[] lookupTypes = new RetsLookupType[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			RetsLookupType lookupType = buildLookupType();
			setAttributes(lookupType, columns, data);
			lookupTypes[i] = lookupType;
		}
		return lookupTypes;
	}

	private void attachValidationLookup(Metadata metadata, Element root) throws MetadataParseException {
		List<?> containers = root.getChildren(CONTAINER_VALIDATIONLOOKUP);
		for (Object o : containers) {
			Element container = (Element) o;
			RetsResource parent = metadata.getResource(getNonNullAttribute(container, ATTRIBUTE_RESOURCE));
			RetsValidationLookup[] validationLookups = processValidationLookup(container);
			for (RetsValidationLookup validationLookup : validationLookups) {
				parent.addChild(MetadataType.VALIDATION_LOOKUP, validationLookup);
			}
		}
	}

	private RetsValidationLookup[] processValidationLookup(Element container) {
		String[] columns = getColumns(container);
		List<?> rows = container.getChildren(DATA);
		RetsValidationLookup[] validationLookups = new RetsValidationLookup[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			RetsValidationLookup validationLookup = buildValidationLookup();
			setAttributes(validationLookup, columns, data);
			validationLookups[i] = validationLookup;
		}
		return validationLookups;
	}

	private void attachValidationLookupType(Metadata metadata, Element root) throws MetadataParseException {
		List<?> containers = root.getChildren(CONTAINER_VALIDATIONLOOKUPTYPE);
		for (Object o : containers) {
			Element container = (Element) o;
			RetsValidationLookup parent = metadata.getValidationLookup(getNonNullAttribute(container, ATTRIBUTE_RESOURCE),
					getNonNullAttribute(container, ATTRIBUTE_VALIDATIONLOOKUP));
			RetsValidationLookupType[] validationLookupTypes = processValidationLookupType(container);
			for (RetsValidationLookupType validationLookupType : validationLookupTypes) {
				parent.addChild(MetadataType.VALIDATION_LOOKUP_TYPE, validationLookupType);
			}
		}
	}

	private RetsValidationLookupType[] processValidationLookupType(Element container) {
		String[] columns = getColumns(container);
		List<?> rows = container.getChildren(DATA);
		RetsValidationLookupType[] validationLookupTypes = new RetsValidationLookupType[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			RetsValidationLookupType validationLookupType = buildValidationLookupType();
			setAttributes(validationLookupType, columns, data);
			validationLookupTypes[i] = validationLookupType;
		}
		return validationLookupTypes;
	}

	private void attachValidationExternal(Metadata metadata, Element root) {
		List<?> containers = root.getChildren(CONTAINER_VALIDATIONEXTERNAL);
		for (Object o : containers) {
			Element container = (Element) o;
			RetsResource parent = metadata.getResource(container.getAttributeValue(ATTRIBUTE_RESOURCE));
			RetsValidationExternal[] validationExternals = processValidationExternal(container);
			for (RetsValidationExternal validationExternal : validationExternals) {
				parent.addChild(MetadataType.VALIDATION_EXTERNAL, validationExternal);
			}
		}
	}

	private RetsValidationExternal[] processValidationExternal(Element container) {
		String[] columns = getColumns(container);
		List<?> rows = container.getChildren(DATA);
		RetsValidationExternal[] validationExternals = new RetsValidationExternal[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			RetsValidationExternal validationExternal = buildValidationExternal();
			setAttributes(validationExternal, columns, data);
			validationExternals[i] = validationExternal;
		}
		return validationExternals;
	}

	private void attachValidationExternalType(Metadata metadata, Element root) throws MetadataParseException {
		List<?> containers = root.getChildren(CONTAINER_VALIDATIONEXTERNALTYPE);
		for (Object o : containers) {
			Element container = (Element) o;
			RetsValidationExternal parent = metadata.getValidationExternal(getNonNullAttribute(container,
					ATTRIBUTE_RESOURCE), getNonNullAttribute(container, ATTRIBUTE_VALIDATIONEXTERNAL));
			RetsValidationExternalType[] validationExternalTypes = processValidationExternalType(container);
			for (RetsValidationExternalType validationExternalType : validationExternalTypes) {
				parent.addChild(MetadataType.VALIDATION_EXTERNAL_TYPE, validationExternalType);
			}
		}
	}

	private RetsValidationExternalType[] processValidationExternalType(Element container) {
		String[] columns = getColumns(container);
		List<?> rows = container.getChildren(DATA);
		RetsValidationExternalType[] validationExternalTypes = new RetsValidationExternalType[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			RetsValidationExternalType validationExternalType = buildValidationExternalType();
			setAttributes(validationExternalType, columns, data);
			validationExternalTypes[i] = validationExternalType;
		}
		return validationExternalTypes;
	}

	private void attachValidationExpression(Metadata metadata, Element root) throws MetadataParseException {
		List<?> containers = root.getChildren(CONTAINER_VALIDATIONEXPRESSION);
		for (Object o : containers) {
			Element container = (Element) o;
			RetsResource parent = metadata.getResource(getNonNullAttribute(container, ATTRIBUTE_RESOURCE));
			RetsValidationExpression[] expressions = processValidationExpression(container);
			for (RetsValidationExpression expression : expressions) {
				parent.addChild(MetadataType.VALIDATION_EXPRESSION, expression);
			}
		}
	}

	private RetsValidationExpression[] processValidationExpression(Element container) {
		String[] columns = getColumns(container);
		List<?> rows = container.getChildren(DATA);
		RetsValidationExpression[] expressions = new RetsValidationExpression[rows.size()];
		for (int i = 0; i < expressions.length; i++) {
			Element element = (Element) rows.get(i);
			String[] data = split(element);
			RetsValidationExpression expression = buildValidationExpression();
			setAttributes(expression, columns, data);
			expressions[i] = expression;
		}
		return expressions;
	}

}

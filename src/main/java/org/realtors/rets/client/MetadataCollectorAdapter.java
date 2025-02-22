package org.realtors.rets.client;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.realtors.rets.metadata.MetadataCollector;
import org.realtors.rets.metadata.MetadataObject;
import org.realtors.rets.metadata.MetadataType;

public abstract class MetadataCollectorAdapter implements MetadataCollector {


	private static final Log LOG = LogFactory.getLog(MetadataCollectorAdapter.class);

	public MetadataObject[] getMetadata(MetadataType type, String path) {
		return getSome(type, path, "0");
	}

	public MetadataObject[] getMetadataRecursive(MetadataType type, String path) {
		return getSome(type, path, "*");
	}

	private MetadataObject[] getSome(MetadataType type, String path, String sfx) {
		boolean compact = Boolean.getBoolean("rets-client.metadata.compact");
		try {
			GetMetadataRequest req;
			if (path == null || path.equals("")) {
				req = new GetMetadataRequest(type.name(), sfx);
			} else {
				String[] ppath = StringUtils.split(path, ":");
				String[] id = new String[ppath.length + 1];
				System.arraycopy(ppath, 0, id, 0, ppath.length);
				id[ppath.length] = sfx;
				req = new GetMetadataRequest(type.name(), id);
			}
			if (compact) {
				req.setCompactFormat();
			}
			GetMetadataResponse response;

			response = doRequest(req);

			return response.getMetadata();
		} catch (RetsException e) {
			LOG.error("bad metadata request", e);
			return null;
		}
	}

	/**
	 * Perform operation of turning a GetMetadataRequest into
	 * a GetMetadataResponse
	 *
	 * @param req Requested metadata
	 * @return parsed MetaObjects
	 * @throws RetsException if an error occurs
	 */
	protected abstract GetMetadataResponse doRequest(GetMetadataRequest req) throws RetsException;
}

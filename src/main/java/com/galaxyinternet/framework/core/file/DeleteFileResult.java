package com.galaxyinternet.framework.core.file;

import java.util.List;

public class DeleteFileResult extends FileResult {
	List<String> keysOfDeletedObjects;

	public List<String> getKeysOfDeletedObjects() {
		return keysOfDeletedObjects;
	}

	public void setKeysOfDeletedObjects(List<String> keysOfDeletedObjects) {
		this.keysOfDeletedObjects = keysOfDeletedObjects;
	}

}

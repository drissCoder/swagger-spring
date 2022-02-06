package com.javainuse.payload;

public class UploadFileResponse
{
	private final String fileName;
	private final String fileDownloadUri;
	private final String fileType;
	private final long size;

	public UploadFileResponse(final String fileName, final String fileDownloadUri, final String fileType, final long size)
	{
		this.fileName = fileName;
		this.fileDownloadUri = fileDownloadUri;
		this.fileType = fileType;
		this.size = size;
	}

	// Getters and Setters (Omitted for brevity)
}

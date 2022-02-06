package com.javainuse.service;


import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.javainuse.exception.FileStorageException;
import com.javainuse.exception.MyFileNotFoundException;
import com.javainuse.property.FileStorageProperties;


@Service
public class FileStorageService
{

	private final Path fileStorageLocation;

	@Autowired
	public FileStorageService(final FileStorageProperties fileStorageProperties)
	{
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		try
		{
			Files.createDirectories(this.fileStorageLocation);
		}
		catch (final Exception ex)
		{
			throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
		}
	}

	public String storeFile(final MultipartFile file)
	{
		// Normalize file name
		final String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try
		{
			// Check if the file's name contains invalid characters
			if (fileName.contains(".."))
			{
				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
			}

			// Copy file to the target location (Replacing existing file with the same name)
			final Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return fileName;
		}
		catch (final IOException ex)
		{
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
		}
	}

	public Resource loadFileAsResource(final String fileName)
	{
		try
		{
			final Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			final Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists())
			{
				return resource;
			}
			else
			{
				throw new MyFileNotFoundException("File not found " + fileName);
			}
		}
		catch (final MalformedURLException ex)
		{
			throw new MyFileNotFoundException("File not found " + fileName, ex);
		}
	}
}

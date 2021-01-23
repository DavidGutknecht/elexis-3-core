package ch.elexis.core.services;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.InvalidPathException;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;

import ch.elexis.core.services.internal.VirtualFilesystemHandle;
import ch.elexis.core.utils.CoreUtil;

@Component
public class VirtualFilesystemService implements IVirtualFilesystemService {
	
	@Override
	public IVirtualFilesystemHandle of(String urlString) throws IOException{
		
		if (StringUtils.startsWith(urlString, "\\\\")) {
			String replaced = urlString.replace("\\", "/");
			if (CoreUtil.isWindows()) {
				// https://wiki.eclipse.org/Eclipse/UNC_Paths
				urlString = "file://" + replaced;
			} else {
				urlString = "smb:" + replaced;
			}
		}
		
		System.out.println("of "+urlString);
		
		URL url = assertUrl(urlString);
		return new VirtualFilesystemHandle(url);
	}
	
	@Override
	public IVirtualFilesystemHandle of(File file) throws IOException{
		if (file == null) {
			return null;
		}
		
		URL url = file.toURI().toURL();
		return new VirtualFilesystemHandle(url);
	}
	
	private URL assertUrl(String urlString) throws IOException{
		if (StringUtils.isBlank(urlString)) {
			throw new IOException("urlString is null");
		}
		
		URL url = isValidURL(urlString);
		if (url != null) {
			return url;
		} else {
			File file = new File(urlString);
			try {
				file.toPath();
				return file.toURI().toURL();
			} catch (InvalidPathException e) {}
			
		}
		
		throw new IOException("Can not handle url string [" + urlString + "]");
	}
	
	private URL isValidURL(String urlString){
		try {
			URL url = new URL(urlString);
			url.toURI();
			return url;
		} catch (Exception exception) {
			return null;
		}
	}
	
}

package net.eduard.api.setup;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.plugin.java.JavaPlugin;

import sun.net.www.protocol.file.FileURLConnection;

public final class PluginAPI {
	/**
	 * Private helper method
	 * 
	 * @param directory
	 *            The directory to start with
	 * @param pckgname
	 *            The package name to search for. Will be needed for getting the
	 *            Class object.
	 * @param classes
	 *            if a file isn't loaded but still is in the directory
	 * @throws ClassNotFoundException
	 */
	private static void checkDirectory(File directory, String pckgname,
			ArrayList<Class<?>> classes) throws ClassNotFoundException {
		File tmpDirectory;

		if (directory.exists() && directory.isDirectory()) {
			final String[] files = directory.list();

			for (final String file : files) {
				if (file.endsWith(".class")) {
					try {
						classes.add(Class.forName(pckgname + '.'
								+ file.substring(0, file.length() - 6)));
					} catch (final NoClassDefFoundError e) {
						// do nothing. this class hasn't been found by the
						// loader, and we don't care.
					}
				} else if ((tmpDirectory = new File(directory, file))
						.isDirectory()) {
					checkDirectory(tmpDirectory, pckgname + "." + file,
							classes);
				}
			}
		}
	}

	/**
	 * Private helper method.
	 * 
	 * @param connection
	 *            the connection to the jar
	 * @param pckgname
	 *            the package name to search for
	 * @param classes
	 *            the current ArrayList of all classes. This method will simply
	 *            add new classes.
	 * @throws ClassNotFoundException
	 *             if a file isn't loaded but still is in the jar file
	 * @throws IOException
	 *             if it can't correctly read from the jar file.
	 */
	private static void checkJarFile(JarURLConnection connection,
			String pckgname, ArrayList<Class<?>> classes)
			throws ClassNotFoundException, IOException {
		final JarFile jarFile = connection.getJarFile();
		final Enumeration<JarEntry> entries = jarFile.entries();
		String name;

		for (JarEntry jarEntry = null; entries.hasMoreElements()
				&& ((jarEntry = entries.nextElement()) != null);) {
			name = jarEntry.getName();

			if (name.contains(".class")) {
				name = name.substring(0, name.length() - 6).replace('/', '.');

				if (name.contains(pckgname)) {
					classes.add(Class.forName(name));
				}
			}
		}
	}
	/**
	 * Pega uma lista de classes de uma package
	 * 
	 * @param plugin
	 *            Plugin
	 * @param pkgname
	 *            Package
	 * @return Lista de Classes
	 */
	public static ArrayList<Class<?>> getClassesForPackage(JavaPlugin plugin,
			String pkgname) {
		ArrayList<Class<?>> classes = new ArrayList<>();

		CodeSource src = plugin.getClass().getProtectionDomain()
				.getCodeSource();
		if (src != null) {
			URL resource = src.getLocation();
			resource.getPath();
			processJarfile(resource, pkgname, classes);
		}
		return classes;
	}
	/**
	 * Processa um arquivo lingando se preciso for
	 * 
	 * @param resource
	 *            URL de Recurso
	 * @param pkgname
	 *            Package
	 * @param classes
	 *            Lista de Classes
	 */
	@SuppressWarnings("resource")
	private static void processJarfile(URL resource, String pkgname,
			ArrayList<Class<?>> classes) {
		String relPath = pkgname.replace('.', '/');
		String resPath = resource.getPath().replace("%20", " ");
		String jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar")
				.replaceFirst("file:", "");
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(jarPath);
		} catch (IOException e) {
			throw new RuntimeException(
					"Unexpected IOException reading JAR File '" + jarPath
							+ "'. Do you have strange characters in your folders? Such as #?",
					e);
		}

		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String entryName = entry.getName();
			String className = null;
			if ((entryName.endsWith(".class"))
					&& (entryName.startsWith(relPath))
					&& (entryName.length() > relPath.length() + "/".length())) {
				className = entryName.replace('/', '.').replace('\\', '.')
						.replace(".class", "");
			}
			if (className != null) {
				try {
					Class<?> c = Class.forName(className);
					if (c != null)
						classes.add(c);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}
	/**
	 * Attempts to list all the classes in the specified package as determined
	 * by the context class loader
	 * 
	 * @param pckgname
	 *            the package name to search
	 * @return a list of classes that exist within that package
	 * @throws ClassNotFoundException
	 *             if something went wrong
	 */
	public static ArrayList<Class<?>> getClassesForPackage(String pckgname)
			throws ClassNotFoundException {
		final ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

		try {
			final ClassLoader cld = Thread.currentThread()
					.getContextClassLoader();

			if (cld == null)
				throw new ClassNotFoundException("Can't get class loader.");

			final Enumeration<URL> resources = cld
					.getResources(pckgname.replace('.', '/'));
			URLConnection connection;

			for (URL url = null; resources.hasMoreElements()
					&& ((url = resources.nextElement()) != null);) {
				try {
					connection = url.openConnection();

					if (connection instanceof JarURLConnection) {
						checkJarFile((JarURLConnection) connection, pckgname,
								classes);
					} else if (connection instanceof FileURLConnection) {
						try {
							checkDirectory(new File(
									URLDecoder.decode(url.getPath(), "UTF-8")),
									pckgname, classes);
						} catch (final UnsupportedEncodingException ex) {
							throw new ClassNotFoundException(
									pckgname + " does not appear to be a valid package (Unsupported encoding)",
									ex);
						}
					} else
						throw new ClassNotFoundException(pckgname + " ("
								+ url.getPath()
								+ ") does not appear to be a valid package");
				} catch (final IOException ioex) {
					throw new ClassNotFoundException(
							"IOException was thrown when trying to get all resources for "
									+ pckgname,
							ioex);
				}
			}
		} catch (final NullPointerException ex) {
			throw new ClassNotFoundException(
					pckgname + " does not appear to be a valid package (Null pointer exception)",
					ex);
		} catch (final IOException ioex) {
			throw new ClassNotFoundException(
					"IOException was thrown when trying to get all resources for "
							+ pckgname,
					ioex);
		}

		return classes;
	}
}

package net.eduard.api.lib.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.bukkit.plugin.java.JavaPlugin;
/**
 * Sistema de configuração simples que suporte Comentarios
 * Post: https://bukkit.org/threads/resource-most-easy-config-manager-ever-resource.187997
 * https://bukkit.org/threads/tut-custom-yaml-configurations-with-comments.142592/
 * @author Internet
 */
public class MyConfigManager {
	private final JavaPlugin plugin;

	public MyConfigManager(JavaPlugin plugin) {
		this.plugin = plugin;

	}

	public MyConfig getNewConfig(String fileName, String[] header) {
		File file = getConfigFile(fileName);
		if (!file.exists()) {
			prepareFile(fileName);
			if ((header != null) && (header.length != 0)) {
				setHeader(file, header);
			}
		}
		return new MyConfig(getConfigContent(fileName), file,
				getCommentsNum(file), this.plugin);
	}

	/**
	 *
	 * @param fileName Nome
	 * @return novo MyConfig
	 */
	public MyConfig getNewConfig(String fileName) {
		return getNewConfig(fileName, null);
	}

	private File getConfigFile(String file) {
		if ((file == null) || (file.isEmpty()))
			return  new File("arquivo");
		File configFile;
		if (file.contains("/")) {
			if (file.startsWith("/"))
				configFile = new File(this.plugin.getDataFolder() + file.replace("/", File.separator));
			else {
				configFile = new File(this.plugin.getDataFolder() + File.separator + file.replace("/", File.separator));
			}
		} else {
			configFile = new File(this.plugin.getDataFolder(), file);
		}
		return configFile;
	}

	public void prepareFile(String filePath, String resource) {
		File file = getConfigFile(filePath);
		if (file.exists()) {
			return;
		}
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
			if ((resource != null) && (!resource.isEmpty())) {
				copyResource(this.plugin.getResource(resource), file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void prepareFile(String filePath) {
		prepareFile(filePath, null);
	}

	public void setHeader(File file, String[] header) {
		if (!file.exists()) {
			return;
		}
		try {
			StringBuilder config = new StringBuilder();
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String currentLine;
			while ((currentLine = reader.readLine()) != null) {
				String currentLine1 = currentLine;
				config.append(currentLine1 + "\n");
			}
			reader.close();
			config.append("# +----------------------------------------------------+ #\n");
			for (String line : header) {
				if (line.length() <= 50) {
					int lenght = (50 - line.length()) / 2;
					StringBuilder finalLine = new StringBuilder(line);
					for (int i = 0; i < lenght; i++) {
						finalLine.append(" ");
						finalLine.reverse();
						finalLine.append(" ");
						finalLine.reverse();
					}
					if (line.length() % 2 != 0) {
						finalLine.append(" ");
					}
					config.append("# < ").append(finalLine.toString()).append(" > #\n");
				}
			}
			config.append("# +----------------------------------------------------+ #");

			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(prepareConfigString(config.toString()));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Transforma os comanentarios # em se§§es
	 * 
	 * @param file
	 * @return
	 */
	public InputStream getConfigContent(File file) {
		if (!file.exists()) {
			return null;
		}
		try {
			int commentNum = 0;

			String pluginName = getPluginName();

			StringBuilder whole = new StringBuilder();
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String currentLine;
			while ((currentLine = reader.readLine()) != null) {
				if (currentLine.startsWith("#")) {
					String addLine = currentLine.replaceFirst("#", pluginName + "_COMMENT_" + commentNum + ":");
					whole.append(addLine + "\n");
					commentNum++;
				} else {
					whole.append(currentLine + "\n");
				}
			}
			String config = whole.toString();
			InputStream configStream = new ByteArrayInputStream(config.getBytes(StandardCharsets.UTF_8));

			reader.close();
			return configStream;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private int getCommentsNum(File file) {
		if (!file.exists()) {
			return 0;
		}
		try {
			int comments = 0;

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String currentLine;
			while ((currentLine = reader.readLine()) != null) {
				if (currentLine.startsWith("#")) {
					comments++;
				}
			}
			reader.close();
			return comments;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public InputStream getConfigContent(String filePath) {
		return getConfigContent(
				getConfigFile(filePath));
	}

	private String prepareConfigString(String configString) {
		int lastLine = 0;
		int headerLine = 0;

		String[] lines = configString.split("\n");
		StringBuilder config = new StringBuilder();
		for (String line : lines) {
			if (line.startsWith(getPluginName() + "_COMMENT")) {
				String comment = "#" + line.trim().substring(line.indexOf(":") + 1);
				if (comment.startsWith("# +-")) {
					if (headerLine == 0) {
						config.append(comment + "\n");
						lastLine = 0;
						headerLine = 1;
					} else if (headerLine == 1) {
						config.append(comment + "\n\n");
						lastLine = 0;
						headerLine = 0;
					}
				} else {
					String normalComment1;
					if (comment.startsWith("# ' "))
						normalComment1 = comment.substring(0, comment.length() - 1).replaceFirst("# ' ", "# ");
					else {
						normalComment1 = comment;
					}
					if (lastLine == 0)
						config.append(normalComment1 + "\n");
					else if (lastLine == 1) {
						config.append("\n" + normalComment1 + "\n");
					}
					lastLine = 0;
				}
			} else {
				config.append(line + "\n");
				lastLine = 1;
			}
		}
		return config.toString();
	}

	public void saveConfig(String configString, File file) {
		String configuration = prepareConfigString(configString);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(configuration);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getPluginName() {
		return this.plugin.getDescription().getName();
	}

	private void copyResource(InputStream resource, File file) {
		try {
			OutputStream out = new FileOutputStream(file);

			byte[] buf = new byte[1024];
			int length;
			while ((length = resource.read(buf)) > 0) {
				out.write(buf, 0, length);
			}
			out.close();
			resource.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
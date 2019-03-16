package net.eduard.api.lib.old;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Sistema de criar configuração usando PARSER próprio semelhante {@link Properties}<br>
 * Versão Anterior {@link ConfigProperties} 1.0
 * @since 0.7
 * @author Eduard
 * @version 2.0
 * @deprecated Use {@link Properties}
 * 
 */
public class SimplesConfig
{
  private List<String> lines = new ArrayList<String>();
  
  private Path path;
  
  public SimplesConfig(String path)
  {
    setPath(Paths.get(path, new String[0]));
    if (getFile().exists()) {
      reload();
    }
  }
  
  public void addComment(int index, String comment)
  {
    if (index < this.lines.size()) {
      this.lines.add(index, "# " + comment);
    }
  }
  
  public void clear()
  {
    this.lines.clear();
  }
  
  public boolean contains(String section)
  {
    return getSection(section) != -1;
  }
  
  public void create(String section)
  {
    set(section, "{}");
  }
  
  public void delete(String section)
  {
    int id = getSection(section);
    if (contains(section)) {
      this.lines.remove(id);
    }
  }
  
  public Object get(String section)
  {
    int id = getSection(section);
    if (contains(section)) return ((String)this.lines.get(id)).split(": ")[1];
    return null;
  }
  
  public File getFile()
  {
    return getPath().toFile();
  }
  
  public int getInt(String section)
  {
    return Principal.toInt(get(section));
  }
  
  public Path getPath()
  {
    return this.path;
  }
  
  private int getSection(String section)
  {
    section = getText(section);
    for (int i = 0; i < this.lines.size(); i++) {
      if (((String)this.lines.get(i)).startsWith(section + ": ")) return i;
    }
    return -1;
  }
  
  private String getText(String section)
  {
    return section.replace(':', '_').replace(' ', '_');
  }
  
  public boolean isComment(int index)
  {
    if (index >= this.lines.size()) return false;
    return isComment((String)this.lines.get(index));
  }
  
  public boolean isComment(String line)
  {
    return line.startsWith("# ");
  }
  
  public boolean isEmpty(String line)
  {
    return line.startsWith(" ") | line.isEmpty();
  }
  

  public boolean isSection(int index)
  {
    if (index >= this.lines.size()) return false;
    return isSection((String)this.lines.get(index));
  }
  
  public boolean isSection(String line)
  {
    return !isComment(line) & line.contains(": ");
  }
  
  public void reload()
  {
    try {
      List<String> list = Files.readAllLines(getPath(), Charset.defaultCharset());
      for (String line : list) {
        if (isComment(line)) {
          this.lines.add(line);
        } else if (isSection(line)) {
          this.lines.add(line);
        }
      }
    }
    catch (Exception localException) {}
  }
  


  public void remove(int index)
  {
    if (index < this.lines.size()) {
      this.lines.remove(index);
    }
  }
  
  public void save()
  {
    try {
      Files.write(getPath(), this.lines, Charset.defaultCharset(), new OpenOption[0]);
    }
    catch (Exception localException) {}
  }
  
  public void set(String section, Object value)
  {
    int id = getSection(section);
    if (contains(section)) {
      this.lines.set(id, getText(section) + ": " + value.toString());
    } else {
      this.lines.add(getText(section) + ": " + value.toString());
    }
  }
  
  private void setPath(Path path)
  {
    this.path = path;
  }
  
  public void showLines()
  {
    int index = 0;
    for (String line : this.lines) {
      System.err.println("N:" + index + "/ " + line);
      index++;
    }
  }
}

package net.eduard.api.lib.old;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
/**
 * Sistema de Arquivos de configurações usando PARSER próprio de YAML<br>
 * 
 * @version 1.0
 * @author Eduard
 * @deprecated Versão atual {@link net.eduard.api.lib.config.Config}
 *
 */
public class Config
{
  private List<String> lines = new ArrayList<>();
  
  private Path path;
  
  public Config(String path)
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
    return has(getSection(section));
  }
  
  public void create(String section)
  {
    set(section, "{}");
  }
  
  public Object get(String section)
  {
    section = getSection(section);
    if (has(section)) return ((String)this.lines.get(getIndex(section))).split(": ")[1];
    return null;
  }
  
  private String getEmpty(int size)
  {
    StringBuilder builder = new StringBuilder();
    for (int id = 0; id < size; id++) {
      builder.append(" ");
    }
    return builder.toString();
  }
  
  public File getFile()
  {
    return getPath().toFile();
  }
  
  private int getIndex(String section)
  {
    if (section.contains(":")) {
      String[] split = section.split(":");
      for (int id = 0; id < this.lines.size(); id++)
      {
        if (((String)this.lines.get(id)).startsWith(getEmpty((split.length - 1) * 2) + split[(split.length - 1)] + ": ")) {
          int index = split.length - 1;
          for (int number = id; number >= 0; number--)
          {
            if (((String)this.lines.get(number)).startsWith(getEmpty(index * 2) + split[index] + ": ")) {
              if (index == 0) { return id;
              }
              index--;
            }
          }
        }
      }
    }
    else {
      for (int id = 0; id < this.lines.size(); id++) {
        if (((String)this.lines.get(id)).startsWith(section + ": ")) return id;
      }
    }
    return -1;
  }
  
  public Path getPath()
  {
    return this.path;
  }
  
  private String getSection(String section)
  {
    section = section.replace(' ', '_').replace(':', '_').replace('.', ':');
    String[] split = section.split(":");
    List<String> lines = new ArrayList<>();
    String[] arrayOfString1; int j = (arrayOfString1 = split).length; for (int i = 0; i < j; i++) { String subsection = arrayOfString1[i];
      if (!subsection.isEmpty()) {
        lines.add(subsection);
      }
    }
    StringBuffer builder = new StringBuffer();
    for (int i = 0; i < lines.size(); i++) {
      if (i + 1 == lines.size()) {
        builder.append((String)lines.get(i));
      } else {
        builder.append((String)lines.get(i) + ":");
      }
    }
    return builder.toString();
  }
  
  private boolean has(String section)
  {
    if (section.isEmpty()) return false;
    return getIndex(section) != -1;
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
  
  public boolean isSection(int index)
  {
    if (index >= this.lines.size()) return false;
    return isSection((String)this.lines.get(index));
  }
  
  public boolean isSection(String line)
  {
    return !isComment(line) & line.contains(": ");
  }
  
  public void reload() {
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
  
  public void set(String section, Object object)
  {
    if (section.isEmpty()) return;
    section = getSection(section);
    int index = getIndex(section);
    
    if (section.contains(":"))
    {
      String[] split = section.split(":");
      int size = split.length - 1;
      if (has(section)) {
        this.lines.set(index, 
          getEmpty(size * 2) + split[size] + ": " + object.toString());
      } else {
        String text = split[0];
        int id = 0;
        for (int i = 0; i < split.length; i++) {
          if (i == 0) {
            if (!has(text)) {
              id = this.lines.size();
              this.lines.add(id, getEmpty(i * 2) + split[i] + ": ");
            } else {
              id = getIndex(text);
            }
            text = text + ":" + split[(i + 1)];
          } else {
            if (!has(text)) {
              id++;
              this.lines.add(id, getEmpty(i * 2) + split[i] + ": ");
            } else {
              id = getIndex(text);
            }
            
            if (i == split.length - 1)
            {
              this.lines.set(id, getEmpty(i * 2) + split[i] + ": " + object.toString());
            }
            else {
              text = text + ":" + split[(i + 1)];
            }
            
          }
          
        }
      }
    }
    else if (has(section)) {
      this.lines.set(index, section + ": " + object.toString());
    } else {
      this.lines.add(section + ": " + object.toString());
    }
  }
  


  public void setModificado(String section, Object object)
  {
    if (section.isEmpty()) return;
    section = getSection(section);
    int index = getIndex(section);
    
    if (section.contains(":"))
    {
      String[] split = section.split(":");
      int size = split.length - 1;
      if (has(section)) {
        this.lines.set(index, 
          getEmpty(size * 2) + split[size] + ": " + object.toString());
      } else {
        String text = split[0];
        int id = 0;
        for (int i = 0; i < split.length; i++) {
          if (i == 0) {
            if (!has(text)) {
              id = this.lines.size();
              this.lines.add(id, getEmpty(i * 2) + split[i] + ": ");
            } else {
              id = getIndex(text);
            }
            text = text + ":" + split[(i + 1)];
          } else {
            if (!has(text)) {
              id++;
              this.lines.add(id, getEmpty(i * 2) + split[i] + ": ");
            } else {
              id = getIndex(text);
            }
            
            if (i == split.length - 1)
            {
              this.lines.set(id, getEmpty(i * 2) + split[i] + ": " + object.toString());
            }
            else {
              text = text + ":" + split[(i + 1)];
            }
            
          }
          
        }
        
      }
    }
    else if (has(section)) {
      this.lines.set(index, section + ": " + object.toString());
    } else {
      this.lines.add(section + ": " + object.toString());
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

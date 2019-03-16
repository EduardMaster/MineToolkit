package net.eduard.api.lib.old;


/**
 * Tag do jogador<br>
 * Versão anterior {@link TagSetup} 1.0
 * 
 * @version 1.0
 * @since 0.7
 * @author Eduard
 * @deprecated Versão Atual {@link net.eduard.api.lib.game.Tag}
 *
 */

public class TagInfoSetup
  implements TextSetup
{
  private String prefix;
  private String suffix;
  
  public TagInfoSetup(String prefix, String suffix)
  {
    prefix = getText(prefix);
    suffix = getText(suffix);
    setPrefix(prefix);
    setSuffix(suffix);
  }
  
  public String getPrefix()
  {
    return this.prefix;
  }
  
  public void setPrefix(String prefix)
  {
    this.prefix = prefix;
  }
  
  public String getSuffix()
  {
    return this.suffix;
  }
  
  public void setSuffix(String suffix)
  {
    this.suffix = suffix;
  }
}

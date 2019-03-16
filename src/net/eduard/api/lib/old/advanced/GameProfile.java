package net.eduard.api.lib.old.advanced;

import java.lang.reflect.Field;
import java.util.UUID;
/**
 * Representa a GameProfile do NMS
 * 
 * @version 1.0
 * @since 0.7
 * @author Eduard
 *
 */
public class GameProfile
  extends Packet
{
  private Object profile;
  
  public UUID getId()
    throws Exception
  {
    Field id = getProfile().getClass().getDeclaredField("id");
    id.setAccessible(true);
    return (UUID)id.get(getProfile());
  }
  
  public String getName() throws Exception
  {
    Field name = getProfile().getClass().getDeclaredField("name");
    name.setAccessible(true);
    return (String)name.get(getProfile());
  }
  
  public Object getProfile()
  {
    return this.profile;
  }
  
  public void setName(String displayName)
    throws Exception
  {
    Field name = getProfile().getClass().getDeclaredField("name");
    name.setAccessible(true);
    name.set(getProfile(), displayName);
  }
  
  public void setProfile(Object profile)
  {
    this.profile = profile;
  }
}

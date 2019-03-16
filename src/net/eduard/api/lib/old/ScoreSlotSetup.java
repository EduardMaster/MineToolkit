package net.eduard.api.lib.old;


public class ScoreSlotSetup
{
  private int slot;
  private String text;
  
  public ScoreSlotSetup(int slot, String text)
  {
    if (((slot < 1 ? 1 : 0) | (slot > 15 ? 1 : 0)) != 0) {
      slot = 1;
    }
    if (text.isEmpty()) {
      text = "Empty";
    }
    setSlot(slot);
    setText(text);
  }
  
  public int getSlot()
  {
    return this.slot;
  }
  
  public void setSlot(int slot)
  {
    this.slot = slot;
  }
  
  public String getText()
  {
    return this.text;
  }
  
  public void setText(String text)
  {
    this.text = text;
  }
}

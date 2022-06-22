public interface Life {
  public int getHealth();
  public int getAttack();
  public void addDamage(int attack);
  public int getCurrentLandIndex();
  public void setCurrentLandIndex(int landIndex);
  public String getType();
}

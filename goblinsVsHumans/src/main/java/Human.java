public class Human implements Life {
  private int health;
  private int[] attacks;
  private int currentLandIndex;
  
  public Human () {
    this.health = 100;
    this.attacks = new int[] {25, 50, 75};
  }

  public int getHealth () { return this.health; }

  public int getAttack() {
    int randomIndex = (int) Math.floor(Math.random() * (this.attacks.length + 1));
    return this.attacks[randomIndex];
  }

  public void addDamage(int attack) { this.health -= attack; }

  public int getCurrentLandIndex() { return this.currentLandIndex; }

  public void setCurrentLandIndex (int landIndex) { this.currentLandIndex = landIndex; }

  public String getType () { return String.format("%s", this.getClass().getName()); }

  @Override
  public String toString() { return String.format("Type: %s; Stats: Health - %s , Current Land - %s", this.getClass().getName(), this.getHealth(), this.getCurrentLandIndex()); }
}

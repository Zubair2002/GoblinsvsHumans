import java.util.HashMap;

public class Land {
  private boolean occupied;
  private HashMap<String, Class<?>> occupants;
  private int currentGridIndex;

  public Land () {
    this.occupied = false;
    this.occupants = new HashMap<String, Class<?>>();
  }

  public boolean getOccupationStatus () { return this.occupied; }

  public void setOccupationStatus (boolean status) { this.occupied = status; };

  public HashMap<String, Class<?>> getOccupants () { return this.occupants; };

  public void addOccupant (Class<?> life) {
    this.occupants.put(life.getType(), life);
  }

  public Class<?> removeOccupant (Class<?> life) {
    this.occupants.remove(life.getType());
  }

  public int getCurrentGridIndex () { return this.currentGridIndex; }

  public void setCurrentGridIndex (int gridIndex) { this.currentGridIndex = gridIndex; }

  public String getType () { return String.format("%s", this.getClass().getName()); }

  public String getDisplay () {
    String occupants = "";
    this.getOccupants.forEach((k, v) -> {
      occupants += String.format("{%s}", v)
    });
    return String.format("[%s]", occupants);
  }

  public String toString () {
    return String.format("Type: %s; Stats: Occupied - %s, Occupants - [%s], Current Grid Index - %s", this.getType(), this.getOccupationStatus(), this.getDisplay(), this.getCurrentGridIndex());
  }
}

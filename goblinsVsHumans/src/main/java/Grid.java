import java.util.ArrayList;
import java.util.Queue;
import java.lang.Exception;

public class Grid {
  private int dimension;
  private int lives;
  private int mover;
  private Class<?> winner;
  private ArrayList<Land> view;
  private Land lastUpdatedLand;
  private Queue<Goblin> goblinQ;
  private Queue<Human> humanQ;

  public Grid (int dimension) {
    this.dimension = dimension;
    this.lives = this.dimension * 2;
    this.mover = (int) Math.floor(Math.random() * 2);
    this.view = new ArrayList<Land>();
    this.goblinQ = new Queue<Goblin>();
    this.humanQ = new Queue<Human>();
  }

  public Grid () {
    this.dimension = 3;
    this.lives = 6;
    this.mover = (int) Math.floor(Math.random() * 2);
    this.view = new ArrayList<Land>();
    this.goblinQ = new Queue<Goblin>();
    this.humanQ = new Queue<Human>();
  }

  public ArrayList<Land> populateLand (int dimension) {
    for (int i = 0; i < dimension * dimension; i++) {
      Land newLand = new Land();
      newLand.setCurrentGridIndex(i);
      this.view.add(newLand);
    }
    return this.view;
  }

  public ArrayList<Land> setGoblinsAndHumans (int dimension) {
    for (int i = 0; i < dimension; i++) {
      Goblin newGoblin = new Goblin();
      Land landForGoblin = this.view.get(i);
      landForGoblin.addOccupant(newGoblin);
      this.goblinQ.add(newGoblin);
    }
    for (int i = this.view.size() - dimension; i < this.view.size(); i++) {
      Human newHuman = new Human();
      Land landForHuman = this.view.get(i);
      landForHuman.addOccupant(newHuman);
      this.humanQ.add(newHuman);

    }
    return this.view;
  }

  public char generateMoveDirection () {
    char[] directions = new String[] {"N", "S", "W", "E"};
    int randomIndex = (int) Math.floor(Math.random() * (directions.length + 1));
    return directions[randomIndex];
  }

  public boolean moveIsInBounds (Char direction, Class<?> mover, ArrayList<Land> view) {
    int moversCurrentPosition = mover.getCurrentLandIndex();
    if (direction == "N") {
      if (moversCurrentPosition - this.dimension < 0) {
        return false;
      }
    } else if (direction == "S") {
      if (moversCurrentPosition + this.dimension > this.dimension * this.dimension - 1) {
        return false;
      }
    } else if (direction == "W") {
      if (moversCurrentPosition - 1 < 0) {
        return false;
      }
    } else if (direction == "E") {
      if (moversCurrentPosition + 1 > this.dimension * this.dimension - 1) {
        return false;
      }
    }
    return true;
  }

  public int blockOrMove (Char direction, Class<?> mover, ArrayList<Land> view) {
    int moversCurrentPosition = mover.getCurrentLandIndex();
    HashMap<String, Class<?>> targetOccupants;
    if (direction == "N") {
      Land northTargetPosition = this.view.get(moversCurrentPosition - this.dimension);
      targetOccupants = northTargetPosition.getOccupants();
      if (targetOccupants.containsKey(mover.getType())) {
        return 0;
      }
    } else if (direction == "S") {
      Land southTargetPosition = this.view.get(moversCurrentPosition + this.dimension);
      targetOccupants = southTargetPosition.getOccupants();
      if (targetOccupants.containsKey(mover.getType())) {
        return 0;
      }
    } else if (direction == "W") {
      Land westTargetPosition = this.view.get(moversCurrentPosition - 1);
      targetOccupants = westTargetPosition.getOccupants();
      if (targetOccupants.containsKey(mover.getType())) {
        return 0;
      }
    } else if (direction == "E") {
      Land eastTargetPosition = this.view.get(moversCurrentPosition + 1);
      targetOccupants = eastTargetPosition.getOccupants();
      if (targetOccupants.containsKey(mover.getType())) {
        return 0;
      }
    }
    return 1;
  }

  public ArrayList<Land> moveLife (Char direction, Class<?> mover, ArrayList<Land> view) {
    int moversCurrentPosition = mover.getCurrentLandIndex();
    int moversTargetPosition;
    if (direction == "N") {
      moversTargetPosition -= this.dimension;
    } else if (direction == "S") {
      moversTargetPosition += this.dimension;
    } else if (direction == "W") {
      moversTargetPosition -= 1;
    } else if (direction == "E") {
      moversTargetPosition += 1;
    }
    Land currentLand = this.view.get(moversCurrentPosition);
    currentLand.removeOccupant(mover);
    currentLand.setOccupationStatus(false);
    view.set(moversCurrentPosition, currentLand);
    Land destinationLand = this.view.get(moversTargetPosition);
    destinationLand.addOccupant(mover);
    destinationLand.setOccupationStatus(true);
    view.set(moversTargetPosition, destinationLand);
    mover.setCurrentLandIndex(moversTargetPosition);
    this.lastUpdatedLand = destinationLand;
    this.view = view;
    return this.view;
  }

  public int settleOrCombat (Land lastUpdatedLand) {
    HashMap<String, Class<?>> targetOccupants = lastUpdatedLand.getOccupants();
    if (targetOccupants.size() > 1) {
      return 1;
    }
    return 0;
  }

  public ArrayList<Land> combatAndEliminate (Land lastUpdatedLand, ArrayList<Land> view) {
    HashMap<String, Class<?>> occupants = lastUpdatedLand.getOccupants();
    Goblin goblinFighter = occupants.get("Goblin");
    Human humanFighter = occupants.get("Human");
    while (goblinFighter.getHealth() > 0 && humanFighter.getHealth() > 0) {
      int goblinAttack = goblinFighter.getAttack();
      int humanAttack = humanFighter.getAttack();
      humanFighter.addDamage(goblinAttack);
      goblinFighter.addDamage(humanAttack);
    }
    if (goblinFighter.getHealth() > 0) {
      lastUpdatedLand.removeOccupant(humanFighter);
      lastUpdatedLand.addOccupant(goblinFighter);
    } else if (humanFighter.getHealth() > 0) {
      lastUpdatedLand.removeOccupant(goblinFighter);
      lastUpdatedLand.addOccupant(humanFighter);
    }
    this.lives--;
    view.set(lastUpdatedLand.getCurrentGridIndex(), lastUpdatedLand);
    this.view = view;
    return this.view;
  }

  public void playGame () {
    do {
      // get mover and etc.
      // -> create queue for both goblin and human
      // -> take mover from beginning of queue, then operate on mover
      // -> when done, put mover back at end of queue
    } while (this.lives > 1);
  }

  public void initializeGrid () {
    this.populateLand();
    this.setGoblinsAndHumans();
    try {
      this.playGame();
    } catch(Exception e) {
      System.out.println(e);
    } finally {
      System.out.println("Game over");
    }
  }

  @Override
  public String toString () {
    String result = "";
    for (int i = 0; i < view.size(); i++) {
      Land currentLand = view.get(i);
      if ((i + 1) % this.getDimension() == 0) {
        result += (currentLand.getDisplay() + "\n");
      } else {
        result += (currentLand.getDisplay();
      }
    }
    return result;
  }
}

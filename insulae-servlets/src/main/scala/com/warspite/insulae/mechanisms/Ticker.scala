package com.warspite.insulae.mechanisms
import com.warspite.common.cli.WarspitePoller
import com.warspite.common.cli.CliListener
import com.warspite.common.cli.annotations.Cmd
import com.warspite.insulae.database.InsulaeDatabase

object Ticker {
  val POLL_INTERVAL = 1000;

  var lock: Long = 0;
}

class Ticker(var tickIntervalInMinutes: Int) extends WarspitePoller(Ticker.POLL_INTERVAL) with CliListener {
  private var lastTick: Long = 0;
  private var db: InsulaeDatabase = null
  var ready = false;
  
  override def setup() { 
    if(db == null) 
      throw new RuntimeException("Could not start ticker, because database is unset.");
    
    lastTick = System.currentTimeMillis();
    ready = true;
  }
  
  override def teardown() {}

  override def act() {
    if (ready && System.currentTimeMillis() > lastTick + tickIntervalInMinutes * 60000)
      tick();
  }

  def setDatabase(db: InsulaeDatabase) { 
    this.db = db;
  }
  
  @Cmd(name = "setInterval", description = "Sets the current interval in minutes.", printReturnValue = true)
  def setInterval(interval: Int): String = {
    tickIntervalInMinutes = interval;
    printInterval();
  }

  @Cmd(name = "interval", description = "Print the current tick interval.", printReturnValue = true)
  def printInterval(): String = {
    "Tick interval: " + tickIntervalInMinutes + " minutes.";
  }

  @Cmd(name = "left", description = "Gets the time left until next tick, in seconds.", printReturnValue = true)
  def printSecondsUntilTick(): String = {
    "Next tick in " + (lastTick + tickIntervalInMinutes*60000 - System.currentTimeMillis()) / 1000 + " seconds.";
  }

  @Cmd(name = "tick", description = "Cause an instant tick.", printReturnValue = true)
  def tick(): String = {
    if(!ready) {
      logger.debug("Tried to tick, but ticker is unready.");
      return "Ticker is unready!";
    }
    
    lastTick = System.currentTimeMillis();
    logger.info("Starting tick.");
    
    db.industry.tickBuildingActionPoints();

    logger.info("Tick complete!");
    "Tick complete!";
  }
}
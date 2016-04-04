package mimer29or40.craftPlanner.mod.util;

import mimer29or40.craftPlanner.mod.CraftPlanner;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

public class Log
{
    private static void log(Level logLevel, String format, Object... object)
    {
        FMLLog.log(CraftPlanner.MOD_ID, logLevel, String.format(format, object));
    }

    public static void off(String format, Object... object)
    {
        log(Level.OFF, format, object);
    }

    public static void fatal(String format, Object... object)
    {
        log(Level.FATAL, format, object);
    }

    public static void error(String format, Object... object)
    {
        log(Level.ERROR, format, object);
    }

    public static void warn(String format, Object... object)
    {
        log(Level.WARN, format, object);
    }

    public static void info(String format, Object... object)
    {
        log(Level.INFO, format, object);
    }

    public static void debug(String format, Object... object)
    {
        log(Level.DEBUG, format, object);
    }

    public static void trace(String format, Object... object)
    {
        log(Level.TRACE, format, object);
    }

    public static void all(String format, Object... object)
    {
        log(Level.ALL, format, object);
    }
}

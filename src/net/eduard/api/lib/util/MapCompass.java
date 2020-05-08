package net.eduard.api.lib.util;

import java.util.ArrayList;

/**
 * @author Internet
 */
public class MapCompass
{
    public static Point getCompassPointForDirection(final double inDegrees) {
        double degrees = (inDegrees - 180.0) % 360.0;
        if (degrees < 0.0) {
            degrees += 360.0;
        }
        if (0.0 <= degrees && degrees < 22.5) {
            return Point.N;
        }
        if (22.5 <= degrees && degrees < 67.5) {
            return Point.NE;
        }
        if (67.5 <= degrees && degrees < 112.5) {
            return Point.E;
        }
        if (112.5 <= degrees && degrees < 157.5) {
            return Point.SE;
        }
        if (157.5 <= degrees && degrees < 202.5) {
            return Point.S;
        }
        if (202.5 <= degrees && degrees < 247.5) {
            return Point.SW;
        }
        if (247.5 <= degrees && degrees < 292.5) {
            return Point.W;
        }
        if (292.5 <= degrees && degrees < 337.5) {
            return Point.NW;
        }
        if (337.5 <= degrees && degrees < 360.0) {
            return Point.N;
        }
        return null;
    }
    
    public static ArrayList<String> getCompass(final Point point, final String colorActive, final String colorDefault) {
        final ArrayList<String> ret = new ArrayList<String>();
        String row = "";
        row = String.valueOf(row) + Point.NW.toString(Point.NW == point, colorActive, colorDefault);
        row = String.valueOf(row) + Point.N.toString(Point.N == point, colorActive, colorDefault);
        row = String.valueOf(row) + Point.NE.toString(Point.NE == point, colorActive, colorDefault);
        ret.add(row);
        row = "";
        row = String.valueOf(row) + Point.E.toString(Point.E == point, colorActive, colorDefault);
        row = String.valueOf(row) + colorDefault + "+";
        row = String.valueOf(row) + Point.W.toString(Point.W == point, colorActive, colorDefault);
        ret.add(row);
        row = "";
        row = String.valueOf(row) + Point.SW.toString(Point.SW == point, colorActive, colorDefault);
        row = String.valueOf(row) + Point.S.toString(Point.S == point, colorActive, colorDefault);
        row = String.valueOf(row) + Point.SE.toString(Point.SE == point, colorActive, colorDefault);
        ret.add(row);
        return ret;
    }
    
    public static ArrayList<String> getAsciiCompass(final double inDegrees, final String colorActive, final String colorDefault) {
        return getCompass(getCompassPointForDirection(inDegrees), colorActive, colorDefault);
    }
    
    public enum Point
    {
        N("N", 0, 'N'), 
        NE("NE", 1, '/'), 
        E("E", 2, 'O'), 
        SE("SE", 3, '\\'), 
        S("S", 4, 'S'), 
        SW("SW", 5, '/'), 
        W("W", 6, 'L'), 
        NW("NW", 7, '\\');
        
        public final char asciiChar;
        
        private Point(final String s, final int n, final char asciiChar) {
            this.asciiChar = asciiChar;
        }
        
        @Override
        public String toString() {
            return String.valueOf(this.asciiChar);
        }
        
        public String toString(final boolean isActive, final String colorActive, final String colorDefault) {
            return String.valueOf(isActive ? colorActive : colorDefault) + String.valueOf(this.asciiChar);
        }
    }
}

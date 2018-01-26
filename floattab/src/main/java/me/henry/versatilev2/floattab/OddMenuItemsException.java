package me.henry.versatilev2.floattab;

/**
 * Created by henry on 2018/1/16.
 */

public class OddMenuItemsException extends Exception{
    public OddMenuItemsException() {
        super("Your menu should have non-odd size");
    }
}

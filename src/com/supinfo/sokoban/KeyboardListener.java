package com.supinfo.sokoban;

import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyboardListener implements NativeKeyListener
{
    private Player player;

    KeyboardListener(Player player)
    {
        this.player = player;
    }

    public void nativeKeyPressed(NativeKeyEvent e)
    {
        switch(e.getKeyCode())
        {
            case NativeKeyEvent.VK_ESCAPE:
                GlobalScreen.unregisterNativeHook();
                break;

            case NativeKeyEvent.VK_LEFT:
            case NativeKeyEvent.VK_DOWN:
            case NativeKeyEvent.VK_RIGHT:
            case NativeKeyEvent.VK_UP:
                if(this.player.getScore().timerIsOn())
                {
                    this.player.move(e.getKeyCode());
                }
                break;

            case NativeKeyEvent.VK_N:
                if(!this.player.getScore().timerIsOn() && this.player.getMap().getLevel() < Map.getLastLevel())
                {
                    this.player.playNextLevel();
                }
                break;

            case NativeKeyEvent.VK_R:
                this.player.replay();
                break;

            default:
                break;
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e)
    {

    }

    public void nativeKeyTyped(NativeKeyEvent e)
    {

    }
}
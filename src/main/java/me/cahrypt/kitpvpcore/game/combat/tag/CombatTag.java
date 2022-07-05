package me.cahrypt.kitpvpcore.game.combat.tag;

import me.cahrypt.kitpvpcore.player.KitPlayer;

public class CombatTag {
    private final KitPlayer receiver;
    private final KitPlayer inflicter;

    public CombatTag(KitPlayer receiver, KitPlayer inflicter) {
        this.receiver = receiver;
        this.inflicter = inflicter;
    }

    public KitPlayer getReceiver() {
        return receiver;
    }

    public KitPlayer getInflicter() {
        return inflicter;
    }
}

package cc.eumc.eusnewbieprotection;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class EusNewbieProtection extends JavaPlugin implements Listener {
    String NewbiePermissionNode;

    Double DamageFactor;

    String DamageDecreaseTitle;
    String DamageDecreaseSubtitle;

    @Override
    public void onEnable() {
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            saveDefaultConfig();
        }
        reloadConfig();

        NewbiePermissionNode = getConfig().getString("Settings.NewbiePermissionNode", "newbie.protection");
        DamageFactor = getConfig().getDouble("Settings.Protection.DamageFactor", 0.5);

        DamageDecreaseTitle = getConfig().getString("Settings.Message.DamageDecrease.Title", "").replace("&", "§");
        DamageDecreaseSubtitle = getConfig().getString("Settings.Message.DamageDecrease.Subtitle", "&7-"+toPercentage((float) (1-DamageFactor), 2))
                                    .replace("&", "§");

        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("Newbie permission node: " + NewbiePermissionNode);
        getLogger().info("Protection -> Damage factor: " + DamageFactor);
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntityType() != EntityType.PLAYER) return;

        Player player = (Player) e.getEntity();
        if (player.hasPermission(NewbiePermissionNode)) {
            e.setDamage(e.getDamage()* DamageFactor);
        }
        player.sendTitle(DamageDecreaseTitle, DamageDecreaseSubtitle, 2, 15, 4);
    }

    static String toPercentage(float n, int digits){
        return String.format("%."+digits+"f",n*100)+"%";
    }
}

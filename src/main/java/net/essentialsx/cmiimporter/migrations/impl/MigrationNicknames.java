package net.essentialsx.cmiimporter.migrations.impl;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import net.essentialsx.cmiimporter.CMIImporter;
import net.essentialsx.cmiimporter.migrations.AbstractMigration;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class MigrationNicknames extends AbstractMigration {

    public MigrationNicknames(CMIImporter importer, Essentials essentials) {
        super(importer, essentials, "Nicknames", "Imports user nickname data.", true);
    }

    @Override
    public void run() {
        try {
            List<DbRow> results = DB.getResults("SELECT player_uuid, nickname FROM " + table("users") + " WHERE player_uuid IS NOT NULL AND nickname IS NOT NULL");
            for (DbRow row : results) {
                UUID uuid = UUID.fromString(row.getString("player_uuid"));
                User user = essentials.getUser(uuid);
                if (user == null) {
                    logWarning(String.format("Unable to migrate nickname data for UUID %s!", uuid));
                    continue;
                }
                String nickname = row.getString("nickname");
                if (nickname != null) {
                    user.setNickname(nickname);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

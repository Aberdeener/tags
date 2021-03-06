package broccolai.tags.commands;

import broccolai.tags.commands.context.CommandUser;
import broccolai.tags.factory.CloudArgumentFactory;
import broccolai.tags.model.tag.Tag;
import broccolai.tags.model.user.TagsUser;
import broccolai.tags.service.message.MessageService;
import broccolai.tags.service.tags.TagsService;
import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

public class TagsAdminCommand {

    private final @NonNull Permission permission;
    private final @NonNull MessageService messageService;
    private final @NonNull TagsService tagsService;

    @Inject
    public TagsAdminCommand(
            final @NonNull CommandManager<CommandUser> manager,
            final @NonNull CloudArgumentFactory argumentFactory,
            final @NonNull MessageService messageService,
            final @NonNull TagsService tagsService,
            final @NonNull Permission permission
    ) {
        this.permission = permission;
        this.messageService = messageService;
        this.tagsService = tagsService;

        Command.Builder<CommandUser> tagsCommand = manager.commandBuilder("tagsadmin");

        manager.command(tagsCommand
                .literal("give")
                .argument(argumentFactory.user("target"))
                .argument(argumentFactory.tag("tag", false, false))
                .handler(this::handleGive)
        );

        manager.command(tagsCommand
                .literal("remove")
                .argument(argumentFactory.user("target"))
                .argument(argumentFactory.tag("tag", false, false))
                .handler(this::handleRemove)
        );
    }

    private void handleGive(final @NonNull CommandContext<CommandUser> context) {
        TagsUser target = context.get("target");
        Tag tag = context.get("tag");

        this.tagsService.grant(target, tag);
        context.getSender().sendMessage(this.messageService.commandAdminGive(tag, target));
    }

    private void handleRemove(final @NonNull CommandContext<CommandUser> context) {
        TagsUser target = context.get("target");
        Tag tag = context.get("tag");
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(target.uuid());

        this.permission.playerRemove(null, offlinePlayer, "tags.tag." + tag.id());
        context.getSender().sendMessage(this.messageService.commandAdminRemove(tag, target));
    }

}

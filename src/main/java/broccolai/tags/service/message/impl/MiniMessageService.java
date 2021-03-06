package broccolai.tags.service.message.impl;

import broccolai.tags.model.tag.Tag;
import broccolai.tags.model.user.TagsUser;
import broccolai.tags.service.message.MessageService;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import org.bukkit.Bukkit;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;

@Singleton
public final class MiniMessageService implements MessageService {

    private static final MiniMessage MINI = MiniMessage.get();

    @Override
    public Component commandAdminGive(
            @NonNull final Tag tag,
            @NonNull final TagsUser target
    ) {
        Template tagComponent = Template.of("tag", tag.component());
        Template targetComponent = Template.of("target", this.nameFromUser(target));

        return Messages.COMMAND_ADMIN_GIVE.asComponent(tagComponent, targetComponent);
    }

    @Override
    public Component commandAdminRemove(
            @NonNull final Tag tag,
            @NonNull final TagsUser target
    ) {
        Template tagComponent = Template.of("tag", tag.component());
        Template targetComponent = Template.of("target", this.nameFromUser(target));

        return Messages.COMMAND_ADMIN_GIVE.asComponent(tagComponent, targetComponent);
    }

    //todo: Add user to TagUser object?
    private String nameFromUser(final @NonNull TagsUser user) {
        return Bukkit.getOfflinePlayer(user.uuid()).getName();
    }

    private @NonNull Template[] mergeTemplateArrays(final @NonNull Template[] first, final @NonNull Template[]... rest) {
        int totalLength = first.length;

        for (Template[] array : rest) {
            totalLength += array.length;
        }

        Template[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;

        for (Template[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }

        return result;
    }

    //todo: Implement locale
    private enum Messages {
        COMMAND_ADMIN_GIVE("Tag <tag> has been given to <target>"),
        COMMAND_ADMIN_REMOVE("Tag <tag> has been removed from <target>");

        private final String serialised;

        Messages(final String serialised) {
            this.serialised = serialised;
        }

        public Component asComponent(final @NonNull Template... templates) {
            return MINI.parse(this.serialised, templates);
        }
    }
}

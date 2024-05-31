package yan.lx.bedrockminer.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import yan.lx.bedrockminer.command.argument.BlockIdentifierArgument;
import yan.lx.bedrockminer.config.Config;
import yan.lx.bedrockminer.utils.MessageUtils;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class TaskCommand extends BaseCommand {

    @Override
    public String getName() {
        return "task";
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder, CommandRegistryAccess registryAccess) {
        builder.then(literal("limit")
                .then(argument("limit", IntegerArgumentType.integer(1, 5))
                        .executes(this::toggleSwitch)))

                .then(literal("floor").then(literal("add").then(argument("层数", LongArgumentType.longArg(-64, 319))
                        .executes(this::addFloor)))
                )

                .then(literal("floor").then(literal("remove").then(argument("层数", LongArgumentType.longArg(-64, 319))
                        .executes(this::removeFloor)))
                )

//                .then(literal("addBlackFloor")
//                        .then(argument("层数", IntegerArgumentType.integer(-64, 319))
//                                .executes(this::addFloor)))
//
//                .then(literal("removeBlackFloor")
//                        .then(argument("层数", IntegerArgumentType.integer(-64, 319))
//                                .executes(this::removeFloor)))
        ;
    }


    private int toggleSwitch(CommandContext<FabricClientCommandSource> context) {
        var config = Config.getInstance();
        var limit = IntegerArgumentType.getInteger(context, "limit");
        if (config.taskLimit != limit) {
            config.taskLimit = limit;
            Config.save();
        }
        MessageUtils.addMessage(Text.translatable("bedrockminer.command.limit").getString().replace("%limit%", String.valueOf(limit)));
        return 0;
    }

    private int addFloor(CommandContext<FabricClientCommandSource> context) {
        var config = Config.getInstance();
        Long add = LongArgumentType.getLong(context, "层数");
        if (!config.floorsBlacklist.contains(add.intValue())) {
            config.floorsBlacklist.add(add.intValue());
            Config.save();
        }
        MessageUtils.addMessage(Text.translatable("bedrockminer.command.floorBlackList").getString()
                .replace("%type%", "add")
                .replace("%count%", String.valueOf(add)));
        return 0;
    }

    private int removeFloor(CommandContext<FabricClientCommandSource> context) {
        var config = Config.getInstance();
        Long remove = LongArgumentType.getLong(context, "层数");
        if (config.floorsBlacklist.contains(remove.intValue())) {
            config.floorsBlacklist.remove((Integer) remove.intValue());
            Config.save();
        }
        MessageUtils.addMessage(Text.translatable("bedrockminer.command.floorBlackList").getString()
                .replace("%type%", "remove")
                .replace("%count%", String.valueOf(remove)));
        return 0;
    }
}

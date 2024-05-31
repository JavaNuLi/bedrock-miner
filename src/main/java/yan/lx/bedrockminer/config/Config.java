package yan.lx.bedrockminer.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import org.jetbrains.annotations.Nullable;
import yan.lx.bedrockminer.BedrockMinerMod;
import yan.lx.bedrockminer.utils.BlockUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Config {
    private static final File file = new File(FabricLoader.getInstance().getConfigDir().toFile(), "bedrockminer.json");
    private static Config instance = Config.load();

    public boolean debug = false;
    public List<Integer> floorsBlacklist = new ArrayList<>();
    public int taskLimit = 1;
    public List<String> blockWhitelist = new ArrayList<>();
    public List<String> blockBlacklist = new ArrayList<>();

    public Config() {
        init();
    }

    public void init() {
        // 方块白名单(默认)
        blockWhitelist = new ArrayList<>();
        blockWhitelist.add(BlockUtils.getId(Blocks.BEDROCK));                  // 基岩
        blockWhitelist.add(BlockUtils.getId(Blocks.END_PORTAL));               // 末地传送门
        blockWhitelist.add(BlockUtils.getId(Blocks.END_PORTAL_FRAME));         // 末地传送门-框架
        blockWhitelist.add(BlockUtils.getId(Blocks.END_GATEWAY));              // 末地折跃门
    }

    public static Config load() {
        Config config = null;
        Gson gson = new Gson();
        try (Reader reader = new FileReader(file)) {
            config = gson.fromJson(reader, Config.class);
            BedrockMinerMod.LOGGER.info("已成功加载配置文件");
        } catch (Exception e) {
            if (file.exists()) {
                if (file.delete()) {
                    BedrockMinerMod.LOGGER.info("无法加载配置,已成功删除配置文件");
                } else {
                    BedrockMinerMod.LOGGER.info("无法加载配置,删除配置文件失败");
                }
            } else {
                BedrockMinerMod.LOGGER.info("找不到配置文件");
            }
        }
        if (config == null) {
            config = new Config();
        }
        instance = config;
        save();
        return config;
    }

    public static void save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(instance, writer);
        } catch (IOException e) {
            BedrockMinerMod.LOGGER.info("无法保存配置文件");
            e.printStackTrace();
        }
    }

    public static void addFloor(int floor) {
        instance.floorsBlacklist.add(floor);
    }

    public static void removeFloor(int floor) {
        instance.floorsBlacklist.remove(floor);
    }

    public static Config getInstance() {
        return instance;
    }
}

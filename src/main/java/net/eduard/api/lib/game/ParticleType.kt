package net.eduard.api.lib.game

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.Potion
import org.bukkit.potion.PotionType


enum class ParticleType(var particleName: String= "nome",
                        var id: Int= 0,
                        var ptName :
                        String = particleName
, var icon : ItemStack = ItemStack(Material.PAPER)
) {

    HUGE_EXPLOSION("hugeexplosion", 0," Enorme explosão",
    ItemStack(Material.TNT)),
    LARGE_EXPLODE("largeexplode", 1,"Grande explosão",
        ItemStack(Material.getMaterial(289))),
    FIREWORKS_SPARK("fireworksSpark", 2,"Faíscas de fogos de artifício"
    ,ItemStack(Material.FIREWORK_CHARGE))
    , BUBBLE("bubble", 3,"Bolha",
        ItemStack(Material.INK_SACK,1,12)),
    SUSPEND("suspend", 4,"Invalido" ,
        ItemStack(Material.BARRIER)),
    DEPTH_SUSPEND("depthSuspend", 5,"Profundidade suspensa"
    , ItemStack(Material.FLINT_AND_STEEL)),
    TOWN_AURA("townaura", 6,"Aura da cidade"
    , ItemStack(Material.BEDROCK)),
    CRIT("crit", 7,"Critico",ItemStack(Material.IRON_SWORD)),
    MAGIC_CRIT("magicCrit", 8,"Crítico mágico", ItemStack(Material.ENCHANTED_BOOK)),
    MOB_SPELL("mobSpell", 9,
        "Feitiço de monstro",ItemStack(Material.FIREWORK_CHARGE)),

    MOB_SPELL_AMBIENT("mobSpellAmbient", 10,
        "Feitiço de monstro ambiental",Potion(PotionType.NIGHT_VISION).toItemStack(1)),
    SPELL("spell", 11,"Feitiço",ItemStack(Material.GLASS_BOTTLE)),
    INSTANT_SPELL("instantSpell", 12,"Feitiço instantaneo",Potion(PotionType.INSTANT_HEAL,1, true,false).toItemStack(1)),
    WITCH_MAGIC("witchMagic", 13,"Bruxa",Potion(PotionType.JUMP,1, true,false).toItemStack(1)),
    NOTE("note", 14,"Nota",ItemStack(Material.RECORD_9)),
    PORTAL("portal", 15,"Portal", ItemStack(Material.EYE_OF_ENDER)),
    ENCHANTMENT_TABLE("enchantmenttable", 16, "Mesa de encantamento"
    ,ItemStack(Material.ENCHANTMENT_TABLE)),

    EXPLODE("explode", 17,"Explosão",ItemStack(Material.EXPLOSIVE_MINECART)),
    FLAME("flame", 18,"Chama",ItemStack(Material.FLINT_AND_STEEL)),
    LAVA("lava", 19,"Lava",ItemStack(Material.BLAZE_POWDER)),
    FOOTSTEP("footstep",
        20,"Movimento",ItemStack(Material.DIAMOND_BOOTS)),
    SPLASH("splash", 21
    ,"Respingo",ItemStack(Material.WATER_LILY)),
    LARGE_SMOKE("largesmoke", 22,"Grande fumaça",ItemStack(Material.FEATHER)),
    CLOUD("cloud", 23,"Nuvem",ItemStack(Material.SUGAR)),
    RED_DUST("reddust", 24,"Poeira vermelha",ItemStack(Material.REDSTONE)),
    SNOWBALL_POOF("snowballpoof", 25,"Bola de Neve Poof",
        ItemStack(Material.SNOW_BALL)),
    DRIP_WATER("dripWater", 26,"Gota d'água",ItemStack(Material.GHAST_TEAR)),
    DRIP_LAVA("dripLava", 27,"Gota de Lava",ItemStack(Material.LAVA_BUCKET))
    , SNOW_SHOVEL("snowshovel", 28,"Bola de neve",ItemStack(Material.SNOW_BLOCK)),
    SLIME("slime", 29,"Slime",ItemStack(Material.SLIME_BALL)),
    HEART("heart", 30,"Coração", ItemStack(Material.APPLE)),
    ANGRY_VILLAGER("angryVillager", 31,"Villager Bravo", ItemStack(Material.FIREBALL)),
    HAPPY_VILLAGER("happyVillager", 32,"Villager Feliz"
    , ItemStack(Material.EMERALD)),
    WAKE("wake",33,"Despertado",ItemStack(Material.BED))




}
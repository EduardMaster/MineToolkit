rootProject.name = "eduardapi"
include(":MineNMS")
include(":MineNMS-1_8_9")
include(":MineNMS-1_7")
include(":MineNMS-1_12")
include("MineUtils")
include(":SQLManager")
include("JavaUtils")
include("Minigame")
findProject(":Minigame")?.name = "minigame"

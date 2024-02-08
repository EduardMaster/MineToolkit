## This document is in Portuguese

### Quando eu comecei a programar?
Iniciei estudos de programação bukkit em 2014 mês 10

### Detalhes do que foi criado nas versões antigas

## Versão 0.1
### Data
2015-07-16
### Descrição:
- EduardAPI ainda era só uma Ideia tinha criado apenas uma classe chamado de Eduard com varias classes internas
- dentro destas classes tinha varias ideias como controle proprio de Tempo tipo um BukkitRunnable proprio,
- facilidade em criação de Scoreboard, criação de configuração com nome diferenciado alem do config.yml,
- criação de craft simples e composto, criação de efeito de Fogos de artifico, criação de minigame e arenas
- o objetivo de ser tudo junto era poder ficar facilmente arrastando deprojeto em projeto de plugin
### Classes: (Classes Internas)
- GUI (Event $Listener)
- Scoreboards - Scoreboard simples de fazer
- FireworkType - Desnecessário
- CraftExtra - CraftComposto $ShapedRecipe
- CraftNormal - Craft simples $ShapelessRecipe
- Fireworks - Tipo um Builder de FireworkEffect
- Timers - Fazer timer de minigame
- Time - Fazer uma Repetição de ação $BukkitRunnable
- Delay - Fazer um Atraso de ação $BukkitRunnable
- Delays - Armazenar delays de players
- Cooldowns - Criar Cooldown com Ação executada ao terminar o cooldown
- Cooldown - Criar Cooldown com apenas Timestamp
- Minigame (Arena) - Minigame salvando e puxando dados diretamente de Config
- Configs - Criar configs customizadas $YamlConfiguration com $File
### Interfaces:
- ITimer - 3 Funções de ações com inicio, meio e fim
- IPlayer - Função para Placeholder de Scoreboard
- Eduard - Varios métodos default do Java8 e com Método estatico getInstance() que gerava uma instancia nova da Interface
### Veredito da versão:
 - Nesta ponto a api estava muito longe de ser uma api usavel tudo muito acomplato dificil de entender e atualizar

## Versão 0.2
### Data
2015-07-18 - Dois dias depois
### Descrição:
- Nesta versão já existia um JAR criado dele porem com muita repetição de classes e métodos
- e o pior tinha varias packages
, net/eduard/api/java7, net/eduard/api/java8_class, net/eduard/api/java8_interface, net/eduard/java7, net/eduard/java8, net/eduard/java8_interface
- Isso gerava muita confusão na hora de usar, o mótivo de ter feito isso que estava em transição do Java 7 para o 8 e queria
- continuar com as classes da maneira antiga, erro fatal porem né não sabia
- Eu nem sei como fazia para o Eclipse entender que estava trabalhando com Java 8 e 7 ao mesmo tempo
### Mudanças:
- Classe ActionGUI, para definir como o GUI seria aberto
- Novos métodos na Interface Eduard
- IconEffect -> Efeito ao clique
### Veredito da versão:
- Ainda não usavel e o pior varias classes repetidas

## Versão 0.3
### Data
2015-08-28 Quase um mês depois
### Descrição:
- Nesta versão tudo ficou organizado em packages, varias, já estava começando a melhorar, porem criei um padrão de nomeclatura
- que terminava tudo com Setup até mesmo coisas que não fazia sentido hoje
- Vou marcar classes novas e como era na versão antiga, Package base do Jar /net/eduard/ , e Main class chamava Manager
- ficava nesta package
- Varias classes de reflection do NMS criado na package /packet/
### Classes refratoradas
- Configs -> /config/ConfigSetup
- Cooldowns -> /cooldown/CooldownSetup1
- Cooldown -> /cooldown/CooldownSetup2
- CraftExtra -> /craft/CraftSetup1
- CraftNormal -> /craft/CraftSetup2
- FireworkType -> /firework/FireworkType
- Fireworks -> /firework/FireworkSetup
- ActionGUI -> /gui/ GuiOpenType
- Minigame -> /gui/MinigameSetup
- Arena -> /gui/MinigameSetup
- Scoreboards -> scoreboard/ScoreboardSetup - Scoreboard criada facilmente
- Explosion -> explosion/ExplosionSetup
### classes novas:
- explosion/ExplosionSetup - Explosão para ser configuravel
- gui/GuiItemSetup - Item para gui
- gui/GuiType - Enum desnecessária
- item/ItemSetup - Extende ItemStack
- item/ItemMangaer - Interface desnecessária
- click/ClckSetup - Representa um clique executar uma ação expecifica
- click/ClickItemSetup - Representa qual item que tem que clicar e extende ItemSetup
- click/ClickItemManager - Representa o efeito do clique
- money/MoneySetup - Controle de economia direto com ConfigSetup
- packet/CraftItemStack -> CraftItemStack com Reflection
- packet/CraftPlayer
- packe/EnumClientCommand
- packet/GameProfile
- packet/MineItemStack -> ItemStack NMS com Reflection
- packet/MinePlayer -> EntityPlayer NMS com Reflection
- packet/Packet
- packet/NBTTagList
- packet/NBTCompound
- packet/PacketPlayInClientCommand
- packet/PacketPlayOutNamedEntitySpawn
- packet/PlayerConnection
- save/SaveSetup - Salva informações do servidor
- scoreboard/ScoreSlotSetup - Slot da scoreboard
- sound/SoundManager - Interface desnecessária
- sound/SoundSetup - Representa som
- tag/TagInfoSetup - Representa uma Tag
- tag/TagSetup - Sistema de Tag
- api/text/TextSetup - Representa um Texto com 16 letras
- api/create/CreateSetup - Api de criação de ItemStack
- api/locaion/LocationSetup - Api de utilização com Location
- api/random/RandomSetup - Api de geração de numeros aleatorios
- api/EduardAPI - Classe tipo Main porem extende ConfigSetup e implementa um monte de coisa
### Veredito da versão:
- Api usavel neste momento porem a nomeclatura de tudo esta meio confusa
- E a package principal não esta do jeito mais correto
- que é net.eduard.eduardapi.

## Versão 0.4
### Data
2015-11-23 - Tres mêses depois
### Descrição
- Nesta versão houve novamente refratorações e movimento de classes de uma Package para outra,
- e a Package principal se tornou net/eduard_api/ e a main Eduard na mesma package, nesta versão
- criei um sistema de LifeCycle manual, com métodos onEnable(), onDisable() e onLoad()
- Foram removidos suffix "Setup" de quase todas as classes
  ### Classes refratoradas:
    - ConfigSetup -> /config/Config
    - MoneySetup -> /money/Money
    - MinigameSetup -> /minigame/Minigame
    - ArenaSetup -> /minigame/Arena
    - TimeSetup -> /time/EventSetup
    - CooldownSetup1 -> /time/cooldown/Cooldown1
    - CooldownSetup2 -> /time/cooldown/Cooldown2
- TagInfoSetup => /player/tag/Tag
- TagSetup -> /player/tag/Tags
- ScoreSetup -> /player/scoreboard/Scoreboard
- CraftSetup2 -> game/craft/Craft
- CraftSetup1 -> game/craft/Crafts
- ItemSetup -> game/item/Item
- ExplosionSetup -> game/explosion/Explosion
- FireworkSetup -> game/firework/Firework
- FireworkTypeSetup -> game/firework/FireworkType
- GUI -> player/gui/Gui
- GuiItemSetup -> player/gui/Slot
- GuiType -> player/gui/GuiType
### classes removidas:
- ScoreSlotSetup

### Classes novas:
- player/Fake - Controle de Fake /fake da vida
- game/effect/Effects - Efeito visual configuravel
- manager/extra/Eventos... 10 classes de Eventos
- manager/test/event... 6 classes de Eventos
- manager/util/... classes adicionais uteis

### Veredito da versão:
- Versão melhorada porem muitas mudanças de Packages ainda muita instabilidade
- Monta um código derrepente tem que trocar importação porque mudou as packages da api


0.5:
  Data: 2015-12-17 - Quase 1 mês depois
  Descrição:
  - Nesta versão teve algumas Refratorações e a package mudou novamente para net/eduard/eduard_api/ e
  - a classe Main se tornou Main na mesma package, package /packet continou mesmo jeito
  - Classes de alterações de Eventos movidas para /edits/ e /extra/
  - Criação de tres classes de Cooldown uma usando Player como Key da HashMap, outra com UUID, e outra com String
  - Nova onda de nomeclatura em vez de sempre terminando com Setup terminando com Effect
  - Surgimento das primeiras classes proprias de Config, nesta versão para fazer um plugin se extendia a classe manager/EduardAPI
  - Surgimento da primeira classe com tudo dentro manager/API que se tornaria Mine no futuro
  - Tudo relacionado ao game foi jogado para a package /game/
  - Tendencia a colocar 's' no final de todos objetos para evitar bug de importação
  - Aos poucos a package /game/ começou a ser uma forte tendencia de centralização de classes

  class refratoradas:
    - SaveSetup -> extra/Save
  - TimeSetup -> time/util/TimedEffect
  - GuiType -> player/gui/util/GuiType
  - PlayerEffect -> player/gui/util/PlayerEffect
  - Save -> manager/Information
  - SoundSetup -> game/sound/Sounds
  - Craft -> game/craft/normal/Craft
  - Crafts -> game/craft/simples/Craft
  - Arena -> /config/Arena
  - Config -> /config/Config
  - Minigame -> /config/Minigame
  - Money -> /config/Money



  classes novas:
    - extra/VaultSetup - Classe para controlar o Vault
    - game/Jump - Efeito de impulso para cima ou para frente
    - game/potion/Potions - Classe com Efeitos de poção configuravel desnecessária pois existe PotionEffect

  veredito da versão:
  - Ainda assim a api não é boa para utilização pois há muitas refratorações de classes isso é péssimo para manter
  - Códigos sempre funcionando porisso ainda continua sendo 0.5 em vez de ser a primeira 1.0


0.6:
  Data: 2016-05-14 - Quase 5 mês depois
  Descrição:
    - No ano de 2016 comecei a trabalhar diariamente então não programava mais com frequencia
    - Nesta versão novamente foram feitas mais refratorações separando mais ainda as classes
    - Package net/eduard/tutorial/ criado onde ficava todo código Snippets de Listener ou CommandExectur
    - Surgimento da package onde ficava classes que termina como API um padrão que ainda uso até hoje
    - na package /util/api/
    - Package packet/ dividida em subpackages packet/game , packet/item/ , packet/player/
    - Surgimento da package Time, trazendo todas classes relacionadas com Tempo para lá
    - Surgimento dos primeiros comandos dentro da package manager/command/

  classes refratoradas:
    - Money -> /config/money/Money
    - Minigame -> /config/minigame/Minigame
    - Arena -> /config/Minigame/Arena
    - VaultAPI -> /manager/vault/Vault
    - Informatio -> Classe transformada em varias outras que ficaram na package /manager/information/info
    - Craft -> game/craft/CraftNormal
    - Craft -> game/craft/CraftSimples
    - Fake -> game/Fakes

  classes novas:
    - /config/money/Account -> Representa contas dos jogadores
    - /manager/map/Maps - Surgimento da classe que um dia se tornaria Schematic
    - /manager/map/util/MapParts - Representa partes do Mapa
    - varias classes de comandos na packge /manager/command/
    - Commands - Representa um comando CommandExector
    - SubCommands - Representa um SubComando do Commands
    - game/TabList - Aplicar uma mudança no tab
    - /game/particle/ParticleEffect - Efeito de particula
    - /game/particle/ParticleType - Tipo de Efeito de particula



0.7: Embreve vou marcar as mudanças que rolou nesta versão.
0.8: Embreve vou marcar as mudanças que rolou nesta versão.
0.9: Embreve vou marcar as mudanças que rolou nesta versão.
0.10: Embreve vou marcar as mudanças que rolou nesta versão.
1.0: Parando de renomear package principal, Package setup/ fica as classes mais importantes
1.1: Embreve vou marcar as mudanças que rolou nesta versão.
1.2: Embreve vou marcar as mudanças que rolou nesta versão.
1.3: Embreve vou marcar as mudanças que rolou nesta versão.
1.4: Embreve vou marcar as mudanças que rolou nesta versão.
1.5: Embreve vou marcar as mudanças que rolou nesta versão.
1.6: Embreve vou marcar as mudanças que rolou nesta versão.
1.7:
  Descriçao: Utilizando o Kotlin em boa parte do projeto
  Detalhes:
  - Os debugs de tudo é ativavel pela config.yml
  - Movendo arquivos da pasta lib/abstraction para api/abstraction
  - Colocando lista de classes de todos os Kits conhecidos de HG e KitPvP na pasta api/server/kits
    antes estas classes ficavam nos dois projetos pagos, EduHG e EduKitPvP,
    resolvi que como os kits são os mesmos é melhor ficam em um lugar só
  - Movi 3 classes da pasta api/server/kits para api/server/kit
  - Removido métodos da Extra relacionados a contagem de Valor de Projeto pois agora sigo o padrão do Site que calcula o projeto dependendo de coisas que os propríos compradores podem avaliar
  - Método removido calculateClassValue(class)->double que calcula o valor da Classe com base em um HashMap de valores levando em consideração até suas variaveis
  - Método removido setPrice(class,double) que define o valor da Classe dentro do HashMap
  - Método removido getPrice(class)->double que retorna o valor da Classe vinda do HashMap e 0 caso não tenha esta classe no Mapa
  - Método removido InfoGenerator.saveObject(path,player) porque não tem porque salva infos dos jogadores se não vou controlar
  - Colocando a maioria dos códigos que estavam na classe EduardAPI em varias outras classes
  - Transferindo Constantes da classe Mine e Extra para a EduardAPI pois não será usado por ninguem é apenas informativo
  - Comecei a criar Storage v2 (Storation) só para ver se conseguia ainda, tem como Objetivo ser mais compacto e mais simples
  - Comecei a transformar as classes Java para Kotlin
  - Reformulando onde fica as classe em qual package, muitas refatorações
  - Removendo classe EduardLIB que extendia JavaPlugin
  - Retirando lista de classes de todos os Kits conhecidos de HG e KitPvP e colocando no projeto HG
  - Colocando arquivos da package player/ , world/ e outras foram todas para package /game/
  - Deletei a Storage v2 (Storation) não precisa ter dois sistema de Automatização de armazenamento
  - Deletei o repositorio da package lib/ que existia (EduardLIB)
  - Criando o SQLManager um ORM feito para facilitar a sua vida 50% concluido, falta relacionamento entre tabelas
  - Atualizando DBManager classe retirando independencia agora depende da classe SQLManager ou seja não da pra usar isso fora deste projeto
  - Em breve será reconstruido todos os códigos para que use StringBuilder para maior eficiência
  - StorageAPI sistema de Referencia para Lista e Mapas concertado

package net.eduard.api.server

/**
 * API de Economia do Servidor
 */
interface EconomySystem : PluginSystem{

    fun getGroupBonus(groupName : String) : Double
    fun getGroupDiscount(groupName : String) : Double
    fun setGroupBonus(groupName : String, percent : Double)
    fun setGroupDiscount(groupName : String, percent : Double)
    /**
     * Deposita dinheiro para o jogador com bonus aplicado
     */
    fun giveBonus(playerName : String, amount : Double) : Double

    /**
     * Retira dinheiro do jogador com desconto aplicado
     */
    fun takeDiscounted(playerName : String, amount: Double) : Double
    /**
     * Verifica se o jogador possui a quantia aplicando desconto
     */
    fun checkDiscounted(playerName: String, amount: Double) : Boolean



    /**
     * Deposita dinheiro para o jogador
     */
    fun give(playerName : String, amount : Double)

    /**
     * Retira dinheiro do jogador
     */
    fun take(playerName : String, amount : Double)

    /**
     * Verifica se o jogador possui a quantia
     */
    fun check(playerName : String, amount : Double) : Boolean

    /**
     * Modifica o dinheiro do jogador
     */
    fun modify(playerName : String, amount : Double)

    /**
     * Pega o Multiplicador do Bonus de dinheiro recebido (Padrão 0.0)
     */
    fun getBonusMultiplier(playerName : String) : Double

    /**
     *  Pega o Multiplicador de Desconto do dinheiro pago (Padrão 0.0)
     */
    fun getDiscountMultiplier(playerName : String) : Double

    /**
     * Aplica uma transação entre dois jogadores (Pagamento de dividas)
     */
    fun pay(payerName : String, receiverName : String, amount: Double)

    fun getBuyLimit(playerName : String) : Double
    fun getSellLimit(playerName : String) : Double
    fun giveBuyLimit(playerName : String, amount : Double)
    fun giveSellLimit(playerName : String, amount : Double)
    fun takeBuyLimit(playerName : String, amount : Double)
    fun takeSellLimit(playerName : String, amount : Double)
    fun modifySellLimit(playerName : String, amount : Double)
    fun modifyBuyLimit(playerName : String, amount : Double)
    fun checkBuyLimit(playerName : String, amount : Double) : Boolean
    fun checkSellLimit(playerName : String, amount : Double) : Boolean
    fun getBuyAmountFixed(playerName : String,amount: Double, pricePerAmount : Double) : Double
    fun getSellAmountFixed(playerName : String,amount: Double, pricePerAmount : Double) : Double


}
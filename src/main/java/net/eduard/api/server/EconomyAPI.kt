package net.eduard.api.server

/**
 * API de Economia do Servidor
 */
interface EconomyAPI : EduardPluginsAPI{

    /**
     * Deposita dinheiro para o jogador com bonus aplicado
     */
    fun giveBonus(player : String, amount : Double) : Double

    /**
     * Retira dinheiro do jogador com desconto aplicado
     */
    fun takeDiscounted(player : String, amount: Double) : Double
    /**
     * Verifica se o jogador possui a quantia aplicando desconto
     */
    fun checkDiscounted(player: String,amount: Double) : Boolean



    /**
     * Deposita dinheiro para o jogador
     */
    fun give(player : String, amount : Double)

    /**
     * Retira dinheiro do jogador
     */
    fun take(player : String, amount : Double)

    /**
     * Verifica se o jogador possui a quantia
     */
    fun check(player : String, amount : Double) : Boolean

    /**
     * Modifica o dinheiro do jogador
     */
    fun modify(player : String, amount : Double)

    /**
     * Pega o Multiplicador do Bonus de dinheiro recebido (Padrão 0.0)
     */
    fun getBonusMultiplier(player : String) : Double

    /**
     *  Pega o Multiplicador de Desconto do dinheiro pago (Padrão 0.0)
     */
    fun getDiscountMultiplier(player : String) : Double

    /**
     * Aplica uma transação entre dois jogadores (Pagamento de dividas)
     */
    fun pay(payer : String, receiver : String, amount: Double)
}
package net.eduard.api.lib.modules;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.CodeSource;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * API contendo coisas relacionado a Textos, Numeros, Arquivos, Metodos
 * importantes e Reflection
 *
 * @author Eduard
 * @version 2.0
 * @since Lib v2.0
 */
public final class Extra {
    /**
     * Testa se o numero passado é da coluna expecificada
     *
     * @param index  Numero
     * @param colunm Coluna
     * @return O resultado do teste
     */
    public static boolean isColumn(int index, int colunm) {
        return Extra.getColumn(index) == colunm;
    }

    /**
     * Tipo de geração de Key
     *
     * @author Eduard
     */
    public enum KeyType {
        /**
         * ID UNICO
         */
        UUID,
        /**
         * LETRAS
         */
        LETTER,
        /**
         * NUMEROS
         */
        NUMERIC,
        /**
         * NUMEROS E LETRAS
         */
        ALPHANUMERIC
    }

    /**
     * Pega o texto escrito no command
     *
     * @param init Inicio do texto
     * @param args Argumentos
     * @return Texto criado
     */
    public static String getText(int init, String... args) {
        StringBuilder text = new StringBuilder();
        int id = 0;
        for (String arg : args) {
            if (id < init) {
                id++;
                continue;
            }
            text.append(" ").append(toChatMessage(arg));
            id++;
        }
        return text.toString();
    }

    /**
     * Tenta ler o arquivo dentro do Jar
     *
     * @param loader ClassLoader que ligou a classe
     * @param name   Nome do Recurso (Arquivo dentro do Jar)
     * @return InputStream da leitura
     * @throws IOException Erro
     */
    public static InputStream getResource(ClassLoader loader, String name) throws IOException {
        URL url = loader.getResource(name);
        if (url == null)
            return null;
        URLConnection connection = url.openConnection();
        connection.setUseCaches(false);
        return connection.getInputStream();
    }

    /**
     * Copia a pasta do mundo para outra pasta
     *
     * @param source Pasta do Mundo
     * @param target Pasta destino
     */

    public static void copyWorldFolder(File source, File target) {

        try {
            List<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.dat"));
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if (!target.exists())
                        target.mkdirs();
                    String[] files = source.list();
                    for (String file : Objects.requireNonNull(files)) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyWorldFolder(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Instancia da classe DecimalFormat para o PT-BR
     */
    public static DecimalFormat MONEY = new DecimalFormat("###,###.##",
            DecimalFormatSymbols.getInstance(Locale.forLanguageTag("PT-BR")));

    public static SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat FORMAT_TIME = new SimpleDateFormat("HH:mm:ss");
    public static SimpleDateFormat FORMAT_DATETIME = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
    private static final Map<String, String> REPLACERS = new LinkedHashMap<>();


    private static final Random RANDOM = new Random();


    private static final Map<Class<?>, Class<?>> wrappers = new HashMap<>();

    static {

        wrappers.put(String.class, String.class);
        wrappers.put(int.class, Integer.class);
        wrappers.put(double.class, Double.class);
        wrappers.put(long.class, Long.class);
        wrappers.put(byte.class, Byte.class);
        wrappers.put(short.class, Short.class);
        wrappers.put(float.class, Float.class);
        wrappers.put(boolean.class, Boolean.class);
        wrappers.put(char.class, Character.class);

    }

    static {
        REPLACERS.put("#b", "org.bukkit.");
        REPLACERS.put("#s", "org.spigotmc.");
        REPLACERS.put("#a", "net.eduard.api.");
        REPLACERS.put("#e", "net.eduard.eduardapi.");
        REPLACERS.put("#k", "net.eduard.api.kits.");
        REPLACERS.put("#p", "#mPacket");
        REPLACERS.put("#m", "net.minecraft.server.#v.");
        REPLACERS.put("#c", "org.bukkit.craftbukkit.#v.");
        REPLACERS.put("#s", "org.bukkit.");
    }

    /**
     * Ofusca ou desofuca uma String
     *
     * @param str String
     * @return a String alterada
     */
    public static String allatoriOfucation(String str) {
        int tamanhoDoTexto = str.length();
        char[] umaArrayDeLetras = new char[tamanhoDoTexto];
        int TamanhoDoTextoMenosUm = tamanhoDoTexto - 1;
        while (true) {
            if (TamanhoDoTextoMenosUm >= 0) {
                int PenultimoCaractere = str.charAt(TamanhoDoTextoMenosUm);
                int TamanhoDoTextoMenusUmSomadoComMaisUm = TamanhoDoTextoMenosUm + -1;
                int NaoSeiSeEhUmNUmeroOuUmChar = (char) (PenultimoCaractere ^ 56);
                umaArrayDeLetras[TamanhoDoTextoMenosUm] = (char) NaoSeiSeEhUmNUmeroOuUmChar;
                if (TamanhoDoTextoMenusUmSomadoComMaisUm >= 0) {
                    TamanhoDoTextoMenosUm = TamanhoDoTextoMenusUmSomadoComMaisUm + -1;
                    int nesteNumeroEuNaoEntendiOqueEleEh = str.charAt(TamanhoDoTextoMenusUmSomadoComMaisUm);
                    int NaoSeiSeEhUmNUmeroOuUmChar2 = (char) (nesteNumeroEuNaoEntendiOqueEleEh ^ 70);
                    umaArrayDeLetras[TamanhoDoTextoMenusUmSomadoComMaisUm] = (char) NaoSeiSeEhUmNUmeroOuUmChar2;
                    continue;
                }
            }
            return new String(umaArrayDeLetras);
        }

    }

    /**
     * Centraliza a Array
     *
     * @param paragraph Paragrafo em forma de Array de Texto
     * @param title     Titulo do Texto
     */
    public static void box(String[] paragraph, String title) {
        ArrayList<String> buffer = new ArrayList<String>();
        StringBuilder at = new StringBuilder();

        int side1 = (int) Math.round(25.0D - (title.length() + 4) / 2.0D);
        int side2 = (int) (26.0D - (title.length() + 4) / 2.0D);
        at.append('+');
        for (int t = 0; t < side1; t++) {
            at.append('-');
        }
        at.append("{ ");
        at.append(title);
        at.append(" }");
        for (int t = 0; t < side2; t++) {
            at.append('-');
        }
        at.append('+');
        buffer.add(at.toString());
        at = new StringBuilder();
        buffer.add("|                                                   |");
        int j = paragraph.length;
        for (int i = 0; i < j; i++) {
            String s = paragraph[i];
            at.append("| ");
            int left = 49;
            for (int t = 0; t < s.length(); t++) {
                at.append(s.charAt(t));
                left--;
                if (left == 0) {
                    at.append(" |");
                    buffer.add(at.toString());
                    at = new StringBuilder();
                    at.append("| ");
                    left = 49;
                }
            }
            while (left-- > 0) {
                at.append(' ');
            }
            at.append(" |");
            buffer.add(at.toString());
            at = new StringBuilder();
        }
        buffer.add("|                                                   |");
        buffer.add("+---------------------------------------------------+");

        System.out.println(" ");
        for (String line : buffer.toArray(new String[buffer.size()])) {
            System.out.println(line);
        }
        System.out.println(" ");
    }


    /**
     * Verifica se contem a Mensagem dentro do Texto (Ignora se é maiuscula ou
     * minuscula)
     *
     * @param message Mensagem
     * @param text    Texto
     * @return a resposta
     */
    public static boolean contains(String message, String text) {
        return message.toLowerCase().contains(text.toLowerCase());
    }

    /**
     * Faz uma copia da Stream para UTF8 no arquivo
     *
     * @param is   Stream
     * @param file Arquivo
     * @throws IOException Erro
     */
    public static void copyAsUTF8(InputStream is, File file) throws IOException {
        if (is == null)
            return;
        InputStreamReader in = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(in);
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        br.close();
        in.close();
        is.close();
        Files.write(file.toPath(), lines, StandardCharsets.UTF_8);

    }

    /**
     * Faz uma copia da Path para UTF8 no arquivo
     *
     * @param path Path
     * @param file Arquivo
     * @throws IOException Erro
     */
    public static void copyAsUTF8(Path path, File file) throws IOException {
        List<String> lines = Files.readAllLines(path);
        Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
    }

    /**
     * Corta o texto em um tamanho escolhido
     *
     * @param text   Texto
     * @param lenght Tamanho
     * @return Texto cortado
     */
    public static String cutText(String text, int lenght) {
        return text.length() > lenght ? text.substring(0, lenght) : text;
    }

    /**
     * Detela a pasta e todos os arquivos dentro
     *
     * @param file Pasta
     */
    public static void deleteFolder(File file) {
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File value : files) {
                    if (value.isDirectory()) {
                        deleteFolder(value);
                        value.delete();
                    } else {
                        value.delete();
                    }
                }
            }
            file.delete();
        }
    }

    /**
     * Verifica se uma Array é identica a outra
     *
     * @param firstArray  Array 1
     * @param secondArray Array 2
     * @return se são iguais
     */
    public static boolean equalsArray(Class<?>[] firstArray, Class<?>[] secondArray) {
        if (firstArray.length == secondArray.length) {
            for (int i = 0; i < secondArray.length; i++) {
                if (!firstArray[i].equals(secondArray[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Envia um POST request para a URL
     *
     * @param targetURL     URL
     * @param urlParameters Parametros enviados
     * @return uma resposta em forma de XML ou JSON
     */
    public static String executePost(String targetURL, String urlParameters) {
        HttpURLConnection connection = null;
        try {
            // Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            // Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            // Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Defaz o ZIP do Arquivo
     *
     * @param zipIn    Input Stream (Conexao de Algum Arquivo)
     * @param filePath Destino Arquivo
     */
    public static void extractFile(ZipInputStream zipIn, String filePath) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
            byte[] bytesIn = new byte[4096];
            int read = 0;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Formata o texto aplicando as cores do CHAT_COLOR
     *
     * @param text Text normal
     * @return Text formated
     */
    public static String formatColors(String text) {
        char[] chars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a', 'b', 'c', 'd', 'e', 'f', 'n', 'r', 'l',
                'k', 'o', 'm'};
        char[] array = text.toCharArray();
        for (int t = 0; t < array.length - 1; t++) {
            if (array[t] == '&') {
                for (char c : chars) {
                    if (c == array[(t + 1)]) {
                        array[t] = '§';
                    }
                }
            }
        }
        return new String(array);
    }

    /**
     * Formata o resultado da subtração de (numero antigo - numero atual)
     *
     * @param timestamp Numero Antigo
     * @return Texto do numero formatado
     */
    public static String formatDiference(long timestamp) {
        return formatTime(timestamp - System.currentTimeMillis());
    }

    /**
     * Interpreta um numero utilizando formatação OP<br>
     * Tipo "1B" = 1000000000D
     *
     * @param text Numero em forma de Texto
     * @return numero Interpretado
     */
    public static double fromMoneyToDouble(String text) {
        if (text == null) {
            return 0D;
        }


        text = text.toLowerCase();
        List<String> classificacoes = Arrays.asList("k", "m", "b", "t", "q", "qq", "s", "ss", "O", "n", "d", "un", "dd", "td", "qd", "qqd", "sd", "ssd", "od", "nd", "vd");
        for (int i = classificacoes.size() - 1; i >= 0; i--) {
            String sigla = classificacoes.get(i);
            if (text.endsWith(sigla)) {
                text = text.replace(sigla, "");

                double valor = Extra.toDouble(text);
                valor = fixDouble(valor);
                double potencia = Math.pow(10, (i + 1) * 3);

                return valor * potencia;


            }
        }

        return fixDouble(Extra.toDouble(text));
    }

    public static double fixDouble(double number) {
        if (Double.isInfinite(number) || Double.isNaN(number)) {
            return 1;
        }
        return number;
    }

    /**
     * Formata um numero grande, formatação estilo de servidores OP
     * <br>
     * Método concertado dia 22/03/2020
     *
     * @param numero Numero grande
     * @return Numero formatado
     * @author Eduard
     */
    public static String formatMoney(double numero) {

        List<String> classificacoes = Arrays.asList("", "k", "m", "b", "t", "q", "qq", "s", "ss", "O", "n", "d", "un", "dd", "td", "qd", "qqd", "sd", "ssd", "od", "nd", "vd");
        DecimalFormat formatador = new DecimalFormat("#,###.###", new DecimalFormatSymbols(Locale.US));

        String numeroFormatado = formatador.format(numero);


        char separador = formatador.getDecimalFormatSymbols().getGroupingSeparator();

        String[] conjuntoDeTresCasas = numeroFormatado.split("" + separador);
        int tamanho = conjuntoDeTresCasas.length;
        if (tamanho <= 1) {

            return numeroFormatado;
        }

        String sigla = classificacoes.get(classificacoes.size() - 1);
        if (tamanho <= classificacoes.size()) {
            sigla = classificacoes.get(tamanho - 1);
        }
        try {
            Number numeroFinal = formatador.parse(conjuntoDeTresCasas[0] + formatador.getDecimalFormatSymbols().getDecimalSeparator() + conjuntoDeTresCasas[1]);
            return formatador.format(numeroFinal) + sigla.toUpperCase();
        } catch (Exception ex) {
            return "-1.0";
        }

    }

    /**
     * Formata um numero grande, formatação estilo de servidores OP
     *
     * @param numero Numero grande
     * @return Numero formatado
     * @author THG com pequenas mudanças feitas por min
     * @deprecated Este método apresenta pequenos bugs
     */
    @Deprecated
    public static String formatMoney2(double numero) {
        DecimalFormat formatador = new DecimalFormat("#,###.##", new DecimalFormatSymbols(Locale.US));
        String formatado = formatador.format(numero);
        String v = formatado.split(",")[0];
        formatado = numero >= 1000000 && numero <= 999999999 ? v + "M"
                : numero >= 1000000000 && numero <= 999999999999L ? v + "B"
                : numero >= 1000000000000L && numero <= 999999999999999L ? v + "T"
                : numero >= 1000000000000000L && numero <= 999999999999999999L ? v + "Q"
                : numero >= 1000000000000000000L && String.valueOf(numero).length() <= 21
                ? v + "QUI"
                : String.valueOf(numero).length() > 21 ? "999QUI"
                : String.valueOf(numero).length() < 7 ? formatado : formatado;

        return formatado;
    }


    /**
     * No dia 28/07/2019 o ViniOtaku#0666 passou este Metodo de formatacao<br>
     * se usar formatMoney3(numero, 0)
     *
     * @param numero    Numero grandão
     * @param iteration Interação em N
     * @return Numero Formatado
     * @deprecated Por alguma razão deu falhas no funcionamento mas funcionava bem
     */
    @Deprecated
    public static String formatMoney3(double numero, int iteration) {
        String[] letras = new String[]{"K", "M", "B", "Q", "QQ", "S", "SS", "O", "N", "D"};
        double d = ((long) numero / 100D) / 10.0;
        boolean isRound = (d * 10) % 10 == 0;


        return (d < 1000 ? ((d > 99.9 || isRound || (!isRound && d > 9.99) ? (int) d : d + "") + " " + letras[iteration])
                : formatMoney3(d, iteration + 1));
    }

    /**
     * Cria uma formação de tempo muito melhor do que a DateFormat faz
     *
     * @param time Tempo em forma de numero
     * @return Tempo formatado
     */
    public static String formatTime(long time) {
        if (time == 0L) {
            return "never";
        }
        long day = TimeUnit.MILLISECONDS.toDays(time);
        long hours = TimeUnit.MILLISECONDS.toHours(time) - day * 24L;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.MILLISECONDS.toHours(time) * 60L;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MILLISECONDS.toMinutes(time) * 60L;
        StringBuilder sb = new StringBuilder();
        if (day > 0L) {
            sb.append(day).append(" ").append(day == 1L ? "dia" : "dias").append(" ");
        }
        if (hours > 0L) {
            sb.append(hours).append(" ").append(hours == 1L ? "hora" : "horas").append(" ");
        }
        if (minutes > 0L) {
            sb.append(minutes).append(" ").append(minutes == 1L ? "minuto" : "minutos").append(" ");
        }
        if (seconds > 0L) {
            sb.append(seconds).append(" ").append(seconds == 1L ? "segundo" : "segundos");
        }
        String diff = sb.toString();
        return diff.isEmpty() ? "agora" : diff;
    }


    /**
     * Pega uma lista de classes de uma package <br>
     * metodo incompleto
     *
     * @param classe  Plugin
     * @param pkgname Package
     * @return Lista de Classes
     */
    public static List<String> getClassesName(Class<?> classe, String pkgname) {
        List<String> classes = new ArrayList<>();
        CodeSource src = classe.getProtectionDomain().getCodeSource();
        if (src != null) {
            URL resource = src.getLocation();
            try {
                //
                String resPath = resource.getPath().replace("%20", " ");
                String jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
//				try {
//					return getClassesName(new JarFile(jarPath), pkgname);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return classes;
    }


    public static List<String> getClassesName(JarFile jar, String pack) {
        List<String> lista = new ArrayList<>();
        try {
            String relPath = pack.replace('.', '/');
            // (entryName.length() > relPath.length() + "/".length())
            // String resPath = resource.getPath().replace("%20", " ");
            // String jarPath = resPath.replaceFirst("[.]jar[!].*",
            // ".jar").replaceFirst("file:", "");
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = (JarEntry) entries.nextElement();
                String entryName = entry.getName();
                if ((entryName.endsWith(".class")) && (entryName.startsWith(relPath)) && !entryName.contains("$")) {
                    String classeName = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
                    lista.add(classeName);

                }

            }
            jar.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static boolean getChance(double chance) {

        return Math.random() <= chance;
    }

    /**
     * Pega uma lista de classes de uma package
     *
     * @param classe  Plugin
     * @param pkgname Package
     * @return Lista de Classes
     */
    public static List<Class<?>> getClasses(Class<?> classe, String pkgname) {
        List<Class<?>> classes = new ArrayList<>();
        CodeSource src = classe.getProtectionDomain().getCodeSource();
        if (src != null) {
            URL resource = src.getLocation();
            try {

                String resPath = resource.getPath().replace("%20", " ");
                String jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
                try {
                    return getClasses(new JarFile(jarPath), pkgname);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return classes;
    }

    public static List<Class<?>> getClasses(JarFile jar, String pack) {
        List<Class<?>> lista = new ArrayList<>();
        try {
            String relPath = pack.replace('.', '/');
            // (entryName.length() > relPath.length() + "/".length())
            // String resPath = resource.getPath().replace("%20", " ");
            // String jarPath = resPath.replaceFirst("[.]jar[!].*",
            // ".jar").replaceFirst("file:", "");
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                if ((entryName.endsWith(".class")) && (entryName.startsWith(relPath)) && !entryName.contains("$")) {

                    String classeName = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
                    try {
                        Class<?> claz = Class.forName(classeName);

                        lista.add(claz);
                    } catch (Error e) {
                        System.out.println("Error on load " + classeName);
                    }

                }

            }
            jar.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static Class<?> getClassFrom(Object object) throws Exception {
        if (object instanceof Class) {
            return (Class<?>) object;
        }
        if (object instanceof String) {
            String string = (String) object;
            if (string.startsWith("#")) {
                for (Entry<String, String> entry : REPLACERS.entrySet()) {
                    string = string.replace(entry.getKey(), entry.getValue());
                }
                return Class.forName(string);
            }
        }
        try {
            return (Class<?>) object.getClass().getField("TYPE").get(0);
        } catch (Exception ignored) {
        }
        return object.getClass();
    }

    /**
     * Descobre qual é a coluna baseada no numero
     *
     * @param index Numero
     * @return A coluna
     */
    public static int getColumn(int index) {
        if (index < 9) {
            return index + 1;
        }
        return (index % 9) + 1;
    }

    /**
     * Pega o comando da string inteira
     *
     * @param message String inteira
     * @return comando
     */
    public static String getCommandName(String message) {
        String command = message;
        if (message.contains(" "))
            command = message.split(" ")[0];
        return command;
    }


    /**
     * Pega o tempo restante que podera sair do cooldown
     *
     * @param before  Tempo anterior
     * @param seconds Segundos de cooldown
     * @return Tempo restante
     */
    public static long getCooldown(long before, long seconds) {

        long now = System.currentTimeMillis();
        long cooldown = seconds * 1000;

        // +5 - 19 + 15

        return +cooldown - now + before;

    }


    public static int getIndex(int column, int line) {
        if (line <= 0) {
            line = 1;
        }

        if (column > 9) {
            column = 9;
        }
        if (column <= 0) {
            column = 1;
        }

        int index = (line - 1) * 9;
        return index + (column - 1);
    }

    public static int getLine(int index) {
        return (index / 9) + 1;
    }


    public static long getNow() {
        return System.currentTimeMillis();
    }


    /**
     * Pega o id retornado da Mojang vindo em um JSON
     *
     * @param playerName Nick do jogador
     * @return o ID retornado da Mojang
     */
    public static String getPlayerUUIDByName(String playerName) {

        try {
            URL link = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            URLConnection conexao = link.openConnection();
            InputStream stream = conexao.getInputStream();
            String json = readSTR(stream, StandardCharsets.UTF_8);
            JsonParser parser = new JsonParser();
            JsonObject object = parser.parse(json).getAsJsonObject();
            return object.get("id").getAsString();

        } catch (Exception ignored) {

        }

        return null;
    }

    public static String getProgressBar(double money, double price, String completedColor, String needColor,
                                        String symbol) {
        return getProgressBar(money / price, completedColor, needColor, symbol);
    }


    public static String getProgressBar(double percent, String completedColor, String needColor,
                                        String symbol) {
        StringBuilder result = new StringBuilder();
        double div = percent;
        // 10 5 2
        // long redonde = Math.round(div * 100);
        // long con = redonde / 10;
        if (div > 1) {
            div = 1;
        }
        double rest = 1D - div;
        result.append(completedColor);
        while (div > 0) {
            result.append(symbol);
            div -= 0.1;
        }
        result.append(needColor);
        while (rest > 0) {
            result.append(symbol);
            rest -= 0.1;
        }
        return result.toString();
    }


    /*
     * INICIO DA AREA DE REFLECTION
     *
     */
    public static Constructor<?> getConstructor(Object object, Object... parameters) throws Exception {

        Class<?> claz = getClassFrom(object);
        try {
            Constructor<?> cons = claz.getDeclaredConstructor(getParameters(parameters));
            cons.setAccessible(true);
            return cons;
        } catch (Exception e) {
            Constructor<?> cons = claz.getConstructor(getParameters(parameters));
            cons.setAccessible(true);
            return cons;
        }

    }

    public static Method getMethod(Object object, String name, Object... parameters) throws Exception {
        Class<?> claz = getClassFrom(object);
        try {
            Method method = claz.getDeclaredMethod(name, getParameters(parameters));
            method.setAccessible(true);
            return method;
        } catch (Exception e) {
            Method method = claz.getMethod(name, getParameters(parameters));
            method.setAccessible(true);
            return method;
        }

    }

    public static Object getNew(Object object, Object... values) throws Exception {
        return getConstructor(object, values).newInstance(values);

    }

    public static Object getFieldValue(Object object, String name) throws Exception {
        return getField(object, name).get(object);
    }

    /**
     * Retorna o construtor vazio da classe
     * @param clz Classe com o Construtor vazio
     * @param <T> Classe
     * @return Construtor da classe com zero parametros
     */
    public static <T> Constructor<T> getEmptyConstructor(Class<T> clz) {
        Constructor<T> constructor = null;
        for (Constructor<?> loopConstructor : clz.getDeclaredConstructors()) {
            if (loopConstructor.getParameterCount() == 0) {
                constructor = (Constructor<T>) loopConstructor;
                break;
            }
        }
        return constructor;
    }

    public static void setFieldValue(Object object, String name, Object value) throws Exception {
        getField(object, name).set(object, value);
    }

    public static Object getNew(Object object, Object[] parameters, Object... values) throws Exception {
        return getConstructor(object, parameters).newInstance(values);
    }

    public static Class<?>[] getParameters(Object... parameters) throws Exception {
        Class<?>[] objects = new Class<?>[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            objects[i] = getClassFrom(parameters[i]);
        }
        return objects;

    }

    public static Object getMethodInvoke(Object object, String name, Object... values) throws Exception {

        return getMethod(object, name, values).invoke(object, values);
    }

    public static Object getMethodInvoke(Object object, String name, Object[] parameters, Object... values) throws Exception {
        try {
            return getMethod(object, name, parameters).invoke(object, values);
        } catch (InvocationTargetException e) {
            return null;
        }

    }

    public static Field getField(Object object, String name) throws Exception {
        Class<?> claz = getClassFrom(object);
        try {
            Field field = claz.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (Exception e) {
            Field field = claz.getField(name);
            field.setAccessible(true);
            return field;
        }

    }

    /*

    FIM DA AREA DE REFLECTION

     */

    public static <E> E getRandom(List<E> objects) {
        if (objects.size() >= 1)
            return objects.get(getRandomInt(1, objects.size()) - 1);
        return null;
    }

    @SafeVarargs
    public static <E> E getRandom(E... objects) {
        if (objects.length >= 1)
            return objects[getRandomInt(1, objects.length) - 1];
        return null;
    }

    public static double getRandomDouble(double minValue, double maxValue) {

        double min = Math.min(minValue, maxValue), max = Math.max(minValue, maxValue);
        return min + (max - min) * RANDOM.nextDouble();
    }


    public static int getRandomInt(int minValue, int maxValue) {

        int min = Math.min(minValue, maxValue), max = Math.max(minValue, maxValue);
        return min + RANDOM.nextInt(max - min + 1);
    }

    public static String getReplacer(String key) {
        return REPLACERS.get(key);
    }

    /**
     * Pega um Objecto serializavel do Arquivo
     *
     * @param file Arquivo
     * @return Objeto
     */
    public static Object getSerializable(File file) {
        if (!file.exists()) {
            return null;
        }
        try {

            FileInputStream getStream = new FileInputStream(file);
            ObjectInputStream get = new ObjectInputStream(getStream);
            Object object = get.readObject();
            get.close();
            getStream.close();
            return object;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Pega o Ip do Conexao do Servidor
     *
     * @return Ip do Servidor
     */
    public static String getServerIp() {
        String ip = null;
        try {
            URLConnection connect = new URL("http://checkip.amazonaws.com/").openConnection();
            connect.addRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            Scanner scan = new Scanner(connect.getInputStream());

            if (scan.hasNext()) {
                ip = scan.nextLine();
            }
            scan.close();

        } catch (Exception ex) {
            ex.printStackTrace();


        }
        return ip;
    }

    /**
     * Retorna um JsonObject com os dados mais impotantes o
     *
     * @param playerUUID UUID do Jogador
     * @return JsonObject com os dados da SKIN
     */
    public static JsonObject getSkinProperty(String playerUUID) {

        try {
            URL link = new URL(
                    "https://sessionserver.mojang.com/session/minecraft/profile/" + playerUUID + "?unsigned=false");
            URLConnection conexao = link.openConnection();
            conexao.setUseCaches(true);
            InputStream stream = conexao.getInputStream();

            String json = readSTR(stream, StandardCharsets.UTF_8);
            JsonParser parser = new JsonParser();
            JsonObject object = parser.parse(json).getAsJsonObject();
            return object.get("properties").getAsJsonArray().get(0).getAsJsonObject();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }


    public static String formatSeconds(int time, String secondDisplay, String minuteDisplay) {
        if (time >= 60) {
            int min = time / 60;
            int sec = time % 60;
            if (sec == 0) {
                return min + minuteDisplay;
            } else {
                return min + minuteDisplay + sec + secondDisplay;
            }

        }
        return time + secondDisplay;
    }


    public static String formatSeconds1(int seconds) {

        return formatSeconds(seconds, "s", "m");

    }

    public static String formatSeconds2(int seconds) {

        return formatSeconds(seconds, " seg", " min ");

    }

    public static String formatSeconds3(int seconds) {

        return formatSeconds(seconds, " segundo(s)", " minuto(s) ");

    }

    /**
     * @param type Variavel {@link Type} (Classe/Tipo)
     * @return Tipo 1 de type, caso type seja um {@link ParameterizedType}
     */
    public static Class<?> getTypeKey(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            Type[] types = parameterizedType.getActualTypeArguments();

            return (Class<?>) types[0];

        }
        return null;
    }

    /**
     * @param type Variavel (Classe)
     * @return Tipo 2 de type, caso type seja um {@link ParameterizedType}
     */
    public static Class<?> getTypeValue(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] types = parameterizedType.getActualTypeArguments();
            if (types.length > 1) {
                return (Class<?>) types[1];
            }

        }
        return null;
    }


    /**
     * Gera o preco da classe baseado em suas variaveis , metodos e demais coisas
     *
     * @param claz      Classe
     * @param blacklist Classes que não poderam ter seu valor mais relido
     * @return Valor da classe
     */
    public static double getValueOf(Class<?> claz, ArrayList<Class<?>> blacklist, boolean debug) {
        double finalValue = 0.1;
        // sem esta verificação dependo pode ultrapassar as casas da centena vale muito

        if (claz.getPackage().getName().startsWith("java")) {
            blacklist.add(claz);

            return 0;
        }
        if (claz.getPackage().getName().startsWith("com.google")) {
            blacklist.add(claz);
            return 0;
        }
        try {

            Field[] lista = claz.getDeclaredFields();
            for (Field variable : lista) {
                if (debug)
                    System.out.println(claz.getSimpleName() + " Var: " + variable.getName());
                try {

                    Class<?> type = variable.getType();
                    finalValue += 0.05;
                    if (debug)
                        System.out.println(claz.getSimpleName() + " +0.05");
                    if (type.isPrimitive()) {

                        continue;
                    }
                    if (type.isEnum()) {
                        continue;
                    }
                    if (type.isArray()) {
                        continue;
                    }
                    if (type.isAnnotation()) {
                        continue;
                    }

                    if (blacklist.contains(type)) {
                        if (debug)
                            System.out.println(claz.getSimpleName() + " Class in blacklist " + type.getSimpleName());
                        continue;
                    } else {
                        if (debug)
                            System.out.println(
                                    claz.getSimpleName() + " Class is not in blacklist " + type.getSimpleName());
                    }
                    if (!type.isAssignableFrom(claz)) {
                        if (debug)
                            System.out.println(claz.getSimpleName() + " Reading value of " + type.getSimpleName());
                        blacklist.add(claz);
                        finalValue += getValueOf(type, blacklist, debug);
                    } else {
                        if (debug)
                            System.out
                                    .println("" + claz.getSimpleName() + " is assignable from " + claz.getSimpleName());
                    }
                } catch (Error e) {
                    if (debug)
                        System.out.println("Failed tu read this field");
                }
            }
            for (Method method : claz.getDeclaredMethods()) {
                if (debug)
                    System.out.println(claz.getSimpleName() + " Metd: " + method.getName());
                finalValue += 0.025;
                if (debug)
                    System.out.println(claz.getSimpleName() + " +0.025");
                double calc = 0.005 * method.getParameterCount();
                finalValue += calc;

                if (debug)
                    System.out.println(claz.getSimpleName() + " +" + calc);

            }
            if (claz.getSuperclass() != null)
                while (!claz.getSuperclass().equals(Object.class)) {
                    if (debug)
                        System.out.println("" + claz.getSimpleName() + " Reading class super "
                                + claz.getSuperclass().getSimpleName());
                    blacklist.add(claz);
                    finalValue += getValueOf(claz.getSuperclass(), blacklist, debug);
                    claz = claz.getSuperclass();
                    if (claz == null)
                        break;
                }
            if (debug)
                System.out.println(claz.getSimpleName() + " Final value: " + finalValue);
        } catch (Error e) {
            if (debug)
                System.out.println("Failed to load this class");
        }
        return finalValue;
    }

    public static Class<?> getWrapperOrReturn(Class<?> clazz) {
        Class<?> wrapper = getWrapper(clazz);
        if (wrapper == null) {
            wrapper = clazz;
        }
        return wrapper;
    }

    public static Class<?> getWrapper(Class<?> clazz) {
        for (Entry<Class<?>, Class<?>> wrapperEntry : wrappers.entrySet()) {
            if (wrapperEntry.getKey().equals(clazz) || wrapperEntry.getValue().equals(clazz)) {
                return wrapperEntry.getValue();
            }
        }
        return null;
    }


    /**
     * Retorna se (now < (seconds + before));
     *
     * @param before  (Antes)
     * @param seconds (Cooldown)
     * @return
     */
    public static boolean inCooldown(long before, long seconds) {

        long now = System.currentTimeMillis();
        long cooldown = seconds * 1000;
        return now <= (cooldown + before);

    }

    public static boolean isCloneable(Class<?> claz) {
        return Cloneable.class.isAssignableFrom(claz);
    }

    public static boolean isDirectory(File file) {
        try {
            return (file.isDirectory());
        } catch (Exception e) {
            return isDirectory(file.getName());
        }
    }

    public static boolean isDirectory(String name) {

        if (name.endsWith(File.separator)) {
            return true;
        }
        if (name.endsWith("/")) {
            return true;
        }
        if (name.endsWith(File.pathSeparator)) {
            return true;

        }
        return false;

    }

    /**
     * Testa se um IP é Proxy
     *
     * @param ip IP
     * @return se o IP é Proxy
     */
    public static boolean isIpProxy(String ip) {
        try {
            String url = "http://botscout.com/test/?ip=" + ip;
            Scanner scanner = new Scanner(new URL(url).openStream());
            if (scanner.findInLine("Y") != null) {
                scanner.close();
                return true;
            }
            scanner.close();

        } catch (Exception e) {
        }
        return false;
    }

    /**
     * @param claz Classe
     * @return Se a claz § uma {@link List} (Lista)
     */
    public static boolean isList(Class<?> claz) {
        return List.class.isAssignableFrom(claz);
    }

    /**
     * @param claz Classe
     * @return Se a claz § um {@link Map} (Mapa)
     */
    public static boolean isMap(Class<?> claz) {
        return Map.class.isAssignableFrom(claz);
    }

    public static boolean isMultBy(int number1, int numer2) {

        return number1 % numer2 == 0;
    }

    /**
     * @param claz Classe
     * @return Se a claz § um {@link String} (Texto)
     */
    public static boolean isString(Class<?> claz) {
        return String.class.isAssignableFrom(claz);
    }

    /**
     * @param claz Classe
     * @return Se a claz § do tipo Primitivo ou Wrapper (Envolocro)
     */
    public static boolean isWrapper(Class<?> claz) {
        if (isString(claz))
            return true;
        try {
            claz.getField("TYPE").get(0);
            return true;
        } catch (Exception e) {
            return claz.isPrimitive();
        }
    }

    /**
     * Tenta carregar uma classe e a retorna
     *
     * @param name Endere§o
     * @return Classe carregada
     */
    public static Class<?> loadClass(String name) {
        Class<?> claz = null;
        try {
            claz = Class.forName(name);
        } catch (Exception e) {
        }
        return claz;
    }

    public static <E> List<E> mover(int casasMovida, List<E> lista) {
        List<E> listaCopia = new ArrayList<>();
        if (casasMovida > lista.size()) {
            casasMovida = 1;
        }
        for (int i = 0; i < lista.size(); i++) {
            int m = i + casasMovida;
            if (m >= lista.size()) {
                m -= lista.size();
            }
            E dado = lista.get(m);
            listaCopia.add(dado);
        }
        return listaCopia;
    }

    /**
     * Gera uma nova Key
     *
     * @param type    Tipo da Key
     * @param maxSize Tamanho da Key
     * @return Key em forma de STRING
     */
    public static String newKey(KeyType type, int maxSize) {

        String key = "";
        if (type == KeyType.UUID) {
            key = UUID.randomUUID().toString();
        } else if (type == KeyType.LETTER) {
            final StringBuilder buffer = new StringBuilder();
            String characters = "";
            characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
            final int charactersLength = characters.length();
            for (int i = 0; i < maxSize; ++i) {
                final double index = Math.random() * charactersLength;
                buffer.append(characters.charAt((int) index));
            }
            key = buffer.toString();
        } else if (type == KeyType.NUMERIC) {
            final StringBuilder buffer = new StringBuilder();
            String characters = "";
            characters = "0123456789";
            final int charactersLength = characters.length();
            for (int i = 0; i < maxSize; ++i) {
                final double index = Math.random() * charactersLength;
                buffer.append(characters.charAt((int) index));
            }
            key = buffer.toString();
        } else if (type == KeyType.ALPHANUMERIC) {
            final StringBuilder buffer = new StringBuilder();
            String characters = "";
            characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            final int charactersLength = characters.length();
            for (int i = 0; i < maxSize; ++i) {
                final double index = Math.random() * charactersLength;
                buffer.append(characters.charAt((int) index));
            }
            key = buffer.toString();
        }
        return key;

    }

    public static void newReplacer(String key, String replacer) {
        REPLACERS.put(key, replacer);
    }

    @SafeVarargs
    public static <T> Set<T> newSet(T... array) {
        return new HashSet<>(Arrays.asList(array));
    }

    public static long parseDateDiff(String time, boolean future) throws Exception {
        Pattern timePattern = Pattern.compile(
                "(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?(?:([0-9]+)\\s*(?:s[a-z]*)?)?",
                2);
        Matcher m = timePattern.matcher(time);
        int years = 0;
        int months = 0;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        boolean found = false;
        while (m.find())
            if ((m.group() != null) && (!m.group().isEmpty())) {
                for (int i = 0; i < m.groupCount(); i++) {
                    if ((m.group(i) != null) && (!m.group(i).isEmpty())) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    if ((m.group(1) != null) && (!m.group(1).isEmpty())) {
                        years = Integer.parseInt(m.group(1));
                    }
                    if ((m.group(2) != null) && (!m.group(2).isEmpty())) {
                        months = Integer.parseInt(m.group(2));
                    }
                    if ((m.group(3) != null) && (!m.group(3).isEmpty())) {
                        weeks = Integer.parseInt(m.group(3));
                    }
                    if ((m.group(4) != null) && (!m.group(4).isEmpty())) {
                        days = Integer.parseInt(m.group(4));
                    }
                    if ((m.group(5) != null) && (!m.group(5).isEmpty())) {
                        hours = Integer.parseInt(m.group(5));
                    }
                    if ((m.group(6) != null) && (!m.group(6).isEmpty())) {
                        minutes = Integer.parseInt(m.group(6));
                    }
                    if ((m.group(7) == null) || (m.group(7).isEmpty()))
                        break;
                    seconds = Integer.parseInt(m.group(7));

                    break;
                }
            }
        if (!found) {
            throw new Exception("Illegal Date");
        }
        if (years > 20) {
            throw new Exception("Illegal Date");
        }
        Calendar calendar = new GregorianCalendar();
        if (years > 0) {
            calendar.add(Calendar.YEAR, years * (future ? 1 : -1));
        }
        if (months > 0) {
            calendar.add(Calendar.MONTH, months * (future ? 1 : -1));
        }
        if (weeks > 0) {
            calendar.add(Calendar.WEEK_OF_YEAR, weeks * (future ? 1 : -1));
        }
        if (days > 0) {
            calendar.add(Calendar.DATE, days * (future ? 1 : -1));
        }
        if (hours > 0) {
            calendar.add(Calendar.HOUR_OF_DAY, hours * (future ? 1 : -1));
        }
        if (minutes > 0) {
            calendar.add(Calendar.MINUTE, minutes * (future ? 1 : -1));
        }
        if (seconds > 0) {
            calendar.add(Calendar.SECOND, seconds * (future ? 1 : -1));
        }
        return calendar.getTimeInMillis();
    }


    /**
     * Le todas as linhas do arquivo
     *
     * @param file Arquivo
     * @return Linhas lida
     */
    public static List<String> readLines(File file) {
        Path path = file.toPath();
        try {

            return Files.readAllLines(path);
        } catch (Exception ignored) {

        }
        try {

            return Files.readAllLines(path, Charset.defaultCharset());
        } catch (Exception ignored) {
        }
        List<String> lines = new ArrayList<>();
        try {

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();

        } catch (Exception ignored) {
        }
        return lines;

    }

    /**
     * Le um Texto de uma stream
     *
     * @param stream  Stream de bytes
     * @param charset Conjunto de caracteres
     * @return o texto lido da stream
     * @throws IOException Erro ao ler
     */
    public static String readSTR(InputStream stream, Charset charset) throws IOException {
        byte[] bytes = new byte[stream.available()];
        if (stream.read(bytes) != -1) {
            return new String(bytes, charset);
        } else return "";
    }

    public static String removeBrackets(String... message) {

        return Arrays.toString(message).replace("[", "").replace("]", "");
    }


    /**
     * Salva um Objecto no Arquivo em forma de serialização Java
     *
     * @param object Objeto (Dado)
     * @param file   Arquivo
     */
    public static void setSerializable(Object object, File file) {
        try {
            FileOutputStream saveStream = new FileOutputStream(file);
            ObjectOutputStream save = new ObjectOutputStream(saveStream);
            if (object instanceof Serializable) {
                save.writeObject(object);
            }
            save.close();
            saveStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public static String simpleDeosfucation(String str) {
        final String[] split = str.split(";");
        final int[] parse = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            parse[i] = Integer.parseInt(split[i]) - split.length * split.length;
        }
        StringBuilder build = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            build.append((char) parse[i]);
        }
        return build.toString();
    }

    public static String simpleOfuscation(String str) {
        String build = "";
        for (int i = 0; i < str.length(); i++) {
            build = build.equals("") ? "" + (str.charAt(i) + str.length() * str.length())
                    : build + ";" + (str.charAt(i) + str.length() * str.length());
        }
        return build;
    }

    public static boolean startWith(String message, String text) {
        return message.toLowerCase().startsWith(text.toLowerCase());
    }

    /**
     * Transforma um objeto em boolean
     *
     * @param obj any
     * @return Boolean
     */
    public static Boolean toBoolean(Object obj) {

        if (obj == null) {
            return false;
        }
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        try {
            return Boolean.valueOf(obj.toString());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Transforma um objeto em byte
     *
     * @param object any
     * @return Byte
     */
    public static Byte toByte(Object object) {

        if (object == null) {
            return 0;
        }
        if (object instanceof Byte) {
            return (Byte) object;
        }
        if (object instanceof Number) {
            Number number = (Number) object;
            return number.byteValue();
        }
        try {
            return Byte.valueOf(object.toString());
        } catch (Exception e) {
            return 0;
        }

    }

    public static String toChatMessage(String text) {
        return text.replace("&", "§");
    }

    public static String toConfigMessage(String text) {
        return text.replace("§", "&");
    }

    public static List<String> toConfigMessages(List<String> lore) {
        List<String> lines = new ArrayList<String>();
        for (String line : lore) {
            lines.add(toConfigMessage(line));
        }
        return lines;
    }

    public static String toDecimal(Object number) {
        return toDecimal(number, 2);
    }

    public static String toDecimal(Object number, int max) {
        String text = "" + number;
        if (text.contains(".")) {
            String[] split = text.replace(".", ",").split(",");
            if (split[1].length() >= max) {
                return split[0] + "." + split[1].substring(0, max);
            }
            return text;
        }
        return text;
    }

    public static Double toDouble(Object object) {

        if (object == null) {
            return 0D;
        }
        if (object instanceof Double) {
            return (Double) object;
        }
        if (object instanceof Number) {
            Number number = (Number) object;
            return number.doubleValue();
        }
        try {
            return Double.valueOf(object.toString());
        } catch (Exception e) {
            return 0D;
        }

    }

    public static Float toFloat(Object object) {

        if (object == null) {
            return 0F;
        }
        if (object instanceof Float) {
            return (Float) object;
        }
        if (object instanceof Number) {
            Number number = (Number) object;
            return number.floatValue();
        }
        try {
            return Float.valueOf(object.toString());
        } catch (Exception e) {
            return 0F;
        }

    }

    public static Integer toInt(Object object) {

        if (object == null) {
            return 0;
        }
        if (object instanceof Integer) {
            return (Integer) object;
        }
        if (object instanceof Number) {
            Number number = (Number) object;
            return number.intValue();
        }
        try {
            return Integer.valueOf(object.toString());
        } catch (Exception e) {
            return 0;
        }

    }

    public static Integer toInteger(Object object) {
        return toInt(object);
    }

    /**
     * Transforma o Texto em uma Lista de Texto
     *
     * @param text Texto a ser cortado
     * @param size Tamanho do corte
     * @return Lista de textos
     */
    public static List<String> toLines(String text, int size) {

        List<String> lista = new ArrayList<>();

        String x = text;

        int id = 1;
        while (x.length() >= size) {
            String cut = x.substring(0, size);
            x = text.substring(id * size);
            id++;
            lista.add(cut);
        }
        lista.add(x);
        return lista;

    }

    public static Long toLong(Object object) {

        if (object == null) {
            return 0L;
        }
        if (object instanceof Long) {
            return (Long) object;
        }
        if (object instanceof Number) {
            Number number = (Number) object;
            return number.longValue();
        }
        try {
            return Long.valueOf(object.toString());
        } catch (Exception e) {
            return 0L;
        }
    }

    public static List<String> toMessages(List<Object> list) {
        List<String> lines = new ArrayList<String>();
        for (Object line : list) {
            lines.add(toChatMessage(line.toString()));
        }
        return lines;
    }

    public static Short toShort(Object object) {

        if (object == null) {
            return 0;
        }
        if (object instanceof Short) {
            return (Short) object;
        }
        if (object instanceof Number) {
            Number number = (Number) object;
            return number.shortValue();
        }
        try {
            return Short.valueOf(object.toString());
        } catch (Exception e) {
            return 0;
        }

    }

    /**
     * Transforma um objeto em texto
     *
     * @param object any
     * @return String
     */
    public static String toString(Object object) {

        return object == null ? "" : object.toString();
    }

    /**
     * Transforma uma Coleção de Texto em uma String (Texto)
     *
     * @param text Mensagem
     * @return o texto gerado apartir da coleção
     */
    public static String removeBrackets(Collection<String> text) {
        return text.toString().replace("[", "").replace("]", "");
    }

    /**
     * Transforma uma array de objeto em texto
     *
     * @param objects varios objetos
     * @return Texto
     */
    public static String toText(Object... objects) {

        StringBuilder builder = new StringBuilder();
        for (Object object : objects) {
            builder.append(object);

        }

        return builder.toString();
    }


    /**
     * Captaliza uma String
     *
     * @param name Nome (String)
     * @return String capitalizada
     */
    public static String toTitle(String name) {
        if (name == null)
            return "";
        char first = name.toUpperCase().charAt(0);
        name = name.toLowerCase();
        return first + name.substring(1, name.length());

    }

    /**
     * capitaliza uma Frase
     *
     * @param name     Frase
     * @param replacer O que será colocado entre as palavras
     * @return Frase capitalizada
     */
    public static String toTitle(String name, String replacer) {
        if (name.contains("_")) {
            StringBuilder customName = new StringBuilder();
            int id = 0;
            for (String newName : name.split("_")) {
                if (id != 0) {
                    customName.append(replacer);
                }
                id++;
                customName.append(toTitle(newName));
            }
            return customName.toString();
        }
        return toTitle(name);
    }

    /**
     * Desfazr o ZIP do Arquivo
     *
     * @param zipFilePath   Arquivo
     * @param destDirectory Destino
     */
    public static void unzip(String zipFilePath, String destDirectory) {
        try {
            File destDir = new File(destDirectory);
            if (!destDir.exists())
                destDir.mkdirs();

            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
            ZipEntry entry = zipIn.getNextEntry();

            while (entry != null) {
                String filePath = destDirectory + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    extractFile(zipIn, filePath);
                } else {
                    File dir = new File(filePath);
                    dir.mkdirs();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            zipIn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Valida o nome do usuario se esta certo
     *
     * @param username Nome do usuario
     * @return Se esta certo
     */
    public static boolean validatePlayerName(final String username) {
        final Pattern pattern = Pattern.compile("[a-zA-Z0-9_]{1,16}");
        final Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }


    public static void writeLines(File file, List<String> lines) {
        Path path = file.toPath();
        try {
            Files.write(path, lines, StandardCharsets.UTF_8);
            return;
        } catch (Exception ignored) {
        }
        try {
            Files.write(path, lines, Charset.defaultCharset());
            return;
        } catch (Exception ignored) {
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (String line : lines) {
                writer.write(line + "\n");
            }
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}

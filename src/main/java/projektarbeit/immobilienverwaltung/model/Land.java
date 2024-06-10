package projektarbeit.immobilienverwaltung.model;
/**
 * Represents a country with its name.
 * This enum holds the ISO 3166-1 alpha-2 country codes along with their respective country names.
 */
@SuppressWarnings("SpellCheckingInspection")
public enum Land {
    // List of countries with ISO 3166-1 alpha-2 codes and their names

    //TODO add missing Countries to succeed tests

    AF("Afghanistan"),
    EG("Ägypten"),
    AL("Albanien"),
    DZ("Algerien"),
    AD("Andorra"),
    AO("Angola"),
    AG("Antigua und Barbuda"),
    AR("Argentinien"),
    AM("Armenien"),
    AZ("Aserbaidschan"),
    ET("Äthiopien"),
    AU("Australien"),
    BS("Bahamas"),
    BH("Bahrain"),
    BD("Bangladesch"),
    BB("Barbados"),
    BE("Belgien"),
    BZ("Belize"),
    BJ("Benin"),
    BT("Bhutan"),
    BO("Bolivien"),
    BA("Bosnien und Herzegowina"),
    BW("Botsuana"),
    BR("Brasilien"),
    BN("Brunei"),
    BG("Bulgarien"),
    BF("Burkina Faso"),
    BI("Burundi"),
    CL("Chile"),
    CN("China"),
    DK("Dänemark"),
    DE("Deutschland"),
    DM("Dominica"),
    DO("Dominikanische Republik"),
    DJ("Dschibuti"),
    EC("Ecuador"),
    SV("El Salvador"),
    CI("Elfenbeinküste"),
    ER("Eritrea"),
    EE("Estland"),
    SZ("Eswatini"),
    FJ("Fidschi"),
    FI("Finnland"),
    FR("Frankreich"),
    GA("Gabun"),
    GM("Gambia"),
    GH("Ghana"),
    GR("Griechenland"),
    GD("Grenada"),
    GT("Guatemala"),
    GN("Guinea"),
    GW("Guinea-Bissau"),
    GY("Guyana"),
    HT("Haiti"),
    HN("Honduras"),
    IN("Indien"),
    ID("Indonesien"),
    IQ("Irak"),
    IR("Iran"),
    IE("Irland"),
    IS("Island"),
    IL("Israel"),
    IT("Italien"),
    JM("Jamaika"),
    JP("Japan"),
    YE("Jemen"),
    JO("Jordanien"),
    KH("Kambodscha"),
    CM("Kamerun"),
    CA("Kanada"),
    CV("Kap Verde"),
    KZ("Kasachstan"),
    QA("Katar"),
    KE("Kenia"),
    KG("Kirgisistan"),
    KI("Kiribati"),
    CO("Kolumbien"),
    KM("Komoren"),
    CD("Kongo, Demokratische Republik"),
    CG("Kongo, Republik"),
    KP("Korea, Nord"),
    KR("Korea, Süd"),
    XK("Kosovo"),
    HR("Kroatien"),
    CU("Kuba"),
    KW("Kuwait"),
    LA("Laos"),
    LS("Lesotho"),
    LV("Lettland"),
    LB("Libanon"),
    LR("Liberia"),
    LY("Libyen"),
    LI("Liechtenstein"),
    LT("Litauen"),
    LU("Luxemburg"),
    MG("Madagaskar"),
    MW("Malawi"),
    MY("Malaysia"),
    MV("Malediven"),
    ML("Mali"),
    MT("Malta"),
    MA("Marokko"),
    MH("Marshallinseln"),
    MR("Mauretanien"),
    MU("Mauritius"),
    MX("Mexiko"),
    FM("Mikronesien"),
    MD("Moldau"),
    MC("Monaco"),
    MN("Mongolei"),
    ME("Montenegro"),
    MZ("Mosambik"),
    MM("Myanmar"),
    NA("Namibia"),
    NR("Nauru"),
    NP("Nepal"),
    NZ("Neuseeland"),
    NI("Nicaragua"),
    NL("Niederlande"),
    NE("Niger"),
    NG("Nigeria"),
    MK("Nordmazedonien"),
    NO("Norwegen"),
    OM("Oman"),
    PK("Pakistan"),
    PS("Palästina"),
    PA("Panama"),
    PG("Papua-Neuguinea"),
    PY("Paraguay"),
    PE("Peru"),
    PH("Philippinen"),
    PL("Polen"),
    PT("Portugal"),
    RW("Ruanda"),
    RO("Rumänien"),
    RU("Russland"),
    SB("Salomonen"),
    ZM("Sambia"),
    WS("Samoa"),
    SM("San Marino"),
    ST("São Tomé und Príncipe"),
    SA("Saudi-Arabien"),
    SE("Schweden"),
    CH("Schweiz"),
    SN("Senegal"),
    RS("Serbien"),
    SC("Seychellen"),
    SL("Sierra Leone"),
    ZW("Simbabwe"),
    SG("Singapur"),
    SK("Slowakei"),
    SI("Slowenien"),
    SO("Somalia"),
    ES("Spanien"),
    LK("Sri Lanka"),
    KN("St. Kitts und Nevis"),
    LC("St. Lucia"),
    VC("St. Vincent und die Grenadinen"),
    ZA("Südafrika"),
    SD("Sudan"),
    SS("Südsudan"),
    SR("Suriname"),
    SY("Syrien"),
    TJ("Tadschikistan"),
    TZ("Tansania"),
    TH("Thailand"),
    TL("Timor-Leste"),
    TG("Togo"),
    TO("Tonga"),
    TT("Trinidad und Tobago"),
    TD("Tschad"),
    CZ("Tschechien"),
    TN("Tunesien"),
    TR("Türkei"),
    TM("Turkmenistan"),
    TV("Tuvalu"),
    UG("Uganda"),
    UA("Ukraine"),
    HU("Ungarn"),
    UY("Uruguay"),
    UZ("Usbekistan"),
    VU("Vanuatu"),
    VA("Vatikanstadt"),
    VE("Venezuela"),
    AE("Vereinigte Arabische Emirate"),
    GB("Vereinigtes Königreich"),
    US("Vereinigte Staaten"),
    VN("Vietnam"),
    BY("Weißrussland"),
    CF("Zentralafrikanische Republik"),
    CY("Zypern");

    private final String name;

    /**
     * Constructs a new Land with the specified country name.
     *
     * @param name the name of the country
     */
    Land(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the country.
     *
     * @return the name of the country
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a string representation of the country name.
     *
     * @return the name of the country
     */
    @Override
    public String toString() {
        return name;
    }
}
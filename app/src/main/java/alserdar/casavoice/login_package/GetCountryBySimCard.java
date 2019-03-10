package alserdar.casavoice.login_package;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;

import alserdar.casavoice.OurToast;
import alserdar.casavoice.R;
import alserdar.casavoice.TermsAndConditions;

public class GetCountryBySimCard extends AppCompatActivity {

    TelephonyManager telephonyManager;
    String Country ;
    String dialCode ;
    String currentCountry ;
    int simState ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_country_by_sim_card);

        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        simState = telephonyManager.getSimState();
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                new OurToast().myToast(getBaseContext() ,"SimCard is absent" );
                break;

            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                new OurToast().myToast(getBaseContext() ,"SimCard is locked" );
                break;

            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                new OurToast().myToast(getBaseContext() , "SimCard is pin required" );
                break;

            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                new OurToast().myToast(getBaseContext() ,"SimCard is puk required" );
                break;

            case TelephonyManager.SIM_STATE_UNKNOWN:
                new OurToast().myToast(getBaseContext() ,"SimCard is Unknown" );
                break;

            case TelephonyManager.SIM_STATE_READY:
                //new OurToast().myToast(getBaseContext() ,"SimCard is READY" );
                try {
                    Country = telephonyManager.getSimCountryIso();
                    if (Country != null && Country.length() == 2) {

                        // Toast.makeText(getBaseContext(), Country.toUpperCase(), Toast.LENGTH_LONG).show();
                    }
                    else if (telephonyManager.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA)
                    {
                        Country = telephonyManager.getNetworkCountryIso();
                        if (Country != null && Country.length() == 2)
                        {
                            new OurToast().myToast(getBaseContext() , currentCountry );
                        }
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

                if (Country != null) {
                    switch (Country.toUpperCase())
                    {

                        case "EG" :
                            currentCountry = "Egypt";
                            break;
                        case "AF" :
                            currentCountry = "Afghanistan";
                            break;
                        case "AX" :
                            currentCountry = "Aland Islands";
                            break;
                        case "AL" :
                            currentCountry = "Albania";
                            break;
                        case "DZ" :
                            currentCountry = "Algeria";
                            break;
                        case "AS" :
                            currentCountry = "American Samoa";
                            break;
                        case "AD" :
                            currentCountry = "Andorra";
                            break;
                        case "AO" :
                            currentCountry = "Angola";
                            break;
                        case "AI" :
                            currentCountry = "Anguilla";
                            break;
                        case "AQ" :
                            currentCountry = "Antarctica";
                            break;
                        case "AG" :
                            currentCountry = "Antigua and Barbuda";
                            break;
                        case "AR" :
                            currentCountry = "Argentina";
                            break;
                        case "AM" :
                            currentCountry = "Armenia";
                            break;
                        case "AW" :
                            currentCountry = "Aruba";
                            break;
                        case "AC" :
                            currentCountry = "Ascension Island";
                            break;
                        case "AU" :
                            currentCountry = "Australia";
                            break;
                        case "AT" :
                            currentCountry = "Austria";
                            break;
                        case "AZ" :
                            currentCountry = "Azerbaijan";
                            break;
                        case "BS" :
                            currentCountry = "Bahamas";
                            break;
                        case "BH" :
                            currentCountry = "Bahrain";
                            break;
                        case "BB" :
                            currentCountry = "Barbados";
                            break;
                        case "BD" :
                            currentCountry = "Bangladesh";
                            break;
                        case "BY" :
                            currentCountry = "Belarus";
                            break;
                        case "BE" :
                            currentCountry = "Belgium";
                            break;
                        case "BZ" :
                            currentCountry = "Belize";
                            break;
                        case "BJ" :
                            currentCountry = "Benin";
                            break;
                        case "BM" :
                            currentCountry = "Bermuda";
                            break;
                        case "BT" :
                            currentCountry = "Bhutan";
                            break;
                        case "BW" :
                            currentCountry = "Botswana";
                            break;

                        case "BO" :
                            currentCountry = "Bolivia";
                            break;
                        case "BA" :
                            currentCountry = "Bosnia and Herzegovina";
                            break;
                        case "BV" :
                            currentCountry = "Bouvet Island";
                            break;
                        case "BR" :
                            currentCountry = "Brazil";
                            break;
                        case "IO" :
                            currentCountry = "British Indian Ocean Territory";
                            break;
                        case "BN" :
                            currentCountry = "Brunei Darussalam";
                            break;
                        case "BG" :
                            currentCountry = "Bulgaria";
                            break;
                        case "BF" :
                            currentCountry = "Burkina Faso";
                            break;

                        case "BI" :
                            currentCountry = "Burundi";
                            break;
                        case "KH" :
                            currentCountry = "Cambodia";
                            break;
                        case "CM" :
                            currentCountry = "Cameroon";
                            break;
                        case "CA" :
                            currentCountry = "Canada";
                            break;
                        case "CV" :
                            currentCountry = "Cape Verde";
                            break;
                        case "KY" :
                            currentCountry = "Cayman Islands";
                            break;
                        case "CF" :
                            currentCountry = "Central African Republic";
                            break;
                        case "TD" :
                            currentCountry = "Chad";
                            break;
                        case "CL" :
                            currentCountry = "Chile";
                            break;
                        case "CN" :
                            currentCountry = "China";
                            break;
                        case "CX" :
                            currentCountry = "Christmas Island";
                            break;
                        case "CC" :
                            currentCountry = "Cocos (Keeling) Islands";
                            break;
                        case "CO" :
                            currentCountry = "Colombia";
                            break;
                        case "KM" :
                            currentCountry = "Comoros";
                            break;
                        case "CG" :
                            currentCountry = "Congo";
                            break;
                        case "CD" :
                            currentCountry = "Congo, Democratic Republic";
                            break;
                        case "CK" :
                            currentCountry = "Cook Islands";
                            break;
                        case "CR" :
                            currentCountry = "Costa Rica";
                            break;
                        case "CI" :
                            currentCountry = "Cote D'Ivoire (Ivory Coast)";
                            break;
                        case "HR" :
                            currentCountry = "Croatia (Hrvatska)";
                            break;
                        case "CU" :
                            currentCountry = "Cuba";
                            break;
                        case "CY" :
                            currentCountry = "Cyprus";
                            break;
                        case "CZ" :
                            currentCountry = "Czech Republic";
                            break;
                        case "CS" :
                            currentCountry = "Czechoslovakia (former)";
                            break;
                        case "DK" :
                            currentCountry = "Denmark";
                            break;
                        case "DJ" :
                            currentCountry = "Djibouti";
                            break;
                        case "DM" :
                            currentCountry = "Dominica";
                            break;
                        case "DO" :
                            currentCountry = "Dominican Republic";
                            break;
                        case "TP" :
                            currentCountry = "East Timor";
                            break;
                        case "EC" :
                            currentCountry = "Ecuador";
                            break;
                        case "SV" :
                            currentCountry = "El Salvador";
                            break;
                        case "GQ" :
                            currentCountry = "Equatorial Guinea";
                            break;
                        case "ER" :
                            currentCountry = "Eritrea";
                            break;
                        case "EE" :
                            currentCountry = "Estonia";
                            break;
                        case "ET" :
                            currentCountry = "Ethiopia";
                            break;
                        case "FK" :
                            currentCountry = "Falkland Islands (Malvinas)";
                            break;
                        case "FO" :
                            currentCountry = "Faroe Islands";
                            break;
                        case "FJ" :
                            currentCountry = "Fiji";
                            break;
                        case "FI" :
                            currentCountry = "Finland";
                            break;
                        case "FR" :
                            currentCountry = "France";
                            break;
                        case "FX" :currentCountry = "France, Metropolitan";
                            break;
                        case "GF" :currentCountry = "French Guiana";
                            break;
                        case "PF" :currentCountry = "French Polynesia";
                            break;
                        case "TF" :currentCountry = "French Southern Territories";
                            break;
                        case "MK" :currentCountry = "Macedonia";
                            break;
                        case "GA" : currentCountry = "Gabon";
                            break;
                        case "GM" :currentCountry = "Gambia";
                            break;
                        case "GE" :currentCountry = "Georgia";
                            break;
                        case "DE" :currentCountry = "Germany";
                            break;
                        case "GH" :currentCountry = "Ghana";
                            break;
                        case "GI" :currentCountry = "Gibraltar";
                            break;
                        case "GB" :currentCountry = "Great Britain (UK)";
                            break;
                        case "GR" :currentCountry = "Greece";
                            break;
                        case "GL" :currentCountry = "Greenland";
                            break;
                        case "GD" :currentCountry = "Grenada";
                            break;
                        case "GP" :currentCountry = "Guadeloupe";
                            break;
                        case "GU" :currentCountry = "Guam";
                            break;
                        case "GT" :currentCountry = "Guatemala";
                            break;
                        case "GG" :currentCountry = "Guernsey";
                            break;
                        case "GN" :currentCountry = "Guinea";
                            break;
                        case "GW" :currentCountry = "Guinea-Bissau";
                            break;
                        case "GY" :currentCountry = "Guyana";
                            break;
                        case "HT" :currentCountry = "Haiti";
                            break;
                        case "HM" :currentCountry = "Heard and McDonald Islands";
                            break;
                        case "HN" :currentCountry = "Honduras";
                            break;
                        case "HK" :currentCountry = "Hong Kong";
                            break;
                        case "HU" :currentCountry = "Hungary";
                            break;
                        case "IS" :currentCountry = "Iceland";
                            break;
                        case "IN" :currentCountry = "India";
                            break;
                        case "ID" :currentCountry = "Indonesia";
                            break;
                        case "IR" :currentCountry = "Iran";
                            break;
                        case "IQ" :currentCountry = "Iraq";
                            break;
                        case "IE" :currentCountry = "Ireland";
                            break;
                        case "IL" :currentCountry = "Palestine";
                            break;
                        case "IM" :currentCountry = "Isle of Man";
                            break;
                        case "IT" :currentCountry = "Italy";
                            break;
                        case "JE" :currentCountry = "Jersey";
                            break;
                        case "JM" :currentCountry = "Jamaica";
                            break;
                        case "JP" :currentCountry = "Japan";
                            break;
                        case "JO" :currentCountry = "Jordan";
                            break;
                        case "KZ" :currentCountry = "Kazakhstan";
                            break;
                        case "KE" :currentCountry = "Kenya";
                            break;
                        case "KI" :currentCountry = "Kiribati";
                            break;
                        case "KP" :currentCountry = "Korea (North)";
                            break;
                        case "KR" :currentCountry = "Korea (South)";
                            break;
                        case "KW" :currentCountry = "Kuwait";
                            break;
                        case "KG" :currentCountry = "Kyrgyzstan";
                            break;
                        case "LA" :currentCountry = "Laos";
                            break;
                        case "LV" :currentCountry = "Latvia";
                            break;
                        case "LB" :currentCountry = "Lebanon";
                            break;
                        case "LI" :currentCountry = "Liechtenstein";
                            break;
                        case "LR" :currentCountry = "Liberia";
                            break;
                        case "LY" :currentCountry = "Libya";
                            break;
                        case "LS" :currentCountry = "Lesotho";
                            break;
                        case "LT" :currentCountry = "Lithuania";
                            break;
                        case "LU" :currentCountry = "Luxembourg";
                            break;
                        case "MO" :currentCountry = "Macau";
                            break;
                        case "MG" :currentCountry = "Madagascar";
                            break;
                        case "MW" :currentCountry = "Malawi";
                            break;
                        case "MY" :currentCountry = "Malaysia";
                            break;
                        case "MV" :currentCountry = "Maldives";
                            break;
                        case "ML" :currentCountry = "Mali";
                            break;
                        case "MT" :currentCountry = "Malta";
                            break;
                        case "MH" :currentCountry = "Marshall Islands";
                            break;
                        case "MQ" :currentCountry = "Martinique";
                            break;
                        case "MR" :currentCountry = "Mauritania";
                            break;
                        case "MU" :currentCountry = "Mauritius";
                            break;
                        case "YT" :currentCountry = "Mayotte";
                            break;
                        case "MX" :currentCountry = "Mexico";
                            break;
                        case "FM" :currentCountry = "Micronesia";
                            break;
                        case "MC" :currentCountry = "Monaco";
                            break;
                        case "MD" :currentCountry = "Moldova";
                            break;
                        case "MN" :currentCountry = "Mongolia";
                            break;
                        case "ME" :currentCountry = "Montenegro";
                            break;
                        case "MS" :currentCountry = "Montserrat";
                            break;
                        case "MA" :currentCountry = "Morocco";
                            break;
                        case "MZ" :currentCountry = "Mozambique";
                            break;
                        case "MM" :currentCountry = "Myanmar";
                            break;
                        case "NA" :currentCountry = "Namibia";
                            break;
                        case "NR" :currentCountry = "Nauru";
                            break;
                        case "NP" :currentCountry = "Nepal";
                            break;
                        case "NL" :currentCountry = "Netherlands";
                            break;
                        case "AN" :currentCountry = "Netherlands Antilles";
                            break;
                        case "NT" :currentCountry = "Neutral Zone";
                            break;
                        case "NC" :currentCountry = "New Caledonia";
                            break;
                        case "NZ" :currentCountry = "New Zealand (Aotearoa)";
                            break;
                        case "NI" :currentCountry = "Nicaragua";
                            break;
                        case "NE" :currentCountry = "Niger";
                            break;
                        case "NG" :currentCountry = "Nigeria";
                            break;
                        case "NU" :currentCountry = "Niue";
                            break;
                        case "NF" :currentCountry = "Norfolk Island";
                            break;
                        case "MP" :currentCountry = "Northern Mariana Islands";
                            break;
                        case "NO" :currentCountry = "Norway";
                            break;
                        case "OM" :currentCountry = "Oman";
                            break;
                        case "PK" :currentCountry = "Pakistan";
                            break;
                        case "PW" :currentCountry = "Palau";
                            break;
                        case "PS" :currentCountry = "Palestine";
                            break;
                        case "PA" :currentCountry = "Panama";
                            break;
                        case "PG" :currentCountry = "Papua New Guinea";
                            break;
                        case "PY" :currentCountry = "Paraguay";
                            break;
                        case "PE" :currentCountry = "Peru";
                            break;
                        case "PH" :currentCountry = "Philippines";
                            break;
                        case "PN" :currentCountry = "Pitcairn";
                            break;
                        case "PL" :currentCountry = "Poland";
                            break;
                        case "PT" :currentCountry = "Portugal";
                            break;
                        case "PR" :currentCountry = "Puerto Rico";
                            break;
                        case "QA" :currentCountry = "Qatar";
                            break;
                        case "RE" :currentCountry = "Reunion";
                            break;
                        case "RO" :currentCountry = "Romania";
                            break;
                        case "RU" :currentCountry = "Russian Federation";
                            break;
                        case "RW" :currentCountry = "Rwanda";
                            break;
                        case "GS" :currentCountry = "S. Georgia";
                            break;
                        case "KN" :currentCountry = "Saint Kitts and Nevis";
                            break;
                        case "LC" :currentCountry = "Saint Lucia";
                            break;
                        case "VC" :currentCountry = "Saint Vincent & the Grenadines";
                            break;
                        case "WS" :currentCountry = "Samoa";
                            break;
                        case "SM" :currentCountry = "San Marino";
                            break;
                        case "ST" :currentCountry = "Sao Tome and Principe";
                            break;
                        case "SA" :currentCountry = "Saudi Arabia";
                            break;
                        case "SN" :currentCountry = "Senegal";
                            break;
                        case "RS" :currentCountry = "Serbia";
                            break;
                        case "YU" :currentCountry = "Serbia and Montenegro";
                            break;
                        case "SC" :currentCountry = "Seychelles";
                            break;
                        case "SL" :currentCountry = "Sierra Leone";
                            break;
                        case "SG" :currentCountry = "Singapore";
                            break;
                        case "SI" :currentCountry = "Slovenia";
                            break;
                        case "SK" :currentCountry = "Slovak Republic";
                            break;
                        case "SB" :currentCountry = "Solomon Islands";
                            break;
                        case "SO" :currentCountry = "Somalia";
                            break;
                        case "ZA" :currentCountry = "South Africa";
                            break;
                        case "ES" :currentCountry = "Spain";
                            break;
                        case "LK" :currentCountry = "Sri Lanka";
                            break;
                        case "SH" :currentCountry = "St. Helena";
                            break;
                        case "PM" :currentCountry = "St. Pierre and Miquelon";
                            break;
                        case "SD" :currentCountry = "Sudan";
                            break;
                        case "SR" :currentCountry = "Suriname";
                            break;
                        case "SJ" :currentCountry = "Svalbard & Jan Mayen Islands";
                            break;
                        case "SZ" :currentCountry = "Swaziland";
                            break;
                        case "SE" :currentCountry = "Sweden";
                            break;
                        case "CH" :currentCountry = "Switzerland";
                            break;
                        case "SY" :currentCountry = "Syria";
                            break;
                        case "TW" :currentCountry = "Taiwan";
                            break;
                        case "TJ" :currentCountry = "Tajikistan";
                            break;
                        case "TZ" :currentCountry = "Tanzania";
                            break;
                        case "TH" :currentCountry = "Thailand";
                            break;
                        case "TG" :currentCountry = "Togo";
                            break;
                        case "TK" :currentCountry = "Tokelau";
                            break;
                        case "TO" :currentCountry = "Tonga";
                            break;
                        case "TT" :currentCountry = "Trinidad and Tobago";
                            break;
                        case "TN" :currentCountry = "Tunisia";
                            break;
                        case "TR" :currentCountry = "Turkey";
                            break;
                        case "TM" :currentCountry = "Turkmenistan";
                            break;
                        case "TC" :currentCountry = "Turks and Caicos Islands";
                            break;
                        case "TV" :currentCountry = "Tuvalu";
                            break;
                        case "UG" :currentCountry = "Uganda";
                            break;
                        case "UA" :currentCountry = "Ukraine";
                            break;
                        case "AE" :currentCountry = "United Arab Emirates";
                            break;
                        case "UK" :currentCountry = "United Kingdom";
                            break;
                        case "US" : currentCountry = "United States";
                            break;
                        case "UM" :currentCountry = "US Minor Outlying Islands";
                            break;
                        case "UY" :currentCountry = "Uruguay";
                            break;
                        case "SU" :currentCountry = "USSR (former)";
                            break;
                        case "UZ" :currentCountry = "Uzbekistan";
                            break;
                        case "VU" :currentCountry = "Vanuatu";
                            break;
                        case "VA" :currentCountry = "Vatican City State";
                            break;
                        case "VE" :currentCountry = "Venezuela";
                            break;
                        case "VN" :currentCountry = "Viet Nam";
                            break;
                        case "VG" :currentCountry = "British Virgin Islands";
                            break;
                        case "VI" :currentCountry = "Virgin Islands (U.S.)";
                            break;
                        case "WF" :currentCountry = "Wallis and Futuna Islands";
                            break;
                        case "EH" :currentCountry = "Western Sahara";
                            break;
                        case "YE" :currentCountry = "Yemen";
                            break;
                        case "ZM" :currentCountry = "Zambia";
                            break;
                        case "ZR" :currentCountry = "Zaire";
                            break;
                        case "ZW" :currentCountry = "Zimbabwe";
                            break;
                        default:currentCountry = "Unknown Country";
                            break;
                    }
                }
                break;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                SharedPreferences saveCountry = PreferenceManager.getDefaultSharedPreferences(GetCountryBySimCard.this);
                final SharedPreferences.Editor editor = saveCountry.edit();
                editor.putString("currentCountry" ,currentCountry );
                editor.apply();
                Intent i = new Intent(getBaseContext() , TermsAndConditions.class);
                startActivity(i);
            }
        }).start();
    }

    @Override
    public void onBackPressed() {

    }

}

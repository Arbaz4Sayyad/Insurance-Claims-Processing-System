package in.arbii.rest;


import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SsnController {

    private static final Map<String, String> ssnPrefixToState;

    static {
        ssnPrefixToState = new HashMap<>();
        // New Hampshire
        ssnPrefixToState.put("001", "New Hampshire");
        ssnPrefixToState.put("002", "New Hampshire");
        ssnPrefixToState.put("003", "New Hampshire");

        // Maine
        ssnPrefixToState.put("004", "Maine");
        ssnPrefixToState.put("005", "Maine");
        ssnPrefixToState.put("006", "Maine");
        ssnPrefixToState.put("007", "Maine");

        // Vermont
        ssnPrefixToState.put("008", "Vermont");
        ssnPrefixToState.put("009", "Vermont");

        // Massachusetts
        for (int i = 10; i <= 34; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Massachusetts");
        }

        // Rhode Island
        for (int i = 35; i <= 39; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Rhode Island");
        }

        // Connecticut
        for (int i = 40; i <= 49; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Connecticut");
        }

        // New York
        for (int i = 50; i <= 134; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "New York");
        }

        // New Jersey
        for (int i = 135; i <= 158; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "New Jersey");
        }

        // Pennsylvania
        for (int i = 159; i <= 211; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Pennsylvania");
        }

        // Maryland
        for (int i = 212; i <= 220; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Maryland");
        }

        // Delaware
        for (int i = 221; i <= 222; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Delaware");
        }

        // Virginia
        for (int i = 223; i <= 231; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Virginia");
        }

        // North Carolina
        ssnPrefixToState.put("232", "North Carolina");
        for (int i = 237; i <= 246; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "North Carolina");
        }
        for (int i = 681; i <= 690; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "North Carolina");
        }

        // West Virginia
        for (int i = 232; i <= 236; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "West Virginia");
        }

        // South Carolina
        for (int i = 247; i <= 251; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "South Carolina");
        }
        for (int i = 654; i <= 658; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "South Carolina");
        }

        // Georgia
        for (int i = 252; i <= 260; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Georgia");
        }
        for (int i = 667; i <= 675; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Georgia");
        }

        // Florida
        for (int i = 261; i <= 267; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Florida");
        }
        for (int i = 589; i <= 595; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Florida");
        }
        for (int i = 766; i <= 772; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Florida");
        }

        // Ohio
        for (int i = 268; i <= 302; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Ohio");
        }

        // Indiana
        for (int i = 303; i <= 317; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Indiana");
        }

        // Illinois
        for (int i = 318; i <= 361; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Illinois");
        }

        // Michigan
        for (int i = 362; i <= 386; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Michigan");
        }

        // Wisconsin
        for (int i = 387; i <= 399; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Wisconsin");
        }

        // Kentucky
        for (int i = 400; i <= 407; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Kentucky");
        }

        // Tennessee
        for (int i = 408; i <= 415; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Tennessee");
        }
        for (int i = 756; i <= 763; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Tennessee");
        }

        // Alabama
        for (int i = 416; i <= 424; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Alabama");
        }

        // Mississippi
        for (int i = 425; i <= 428; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Mississippi");
        }
        for (int i = 587; i <= 588; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Mississippi");
        }
        for (int i = 752; i <= 755; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Mississippi");
        }

        // Arkansas
        for (int i = 429; i <= 432; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Arkansas");
        }
        for (int i = 676; i <= 679; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Arkansas");
        }

        // Louisiana
        for (int i = 433; i <= 439; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Louisiana");
        }
        for (int i = 659; i <= 665; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Louisiana");
        }

        // Oklahoma
        for (int i = 440; i <= 448; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Oklahoma");
        }

        // Texas
        for (int i = 449; i <= 467; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Texas");
        }
        for (int i = 627; i <= 645; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Texas");
        }

        // Minnesota
        for (int i = 468; i <= 477; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Minnesota");
        }

        // Iowa
        for (int i = 478; i <= 485; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Iowa");
        }

        // Missouri
        for (int i = 486; i <= 500; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Missouri");
        }

        // North Dakota
        ssnPrefixToState.put("501", "North Dakota");
        ssnPrefixToState.put("502", "North Dakota");

        // South Dakota
        ssnPrefixToState.put("503", "South Dakota");
        ssnPrefixToState.put("504", "South Dakota");

        // Nebraska
        for (int i = 505; i <= 508; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Nebraska");
        }

        // Kansas
        for (int i = 509; i <= 515; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Kansas");
        }

        // Montana
        ssnPrefixToState.put("516", "Montana");
        ssnPrefixToState.put("517", "Montana");

        // Idaho
        ssnPrefixToState.put("518", "Idaho");
        ssnPrefixToState.put("519", "Idaho");

        // Wyoming
        ssnPrefixToState.put("520", "Wyoming");

        // Colorado
        for (int i = 521; i <= 524; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Colorado");
        }
        for (int i = 650; i <= 653; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Colorado");
        }

        // New Mexico
        ssnPrefixToState.put("525", "New Mexico");
        ssnPrefixToState.put("585", "New Mexico");
        ssnPrefixToState.put("648", "New Mexico");
        ssnPrefixToState.put("649", "New Mexico");

        // Arizona
        for (int i = 526; i <= 527; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Arizona");
        }
        ssnPrefixToState.put("600", "Arizona");
        for (int i = 764; i <= 765; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Arizona");
        }

        // Utah
        ssnPrefixToState.put("528", "Utah");
        ssnPrefixToState.put("529", "Utah");
        ssnPrefixToState.put("646", "Utah");
        ssnPrefixToState.put("647", "Utah");

        // Nevada
        ssnPrefixToState.put("530", "Nevada");
        ssnPrefixToState.put("680", "Nevada");

        // Washington
        for (int i = 531; i <= 539; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Washington");
        }

        // Oregon
        for (int i = 540; i <= 544; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Oregon");
        }

        // California
        for (int i = 545; i <= 573; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "California");
        }
        for (int i = 602; i <= 626; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "California");
        }

        // Alaska
        ssnPrefixToState.put("574", "Alaska");

        // Hawaii
        ssnPrefixToState.put("575", "Hawaii");
        ssnPrefixToState.put("576", "Hawaii");
        ssnPrefixToState.put("750", "Hawaii");
        ssnPrefixToState.put("751", "Hawaii");

        // District of Columbia
        for (int i = 577; i <= 579; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "District of Columbia");
        }

        // U.S. Virgin Islands
        ssnPrefixToState.put("580", "U.S. Virgin Islands");

        // Puerto Rico
        for (int i = 580; i <= 584; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Puerto Rico");
        }
        for (int i = 596; i <= 599; i++) {
            ssnPrefixToState.put(String.format("%03d", i), "Puerto Rico");

        // Pacific Ocean territories
        ssnPrefixToState.put("586", "Pacific Ocean territories");
        ssnPrefixToState.put("586", "Guam");
        ssnPrefixToState.put("586", "American Samoa");
        ssnPrefixToState.put("586", "Philippine Islands (under U.S. rule until 1946)");
        ssnPrefixToState.put("586", "Northern Mariana Islands");
       }
    }
    
    @GetMapping("/ssn/{ssn}")
    public String getStateBySsn(@PathVariable String ssn) {
        if (ssn == null || ssn.length() < 3) {
            return "Invalid SSN";
        }
        String prefix = ssn.substring(0, 3);
        String state = ssnPrefixToState.get(prefix);
        return state != null ? state : "State not found for given SSN prefix";
    }
}
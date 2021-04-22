import callAPI from "../utils/api";
import { RedirectUrl } from "./Router";
import { getUserSessionData } from "../utils/session.js";
import PrintError from "./PrintError";

let page = document.querySelector("#page");
let userData = getUserSessionData();
let AdvancedSearchesPage = () => {

    let menu = `
    <div class="menuAdmin">
        <div id="visits" class="condensed small-caps ">Visites</div>
        <div id="advancedSearches" class="condensed small-caps menuAdminOn">Recherche avancées</div>
        <div id="confirmRegister" class="condensed small-caps">Confirmation des inscriptions</div>
    </div>
    `;

    let advancedSearchesBar = `
<div class="searchBar-advanced">
        
            <label class="switch-client-furn switch-client">
                <input type="radio" name="client-furn" value="client">
                <span class="radio-client-furn">
                    <i class="bi bi-people" id="icon-client" ></i>
                </span>
            </label>
            <label class="switch-client-furn switch-furn">
                <input type="radio" name="client-furn" value="furn">
                <span class="radio-client-furn">
                    <i class="bi bi-tag" id="icon-furn" ></i>
                </span>
            </label>
        
        <button class="btn btn-dark condensed small-caps" type="submit">Rechercher</button>
`

    let advancedSearchesPageClient = `
        <p>Recherche:</p>
        <input type="text" class="form-control" id="searchUsername" placeholder="Nom">
        <input type="text" class="form-control" id="searchUserCity" placeholder="Ville">
        <input type="text" class="form-control" id="searchUserPostCode" placeholder="Code Postal">
    </div>
    `;

    let advancedSearchesPageFurniture = `
        <p>Recherche:</p>
        <input type="text" class="form-control" id="searchClientName" placeholder="Nom du client">
        <select class="form-select" aria-label="Type" id="searchType">
            <option selected>Type</option>
            <option value="1">One</option>
            <option value="2">Two</option>
            <option value="3">Three</option>
          </select>
          <div class="searchMontant">
             <p>Montant</p>
            <input type="text" class="form-control" id="montantMin" placeholder="Min.">
            <input type="text" class="form-control" id="montantMax" placeholder="Max.">
          </div>
        
    </div>
    `;

    page.innerHTML = menu + advancedSearchesBar + advancedSearchesPageClient;

    
    document.body.addEventListener('change', function(e){
        let target = e.target;
        switch(target.value){
            case 'client':
                page.innerHTML = menu + advancedSearchesBar + advancedSearchesPageClient;
                target.style.checked = "checked";
                console.log("dans client");
                break;
            case 'furn':
                page.innerHTML = menu + advancedSearchesBar + advancedSearchesPageFurniture;
                console.log("dans furn");
                break;
        }
    });

    

    let visits = document.getElementById("visits");
    visits.addEventListener("click", onVisits);

    let advancedSearches = document.getElementById("advancedSearches");
    advancedSearches.addEventListener("click", onAdvancedSearches);

    let confirmRegister = document.getElementById("confirmRegister");
    confirmRegister.addEventListener("click", onConfirmRegister);
};

const onVisits = (e) => {
    e.preventDefault();
    console.log("to visits");
    RedirectUrl("/visits");
};

const onAdvancedSearches = (e) => {
    e.preventDefault();
    console.log("to advancedSearches")
    RedirectUrl("/advancedSearches");
};

const onConfirmRegister = (e) => {
    e.preventDefault();
    console.log("toConfirmRegistration");
    RedirectUrl("/confirmRegistration");
};

function onSwitch(switchElement, clientDiv, furnDiv, menuDiv){
    console.log("dans onSwitch")
    if(switchElement.checked){
        page.innerHTML = menuDiv + clientDiv;
    }
    else{
        page.innerHTML = menuDiv + furnDiv;
    }
}

export default AdvancedSearchesPage;
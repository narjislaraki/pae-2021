import callAPI from "../utils/api";
import { RedirectUrl } from "./Router";
import { getUserSessionData } from "../utils/session.js";
import PrintError from "./PrintError";

const API_BASE_URL = "/api/searches/";

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
        
        <button class="btn btn-dark condensed small-caps" type="submit" id="search">Rechercher</button>
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

    let search = document.getElementById("search");
    search.addEventListener("click", onSearchClients);

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
    RedirectUrl("/advancedSearches");
};

const onConfirmRegister = (e) => {
    e.preventDefault();
    console.log("toConfirmRegistration");
    RedirectUrl("/confirmRegistration");
};

const onSearchClients = async (e) => {
    e.preventDefault();
    let city = document.getElementById("searchUserCity").value;
    let name = document.getElementById("searchUsername").value;
    let postcode = document.getElementById("searchUserPostCode").value;
    let clientList;
    try{
        clientList = await callAPI(
            "/api/users/validatedList",
            "GET",
            userData.token,
            undefined,
        );
        console.log(clientList);
        console.log(name);
        if (name){
            clientList = clientList.filter(e => e.lastName == name)[0];
        } if (city) {
             clientList = clientList.filter(e => e.address.city == city)[0];
        } if (postcode) {
            clientList = clientList.filter(e => e.address.postcode== postcode)[0]
        }

        
        onShowClientList(clientList);
        
        
    } catch(err){
        if (err == "Error: Admin only") {
            err.message = "Seuls les administrateurs peuvent accéder à cette page !";
          }
          console.error("AdvancedSearchesPage::onSearchClients", err);
          PrintError(err);
    }
}

async function SearchFurn (nameClient, type, maxAmount, minAmount) {

}

const onShowClientList = (data) => {
    console.log("hello");
    let onShowClientList =
      `<table class="table table-light tableShowClientList">
        <thead>
          <tr>
            <th scope="col">Pseudo</th>
            <th scope="col">Prénom</th>
            <th scope="col">Nom</th>
            <th scope="col">Adresse</th>
            <th scope="col"></th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
         
          
      `;
      onShowClientList += data
      .map((user) =>
     `<div class="advancedSearchClientItem-container">
        <div class="advancedSearchClientItem condensed">
        <div class="advancedSearchClientItem_pseudo">${user.username}</div>
        <div class="advancedSearchClientItem_fullName">
            <div class="asci-name">${user.firstName}</div>
            <div class="asci-surname">${user.lastName}</div>
        </div>
        <div class="advancedSearchClientItem_email">${user.email}</div>
        <div class="advancedSearchClientItem_moreInfo">
            <div class="asci-signUpDate">Inscrit depuis: ${user.registrationDate}</div>
            <div class="asci-role">Role: ${user.role} </div>
            <div class="asci-amountBought">Nbr achats:</div>
            <div class="asci-amountSold">Nbr ventes:</div>
        </div>
    </div>
        <div class="furnInfo">
          <div class="advancedSearchClientItem-boughtFurn">
            <p>MEUBLES ACHETÉS</p>
            <img src="" alt="" class="asci-boughtFurn">
            <img src="" alt="" class="asci-boughtFurn">
            <img src="" alt="" class="asci-boughtFurn">
          </div>
         <div class="advancedSearchClientItem-soldFurn">
            <p>MEUBLES VENDUS</p>
            <img src="" alt="" class="asci-soldFurn">
         </div>
        </div>
        </div> `)
                              
      .join("");
    page.innerHTML += onShowClientList;    
    return page;
  }

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
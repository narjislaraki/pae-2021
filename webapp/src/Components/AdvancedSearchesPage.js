import callAPI from "../utils/api";
import { RedirectUrl } from "./Router";
import { getUserSessionData } from "../utils/session.js";
import PrintError from "./PrintError";

const API_BASE_URL = "/api/searches/";

let page = document.querySelector("#page");
let clientMode = true;
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
                clientMode = true;
                console.log(clientMode);
                break;
            case 'furn':
                page.innerHTML = menu + advancedSearchesBar + advancedSearchesPageFurniture;
                clientMode = false;
                console.log(clientMode);
                break;
        }
    });

    let search = document.getElementById("search");
    search.addEventListener("click", onSearch);

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

const onSearch = async (e) => {
    e.preventDefault();
    if(clientMode){
        let city = document.getElementById("searchUserCity").value;
        let name = document.getElementById("searchUsername").value;
        let postcode = document.getElementById("searchUserPostCode").value;
        let clientList = [];
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
                clientList = clientList.filter(e => e.lastName.toLowerCase() == name.toLowerCase());
            } if (city) {
                clientList = clientList.filter(e => e.address.city.toLowerCase() == city.toLowerCase());
            } if (postcode) {
                clientList = clientList.filter(e => e.address.postcode.toLowerCase() == postcode.toLowerCase());
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
    else{
        let username = document.getElementById("searchClientName").value;
        let type = document.getElementById("searchType").value;
        let minAmount = document.getElementById("montantMin").value;
        let maxAmount = document.getElementById("montantMax").value;
        let furnList = [];
        try{
            furnList = await callAPI(
                "/api/furnitures",
                "GET",
                userData.token,
                undefined,
            );
            console.log(furnList);
            /*
            if (username){
                furnList = furnList.filter(e => e.username.toLowerCase() == username.toLowerCase());
            } if (type) {
                furnList = furnList.filter(e => e.furniture.type.toLowerCase() == type.toLowerCase());
            } if (maxAmount) {
                furnList = furnList.filter(e => e.furniture.purchase_price.toLowerCase() == postcode.toLowerCase());
            }

            
            onShowClientList(clientList);
            */
            
            
        } catch(err){
            if (err == "Error: Admin only") {
                err.message = "Seuls les administrateurs peuvent accéder à cette page !";
            }
            console.error("AdvancedSearchesPage::onSearchClients", err);
            PrintError(err);
        }
    }
}

const onShowClientList = (data) => {
    console.log("hello");
    let clientList =
      `<div class="clientHandles">
      <div id="pseudoHandle" class="condensed">PSEUDO</div>
      <div id="namesHandle" class="condensed">NOMS</div>
      <div id="emailHandle" class="condensed">EMAIL</div>
      <div id="moreInfoHandle" class="condensed">INFORMATIONS SUPPL.</div>
  </div>
         
          
      `;
      console.log(data);
      clientList += data
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

    page.innerHTML += clientList;    

    page.innerHTML+= `
    <div class="white-space"></div>
    `
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